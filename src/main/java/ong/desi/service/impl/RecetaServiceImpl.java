package ong.desi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ong.desi.controller.view.IngredienteForm;
import ong.desi.entity.Condimento;
import ong.desi.entity.Estacion;
import ong.desi.entity.Ingrediente;
import ong.desi.entity.ItemReceta;
import ong.desi.entity.Producto;
import ong.desi.entity.Receta;
import ong.desi.entity.RegistroPreparacion;
import ong.desi.entity.TipoIngrediente;
import ong.desi.exception.Excepcion;
import ong.desi.repository.IngredienteRepository;
import ong.desi.repository.ItemRecetaRepository;
import ong.desi.repository.ProductoRepository;
import ong.desi.repository.RecetaRepository;
import ong.desi.repository.RegistroPreparacionRepository;
import ong.desi.service.RecetaService;

@Service
public class RecetaServiceImpl implements RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private ItemRecetaRepository itemRecetaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private RegistroPreparacionRepository registroPreparacionRepository;
    @Autowired 
    private IngredienteRepository ingredienteRepository;

    @Override
    public List<Receta> listarRecetas() {
        return recetaRepository.findByActivaTrue();
    }

    public Optional<Receta> buscarPorId(Long id) {
       return recetaRepository.findByIdAndActivaTrue(id);
    }

    @Override
    @Transactional
    public Receta crearReceta(Receta receta) {

        /* 1) Validar nombre único */
        if (recetaRepository.findByNombre(receta.getNombre()).isPresent()) {
            throw new Excepcion("Ya existe una receta con ese nombre");
        }

        receta.setActiva(true);

        /* 2) Resolver cada ItemReceta */
        for (ItemReceta item : receta.getItems()) {
            Ingrediente ing = item.getIngrediente();
            if (ing == null || ing.getId() == null) {
                throw new Excepcion("Ingrediente o su id es nulo en item");
            }
            
            Ingrediente ingrediente = ingredienteRepository.findById(ing.getId())
                    .orElseThrow(() -> new Excepcion("Ingrediente no existe"));
            
            item.setIngrediente(ingrediente);
            item.setReceta(receta);
  

            /* 2.b) Lógica de stock si es Producto */
            if (ing instanceof Producto producto) {
                Float nuevoStock = producto.getStockDisponible() - item.getCantidad();
                if (nuevoStock < 0) {
                    throw new Excepcion(
                        "No hay stock suficiente para el producto: " + producto.getNombre());
                }
                producto.setStockDisponible(nuevoStock);
                productoRepository.save(producto);
            }
        }

        /* 3) Guardar receta (y, por cascade, sus ítems) */
     
        return recetaRepository.save(receta);
        
    }
    
    @Transactional
    public Receta modificarReceta(Long id, Receta datosActualizados) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new Excepcion("Receta no encontrada"));

        receta.setDescripcion(datosActualizados.getDescripcion());
//marca los items antiguos como inactivos
        for (ItemReceta item : receta.getItems()) {
            item.setActiva(false); // la eliminacion de receta 
            
        }

//agrega nuevos items
        for (ItemReceta item : datosActualizados.getItems()) {
            item.setReceta(receta);
            
            
            // Descontar stock si es Producto
            if (item.getIngrediente() instanceof Producto producto) {
                Float nuevoStock = producto.getStockDisponible() - item.getCantidad();
                if (nuevoStock < 0) {
                    throw new Excepcion("No hay stock suficiente para el producto: " + producto.getNombre());
                }
                producto.descontarStock(item.getCantidad());
                 productoRepository.save(producto); // Guardamos el producto con el nuevo stock
            }

            receta.getItems().add(item);
        }

        return recetaRepository.save(receta);
    }

    @Transactional
    public void eliminarReceta(Long id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new Excepcion("Receta no encontrada"));

        receta.setActiva(false);
        recetaRepository.save(receta);
    }

    public int calcularCaloriasTotales(Receta receta) {
        return receta.getItems().stream()
        		.filter(ItemReceta::isActiva) // Solo ítems activos
                .mapToInt(ItemReceta::getCalorias)
                .sum();
    }


    public List<Receta> filtrarPorCalorias(int minCalorias, int maxCalorias) {
        return listarRecetas().stream()
                .filter(receta -> {
                    int total = calcularCaloriasTotales(receta);
                    return total >= minCalorias && total <= maxCalorias;
                })
                .toList();
    }
    
    public List<Receta> filtrarPorNombreYCalorias(String nombre, int minCalorias, int maxCalorias) {
        return listarRecetas().stream()
                .filter(receta -> receta.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(receta -> {
                    int total = calcularCaloriasTotales(receta);
                    return total >= minCalorias && total <= maxCalorias;
                })
                .toList();
    }

    @Transactional
    public void prepararReceta(Long recetaId) {
        Receta receta = recetaRepository.findByIdAndActivaTrue(recetaId)
            .orElseThrow(() -> new Excepcion("Receta no encontrada o inactiva"));

        for (ItemReceta item : receta.getItems()) {
            Ingrediente ingrediente = item.getIngrediente();

            if (!(ingrediente instanceof Producto)) {
                throw new Excepcion("El ingrediente " + ingrediente.getNombre() + " no es un producto y no se puede descontar stock");
            }

            Producto producto = (Producto) ingrediente;
            producto.descontarStock(item.getCantidad());
            productoRepository.save(producto); // Asegurate de tener el repo
       
        }
        registroPreparacionRepository.save(new RegistroPreparacion(receta, 1));

    }
    @Override
    public List<Receta> filtrar(String nombre, Float minCalorias, Float maxCalorias) {
        return listarRecetas().stream()
            .filter(r -> nombre == null || r.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(r -> {
                int calorias = calcularCaloriasTotales(r);
                return (minCalorias == null || calorias >= minCalorias)
                    && (maxCalorias == null || calorias <= maxCalorias);
            })
            .toList();
    }

    public List<Receta> filtrarPorNombre(String nombre) {
        return listarRecetas().stream()
            .filter(r -> r.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .toList();
    }
    @Override
    public void guardarIngrediente(IngredienteForm form) {
        // Validación de nombre duplicado (antes de guardar)
        if (ingredienteRepository.findByNombre(form.getNombre()).isPresent()) {
            throw new Excepcion("Ya existe un ingrediente con ese nombre: " + form.getNombre());
        }

        if (TipoIngrediente.PRODUCTO.equals(form.getTipo())) {
            if (form.getStockDisponible() == null || form.getStockDisponible() < 0) {
                throw new Excepcion("El stock no puede ser nulo ni negativo para un Producto.");
            }

            if (form.getPrecioActual() == null || form.getPrecioActual() <= 0) {
                throw new Excepcion("El precio debe ser mayor a cero para un Producto.");
            }

            Producto producto = new Producto();
            producto.setNombre(form.getNombre());
            producto.setCalorias(form.getCalorias());
            producto.setEstacion(form.getEstacion());
            producto.setStockDisponible(form.getStockDisponible());
            producto.setPrecioActual(form.getPrecioActual());
            producto.setActivo(true); // si usás baja lógica

            ingredienteRepository.save(producto);
            System.out.println(">>> Guardado PRODUCTO: " + producto);

        } else if (TipoIngrediente.CONDIMENTO.equals(form.getTipo())) {
            Condimento condimento = new Condimento();
            condimento.setNombre(form.getNombre());
            condimento.setCalorias(form.getCalorias());
            condimento.setEstacion(form.getEstacion());
            condimento.setActivo(true);

            ingredienteRepository.save(condimento);
            System.out.println(">>> Guardado CONDIMENTO: " + condimento);

        } else {
            throw new Excepcion("Tipo de ingrediente no reconocido: " + form.getTipo());
        }
    }

    @Override
    public List<Ingrediente> buscarIngredientes(String nombre, Estacion estacion, TipoIngrediente tipo) {
        return ingredienteRepository.findAll().stream()
            .filter(i -> i.getActivo() == null || i.getActivo()) // solo activos
            .filter(i -> nombre == null || i.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(i -> estacion == null || i.getEstacion() == estacion)
            .filter(i -> {
                if (tipo == null) return true;
                if (tipo == TipoIngrediente.PRODUCTO) return i instanceof Producto;
                if (tipo == TipoIngrediente.CONDIMENTO) return i instanceof Condimento;
                return false;
            })
            .toList();
    }

	@Override
	public Object obtenerIngredientesActivos() {
		// TODO Auto-generated method stub
		return null;
	}

	public void actualizarIngrediente(IngredienteForm form) {
	    Ingrediente ingrediente = ingredienteRepository.findById(form.getId())
	        .orElseThrow(() -> new Excepcion("Ingrediente no encontrado"));

	    // Si el tipo cambió, reemplazamos por un nuevo objeto
	    if (form.getTipo() == TipoIngrediente.PRODUCTO && !(ingrediente instanceof Producto)) {
	        // Eliminar viejo
	        ingredienteRepository.delete(ingrediente);

	        Producto nuevo = new Producto();
	        nuevo.setId(form.getId()); // importante para mantener la relación si hay claves externas
	        nuevo.setNombre(form.getNombre());
	        nuevo.setCalorias(form.getCalorias());
	        nuevo.setEstacion(form.getEstacion());
	        nuevo.setStockDisponible(form.getStockDisponible());
	        nuevo.setPrecioActual(form.getPrecioActual());
	        nuevo.setActivo(true);

	        ingredienteRepository.save(nuevo);
	        return;
	    }

	    if (form.getTipo() == TipoIngrediente.CONDIMENTO && !(ingrediente instanceof Condimento)) {
	        ingredienteRepository.delete(ingrediente);

	        Condimento nuevo = new Condimento();
	        nuevo.setId(form.getId());
	        nuevo.setNombre(form.getNombre());
	        nuevo.setCalorias(form.getCalorias());
	        nuevo.setEstacion(form.getEstacion());
	        nuevo.setActivo(true);

	        ingredienteRepository.save(nuevo);
	        return;
	    }

	    // Si no cambió el tipo, solo actualizamos campos
	    ingrediente.setNombre(form.getNombre());
	    ingrediente.setCalorias(form.getCalorias());
	    ingrediente.setEstacion(form.getEstacion());
	    ingrediente.setActivo(true);

	    if (ingrediente instanceof Producto producto) {
	        producto.setStockDisponible(form.getStockDisponible());
	        producto.setPrecioActual(form.getPrecioActual());
	    }

	    ingredienteRepository.save(ingrediente);
	}


}