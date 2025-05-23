package ong.desi.entity;

import ong.desi.entity.Ocupacion;
import ong.desi.entity.Familia;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Integrante {
    @Id
    private Long id;
    
    @Column(unique = true)  // DNI
    private Long dni;  // Datos requeridos en Integrante 
    
    private String apellido;
    private String nombre;
    private LocalDate fechaNacimiento;
    private int edad;
    private String parentesco;
    
    @Enumerated(EnumType.STRING)
    private Ocupacion ocupacion;
    
    @ManyToOne
    @JoinColumn(name = "familia_id")
    private Familia familia;

    public Familia getFamilia() {
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }

    
    private boolean activo = true; // Para eliminación lógica
    
  
    public Integrante() {
    }
    
    // Getters y setters
    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }


    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Ocupacion getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(Ocupacion ocupacion) {
        this.ocupacion = ocupacion;
    }


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getParentesco() {
		return parentesco;
	}

	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}
}

