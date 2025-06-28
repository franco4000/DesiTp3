package ong.desi.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@DiscriminatorValue("Condimento")
public class Condimento extends Ingrediente{

	public Condimento() { 
		
	}

    public Condimento(String nombre,
                      Float calorias,
                      Estacion estacion) {
        super(nombre, calorias, estacion);   // ← asigna nombre y estación en la superclase
        
    }

}
