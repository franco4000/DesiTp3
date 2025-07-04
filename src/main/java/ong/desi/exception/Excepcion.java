package ong.desi.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class Excepcion extends RuntimeException {

    private String atributo;

    public Excepcion(String mensaje) {
        super(mensaje);
    }

    public Excepcion(String mensaje, String atributo) {
        super(mensaje);
        this.atributo = atributo;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }
    
    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(NumberFormatException.class)
        public String handleNumberFormatException(Model model) {
            model.addAttribute("error", "El valor ingresado para edad no es válido. Use sólo números enteros.");
            return "familias/familia-form"; 
        }
    }

}
