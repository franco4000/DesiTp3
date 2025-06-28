package ong.desi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ong.desi.entity.Familia;
import ong.desi.exception.Excepcion;
import ong.desi.service.FamiliaService;

import java.util.List;

//ALTA DE FAMILIA
@RestController
@RequestMapping("/api/familias")
public class FamiliaController {

    @Autowired
    private FamiliaService familiaService;// Inyecta automáticamente una instancia del servicio FamiliaService.

    // Crear nueva familia 
    @PostMapping
    public ResponseEntity<Familia> crear(@RequestBody Familia familia) throws Excepcion {//Como voluntario quiero dar de alta una familia al sistema...
        return ResponseEntity.ok(familiaService.crearFamilia(familia));
    }

    // LISTADO DE FAMILIAS activas 
    @GetMapping
    public List<Familia> listarTodas() {
        return familiaService.listarTodas();//Como voluntario quiero listar las familias asistidas...
    }

    // MODIFICACION DE FAMILIA 
    @PutMapping("/{id}")
    public ResponseEntity<Familia> actualizar(@PathVariable Long id, @RequestBody Familia familia) {
        return ResponseEntity.ok(familiaService.actualizarFamilia(id, familia));//Como voluntario quiero poder modificar datos de una familia...
    }

    // Buscar por nombre (FILTRO POR NOMBRE) 
    @GetMapping("/buscar")
    public List<Familia> buscarPorNombre(@RequestParam String nombre) {
        return familiaService.buscarPorNombre(nombre);
    }

    //  FILTRO POR NUMERO DE FAMILIA (ID)
    @GetMapping("/buscar/id/{id}")
    public ResponseEntity<Familia> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(familiaService.buscarPorId(id));
    }
    
    //BAJA LOGICA DE FAMILIA 
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        familiaService.eliminarFamilia(id);
        return ResponseEntity.ok().build();//Como voluntario quiero poder dar de baja una familia...
    }

}


