package ong.desi;

	import ong.desi.entity.Familia;
	import ong.desi.entity.Integrante;
	import org.springframework.http.*;
	import org.springframework.web.client.RestTemplate;

	import java.util.Collections;
	import java.util.List;

	public class TestABMCFamilia {

	    private static final String BASE_URL = "http://localhost:8080/api/familias";
	    private static final RestTemplate restTemplate = new RestTemplate();

	    public static void main(String[] args) {

	        // Crear familia
	        Familia nuevaFamilia = new Familia();
	        nuevaFamilia.setNombre("Familia Ramírez");

	        Integrante integrante = new Integrante();
	        integrante.setNombre("Juan Ramírez");
	        integrante.setEdad(40);
	        integrante.setParentesco("Padre");

	        nuevaFamilia.setIntegrantes(List.of(integrante));

	        // Enviar POST
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<Familia> request = new HttpEntity<>(nuevaFamilia, headers);

	        ResponseEntity<Familia> response = restTemplate.postForEntity(BASE_URL, request, Familia.class);
	        Familia creada = response.getBody();

	        System.out.println("Familia creada: " + creada);

	        // Obtener listado
	        Familia[] familias = restTemplate.getForObject(BASE_URL, Familia[].class);
	        System.out.println("Listado de familias:");
	        for (Familia f : familias) {
	            System.out.println(" - " + f.getNombre() + " (ID: " + f.getId() + ")");
	        }

	        // Modificar familia
	        if (creada != null) {
	            creada.setNombre("Familia Ramírez Modificada");
	            HttpEntity<Familia> putRequest = new HttpEntity<>(creada, headers);
	            restTemplate.exchange(BASE_URL + "/" + creada.getId(), HttpMethod.PUT, putRequest, Void.class);
	            System.out.println("✏️ Familia modificada.");
	        }

	        // Eliminar (baja lógica)
	        if (creada != null) {
	            restTemplate.delete(BASE_URL + "/" + creada.getId());
	            System.out.println("Familia eliminada (lógicamente).");
	        }

	        // Consultar nuevamente
	        familias = restTemplate.getForObject(BASE_URL, Familia[].class);
	        System.out.println("Familias restantes:");
	        for (Familia f : familias) {
	            System.out.println(" - " + f.getNombre());
	        }
	    }
	}

