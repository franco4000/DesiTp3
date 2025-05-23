package ong.desi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ong.desi.entity.Familia;
import ong.desi.service.FamiliaService;

import java.util.List;

@RestController
@RequestMapping("/api/familias")
public class FamiliaController {

    @Autowired
    private FamiliaService familiaService;

    // Crear nueva familia
    @PostMapping
    public ResponseEntity<Familia> crear(@RequestBody Familia familia) {
        return ResponseEntity.ok(familiaService.crearFamilia(familia));
    }

    // Listar todas las familias activas
    @GetMapping
    public List<Familia> listarTodas() {
        return familiaService.listarTodas();
    }

    // Actualizar familia
    @PutMapping("/{id}")
    public ResponseEntity<Familia> actualizar(@PathVariable Long id, @RequestBody Familia familia) {
        return ResponseEntity.ok(familiaService.actualizarFamilia(id, familia));
    }

    // Buscar por nombre (filtro parcial)
    @GetMapping("/buscar")
    public List<Familia> buscarPorNombre(@RequestParam String nombre) {
        return familiaService.buscarPorNombre(nombre);
    }

    //  Buscar por número de familia (ID)
    @GetMapping("/buscar/id/{id}")
    public ResponseEntity<Familia> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(familiaService.buscarPorId(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        familiaService.eliminarFamilia(id);
        return ResponseEntity.ok().build();
    }

}


