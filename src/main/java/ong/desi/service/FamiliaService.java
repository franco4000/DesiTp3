package ong.desi.service;

import ong.desi.entity.Familia;
import java.util.List;

import org.springframework.stereotype.Service;


@Service
public interface FamiliaService {
    

	    Familia crearFamilia(Familia familia);
	    
	    Familia actualizarFamilia(Long id, Familia familiaActualizada);
	   
	    List<Familia> buscarPorNombre(String nombre);
	    List<Familia> listarTodas();
	    
	    Familia buscarPorId(Long id);
	    
	    void eliminarFamilia(Long id);
	}

    