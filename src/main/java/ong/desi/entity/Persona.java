package ong.desi.entity;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;

@MappedSuperclass 
public abstract class Persona {

    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;

    // Getters y setters
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
}
