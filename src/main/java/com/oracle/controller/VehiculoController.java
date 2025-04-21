package com.oracle.controller;

import model.Vehiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.ClienteService;
import service.VehiculoService;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String mostrarFormularioVehiculo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "vehiculo";
    }

    @PostMapping
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo) {
        vehiculoService.guardarVehiculo(vehiculo);
        return "redirect:/vehiculos";
    }
}