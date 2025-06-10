package ong.desi.service;



import java.util.List;

import org.springframework.stereotype.Service;

import ong.desi.entity.Receta;
@Service
public interface RecetaService {

	    List<Receta> listarRecetas();
	    List<Receta> filtrarPorCalorias(int min, int max);
	    List<Receta> filtrarPorNombreYCalorias(String nombre, int min, int max);
	    Receta crearReceta(Receta receta);
	    Receta modificarReceta(Long id, Receta datos);
	    void eliminarReceta(Long id);
	    void prepararReceta(Long id);
	    List<Receta> filtrar(String nombre, Integer minCalorias, Integer maxCalorias);
	    List<Receta> filtrarPorNombre(String nombre);


}
