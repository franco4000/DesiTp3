package ong.desi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class ItemReceta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    @Column(nullable = false)
    private Float cantidad; // en kg

    @NotNull(message = "Las calorías son obligatorias")
    @PositiveOrZero(message = "Las calorías deben ser 0 o más")
    @Column(nullable = false)
    private Integer calorias;
    
    @Column(nullable = false)
    private boolean activo = true; // para eliminación lógica

    @ManyToOne
    @JoinColumn(name = "receta_id",  nullable = false)
    private Receta receta;

    @Transient                     
    private Long ingredienteId;
    public Long getIngredienteId() { 
    	return ingredienteId; }
    
    public void setIngredienteId(Long ingredienteId) {
    	this.ingredienteId = ingredienteId; }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ingrediente getIngrediente() {
		return ingrediente;
	}

	public void setIngrediente(Ingrediente ingrediente) {
		this.ingrediente = ingrediente;
	}

	public Float getCantidad() {
		return cantidad;
	}

	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getCalorias() {
		return calorias;
	}

	public void setCalorias(Integer calorias) {
		this.calorias = calorias;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Receta getReceta() {
		return receta;
	}

	public void setReceta(Receta receta) {
		this.receta = receta;
	}


   
}
