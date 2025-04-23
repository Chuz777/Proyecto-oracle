package com.oracle.controller;

import com.oracle.domain.Espacio;
import com.oracle.domain.Nivel;
import com.oracle.service.EspacioService;
import com.oracle.service.NivelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/espacios")
public class EspacioController {

    private final EspacioService espacioService;
    private final NivelService nivelService;

    @Autowired
    public EspacioController(EspacioService espacioService, NivelService nivelService) {
        this.espacioService = espacioService;
        this.nivelService = nivelService;
    }

    @GetMapping
    public String listarEspacios(Model model) {
        List<Espacio> espacios = espacioService.findAll();
        model.addAttribute("espacios", espacios);
        return "espacios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoEspacioForm(Model model) {
        model.addAttribute("espacio", new Espacio());
        model.addAttribute("niveles", nivelService.findAll());
        model.addAttribute("titulo", "Nuevo Espacio");
        return "espacios/form";
    }

    @GetMapping("/editar/{id}")
    public String editarEspacioForm(@PathVariable Long id, Model model) {
        Optional<Espacio> espacio = espacioService.findById(id);
        if (espacio.isPresent()) {
            model.addAttribute("espacio", espacio.get());
            model.addAttribute("niveles", nivelService.findAll());
            model.addAttribute("titulo", "Editar Espacio");
            return "espacios/form";
        } else {
            return "redirect:/espacios";
        }
    }

    @PostMapping("/guardar")
    public String guardarEspacio(@ModelAttribute Espacio espacio, RedirectAttributes flash) {
        try {
            espacioService.save(espacio);
            flash.addFlashAttribute("mensaje", "Espacio guardado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar el espacio: " + e.getMessage());
        }
        return "redirect:/espacios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEspacio(@PathVariable Long id, RedirectAttributes flash) {
        try {
            espacioService.deleteById(id);
            flash.addFlashAttribute("mensaje", "Espacio eliminado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el espacio. Puede tener registros asociados.");
        }
        return "redirect:/espacios";
    }

    @GetMapping("/nivel/{idNivel}")
    public String listarEspaciosPorNivel(@PathVariable Long idNivel, Model model) {
        List<Espacio> espacios = espacioService.findByNivelId(idNivel);
        Optional<Nivel> nivel = nivelService.findById(idNivel);
        
        model.addAttribute("espacios", espacios);
        if (nivel.isPresent()) {
            model.addAttribute("nivel", nivel.get());
            model.addAttribute("detalleNivel", "Nivel " + nivel.get().getNumeroNivel() + 
                              " - Estacionamiento: " + nivel.get().getEstacionamiento().getNombre());
        }
        
        return "espacios/lista";
    }

    @GetMapping("/estado/{estado}")
    public String listarEspaciosPorEstado(@PathVariable String estado, Model model) {
        List<Espacio> espacios = espacioService.findByEstado(estado);
        model.addAttribute("espacios", espacios);
        model.addAttribute("estadoSeleccionado", estado);
        return "espacios/lista";
    }

    @GetMapping("/ubicacion/{ubicacion}")
    public String listarEspaciosPorUbicacion(@PathVariable String ubicacion, Model model) {
        List<Espacio> espacios = espacioService.findByUbicacion(ubicacion);
        model.addAttribute("espacios", espacios);
        model.addAttribute("ubicacionBuscada", ubicacion);
        return "espacios/lista";
    }

    @GetMapping("/buscar-numero")
    public String buscarEspacioPorNivelYNumero(
            @RequestParam Long idNivel, 
            @RequestParam Integer numeroEspacio, 
            Model model) {
        
        Optional<Espacio> espacio = espacioService.findByNivelIdAndNumeroEspacio(idNivel, numeroEspacio);
        
        if (espacio.isPresent()) {
            List<Espacio> espacios = List.of(espacio.get());
            model.addAttribute("espacios", espacios);
        } else {
            model.addAttribute("espacios", List.of());
            model.addAttribute("mensaje", "No se encontró el espacio " + numeroEspacio + " en el nivel seleccionado");
        }
        
        model.addAttribute("idNivel", idNivel);
        model.addAttribute("numeroEspacio", numeroEspacio);
        model.addAttribute("niveles", nivelService.findAll());
        
        return "espacios/lista";
    }

    @GetMapping("/nivel/{idNivel}/disponibles/count")
    public String contarEspaciosDisponiblesPorNivel(@PathVariable Long idNivel, Model model) {
        Long cantidadDisponibles = espacioService.countEspaciosDisponiblesByNivel(idNivel);
        Optional<Nivel> nivel = nivelService.findById(idNivel);
        
        if (nivel.isPresent()) {
            model.addAttribute("nivel", nivel.get());
            model.addAttribute("cantidadDisponibles", cantidadDisponibles);
            model.addAttribute("cantidadTotal", nivel.get().getCapacidadNivel());
            model.addAttribute("porcentajeOcupacion", 
                               100 - ((double)cantidadDisponibles / nivel.get().getCapacidadNivel() * 100));
            
            return "espacios/disponibilidad";
        } else {
            return "redirect:/niveles";
        }
    }

    @GetMapping("/nivel/{idNivel}/primer-disponible")
    public String encontrarPrimerEspacioDisponible(@PathVariable Long idNivel, Model model) {
        List<Espacio> espaciosDisponibles = espacioService.findPrimerEspacioDisponible(idNivel);
        Optional<Nivel> nivel = nivelService.findById(idNivel);
        
        model.addAttribute("espacios", espaciosDisponibles);
        if (nivel.isPresent()) {
            model.addAttribute("nivel", nivel.get());
        }
        
        if (espaciosDisponibles.isEmpty()) {
            model.addAttribute("mensaje", "No hay espacios disponibles en este nivel");
        } else {
            model.addAttribute("primerDisponible", true);
        }
        
        return "espacios/lista";
    }

    @GetMapping("/estadisticas/{idNivel}")
    public String verEstadisticasEspacios(@PathVariable Long idNivel, Model model) {
        List<Object[]> estadisticas = espacioService.estadisticasDeEspaciosPorNivel(idNivel);
        Optional<Nivel> nivel = nivelService.findById(idNivel);
        
        model.addAttribute("estadisticas", estadisticas);
        if (nivel.isPresent()) {
            model.addAttribute("nivel", nivel.get());
        }
        
        return "espacios/estadisticas";
    }

    @GetMapping("/mas-utilizados")
    public String listarEspaciosMasUtilizados(Model model) {
        List<Object[]> espaciosMasUtilizados = espacioService.findEspaciosMasUtilizados();
        model.addAttribute("espaciosMasUtilizados", espaciosMasUtilizados);
        return "espacios/mas-utilizados";
    }
}