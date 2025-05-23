package ong.desi;

import ong.desi.entity.Familia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FamiliaControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCrearFamilia() {
        Familia familia = new Familia();
        familia.setNombre("Familia Rest Test");
        familia.setFechaAlta(LocalDate.now());

        ResponseEntity<Familia> response = restTemplate.postForEntity("/api/familias", familia, Familia.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNombre()).isEqualTo("Familia Rest Test");
    }
    

    @Test
    public void testListarFamilias() {
        ResponseEntity<Familia[]> response = restTemplate.getForEntity("/api/familias", Familia[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testBuscarFamiliaPorNombre() {
        Familia familia = new Familia();
        familia.setNombre("FamiliaNombreRest");
        familia.setFechaAlta(LocalDate.now());
        restTemplate.postForEntity("/api/familias", familia, Familia.class);

        ResponseEntity<Familia[]> response = restTemplate.getForEntity("/api/familias/buscar?nombre=FamiliaNombreRest", Familia[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()[0].getNombre()).isEqualTo("FamiliaNombreRest");
    }

    @Test
    public void testModificarFamilia() {
        Familia familia = new Familia();
        familia.setNombre("Original Rest");
        familia.setFechaAlta(LocalDate.now());

        ResponseEntity<Familia> createResponse = restTemplate.postForEntity("/api/familias", familia, Familia.class);
        Familia creada = createResponse.getBody();
        creada.setNombre("Modificada Rest");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Familia> requestUpdate = new HttpEntity<>(creada, headers);

        ResponseEntity<Familia> response = restTemplate.exchange("/api/familias/" + creada.getId(), HttpMethod.PUT, requestUpdate, Familia.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNombre()).isEqualTo("Modificada Rest");
    }

    @Test
    public void testEliminarFamilia() {
        Familia familia = new Familia();
        familia.setNombre("Eliminar Rest");
        familia.setFechaAlta(LocalDate.now());

        ResponseEntity<Familia> createResponse = restTemplate.postForEntity("/api/familias", familia, Familia.class);
        Long id = createResponse.getBody().getId();

        restTemplate.delete("/api/familias/" + id);

        ResponseEntity<Familia> getResponse = restTemplate.getForEntity("/api/familias/buscar/id/" + id, Familia.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().isActiva()).isFalse();
    }

}