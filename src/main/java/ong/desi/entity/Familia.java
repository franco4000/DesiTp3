package ong.desi.entity;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity 
public class Familia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    private String nombre;  
    private LocalDate fechaAlta = LocalDate.now(); // Fecha de alta con valor por defecto 
    private LocalDate fechaUltimaAsistencia; 

    //mappedBy = "familia": indica que la relación está mapeada en el atributo familia de la clase Integrante.
    @OneToMany(mappedBy = "familia", cascade = CascadeType.ALL)
    private List<Integrante> integrantes = new ArrayList<>(); 
    @OneToMany(mappedBy = "familia")
    private List<Asistido> asistidos;

     private boolean activa = true; 
    
   
    public Familia() {
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

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public List<Integrante> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Integrante> integrantes) {//asigamos una lista de integrantes ala familia
        this.integrantes = integrantes;
     // Relación bidireccional
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
    
    // Método para agregar integrantes , También asegura que el integrante sepa a que familia pertenece.
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
	
	public long contarIntegrantesActivos() {//Devuelve la cantidad de integrantes activos.
	    return integrantes.stream()
	                     .filter(Integrante::isActivo)
	                     .count();
	}

}