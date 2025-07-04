package ong.desi.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
import ong.desi.exception.Excepcion;
import ong.desi.service.FamiliaService;

import java.beans.PropertyEditorSupport;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


@Controller
@RequestMapping("/vista/familias")
@SessionAttributes("familiaForm")
public class FamiliaViewController {
	@GetMapping("/inicio")
	public String paginaPrincipalFamilias() {
	    return "familias/home"; 
	}

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
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Integer.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                    return;
                }


                if (text.contains(",")) {
                    throw new NumberFormatException("Formato inválido: se detectó una coma en un número entero.");
                }

                setValue(Integer.parseInt(text));
            }
        });
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("familiaForm") FamiliaForm form,
                          BindingResult result,
                          Model model,
                          @RequestParam String accion,
                          SessionStatus status,
                          RedirectAttributes redirectAttributes) {

        System.out.println(">>> Acción recibida: " + accion);

        // AGREGAR INTEGRANTE
        if ("agregar".equals(accion)) {
            form.getIntegrantes().add(new IntegranteForm());
            model.addAttribute("familiaForm", form);
            return "familias/familia-form";
        }

        // ELIMINAR INTEGRANTE
        if (accion.startsWith("eliminar_")) {
            int index = Integer.parseInt(accion.replace("eliminar_", ""));
            List<IntegranteForm> integrantes = form.getIntegrantes();

            System.out.println(">>> Eliminando integrante en índice: " + index);
            System.out.println(">>> Cantidad antes de eliminar: " + integrantes.size());

            if (integrantes.size() == 1) {
                // Si hay solo uno, lo limpiamos
                IntegranteForm i = integrantes.get(0);
                i.setDni(null);
                i.setNombre("");
                i.setApellido("");
                i.setFechaNacimiento(null);
                i.setDomicilio("");
                i.setEdad(0);
                i.setParentesco("");
                i.setOcupacion(null);
                System.out.println(">>> Se limpió el único integrante");
            } else if (index >= 0 && index < integrantes.size()) {
                // Si hay más de uno, eliminamos
                integrantes.remove(index);
                System.out.println(">>> Eliminado OK. Nuevo tamaño: " + integrantes.size());
            } else {
                System.out.println(">>> Índice fuera de rango");
            }

            model.addAttribute("familiaForm", form);
            return "familias/familia-form";
        }

        // VALIDACIONES
        if (result.hasErrors()) {
            model.addAttribute("familiaForm", form);
            return "familias/familia-form";
        }

        // GUARDAR FAMILIA (NUEVA O EDITADA)
        try {
            if (form.getId() == null) {
                // Nueva
                familiaService.crearFamilia(form.toEntidad());
                redirectAttributes.addFlashAttribute("mensaje", "¡Familia registrada con éxito!");
            } else {
                // Edición
                familiaService.actualizarFamilia(form.getId(), form.toEntidad());
                redirectAttributes.addFlashAttribute("mensaje", "¡Familia editada con éxito!");
            }

            status.setComplete();
            return "redirect:/vista/familias";

        } catch (Excepcion e) {
            // Manejo de error específico por atributo
            if (e.getAtributo() != null) {
                result.rejectValue("integrantes[0]." + e.getAtributo(), null, e.getMessage());
            } else {
                model.addAttribute("errorGeneral", e.getMessage());
            }
            return "familias/familia-form";
        }
    }

    @GetMapping
    public String listarFamilias(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "id", required = false) Long id,
            Model model) {

        List<Familia> familias;

        if (id != null) {
            try {
                Familia f = familiaService.buscarPorId(id);
                familias = f != null && f.isActiva() ? List.of(f) : List.of();
            } catch (Exception e) {
                familias = List.of(); 
            }
        } else if (nombre != null && !nombre.isBlank()) {
            familias = familiaService.buscarPorNombre(nombre);
        } else {
            familias = familiaService.listarTodas();
        }

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
        List<IntegranteForm> integrantes = familiaForm.getIntegrantes();

        if (integrantes.size() == 1) {
            // Si hay solo un integrante, limpiar sus campos
            IntegranteForm i = integrantes.get(0);
            i.setDni(null);
            i.setNombre("");
            i.setApellido("");
            i.setFechaNacimiento(null);
            i.setDomicilio("");
            i.setEdad(0);
            i.setParentesco("");
            i.setOcupacion(null);
        } else if (indice >= 0 && indice < integrantes.size()) {
            // Si hay más de uno, eliminar normalmente
            integrantes.remove(indice);
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