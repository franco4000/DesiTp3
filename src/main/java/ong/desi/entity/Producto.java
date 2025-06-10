package ong.desi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Producto extends Ingrediente {
	
	@Column(nullable = false)
    private Float stockDisponible;
	@Column(nullable = false)
    private Float precioActual;
	
	public Producto() {
		
	}

	public Producto(String nombre, Float stockDisponible, Float precioActual) {
	    this.setNombre(nombre); // Heredado de Ingrediente
	    this.stockDisponible = stockDisponible;
	    this.precioActual = precioActual;
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
