package ong.desi.service;

import ong.desi.controller.view.FamiliaForm;
import ong.desi.entity.Familia;
import ong.desi.exception.Excepcion;

import java.util.List;

import org.springframework.stereotype.Service;


public interface FamiliaService {
    

	    Familia crearFamilia(Familia familia) throws Excepcion;
	    Familia actualizarFamilia(Long id, Familia familiaActualizada);
	   
	    List<Familia> buscarPorNombre(String nombre);
	    List<Familia> listarTodas();
	    
	    Familia buscarPorId(Long id);
	    
	    void eliminarFamilia(Long id);

	    int contarIntegrantesActivos(Long familiaId);
	    FamiliaForm convertirAFamiliaForm(Familia familia);
	    


	}

    