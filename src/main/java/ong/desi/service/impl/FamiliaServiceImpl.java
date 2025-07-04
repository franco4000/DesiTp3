package ong.desi.service.impl;

import ong.desi.controller.view.FamiliaForm;
import ong.desi.controller.view.IntegranteForm;
import ong.desi.entity.Familia;
import ong.desi.entity.Integrante;
import ong.desi.exception.Excepcion;
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
	  public Familia crearFamilia(Familia familia) throws Excepcion {
	      // Validación básica del nombre
	      if (familia.getNombre() == null || familia.getNombre().trim().isEmpty()) {
	          throw new Excepcion("El nombre de la familia es requerido", "nombre");
	      }

	      // Simulación de fecha de última asistencia
	      familia.setFechaUltimaAsistencia(LocalDate.now());

	      // Filtramos los integrantes válidos (no vacíos)
	      List<Integrante> integrantesValidos = familia.getIntegrantes() != null
	          ? familia.getIntegrantes().stream()
	              .filter(i -> !esIntegranteVacio(i))
	              .collect(Collectors.toList())
	          : List.of();

	      // Validación de DNI duplicado
	      for (Integrante integrante : integrantesValidos) {
	          if (integranteRepository.existsByDni(integrante.getDni())) {
	              throw new Excepcion("Ya existe un integrante con el DNI " + integrante.getDni(), "dni");
	          }
	      }

	      // Guardamos la familia
	      Familia familiaGuardada = familiaRepository.save(familia);

	      // Guardamos los integrantes válidos y vinculamos con la familia
	      for (Integrante integrante : integrantesValidos) {
	          integrante.setFamilia(familiaGuardada);
	          integrante.setActivo(true); // Aseguramos estado activo
	          integranteRepository.save(integrante);
	      }

	      return familiaGuardada;
	  }

	  private boolean esIntegranteVacio(Integrante i) {
		    return i.getDni() == null &&
		           (i.getNombre() == null || i.getNombre().isBlank()) &&
		           (i.getApellido() == null || i.getApellido().isBlank()) &&
		           i.getFechaNacimiento() == null &&
		           (i.getParentesco() == null || i.getParentesco().isBlank()) &&
		           i.getOcupacion() == null;
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
	                 boolean sigue = familiaActualizada.getIntegrantes().stream()
	                     .anyMatch(i -> i.getDni() != null && i.getDni().equals(existente.getDni()));
	                 if (!sigue) {
	                     existente.setActivo(false);
	                     integranteRepository.save(existente);
	                 }
	             }
	            // Segundo, recorremos los nuevos integrantes
	            for (Integrante nuevo : familiaActualizada.getIntegrantes()) {
	            	 //  Valida si el integrante está vacío
	                boolean vacio = 
	                    (nuevo.getDni() == null) &&
	                    (nuevo.getNombre() == null || nuevo.getNombre().isBlank()) &&
	                    (nuevo.getApellido() == null || nuevo.getApellido().isBlank()) &&
	                    (nuevo.getFechaNacimiento() == null) &&
	                    (nuevo.getParentesco() == null || nuevo.getParentesco().isBlank()) &&
	                    (nuevo.getOcupacion() == null);

	                if (vacio) {
	                    System.out.println(">>> Integrante vacío detectado y omitido");
	                    continue; // saltamos este integrante
	                }
	            	
	                // Buscar por DNI
	                Integrante existente = null;
	                if (nuevo.getDni() != null) {
	                    existente = integranteRepository.findByDni(nuevo.getDni()).orElse(null);
	                }

	                if (existente != null) {
	                    // Actualizar campos del integrante existente
	                    existente.setNombre(nuevo.getNombre());
	                    existente.setApellido(nuevo.getApellido());
	                    existente.setFechaNacimiento(nuevo.getFechaNacimiento());
	                    existente.setOcupacion(nuevo.getOcupacion());
	                    existente.setParentesco(nuevo.getParentesco());
	                    existente.setEdad(nuevo.getEdad());
	                    existente.setActivo(true);
	                    integranteRepository.save(existente);
	                } else {
	                    // Crear nuevo integrante
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
	        if (integranteForms.isEmpty()) {
	            integranteForms.add(new IntegranteForm());
	        }
	        form.setIntegrantes(integranteForms);
	        return form;
	    }

}