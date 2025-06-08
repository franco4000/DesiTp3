package ong.desi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ong.desi.entity.Ingrediente;
import ong.desi.entity.ItemReceta;
import ong.desi.entity.Producto;
import ong.desi.entity.Receta;
import ong.desi.entity.RegistroPreparacion;
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


    @Override
    public List<Receta> listarRecetas() {
        return recetaRepository.findByActivaTrue();
    }

    public Optional<Receta> buscarPorId(Long id) {
        return recetaRepository.findByIdAndActivaTrue(id);
    }


    @Transactional
    public Receta crearReceta(Receta receta) {
        if (recetaRepository.findByNombre(receta.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe una receta con ese nombre");
        }

        receta.setActiva(true);
        receta = recetaRepository.save(receta);

        for (ItemReceta item : receta.getItems()) {
            item.setReceta(receta);

            // Lógica para descontar stock si es un Producto
            if (item.getIngrediente() instanceof Producto producto) {
                Float nuevoStock = producto.getStockDisponible() - item.getCantidad();
                if (nuevoStock < 0) {
                    throw new RuntimeException("No hay stock suficiente para el producto: " + producto.getNombre());
                }
                producto.setStockDisponible(nuevoStock);
                productoRepository.save(producto);//guarda el nuevo stock
            }

            itemRecetaRepository.save(item);
        }

        return receta;
        
    }
    @Transactional
    public Receta modificarReceta(Long id, Receta datosActualizados) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        receta.setDescripcion(datosActualizados.getDescripcion());
//marca los items antiguos como inactivos
        for (ItemReceta item : receta.getItems()) {
            item.setActivo(false); // la eliminacion de receta 
            
        }

//agrega nuevos items
        for (ItemReceta item : datosActualizados.getItems()) {
            item.setReceta(receta);
            
            
            // Descontar stock si es Producto
            if (item.getIngrediente() instanceof Producto producto) {
                Float nuevoStock = producto.getStockDisponible() - item.getCantidad();
                if (nuevoStock < 0) {
                    throw new RuntimeException("No hay stock suficiente para el producto: " + producto.getNombre());
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
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        receta.setActiva(false);
        recetaRepository.save(receta);
    }

    public int calcularCaloriasTotales(Receta receta) {
        return receta.getItems().stream()
        		.filter(ItemReceta::isActivo) // Solo ítems activos
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
            .orElseThrow(() -> new RuntimeException("Receta no encontrada o inactiva"));

        for (ItemReceta item : receta.getItems()) {
            Ingrediente ingrediente = item.getIngrediente();

            if (!(ingrediente instanceof Producto)) {
                throw new RuntimeException("El ingrediente " + ingrediente.getNombre() + " no es un producto y no se puede descontar stock");
            }

            Producto producto = (Producto) ingrediente;
            producto.descontarStock(item.getCantidad());
            productoRepository.save(producto); // Asegurate de tener el repo
       
        }
        registroPreparacionRepository.save(new RegistroPreparacion(receta, 1));

    }
    @Override
    public List<Receta> filtrar(String nombre, Integer minCalorias, Integer maxCalorias) {
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


}


