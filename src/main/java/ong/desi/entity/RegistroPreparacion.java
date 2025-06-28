package ong.desi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;


@Entity
public class RegistroPreparacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debe seleccionar una receta")
    @ManyToOne
    private Receta receta;
    @Positive(message = "La cantidad de preparaciones debe ser mayor que 0")
    private int cantidadPreparaciones;
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDateTime fecha;

    public RegistroPreparacion() {
    	
    }

    public RegistroPreparacion(Receta receta, int cantidadPreparaciones) {
        this.receta = receta;
        this.cantidadPreparaciones = cantidadPreparaciones;
        this.fecha = LocalDateTime.now();
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

	public int getCantidadPreparaciones() {
		return cantidadPreparaciones;
	}

	public void setCantidadPreparaciones(int cantidadPreparaciones) {
		this.cantidadPreparaciones = cantidadPreparaciones;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
    
}
