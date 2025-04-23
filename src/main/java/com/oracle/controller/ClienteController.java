package com.oracle.controller;

import com.oracle.domain.Cliente;
import com.oracle.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.findAll();
        model.addAttribute("clientes", clientes);
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoClienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "clientes/form";
    }

    @GetMapping("/editar/{id}")
    public String editarClienteForm(@PathVariable Long id, Model model) {
        Optional<Cliente> cliente = clienteService.findById(id);
        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            model.addAttribute("titulo", "Editar Cliente");
            return "clientes/form";
        } else {
            return "redirect:/clientes";
        }
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente, RedirectAttributes flash) {
        try {
            clienteService.save(cliente);
            flash.addFlashAttribute("mensaje", "Cliente guardado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar el cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes flash) {
        try {
            clienteService.deleteById(id);
            flash.addFlashAttribute("mensaje", "Cliente eliminado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el cliente. Puede tener registros asociados.");
        }
        return "redirect:/clientes";
    }

    @GetMapping("/buscar")
    public String buscarClientesPorApellido(@RequestParam String apellido, Model model) {
        List<Cliente> clientes = clienteService.findByApellido(apellido);
        model.addAttribute("clientes", clientes);
        model.addAttribute("busqueda", apellido);
        return "clientes/lista";
    }

    @GetMapping("/vehiculo-tipo/{tipoVehiculo}")
    public String listarClientesPorTipoVehiculo(@PathVariable String tipoVehiculo, Model model) {
        List<Cliente> clientes = clienteService.findClientesConTipoVehiculo(tipoVehiculo);
        model.addAttribute("clientes", clientes);
        model.addAttribute("tipoVehiculo", tipoVehiculo);
        return "clientes/lista";
    }

    @GetMapping("/con-tickets-activos")
    public String listarClientesConTicketsActivos(Model model) {
        List<Cliente> clientes = clienteService.findClientesConTicketsActivos();
        model.addAttribute("clientes", clientes);
        model.addAttribute("conTicketsActivos", true);
        return "clientes/lista";
    }
}