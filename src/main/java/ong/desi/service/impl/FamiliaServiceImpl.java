package ong.desi.service.impl;

import ong.desi.entity.Familia;
import ong.desi.entity.Integrante;
import ong.desi.repository.FamiliaRepository;
import ong.desi.repository.IntegranteRepository;
import ong.desi.service.FamiliaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FamiliaServiceImpl implements FamiliaService {
	
	 @Autowired
	    private FamiliaRepository familiaRepository;

	  @Autowired
	    private IntegranteRepository integranteRepository;

	  @Override
	    public Familia crearFamilia(Familia familia) {
	        // Validación básica
	        if(familia.getNombre() == null || familia.getNombre().trim().isEmpty()) {
	            throw new IllegalArgumentException("El nombre de la familia es requerido");
	        }

	        // Guardar familia
	        Familia familiaGuardada = familiaRepository.save(familia);

	        // Asignar familia a integrantes
	        if(familia.getIntegrantes() != null) {
	            for(Integrante integrante : familia.getIntegrantes()) {
	                integrante.setFamilia(familiaGuardada);
	                integranteRepository.save(integrante);
	            }
	        }
	        return familiaGuardada;
	    }

	    @Override
	    public List<Familia> listarTodas() {
	        return familiaRepository.findAllActivas();
	    }
	    
	    @Override
	    public List<Familia> buscarPorNombre(String nombre) {
	        return familiaRepository.buscarPorNombreParcial(nombre); // o findByNombreIgnoreCase(nombre)
	    }


	    @Override
	    public Familia buscarPorId(Long id) {
	        return familiaRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Familia no encontrada"));
	    }

	
	    @Override
	    public void eliminarFamilia(Long id) {
	        Familia familia = familiaRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Familia no encontrada"));

	        familia.setActiva(false);
	        familiaRepository.save(familia);
	    }

	    
	    
	    @Override
	    public Familia actualizarFamilia(Long id, Familia familiaActualizada) {
	        Familia familiaExistente = familiaRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Familia no encontrada"));

	        // Solo actualizamos nombre y fecha, no el ID
	        familiaExistente.setNombre(familiaActualizada.getNombre());
	        familiaExistente.setFechaAlta(familiaActualizada.getFechaAlta());

	        return familiaRepository.save(familiaExistente);
	    }

	}