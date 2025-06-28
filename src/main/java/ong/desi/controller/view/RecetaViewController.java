package ong.desi.controller.view;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ong.desi.entity.Ingrediente;
import ong.desi.entity.ItemReceta;
import ong.desi.entity.Receta;
import ong.desi.exception.Excepcion;
import ong.desi.repository.IngredienteRepository;
import ong.desi.service.RecetaService;

@Controller
@RequestMapping("/recetas")
public class RecetaViewController {

    @Autowired RecetaService recetaService;
    @Autowired IngredienteRepository ingredienteRepository;

    /* -------- Alta ---------- */
    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        Receta receta = new Receta();
        for (int i = 0; i < 3; i++) receta.getItems().add(new ItemReceta());

        model.addAttribute("receta", receta);
        model.addAttribute("ingredientesDisponibles", ingredienteRepository.findAll());
        return "recetas/receta";              //  TEMPLATES
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Receta receta, BindingResult result,
    		Model model,  RedirectAttributes redirectAttrs) {
    	
    	if (result.hasErrors()) {
            model.addAttribute("ingredientesDisponibles", ingredienteRepository.findAll());
            return "recetas/receta";
        }

        try {
            receta.getItems().forEach(it -> it.setReceta(receta));
            recetaService.crearReceta(receta);
            return "redirect:/recetas/exito";
        } catch (Excepcion e) {
            model.addAttribute("error", e.getMensaje());
            model.addAttribute("ingredientesDisponibles", ingredienteRepository.findAll());
            redirectAttrs.addFlashAttribute("mensaje", "¡Receta guardada exitosamente!");
            return "redirect:/recetas";

         
        }
        
    }

    /* -------- Éxito ---------- */
    @GetMapping("/exito")
    public String exito() {
        return "exito";
    }
    /* -------- Listado (+ filtros) ---------- */
    @GetMapping
    public String listar(@RequestParam(required = false) String nombre,
                         @RequestParam(required = false) Float minCalorias,
                         @RequestParam(required = false) Float maxCalorias,
                         Model model) {
    	   // Filtra si hay al menos uno de los parámetros no nulos
        if (nombre != null || minCalorias != null || maxCalorias != null) {
            List<Receta> recetas = recetaService.filtrar(nombre, minCalorias, maxCalorias);
            model.addAttribute("recetas", recetas);
        } else {
            // Solo muestra la página, sin buscar ni mostrar nada
            model.addAttribute("recetas", null);
        }

        return "recetas/listado";
}
    /* -------- Editar ---------- */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
    	  Receta receta = recetaService.buscarPorId(id)
    			  .orElseThrow(() ->
                  new RuntimeException("Receta no encontrada"));
        receta.getItems().forEach(i -> i.setIngredienteId(i.getIngrediente().getId())); 
        model.addAttribute("receta", receta);
        model.addAttribute("ingredientesDisponibles", ingredienteRepository.findAll());
        return "recetas/receta";
    }

    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute Receta receta,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ingredientesDisponibles", ingredienteRepository.findAll());
            return "recetas/receta";
        }
        recetaService.modificarReceta(id, receta);
        return "redirect:/recetas";
    }

    /* -------- Eliminar ---------- */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        recetaService.eliminarReceta(id);        // baja lógica
        return "redirect:/recetas";
    }
    @PostMapping("/editar")
    public String editarIngrediente(@ModelAttribute("form") IngredienteForm form, RedirectAttributes attr) {
        // Aquí transformás el form a entidad o buscás el ingrediente y actualizás campos
        Ingrediente ingrediente = ingredienteRepository.findById(form.getId())
                                       .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));
        // Actualizás campos de ingrediente con datos del form
        ingrediente.setNombre(form.getNombre());
        // ... otros campos

        ingredienteRepository.save(ingrediente);

        attr.addFlashAttribute("mensaje", "Ingrediente actualizado");
        return "redirect:/ingredientes/listado";
    }

}
