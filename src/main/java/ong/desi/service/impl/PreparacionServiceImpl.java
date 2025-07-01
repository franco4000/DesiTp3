package ong.desi.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ong.desi.entity.Ingrediente;
import ong.desi.entity.ItemReceta;
import ong.desi.entity.Preparacion;
import ong.desi.entity.Producto;
import ong.desi.entity.Receta;
import ong.desi.repository.PreparacionRepository;
import ong.desi.repository.ProductoRepository;
import ong.desi.repository.RecetaRepository;
import ong.desi.service.PreparacionService;

@Service
public class PreparacionServiceImpl implements PreparacionService {

    @Autowired // Inyectamos los repositorios que necesitaremos para hablar con la BD
    private PreparacionRepository preparacionRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional // Anotación clave: si algo falla, toda la operación se deshace (no se descuenta stock a medias)
    public Preparacion registrarPreparacion(Preparacion preparacion) {
        
    	// - 1 OBTENER DATOS Y VALIDAR -

    	// Busca la receta en la base de datos usando el ID que viene en la preparación
    	Receta receta = recetaRepository.findById(preparacion.getReceta().getId())
    	        .orElseThrow(() -> new RuntimeException("La receta especificada no existe."));

    	// Valida que la fecha de preparación no sea en el futuro
    	if (preparacion.getFechaPreparacion().isAfter(LocalDate.now())) {
    	    throw new RuntimeException("La fecha de preparación no puede ser futura.");
    	}

    	// -Nva VALIDACIÓN-
    	// Usamos el nuevo método del repositorio para verificar si ya existe una preparación
    	// para esa receta en esa fecha.
    	if (preparacionRepository.existsByRecetaAndFechaPreparacion(receta, preparacion.getFechaPreparacion())) {
    	    throw new RuntimeException("Ya se registró una preparación para la receta '" + receta.getNombre() + "' en esta fecha.");
    	}
        
        // 2. LÓGICA DE NEGOCIO, VERIFICAR Y DESCONTAR STOCK 

        // Recorremos cada "renglón" (ItemReceta) de la receta para verificar el stock
        for (ItemReceta item : receta.getItems()) {
            Ingrediente ingrediente = item.getIngrediente();

            // Solo nos interesan los ingredientes que son "Productos", porque son los que tienen stock
            if (ingrediente instanceof Producto) {
                Producto producto = (Producto) ingrediente;
                
                // Calculamos cuánto stock de este producto se necesita para la preparación
                float stockNecesario = item.getCantidad() * preparacion.getCantidadRaciones();

                // Verificamos si hay suficiente stock disponible
                if (producto.getStockDisponible() < stockNecesario) {
                    throw new RuntimeException("No hay stock suficiente para el producto: " + producto.getNombre());
                }
            }
        }
        
        // Si llegamos aquí, significa que hay stock de todo. Ahora sí procedemos a descontar.
        for (ItemReceta item : receta.getItems()) {
            Ingrediente ingrediente = item.getIngrediente();
            
            if (ingrediente instanceof Producto) {
                Producto producto = (Producto) ingrediente;
                
                float stockADescontar = item.getCantidad() * preparacion.getCantidadRaciones();
                
                // Usamos el método que ya existía en la clase Producto para descontar
                producto.descontarStock(stockADescontar);
                
                // Guardamos el producto con su nuevo stock en la base de datos
                productoRepository.save(producto);
            }
        }

        // 3 GUARDAR LA NUEVA PREPARACIÓN 
       
        preparacion.setActiva(true); // Nos aseguramos de que se cree como activa
        preparacion.setReceta(receta); // Asignamos el objeto Receta completo
        return preparacionRepository.save(preparacion);
    }

    @Override
    public List<Preparacion> listarPreparaciones() {
        // Aquí podríamos añadir lógica para que solo devuelva las activas si fuera necesario
        return preparacionRepository.findAll();
    }

    @Override
    @Transactional
    public Preparacion modificarFechaPreparacion(Long id, Preparacion preparacionActualizada) {
        // Buscamos la preparación que se quiere modificar
        Preparacion preparacionExistente = preparacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preparación no encontrada"));
        
        // Según los requisitos, solo se puede modificar la fecha
        preparacionExistente.setFechaPreparacion(preparacionActualizada.getFechaPreparacion());
        
        return preparacionRepository.save(preparacionExistente);
    }

    @Override
    @Transactional
    public void eliminarPreparacion(Long id) {
        // Buscamos la preparación
        Preparacion preparacion = preparacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preparación no encontrada"));
        
        // Realizamos la baja lógica
        preparacion.setActiva(false);
        
        preparacionRepository.save(preparacion);
    }
    
    @Override
    public Preparacion buscarPorId(Long id) {
        return preparacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preparación no encontrada"));
    }
}