package com.oracle.controller;

import com.oracle.domain.Nivel;
import com.oracle.domain.Estacionamiento;
import com.oracle.service.NivelService;
import com.oracle.service.EstacionamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/niveles")
public class NivelController {

    private final NivelService nivelService;
    private final EstacionamientoService estacionamientoService;

    @Autowired
    public NivelController(NivelService nivelService, EstacionamientoService estacionamientoService) {
        this.nivelService = nivelService;
        this.estacionamientoService = estacionamientoService;
    }

    @GetMapping
    public String listarNiveles(Model model) {
        List<Nivel> niveles = nivelService.findAll();
        model.addAttribute("niveles", niveles);
        return "niveles/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoNivelForm(Model model) {
        model.addAttribute("nivel", new Nivel());
        model.addAttribute("estacionamientos", estacionamientoService.findAll());
        model.addAttribute("titulo", "Nuevo Nivel");
        return "niveles/form";
    }

    @GetMapping("/editar/{id}")
    public String editarNivelForm(@PathVariable Long id, Model model) {
        Optional<Nivel> nivel = nivelService.findById(id);
        if (nivel.isPresent()) {
            model.addAttribute("nivel", nivel.get());
            model.addAttribute("estacionamientos", estacionamientoService.findAll());
            model.addAttribute("titulo", "Editar Nivel");
            return "niveles/form";
        } else {
            return "redirect:/niveles";
        }
    }

    @PostMapping("/guardar")
    public String guardarNivel(@ModelAttribute Nivel nivel, RedirectAttributes flash) {
        try {
            nivelService.save(nivel);
            flash.addFlashAttribute("mensaje", "Nivel guardado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar el nivel: " + e.getMessage());
        }
        return "redirect:/niveles";
    }

    /**
     * Método nuevo para guardar un nivel directamente desde la página de detalle del estacionamiento
     */
    @PostMapping("/guardar-en-estacionamiento")
    public String guardarNivelEnEstacionamiento(
            @RequestParam Long idEstacionamiento,
            @RequestParam Integer numeroNivel,
            @RequestParam Integer capacidadNivel,
            RedirectAttributes flash) {
        
        try {
            // Buscar el estacionamiento
            Optional<Estacionamiento> estacionamientoOpt = estacionamientoService.findById(idEstacionamiento);
            
            if (estacionamientoOpt.isPresent()) {
                Estacionamiento estacionamiento = estacionamientoOpt.get();
                
                // Verificar que no exista ya un nivel con el mismo número en este estacionamiento
                Optional<Nivel> nivelExistente = nivelService.findByEstacionamientoAndNumeroNivel(
                        idEstacionamiento, numeroNivel);
                
                if (nivelExistente.isPresent()) {
                    flash.addFlashAttribute("error", 
                            "Ya existe un nivel con el número " + numeroNivel + " en este estacionamiento");
                } else {
                    // Calcular la capacidad actual
                    List<Nivel> nivelesExistentes = nivelService.findByEstacionamientoId(idEstacionamiento);
                    Integer capacidadActual = nivelesExistentes.stream()
                        .mapToInt(Nivel::getCapacidadNivel)
                        .sum();
                    
                    // Verificar que no se exceda la capacidad total
                    Integer capacidadRestante = estacionamiento.getCapacidadTotal() - capacidadActual;
                    if (capacidadNivel > capacidadRestante) {
                        flash.addFlashAttribute("error", 
                            "La capacidad del nivel (" + capacidadNivel + ") excede la capacidad restante (" 
                            + capacidadRestante + ")");
                    } else {
                        // Crear el nuevo nivel
                        Nivel nuevoNivel = new Nivel();
                        nuevoNivel.setEstacionamiento(estacionamiento);
                        nuevoNivel.setNumeroNivel(numeroNivel);
                        nuevoNivel.setCapacidadNivel(capacidadNivel);
                        
                        // Guardar el nivel
                        nivelService.save(nuevoNivel);
                        
                        // Mensaje personalizado según estado de completado
                        if (capacidadActual + capacidadNivel == estacionamiento.getCapacidadTotal()) {
                            flash.addFlashAttribute("mensaje", 
                                "¡Nivel agregado con éxito! Has completado la capacidad total del estacionamiento.");
                        } else {
                            flash.addFlashAttribute("mensaje", "Nivel agregado con éxito al estacionamiento");
                        }
                    }
                }
            } else {
                flash.addFlashAttribute("error", "No se encontró el estacionamiento");
            }
            
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al agregar el nivel: " + e.getMessage());
        }
        
        // Redirigir de vuelta a la página de detalle del estacionamiento
        return "redirect:/estacionamientos/detalle/" + idEstacionamiento;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarNivel(@PathVariable Long id, RedirectAttributes flash) {
        try {
            // Obtener el nivel para saber a qué estacionamiento pertenece
            Optional<Nivel> nivelOpt = nivelService.findById(id);
            Long idEstacionamiento = null;
            
            if (nivelOpt.isPresent()) {
                idEstacionamiento = nivelOpt.get().getEstacionamiento().getIdEstacionamiento();
            }
            
            nivelService.deleteById(id);
            flash.addFlashAttribute("mensaje", "Nivel eliminado con éxito");
            
            // Si sabemos a qué estacionamiento pertenecía, redirigimos allí
            if (idEstacionamiento != null) {
                return "redirect:/estacionamientos/detalle/" + idEstacionamiento;
            }
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el nivel. Puede tener registros asociados.");
        }
        return "redirect:/niveles";
    }

    @GetMapping("/estacionamiento/{idEstacionamiento}")
    public String listarNivelesPorEstacionamiento(@PathVariable Long idEstacionamiento, Model model) {
        List<Nivel> niveles = nivelService.findByEstacionamientoId(idEstacionamiento);
        Optional<Estacionamiento> estacionamiento = estacionamientoService.findById(idEstacionamiento);
        
        model.addAttribute("niveles", niveles);
        if (estacionamiento.isPresent()) {
            model.addAttribute("estacionamiento", estacionamiento.get());
        }
        
        return "niveles/lista";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleNivel(@PathVariable Long id, Model model) {
        Optional<Nivel> nivel = nivelService.findById(id);
        if (nivel.isPresent()) {
            model.addAttribute("nivel", nivel.get());
            
            // Obtenemos datos adicionales para mostrar en el detalle
            Long espaciosDisponibles = nivelService.countEspaciosDisponiblesByNivelId(id);
            Double porcentajeOcupacion = nivelService.calcularPorcentajeOcupacion(id);
            
            model.addAttribute("espaciosDisponibles", espaciosDisponibles);
            model.addAttribute("porcentajeOcupacion", porcentajeOcupacion);
            
            return "niveles/detalle";
        } else {
            return "redirect:/niveles";
        }
    }

    @GetMapping("/capacidad-minima/{capacidadMinima}")
    public String listarNivelesPorCapacidadMinima(@PathVariable Integer capacidadMinima, Model model) {
        List<Nivel> niveles = nivelService.findByCapacidadMinima(capacidadMinima);
        model.addAttribute("niveles", niveles);
        model.addAttribute("capacidadMinima", capacidadMinima);
        return "niveles/lista";
    }

    @GetMapping("/con-espacios-disponibles")
    public String listarNivelesConEspaciosDisponibles(Model model) {
        List<Nivel> niveles = nivelService.findNivelesConEspaciosDisponibles();
        model.addAttribute("niveles", niveles);
        model.addAttribute("conEspaciosDisponibles", true);
        return "niveles/lista";
    }

    @GetMapping("/buscar-numero")
    public String buscarNivelPorNumeroYEstacionamiento(
            @RequestParam Long idEstacionamiento, 
            @RequestParam Integer numeroNivel, 
            Model model) {
        
        Optional<Nivel> nivel = nivelService.findByEstacionamientoAndNumeroNivel(idEstacionamiento, numeroNivel);
        
        if (nivel.isPresent()) {
            List<Nivel> niveles = List.of(nivel.get());
            model.addAttribute("niveles", niveles);
        } else {
            model.addAttribute("niveles", List.of());
            model.addAttribute("mensaje", "No se encontró el nivel " + numeroNivel + " en el estacionamiento seleccionado");
        }
        
        model.addAttribute("idEstacionamiento", idEstacionamiento);
        model.addAttribute("numeroNivel", numeroNivel);
        model.addAttribute("estacionamientos", estacionamientoService.findAll());
        
        return "niveles/lista";
    }
}