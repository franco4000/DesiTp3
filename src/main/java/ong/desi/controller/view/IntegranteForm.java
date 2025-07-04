package ong.desi.controller.view;

import java.time.LocalDate;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ong.desi.entity.Ocupacion;

public class IntegranteForm {

	private Long id;
	@NotNull(message = "El DNI es obligatorio")
	@Min(value = 1000000, message = "El DNI debe ser mayor a 1.000.000")
	@Max(value = 99999999, message = "El DNI no puede superar 99.999.999")
	private Long dni;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]+$", message = "Solo se permiten letras")
    @Size(min = 2, message = "Debe tener al menos 2 caracteres")
    private String nombre;
    @NotBlank(message = "El apellido no puede estar vacío")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]+$", message = "Solo se permiten letras")
    @Size(min = 2, message = "Debe tener al menos 2 caracteres")
    private String apellido;
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha debe ser en el pasado")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String domicilio;
    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad debe ser 0 o mayor")
    @Max(value = 120, message = "La edad no puede ser mayor a 120")
    private Integer edad;
    @NotBlank(message = "El parentesco no puede estar vacío")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]+$", message = "Solo se permiten letras")
    @Size(min = 2, message = "Debe tener al menos 2 caracteres")
    private String parentesco;
    @NotNull(message = "Debe seleccionar una ocupación")
    private Ocupacion ocupacion;
	private boolean activa = true;

    
    public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Long getDni() {
		return dni;
	}
	public void setDni(Long dni) {
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public Integer getEdad() {
		return edad;
	}
	public void setEdad(Integer edad) {
		this.edad = edad;
	}
	public String getParentesco() {
		return parentesco;
	}
	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}
	public Ocupacion getOcupacion() {
		return ocupacion;
	}
	public void setOcupacion(Ocupacion ocupacion) {
		this.ocupacion = ocupacion;
	}
    
}
