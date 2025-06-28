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

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    @Column(nullable = false)
    private Float cantidad;

    @NotNull(message = "Las calorías son obligatorias")
    @PositiveOrZero(message = "Las calorías deben ser 0 o más")
    @Column(nullable = false)
    private Integer calorias;

    @Column(nullable = false)
    private boolean activa = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receta_id")
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

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }
}
