package ong.desi.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeViewController {

	 @GetMapping("/")
	    public String home() {
	        return "recetas/home";          // busca templates/home.html
	    }
}
