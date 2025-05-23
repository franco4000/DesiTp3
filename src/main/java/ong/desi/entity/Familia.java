package ong.desi.entity;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Familia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //nro de familia 
    private String nombre; //Nombre de familia 
    private LocalDate fechaAlta = LocalDate.now(); // Fecha de alta con valor por defecto 
    private LocalDate fechaUltimaAsistencia; // Simulada

    @OneToMany(mappedBy = "familia", cascade = CascadeType.ALL)
    private List<Integrante> integrantes = new ArrayList<>(); //Lista de integrantes incluida en la entidad Familia y recibida en @RequestBody.
    private boolean activa = true; //  la baja de familia está implementada como eliminación lógica,
    
    // Constructor vacío requerido por JPA
    public Familia() {
    }
    
    // Getters y setters
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

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public List<Integrante> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Integrante> integrantes) {
        this.integrantes = integrantes;
     // Relación bidireccional (importante para JPA)
        for (Integrante i : integrantes) {
            i.setFamilia(this);
        }
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
    
    // Método helper para agregar integrantes
    public void agregarIntegrante(Integrante integrante) {
        integrantes.add(integrante);
        integrante.setFamilia(this);
        
    }

	public LocalDate getFechaUltimaAsistencia() {
		return fechaUltimaAsistencia;
	}

	public void setFechaUltimaAsistencia(LocalDate fechaUltimaAsistencia) {
		this.fechaUltimaAsistencia = fechaUltimaAsistencia;
	}
	
	public long contarIntegrantesActivos() {
	    return integrantes.stream()
	                     .filter(Integrante::isActivo)
	                     .count();
	}

}