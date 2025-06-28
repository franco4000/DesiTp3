package ong.desi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@DiscriminatorValue("Producto")
public class Producto extends Ingrediente {
	
	@NotNull(message = "El stock no puede ser nulo")
	@PositiveOrZero(message = "El stock debe ser 0 o más")
	@Column(nullable = false)
    private Float stockDisponible;
	@NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor que 0")
	@Column(nullable = false)
    private Float precioActual;
	
	public Producto() {
		
	}

	public Producto(String nombre,
            Float calorias,
            Float stockDisponible,
            Float precioActual,
            Estacion estacion) {

     super(nombre, calorias, estacion);   //  si el campo está en la superclase Ingrediente
     this.stockDisponible = stockDisponible;
     this.precioActual    = precioActual;
}


	public Float getStockDisponible() {
		return stockDisponible;
	}
	public void setStockDisponible(Float stockDisponible) {
		this.stockDisponible = stockDisponible;
	}
	public Float getPrecioActual() {
		return precioActual;
	}
	public void setPrecioActual(Float precioActual) {
		this.precioActual = precioActual;
	}
 
	public void descontarStock(Float cantidad) {
	    if (stockDisponible < cantidad) {
	        throw new RuntimeException("No hay suficiente stock para el producto: " + getNombre());
	    }
	    this.stockDisponible -= cantidad;
	}

    
}
