package com.oracle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Chuz
 */
@Controller
public class EstacionarController {
    
    @GetMapping("/estacionar")
    public String estacionarPage() {
        System.out.println("Cargando página de estacionar");
        return "estacionar"; // Thymeleaf buscará estacionar.html en templates
    }
    @GetMapping("/index")
public String indexPage() {
    return "index"; // visualiza visualizar.html
}
    
    
    @GetMapping("/visualizar")
public String visualizarPage() {
    return "visualizar"; // visualiza visualizar.html
}

@GetMapping("/salir")
public String salirPage() {
    return "salir"; // visualiza salir.html
}

 
}
