package ong.desi.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ong.desi.entity.Preparacion;
import ong.desi.entity.Receta;
import ong.desi.service.PreparacionService;
import ong.desi.service.RecetaService;

@Controller
@RequestMapping("/preparaciones")
@CrossOrigin(origins = "*")
public class PreparacionController {

    @Autowired
    private PreparacionService preparacionService;

    @Autowired
    private RecetaService recetaService;


    // -MÉTODOS PARA LA PÁGINA WEB (VISTAS) -

    // Muestra el listado de todas las preparaciones
    @GetMapping
    public String mostrarListadoDePreparaciones(Model model) {
        List<Preparacion> preparaciones = preparacionService.listarPreparaciones();
        model.addAttribute("listaPreparaciones", preparaciones);
        return "preparacion/listado";
    }
    
    // Muestra el formulario para crear una NUEVA preparación
    @GetMapping("/nueva")
    public String mostrarFormularioDeAlta(Model model) {
        List<Receta> recetasDisponibles = recetaService.listarRecetas();
        model.addAttribute("preparacion", new Preparacion());
        model.addAttribute("recetasDisponibles", recetasDisponibles);
        return "preparacion/formulario";
    }

    // Muestra el formulario con los datos de una preparación EXISTENTE para editarla
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Long id, Model model) {
        Preparacion preparacion = preparacionService.buscarPorId(id);
        List<Receta> recetasDisponibles = recetaService.listarRecetas();
        model.addAttribute("preparacion", preparacion);
        model.addAttribute("recetasDisponibles", recetasDisponibles);
        return "preparacion/formulario";
    }

    // Procesa el envío del formulario, tanto para CREAR como para EDITAR
    @PostMapping("/guardar")
    public String guardarPreparacion(@ModelAttribute Preparacion preparacion) {
        // Si la preparación tiene un ID, significa que es una edición.
        if (preparacion.getId() != null) {
            preparacionService.modificarFechaPreparacion(preparacion.getId(), preparacion);
        } else {
            // Si no tiene ID, es una creación nueva.
            preparacionService.registrarPreparacion(preparacion);
        }
        return "redirect:/preparaciones"; // Vuelve al listado
    }
    
    // Procesa la baja lógica desde el botón de la tabla
    @PostMapping("/eliminar/{id}")
    public String eliminarPreparacion(@PathVariable Long id) {
        preparacionService.eliminarPreparacion(id);
        return "redirect:/preparaciones";
    }


    // -MÉTODOS PARA LA API JSON - Los que ya tenía, ahora en una sub-ruta /api -

    @PostMapping("/api")
    @ResponseBody 
    public ResponseEntity<Preparacion> registrarApi(@RequestBody Preparacion preparacion) {
        try {
            Preparacion nuevaPreparacion = preparacionService.registrarPreparacion(preparacion);
            return ResponseEntity.ok(nuevaPreparacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/api")
    @ResponseBody
    public List<Preparacion> listarApi() {
        return preparacionService.listarPreparaciones();
    }
    
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Preparacion> modificarApi(@PathVariable Long id, @RequestBody Preparacion preparacion) {
        try {
            Preparacion preparacionModificada = preparacionService.modificarFechaPreparacion(id, preparacion);
            return ResponseEntity.ok(preparacionModificada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarApi(@PathVariable Long id) {
        try {
            preparacionService.eliminarPreparacion(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}