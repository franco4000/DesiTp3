package ong.desi.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuControllerView {

	 @GetMapping("/index")
	    public String mostrarMenuPrincipal() {
	        return "index"; // Busca en templates/index.html
	    }
}
