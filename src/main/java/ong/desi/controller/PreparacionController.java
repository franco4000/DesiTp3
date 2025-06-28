package ong.desi.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ong.desi.entity.Preparacion;
import ong.desi.service.PreparacionService;

@RestController
@RequestMapping("/api/preparaciones") // La URL base para todas las operaciones
@CrossOrigin(origins = "*") // Permite que sea llamado desde cualquier frontend
public class PreparacionController {

    @Autowired
    private PreparacionService preparacionService;

    //  ALTA de una Preparación 
    // Responde a peticiones POST en /api/preparaciones
    @PostMapping
    public ResponseEntity<Preparacion> registrar(@RequestBody Preparacion preparacion) {
        try {
            Preparacion nuevaPreparacion = preparacionService.registrarPreparacion(preparacion);
            return ResponseEntity.ok(nuevaPreparacion);
        } catch (RuntimeException e) {
            // Si el servicio lanza un error (ej: "no hay stock"), devolvemos un error al cliente
            return ResponseEntity.badRequest().body(null);
        }
    }

    // LISTADO de Preparaciones 
    // Responde a peticiones GET en /api/preparaciones
    @GetMapping
    public List<Preparacion> listar() {
        return preparacionService.listarPreparaciones();
    }
    
    //  MODIFICACIÓN de una Preparación 
    // Responde a peticiones PUT en /api/preparaciones/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Preparacion> modificar(@PathVariable Long id, @RequestBody Preparacion preparacion) {
        try {
            Preparacion preparacionModificada = preparacionService.modificarFechaPreparacion(id, preparacion);
            return ResponseEntity.ok(preparacionModificada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // BAJA de una Preparación 
    // Responde a peticiones DELETE en /api/preparaciones/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            preparacionService.eliminarPreparacion(id);
            return ResponseEntity.ok().build(); // Devuelve una respuesta vacía con código 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}