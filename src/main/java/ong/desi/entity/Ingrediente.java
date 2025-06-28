package ong.desi.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo")
public abstract class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(nullable = false, unique = true)
    private String nombre;

    @NotNull(message = "Las calorías no pueden ser nulas")
    @Positive(message = "Las calorías deben ser un número positivo")
    @Column(nullable = false)
    private Float calorias;
    @Enumerated(EnumType.STRING)
    private Estacion estacion; //estacion de año
   

	@Column(nullable = false)
    private Boolean activo = true;

    public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Estacion getEstacion() {
		return estacion;
	}

	public void setEstacion(Estacion estacion) {
		this.estacion = estacion;
	}

	public Ingrediente() {
    	
    }

    protected Ingrediente(String nombre, Float calorias ,Estacion estacion) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.estacion = estacion;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public String toString() {
	    return "Ingrediente{id=" + id + ", nombre='" + nombre + "', calorias=" + calorias + '}';
	}

}
