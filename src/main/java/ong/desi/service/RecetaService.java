package ong.desi.service;



import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ong.desi.controller.view.IngredienteForm;
import ong.desi.entity.Estacion;
import ong.desi.entity.Ingrediente;
import ong.desi.entity.Receta;
import ong.desi.entity.TipoIngrediente;
@Service
public interface RecetaService {//metodos de serviceimpl

	    List<Receta> listarRecetas();
	    List<Receta> filtrarPorCalorias(int min, int max);
	    List<Receta> filtrarPorNombreYCalorias(String nombre, int min, int max);
	    Receta crearReceta(Receta receta);
	    Receta modificarReceta(Long id, Receta datos);
	    void eliminarReceta(Long id);
	    void prepararReceta(Long id);
	    List<Receta> filtrar(String nombre, Float minCalorias, Float maxCalorias);
	    List<Receta> filtrarPorNombre(String nombre);
		Optional<Receta> buscarPorId(Long id);
		void guardarIngrediente(IngredienteForm form);
		List<Ingrediente> buscarIngredientes(String nombre, Estacion estacion, TipoIngrediente tipo);
		Object obtenerIngredientesActivos();


	    
}
