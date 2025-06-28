package ong.desi.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.JoinColumn;

@Entity
public class Asistido extends Persona {

	@NotNull(message = "El asistido debe estar asociado a una familia")
    @ManyToOne
    @JoinColumn(name = "familia_id")
    private Familia familia;

    private boolean activo = true; // eliminación lógica

    public Familia getFamilia() {
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
