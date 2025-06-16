package ong.desi.controller.view;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ong.desi.entity.Familia;
import ong.desi.entity.Integrante;

public class FamiliaForm {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaAlta = LocalDate.now();
    @Valid
    private List<IntegranteForm> integrantes = new ArrayList<>();
	private boolean activa = true;
    
    public FamiliaForm() {
    	
    }

    // Constructor que recibe una entidad para cargar el formulario 
    public FamiliaForm(Familia familia) {
        this.id = familia.getId();
        this.nombre = familia.getNombre();
        this.fechaAlta = familia.getFechaAlta();
        this.activa = familia.isActiva();
    }

    // Convierte el form a entidad para el service
    public Familia toEntidad() {
        Familia familia = new Familia();
        familia.setId(this.id);
        familia.setNombre(this.nombre);
        familia.setFechaAlta(this.fechaAlta);
        familia.setActiva(this.activa);
        
        List<Integrante> lista = this.integrantes.stream().map(form -> {
            Integrante i = new Integrante();
            
            i.setDni(form.getDni());
            i.setNombre(form.getNombre());
            i.setApellido(form.getApellido());
            i.setFechaNacimiento(form.getFechaNacimiento());
            i.setDomicilio(form.getDomicilio());
            i.setEdad(form.getEdad());
            i.setParentesco(form.getParentesco());
            i.setOcupacion(form.getOcupacion());
            i.setActivo(true);
            i.setFamilia(familia); // establece la relación
            return i;
        }).collect(Collectors.toList());

        familia.setIntegrantes(lista);
        return familia;
    }

    public List<IntegranteForm> getIntegrantes() {
		return integrantes;
	}

	public void setIntegrantes(List<IntegranteForm> integrantes) {
		this.integrantes = integrantes;
	}

    public Long getId() {
    	return id; }
    
    public void setId(Long id) { 
    	this.id = id; }

    public String getNombre() {
    	return nombre; }
    
    public void setNombre(String nombre) { 
    	this.nombre = nombre; }

    public LocalDate getFechaAlta() { 
    	return fechaAlta; }
    
    public void setFechaAlta(LocalDate fechaAlta) {
    	this.fechaAlta = fechaAlta; }

    public boolean isActiva() {
    	return activa; }
    
    public void setActiva(boolean activa) { 
    	this.activa = activa; }

    

}

