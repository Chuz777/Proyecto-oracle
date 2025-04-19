package com.ProyectoOracle.controller;

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
        return "estacionar"; // Thymeleaf buscará estacionar.html en templates
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
