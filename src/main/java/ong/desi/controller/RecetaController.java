package ong.desi.controller;

import ong.desi.entity.Receta;

import ong.desi.entity.Receta;
import ong.desi.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/recetas")
@CrossOrigin(origins = "*")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;
    
// GET RECETA listado con filtro por nombre y calorias (HU4)
    @GetMapping
    public List<Receta> filtrar(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) Float minCalorias,
            @RequestParam(required = false) Float maxCalorias) {
return recetaService.filtrar(nombre, minCalorias, maxCalorias);
}

//    POST RECETA alta de recetas con ingrediente HU1 
    @PostMapping
    public Receta crearReceta(@RequestBody Receta receta) {
        return recetaService.crearReceta(receta);
    }

//    PUT RECETA Modificar descripcion e ingredientes (HU3)
    @PutMapping("/{id}")
    public Receta actualizarReceta(@PathVariable Long id, @RequestBody Receta receta) {
        return recetaService.modificarReceta(id, receta);
    }

//    DELETE RECETA Baja logica de receta (HU2)
    @DeleteMapping("/{id}")
    public void eliminarReceta(@PathVariable Long id) {
        recetaService.eliminarReceta(id);
    }

    }


