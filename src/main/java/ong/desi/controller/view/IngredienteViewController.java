package ong.desi.controller.view;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import ong.desi.entity.Condimento;
import ong.desi.entity.Estacion;
import ong.desi.entity.Ingrediente;
import ong.desi.entity.Producto;
import ong.desi.entity.TipoIngrediente;
import ong.desi.exception.Excepcion;
import ong.desi.repository.IngredienteRepository;
import ong.desi.service.RecetaService;

@Controller
@RequestMapping("/ingredientes")
public class IngredienteViewController {

    @Autowired private IngredienteRepository ingredienteRepository;
    @Autowired
    private RecetaService recetaService;


    /* --- Formulario alta --- */
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new IngredienteForm());
        model.addAttribute("estaciones", Estacion.values());
        model.addAttribute("tipos", TipoIngrediente.values());
        return "ingredientes/nuevo"; 
    }
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") IngredienteForm form,
                          BindingResult bindingResult,
                          RedirectAttributes attr,
                          Model model) {

        // Validaciones de formulario
        if (bindingResult.hasErrors()) {
            model.addAttribute("estaciones", Estacion.values());
            model.addAttribute("tipos", TipoIngrediente.values());
            return "ingredientes/nuevo";
        }
        System.out.println(">>> Tipo: " + form.getTipo());
        System.out.println(">>> Hay errores? " + bindingResult.hasErrors());
        bindingResult.getAllErrors().forEach(e -> System.out.println(">>> Error: " + e.getDefaultMessage()));

        try {
          
            recetaService.guardarIngrediente(form); 

            attr.addFlashAttribute("mensaje", "Ingrediente guardado exitosamente");
            return "redirect:/ingredientes/nuevo";

        } catch (Excepcion e) {
            
            model.addAttribute("error", "Error al guardar ingrediente: " + e.getMessage());
            model.addAttribute("estaciones", Estacion.values());
            model.addAttribute("tipos", TipoIngrediente.values());
            return "ingredientes/nuevo";
        }
    }
    @GetMapping("/buscar")
    public String listarIngredientes(@RequestParam(required = false) String nombre,
                                     @RequestParam(required = false) Estacion estacion,
                                     @RequestParam(required = false) TipoIngrediente tipo,
                                     Model model) {

        List<Ingrediente> ingredientes = recetaService.buscarIngredientes(nombre, estacion, tipo);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("param", Map.of("nombre", nombre, "estacion", estacion, "tipo", tipo));
        model.addAttribute("estaciones", Estacion.values());
        model.addAttribute("tipos", TipoIngrediente.values());

        return "ingredientes/listado";
    }
    @GetMapping("/listado")
    public String listado(Model model) {
        model.addAttribute("ingredientes", recetaService.obtenerIngredientesActivos());
        model.addAttribute("estaciones", Estacion.values());
        model.addAttribute("tipos", TipoIngrediente.values());
        // Agrege param vacío para evitar null
        model.addAttribute("param", Map.of("nombre", "", "estacion", "", "tipo", ""));
        return "ingredientes/listado";
    }


    @GetMapping("/editar/{id}")
    public String editarIngrediente(@PathVariable Long id, Model model) {
        Ingrediente ingrediente = ingredienteRepository.findById(id)
            .orElseThrow(() -> new Excepcion("Ingrediente no encontrado"));

        IngredienteForm form = new IngredienteForm(ingrediente);
        model.addAttribute("form", form);
        model.addAttribute("estaciones", Estacion.values());
        model.addAttribute("tipos", TipoIngrediente.values());
        return "ingredientes/nuevo"; // Reutilizás la misma vista
    }
    @GetMapping("/baja/{id}")
    public String darDeBaja(@PathVariable Long id, RedirectAttributes attr) {
        Ingrediente ing = ingredienteRepository.findById(id)
            .orElseThrow(() -> new Excepcion("Ingrediente no encontrado"));

        ing.setActivo(false);
        ingredienteRepository.save(ing);
        attr.addFlashAttribute("mensaje", "Ingrediente dado de baja");

        return "redirect:/ingredientes/listado";
    }
    public List<Ingrediente> obtenerIngredientesActivos() {
        return ingredienteRepository.findAll()
            .stream()
            .filter(i -> i.getActivo() == null || i.getActivo()) // solo los activos
            .collect(Collectors.toList());
    }

}