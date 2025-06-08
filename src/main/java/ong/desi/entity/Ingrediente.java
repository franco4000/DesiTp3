package ong.desi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private Integer calorias;

    
    public Ingrediente() {
    	
    }

    public Ingrediente(String nombre, Integer calorias) {
        this.nombre = nombre;
        this.calorias = calorias;
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

	public Integer getCalorias() {
		return calorias;
	}

	public void setCalorias(Integer calorias) {
		this.calorias = calorias;
	}

	@Override
	public String toString() {
	    return "Ingrediente{id=" + id + ", nombre='" + nombre + "', calorias=" + calorias + '}';
	}

}
