package ong.desi.exception;

public class Excepcion extends RuntimeException {
    
    private String mensaje;

    public Excepcion(String mensaje) {
        super(mensaje);
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
