package ong.desi.controller.view;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ong.desi.entity.Familia;
import ong.desi.service.FamiliaService;

import java.util.List;


@Controller
@RequestMapping("/vista/familias")
@SessionAttributes("familiaForm")
public class FamiliaViewController {

    @Autowired
    private FamiliaService familiaService;
    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        FamiliaForm form = new FamiliaForm();
        form.getIntegrantes().add(new IntegranteForm()); // agrega un integrante vacío
        model.addAttribute("familiaForm", form);
        return "familias/familia-form";
    }

    @GetMapping("/familia/form")
    public String mostrarFormularioEdicion(@RequestParam Long id, Model model) {
        Familia familia = familiaService.buscarPorId(id);
        if (familia == null || !familia.isActiva()) {
            return "redirect:/vista/familias";
        }

        FamiliaForm form = familiaService.convertirAFamiliaForm(familia);
        model.addAttribute("familiaForm", form);
        return "familias/familia-form";
    }
    
    @ModelAttribute("familiaForm")
    public FamiliaForm inicializarFormulario() {
        return new FamiliaForm();
    }


    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("familiaForm") FamiliaForm form,
                          BindingResult result, Model model, RedirectAttributes redirect,
                          SessionStatus status) {

    	System.out.println("Form recibido: " + form);
      
    	  if (result.hasErrors()) {
    		  
    	  System.out.println("Errores de validación: " + result.getAllErrors());
            return "familias/familia-form";
        }

    	 try {
   
              if (form.getId() != null) {
               //  edición buscamos la familia existente y actualizamos 
            	  
            Familia existente = familiaService.buscarPorId(form.getId());
            if (existente != null) {
                existente.setNombre(form.getNombre());
                existente.setFechaAlta(form.getFechaAlta());
               familiaService.actualizarFamilia(existente.getId(), existente);
               redirect.addFlashAttribute("mensaje", "¡Familia editada con éxito!");
            } else {
               model.addAttribute("error", "La familia no existe.");
                return "familias/familia-form";
            }
        } else {
            //  alta
            Familia nueva = form.toEntidad();
            familiaService.crearFamilia(nueva);
            redirect.addFlashAttribute("mensaje", "¡Familia registrada con éxito!");
        }

              
         status.setComplete();
        return "redirect:/vista/familias";
        
     }catch (IllegalArgumentException ex) {
         model.addAttribute("error", ex.getMessage());
         model.addAttribute("familiaForm", form); 
         return "familias/familia-form";
     }
    	 
    }
    
    @GetMapping
    public String listarFamilias(Model model) {
    	 List<Familia> familias = familiaService.listarTodas();
    	 model.addAttribute("familias", familias);
        return "familias/listado"; 
    }
    @PostMapping("/familia/form/agregarIntegrante")
    public String agregarIntegrante(@ModelAttribute("familiaForm") FamiliaForm familiaForm, Model model) {
        familiaForm.getIntegrantes().add(new IntegranteForm());
        model.addAttribute("familiaForm", familiaForm);
        return "familias/familia-form";
    }

    @PostMapping("/familia/form/eliminarIntegrante")
    public String eliminarIntegrante(@ModelAttribute("familiaForm") FamiliaForm familiaForm,
                                     @RequestParam("indice") int indice,
                                     Model model) {
        if (indice >= 0 && indice < familiaForm.getIntegrantes().size()) {
            familiaForm.getIntegrantes().remove(indice);
        }
        
        model.addAttribute("familiaForm", familiaForm);
        return "familias/familia-form";
    }
 
    @PostMapping("/eliminar")
    public String eliminarFamilia(@RequestParam("id") Long id) {
        Familia familia = familiaService.buscarPorId(id);
        if (familia != null) {
            familia.setActiva(false); // eliminación lógica
            familiaService.actualizarFamilia(familia.getId(), familia);
        }
        return "redirect:/vista/familias";
    }
    
}