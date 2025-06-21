package ong.desi.service.impl;

import ong.desi.controller.view.FamiliaForm;
import ong.desi.controller.view.IntegranteForm;
import ong.desi.entity.Familia;
import ong.desi.entity.Integrante;
import ong.desi.repository.FamiliaRepository;
import ong.desi.repository.IntegranteRepository;
import ong.desi.service.FamiliaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service 
@Transactional
public class FamiliaServiceImpl implements FamiliaService {//implementa los métodos  la interfaz FamiliaService.
	
	 @Autowired 
	    private FamiliaRepository familiaRepository;

	  @Autowired
	    private IntegranteRepository integranteRepository;

	  @Override
	  public Familia crearFamilia(Familia familia) {// validando datos y guardando tanto la familia como sus integrantes.
	      // Validación básica del nombre
	      if (familia.getNombre() == null || familia.getNombre().trim().isEmpty()) {
	          throw new IllegalArgumentException("El nombre de la familia es requerido");
	      }

	      // Validación de DNI duplicado
	      if (familia.getIntegrantes() != null) {
	          for (Integrante integrante : familia.getIntegrantes()) {
	              if (integranteRepository.existsByDni(integrante.getDni())) {
	                  throw new IllegalArgumentException("Ya existe un integrante con el DNI " + integrante.getDni());
	              }
	          }
	      }

	      //  Simulación de fecha de última asistencia
	      familia.setFechaUltimaAsistencia(LocalDate.now());

	      // Guardamos la familia
	      Familia familiaGuardada = familiaRepository.save(familia);

	      // Guardamos los integrantes y vinculamos con la familia
	      if (familia.getIntegrantes() != null) {
	          for (Integrante integrante : familia.getIntegrantes()) {
	              integrante.setFamilia(familiaGuardada);
	              integranteRepository.save(integrante);
	          }
	      }

	      return familiaGuardada;
	  }

	  
	  
	    @Override
	    public List<Familia> listarTodas() {//Devuelve una lista de familias activas solamente.
	        return familiaRepository.findByActivaTrue();
	    }
	    
	    @Override
	    public List<Familia> buscarPorNombre(String nombre) {
	        return familiaRepository.buscarPorNombreParcial(nombre); 
	    }


	    @Override
	    public Familia buscarPorId(Long id) {
	        return familiaRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Familia no encontrada"));
	    }

	    @Override
	    public int contarIntegrantesActivos(Long familiaId) {
	        Familia familia = familiaRepository.findById(familiaId)
	            .orElseThrow(() -> new RuntimeException("Familia no encontrada"));
	        return (int) familia.getIntegrantes().stream()
	            .filter(Integrante::isActivo)
	            .count();
	    }

	
	    @Override
	    public void eliminarFamilia(Long id) {// una baja lógica de la familia y sus integrantes.
	        Familia familia = familiaRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Familia no encontrada"));

	        // Baja de la familia
	        familia.setActiva(false);

	        // Baja de los integrantes
	        if (familia.getIntegrantes() != null) {
	            for (Integrante integrante : familia.getIntegrantes()) {
	                integrante.setActivo(false);
	                integranteRepository.save(integrante); // Guarda cambio
	            }
	        }

	        // Guardamos familia
	        familiaRepository.save(familia);
	    }
	    
	    
	    @Override
	    public Familia actualizarFamilia(Long id, Familia familiaActualizada) {
	        Familia familiaExistente = familiaRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Familia no encontrada"));

	        // 1 actualizamos nombre y fecha, no el ID
	        familiaExistente.setNombre(familiaActualizada.getNombre());
	        familiaExistente.setFechaAlta(familiaActualizada.getFechaAlta());

	        // 2. Actualizamos integrantes  Marcar como inactivos los que ya no están
	        if (familiaActualizada.getIntegrantes() != null) {
	            // Primero, marcamos como inactivos los actuales que no estén en la nueva lista
	            for (Integrante existente : familiaExistente.getIntegrantes()) {
	                boolean sigueEnLista = familiaActualizada.getIntegrantes().stream()
	                    .anyMatch(i -> i.getDni().equals(existente.getDni()));
	                if (!sigueEnLista) {
	                    existente.setActivo(false); // eliminación lógica
	                    integranteRepository.save(existente);
	                }
	            }

	            // Segundo, recorremos los nuevos integrantes
	            for (Integrante nuevo : familiaActualizada.getIntegrantes()) {
	                // Verificamos si ya existe por DNI
	                Integrante existente = integranteRepository.findByDni(nuevo.getDni()).orElse(null);
	                if (existente != null) {
	                    // Actualizamos datos del integrante existente
	                    existente.setNombre(nuevo.getNombre());
	                    existente.setApellido(nuevo.getApellido());
	                    existente.setFechaNacimiento(nuevo.getFechaNacimiento());
	                    existente.setOcupacion(nuevo.getOcupacion());
	                    existente.setParentesco(nuevo.getParentesco());
	                    existente.setEdad(nuevo.getEdad());
	                    existente.setActivo(true); // por si estaba dado de baja
	                    integranteRepository.save(existente);
	                } else {
	                    // Es un nuevo integrante
	                    nuevo.setFamilia(familiaExistente);
	                    nuevo.setActivo(true);
	                    integranteRepository.save(nuevo);
	                }
	            }
	        }

	        return familiaRepository.save(familiaExistente);//guardamos cambios
	    }
	    public FamiliaForm convertirAFamiliaForm(Familia familia) {
	        FamiliaForm form = new FamiliaForm();
	        form.setId(familia.getId());
	        form.setNombre(familia.getNombre());
	        form.setFechaAlta(familia.getFechaAlta());

	        List<IntegranteForm> integranteForms = familia.getIntegrantes().stream()
	                .filter(Integrante::isActivo)
	                .map(integrante -> {
	                    IntegranteForm iform = new IntegranteForm();
	                    iform.setId(integrante.getId());
	                    iform.setNombre(integrante.getNombre());
	                    iform.setApellido(integrante.getApellido());
	                    iform.setDni(integrante.getDni());
	                    iform.setFechaNacimiento(integrante.getFechaNacimiento());
	                    iform.setDomicilio(integrante.getDomicilio());
	                    iform.setEdad(integrante.getEdad());
	                    iform.setParentesco(integrante.getParentesco());
	                    iform.setOcupacion(integrante.getOcupacion());
	                    iform.setActiva(true);
	                    return iform;
	                })
	                .collect(Collectors.toList());

	        form.setIntegrantes(integranteForms);
	        return form;
	    }

}