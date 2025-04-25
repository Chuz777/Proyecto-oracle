package com.oracle.controller;

import com.oracle.domain.Vehiculo;
import com.oracle.domain.Cliente;
import com.oracle.service.VehiculoService;
import com.oracle.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final ClienteService clienteService;

    @Autowired
    public VehiculoController(VehiculoService vehiculoService, ClienteService clienteService) {
        this.vehiculoService = vehiculoService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listarVehiculos(Model model) {
        List<Vehiculo> vehiculos = vehiculoService.findAll();
        model.addAttribute("vehiculos", vehiculos);
        return "vehiculos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoVehiculoForm(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("titulo", "Nuevo Vehículo");
        return "vehiculos/form";
    }

    @GetMapping("/editar/{id}")
    public String editarVehiculoForm(@PathVariable Long id, Model model) {
        Optional<Vehiculo> vehiculo = vehiculoService.findById(id);
        if (vehiculo.isPresent()) {
            model.addAttribute("vehiculo", vehiculo.get());
            model.addAttribute("clientes", clienteService.findAll());
            model.addAttribute("titulo", "Editar Vehículo");
            return "vehiculos/form";
        } else {
            return "redirect:/vehiculos";
        }
    }

    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo, RedirectAttributes flash) {
        try {
            vehiculoService.save(vehiculo);
            flash.addFlashAttribute("mensaje", "Vehículo guardado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar el vehículo: " + e.getMessage());
        }
        return "redirect:/vehiculos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id, RedirectAttributes flash) {
        try {
            vehiculoService.deleteById(id);
            flash.addFlashAttribute("mensaje", "Vehículo eliminado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el vehículo. Puede tener registros asociados.");
        }
        return "redirect:/vehiculos";
    }

    @GetMapping("/cliente/{idCliente}")
    public String listarVehiculosPorCliente(@PathVariable Long idCliente, Model model) {
        List<Vehiculo> vehiculos = vehiculoService.findByClienteId(idCliente);
        Optional<Cliente> cliente = clienteService.findById(idCliente);
        
        model.addAttribute("vehiculos", vehiculos);
        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            model.addAttribute("nombreCliente", cliente.get().getNombre() + " " + cliente.get().getApellido());
        }
        
        return "vehiculos/lista";
    }

    @GetMapping("/tipo/{tipoVehiculo}")
    public String listarVehiculosPorTipo(@PathVariable String tipoVehiculo, Model model) {
        List<Vehiculo> vehiculos = vehiculoService.findByTipoVehiculo(tipoVehiculo);
        model.addAttribute("vehiculos", vehiculos);
        model.addAttribute("tipoVehiculo", tipoVehiculo);
        return "vehiculos/lista";
    }

    @GetMapping("/energia/{tipoEnergia}")
    public String listarVehiculosPorTipoEnergia(@PathVariable String tipoEnergia, Model model) {
        List<Vehiculo> vehiculos = vehiculoService.findByTipoEnergia(tipoEnergia);
        model.addAttribute("vehiculos", vehiculos);
        model.addAttribute("tipoEnergia", tipoEnergia);
        return "vehiculos/lista";
    }

    @GetMapping("/estacionados")
    public String listarVehiculosEstacionados(Model model) {
        List<Vehiculo> vehiculos = vehiculoService.findVehiculosEstacionados();
        model.addAttribute("vehiculos", vehiculos);
        model.addAttribute("estacionados", true);
        return "vehiculos/lista";
    }

    @GetMapping("/sin-registro-uso")
    public String listarVehiculosSinRegistroDeUso(Model model) {
        List<Vehiculo> vehiculos = vehiculoService.findVehiculosSinRegistroDeUso();
        model.addAttribute("vehiculos", vehiculos);
        model.addAttribute("sinUso", true);
        return "vehiculos/lista";
    }
    

    @GetMapping("/buscar-placa")
    public String buscarVehiculoPorPlaca(@RequestParam String numeroPlaca, Model model) {
        Optional<Vehiculo> vehiculo = vehiculoService.findByNumeroPlaca(numeroPlaca);
        
        if (vehiculo.isPresent()) {
            List<Vehiculo> vehiculos = List.of(vehiculo.get());
            model.addAttribute("vehiculos", vehiculos);
            model.addAttribute("placa", numeroPlaca);
        } else {
            model.addAttribute("vehiculos", List.of());
            model.addAttribute("placa", numeroPlaca);
            model.addAttribute("mensaje", "No se encontró vehículo con la placa " + numeroPlaca);
        }
        
        return "vehiculos/lista";
    }
}