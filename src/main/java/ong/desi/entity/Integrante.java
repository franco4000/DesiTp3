package ong.desi.entity;

import ong.desi.entity.Ocupacion;
import ong.desi.entity.Familia;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Integrante extends Persona {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private int edad;
    private String parentesco;

    @Enumerated(EnumType.STRING)
    private Ocupacion ocupacion;

    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "familia_id")
    private Familia familia;

   

    public Integrante() {
		super();
		
	}
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public Ocupacion getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(Ocupacion ocupacion) {
        this.ocupacion = ocupacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Familia getFamilia() {
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }
}

