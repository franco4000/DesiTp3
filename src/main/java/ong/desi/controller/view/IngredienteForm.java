package ong.desi.controller.view;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import ong.desi.entity.Condimento;
import ong.desi.entity.Estacion;
import ong.desi.entity.Ingrediente;
import ong.desi.entity.Producto;
import ong.desi.entity.TipoIngrediente;

public class IngredienteForm {
   @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
   @NotNull(message = "Las calorías son obligatorias")
   @DecimalMin(value = "0.1", message = "Las calorías deben ser mayores a 0")
    private Float  calorias;
   @NotNull(message = "Debe seleccionar una estación")
    private Estacion estacion;
   @PositiveOrZero(message = "El stock debe ser 0 o más")
    private Float  stockDisponible; 
   @NotNull(message = "Debe indicar si es Producto o Condimento")
	private TipoIngrediente tipo;       // PRODUCTO o CONDIMENTO
   @Positive (message = "El precio debe ser mayor a 0")
   private Float precioActual;

   @NotNull(message = "El ID del ingrediente es obligatorio para editar")
   private Long id;
   public IngredienteForm() {
	}

   public IngredienteForm(Ingrediente ingrediente) {
	    this.id = ingrediente.getId();
	    this.nombre = ingrediente.getNombre();
	    this.calorias = ingrediente.getCalorias();
	    this.estacion = ingrediente.getEstacion();

	    if (ingrediente instanceof Producto producto) {
	        this.tipo = TipoIngrediente.PRODUCTO;
	        this.stockDisponible = producto.getStockDisponible();
	        this.precioActual = producto.getPrecioActual();
	    } else if (ingrediente instanceof Condimento) {
	        this.tipo = TipoIngrediente.CONDIMENTO;
	    }
	}

   public Long getId() {
       return id;
   }

   public void setId(Long id) {
       this.id = id;
   }

    public Float getPrecioActual() {
	return precioActual;
}
public void setPrecioActual(Float precioActual) {
	this.precioActual = precioActual;
}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Float getCalorias() {
		return calorias;
	}
	public void setCalorias(Float calorias) {
		this.calorias = calorias;
	}
	public Estacion getEstacion() {
		return estacion;
	}
	public void setEstacion(Estacion estacion) {
		this.estacion = estacion;
	}
	public Float getStockDisponible() {
		return stockDisponible;
	}
	public void setStockDisponible(Float stockDisponible) {
		this.stockDisponible = stockDisponible;
	}
	public TipoIngrediente getTipo() {
		return tipo;
	}
	public void setTipo(TipoIngrediente tipo) {
		this.tipo = tipo;
	}

   
}
