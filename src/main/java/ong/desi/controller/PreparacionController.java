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
import ong.desi.service.RecetaService; // Import para RecetaService

@Controller
@RequestMapping("/preparaciones")
@CrossOrigin(origins = "*")
public class PreparacionController {

    @Autowired
    private PreparacionService preparacionService;

   
    // Inyectamos el servicio de recetas para poder listarlas en el formulario.
    @Autowired
    private RecetaService recetaService;


    // -MÉTODOS PARA LA PÁGINA WEB (Thymeleaf) -

    // Método para el LISTADO (este ya lo tenías, está perfecto)
    @GetMapping
    public String mostrarListadoDePreparaciones(Model model) {
        List<Preparacion> preparaciones = preparacionService.listarPreparaciones();
        model.addAttribute("listaPreparaciones", preparaciones);
        return "preparacion/listado";
    }

    
    // Método para MOSTRAR el formulario de nueva preparación
    @GetMapping("/nueva")
    public String mostrarFormularioDeAlta(Model model) {
        List<Receta> recetasDisponibles = recetaService.listarRecetas();
        model.addAttribute("preparacion", new Preparacion());
        model.addAttribute("recetasDisponibles", recetasDisponibles);
        return "preparacion/formulario";
    }

   
    // Método para GUARDAR la nueva preparación que viene del formulario
    @PostMapping("/guardar")
    public String guardarPreparacion(@ModelAttribute Preparacion preparacion) {
        preparacionService.registrarPreparacion(preparacion);
        return "redirect:/preparaciones"; // Redirige al listado
    }


    // - MÉTODOS PARA LA API (JSON) - Los que ya tenía, ahora en una sub-ruta /api -

    @PostMapping("/api")
    @ResponseBody 
    public ResponseEntity<Preparacion> registrar(@RequestBody Preparacion preparacion) {
        try {
            Preparacion nuevaPreparacion = preparacionService.registrarPreparacion(preparacion);
            return ResponseEntity.ok(nuevaPreparacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/api")
    @ResponseBody
    public List<Preparacion> listar() {
        return preparacionService.listarPreparaciones();
    }
    
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Preparacion> modificar(@PathVariable Long id, @RequestBody Preparacion preparacion) {
        try {
            Preparacion preparacionModificada = preparacionService.modificarFechaPreparacion(id, preparacion);
            return ResponseEntity.ok(preparacionModificada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            preparacionService.eliminarPreparacion(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}