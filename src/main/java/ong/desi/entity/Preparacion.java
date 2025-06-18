package ong.desi.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Preparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) // Una preparación debe tener una receta.
    private Receta receta;

    private int cantidadRaciones;

    private LocalDate fechaPreparacion;
    
    private boolean activa = true; // Para la baja lógica

    // Constructores, Getters y Setters 
    
    public Preparacion() {
        // Constructor vacío requerido por JPA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public int getCantidadRaciones() {
        return cantidadRaciones;
    }

    public void setCantidadRaciones(int cantidadRaciones) {
        this.cantidadRaciones = cantidadRaciones;
    }

    public LocalDate getFechaPreparacion() {
        return fechaPreparacion;
    }

    public void setFechaPreparacion(LocalDate fechaPreparacion) {
        this.fechaPreparacion = fechaPreparacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}