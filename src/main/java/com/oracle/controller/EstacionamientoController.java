package com.oracle.controller;

import com.oracle.domain.Estacionamiento;
import com.oracle.domain.Nivel;
import com.oracle.service.EstacionamientoService;
import com.oracle.service.NivelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/estacionamientos")
public class EstacionamientoController {

    private final EstacionamientoService estacionamientoService;
    private final NivelService nivelService;

    @Autowired
    public EstacionamientoController(EstacionamientoService estacionamientoService, NivelService nivelService) {
        this.estacionamientoService = estacionamientoService;
        this.nivelService = nivelService;
    }

    @GetMapping
    public String listarEstacionamientos(Model model) {
        List<Estacionamiento> estacionamientos = estacionamientoService.findAll();
        model.addAttribute("estacionamientos", estacionamientos);
        return "estacionamientos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoEstacionamientoForm(Model model) {
        model.addAttribute("estacionamiento", new Estacionamiento());
        model.addAttribute("titulo", "Nuevo Estacionamiento");
        return "estacionamientos/form";
    }

    @GetMapping("/editar/{id}")
    public String editarEstacionamientoForm(@PathVariable Long id, Model model) {
        Optional<Estacionamiento> estacionamiento = estacionamientoService.findById(id);
        if (estacionamiento.isPresent()) {
            model.addAttribute("estacionamiento", estacionamiento.get());
            model.addAttribute("titulo", "Editar Estacionamiento");
            return "estacionamientos/form";
        } else {
            return "redirect:/estacionamientos";
        }
    }

    @PostMapping("/guardar")
    public String guardarEstacionamiento(
            @ModelAttribute Estacionamiento estacionamiento,
            @RequestParam(value = "numeroNivel", required = false) List<Integer> numerosNiveles,
            @RequestParam(value = "capacidadesNiveles", required = false) List<Integer> capacidadesNiveles,
            RedirectAttributes flash) {

        try {
            // Guardar el estacionamiento
            estacionamientoService.save(estacionamiento);
            flash.addFlashAttribute("mensaje", "Estacionamiento guardado con éxito");

            // Si hay datos de niveles, guardarlos
            if (numerosNiveles != null && capacidadesNiveles != null && !numerosNiveles.isEmpty()
                    && numerosNiveles.size() == capacidadesNiveles.size()) {

                for (int i = 0; i < numerosNiveles.size(); i++) {
                    Integer numeroNivel = numerosNiveles.get(i);
                    Integer capacidadNivel = capacidadesNiveles.get(i);

                    if (capacidadNivel != null && capacidadNivel > 0) {
                        Nivel nivel = new Nivel();
                        nivel.setNumeroNivel(numeroNivel);
                        nivel.setCapacidadNivel(capacidadNivel);
                        nivel.setEstacionamiento(estacionamiento);
                        nivelService.save(nivel);
                    }
                }

                flash.addFlashAttribute("mensaje", "Estacionamiento y niveles guardados con éxito");
            }

            // Redireccionar a la página de detalle
            return "redirect:/estacionamientos/detalle/" + estacionamiento.getIdEstacionamiento();
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar el estacionamiento: " + e.getMessage());
            return "redirect:/estacionamientos";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEstacionamiento(@PathVariable Long id, RedirectAttributes flash) {
        try {
            estacionamientoService.deleteById(id);
            flash.addFlashAttribute("mensaje", "Estacionamiento eliminado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el estacionamiento. Puede tener registros asociados.");
        }
        return "redirect:/estacionamientos";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleEstacionamiento(@PathVariable Long id, Model model) {
        Optional<Estacionamiento> estacionamiento = estacionamientoService.findById(id);
        if (estacionamiento.isPresent()) {
            model.addAttribute("estacionamiento", estacionamiento.get());
            // Obtenemos los niveles asociados a este estacionamiento
            List<Nivel> niveles = nivelService.findByEstacionamientoId(id);
            model.addAttribute("niveles", niveles);
            
            // Contamos los niveles
            Long cantidadNiveles = estacionamientoService.countNivelesByEstacionamientoId(id);
            model.addAttribute("cantidadNiveles", cantidadNiveles);
            
            // Calculamos la capacidad actual (suma de capacidades de los niveles)
            Integer capacidadActual = niveles.stream()
                                        .mapToInt(Nivel::getCapacidadNivel)
                                        .sum();
            model.addAttribute("capacidadActual", capacidadActual);
            
            return "estacionamientos/detalle";
        } else {
            return "redirect:/estacionamientos";
        }
    }

    @GetMapping("/capacidad-minima/{capacidadMinima}")
    public String listarEstacionamientosPorCapacidadMinima(@PathVariable Integer capacidadMinima, Model model) {
        List<Estacionamiento> estacionamientos = estacionamientoService.findByCapacidadMinima(capacidadMinima);
        model.addAttribute("estacionamientos", estacionamientos);
        model.addAttribute("capacidadMinima", capacidadMinima);
        return "estacionamientos/lista";
    }

    @GetMapping("/con-espacios-disponibles")
    public String listarEstacionamientosConEspaciosDisponibles(Model model) {
        List<Estacionamiento> estacionamientos = estacionamientoService.findEstacionamientosConEspaciosDisponibles();
        model.addAttribute("estacionamientos", estacionamientos);
        model.addAttribute("conEspaciosDisponibles", true);
        return "estacionamientos/lista";
    }

    @GetMapping("/buscar-nombre")
    public String buscarEstacionamientoPorNombre(@RequestParam String nombre, Model model) {
        Optional<Estacionamiento> estacionamiento = estacionamientoService.findByNombre(nombre);
        if (estacionamiento.isPresent()) {
            List<Estacionamiento> estacionamientos = List.of(estacionamiento.get());
            model.addAttribute("estacionamientos", estacionamientos);
        } else {
            model.addAttribute("estacionamientos", List.of());
            model.addAttribute("mensaje", "No se encontró estacionamiento con el nombre: " + nombre);
        }
        model.addAttribute("nombreBuscado", nombre);
        return "estacionamientos/lista";
    }

    @GetMapping("/buscar-direccion")
    public String buscarEstacionamientosPorDireccion(@RequestParam String direccion, Model model) {
        List<Estacionamiento> estacionamientos = estacionamientoService.findByDireccion(direccion);
        model.addAttribute("estacionamientos", estacionamientos);
        model.addAttribute("direccionBuscada", direccion);
        return "estacionamientos/lista";
    }
}