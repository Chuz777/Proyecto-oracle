package com.oracle.controller;

import com.oracle.domain.Tarifa;
import com.oracle.service.TarifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tarifas")
public class TarifaController {

    private final TarifaService tarifaService;

    @Autowired
    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @GetMapping
    public String listarTarifas(Model model) {
        List<Tarifa> tarifas = tarifaService.findAll();
        model.addAttribute("tarifas", tarifas);
        return "tarifas/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoTarifaForm(Model model) {
        model.addAttribute("tarifa", new Tarifa());
        model.addAttribute("titulo", "Nueva Tarifa");
        return "tarifas/form";
    }

    @GetMapping("/editar/{id}")
    public String editarTarifaForm(@PathVariable Long id, Model model) {
        Optional<Tarifa> tarifa = tarifaService.findById(id);
        if (tarifa.isPresent()) {
            model.addAttribute("tarifa", tarifa.get());
            model.addAttribute("titulo", "Editar Tarifa");
            return "tarifas/form";
        } else {
            return "redirect:/tarifas";
        }
    }

    @PostMapping("/guardar")
    public String guardarTarifa(@ModelAttribute Tarifa tarifa, RedirectAttributes flash) {
        try {
            tarifaService.save(tarifa);
            flash.addFlashAttribute("mensaje", "Tarifa guardada con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar la tarifa: " + e.getMessage());
        }
        return "redirect:/tarifas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTarifa(@PathVariable Long id, RedirectAttributes flash) {
        try {
            tarifaService.deleteById(id);
            flash.addFlashAttribute("mensaje", "Tarifa eliminada con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar la tarifa. Puede tener registros asociados.");
        }
        return "redirect:/tarifas";
    }

    @GetMapping("/tipo/{tipoTarifa}")
    public String buscarTarifaPorTipo(@PathVariable String tipoTarifa, Model model) {
        Optional<Tarifa> tarifa = tarifaService.findByTipoTarifa(tipoTarifa);
        
        List<Tarifa> tarifas;
        if (tarifa.isPresent()) {
            tarifas = List.of(tarifa.get());
        } else {
            tarifas = List.of();
            model.addAttribute("mensaje", "No se encontró tarifa con el tipo: " + tipoTarifa);
        }
        
        model.addAttribute("tarifas", tarifas);
        model.addAttribute("tipoTarifa", tipoTarifa);
        
        return "tarifas/lista";
    }

    @GetMapping("/rango-precio")
    public String listarTarifasPorRangoPrecio(
            @RequestParam Double precioMinimo, 
            @RequestParam Double precioMaximo,
            Model model) {
        
        List<Tarifa> tarifas = tarifaService.findByPrecioBetween(precioMinimo, precioMaximo);
        model.addAttribute("tarifas", tarifas);
        model.addAttribute("precioMinimo", precioMinimo);
        model.addAttribute("precioMaximo", precioMaximo);
        
        return "tarifas/lista";
    }

    @GetMapping("/ordenar-asc")
    public String listarTarifasOrdenadasPorPrecioAsc(Model model) {
        List<Tarifa> tarifas = tarifaService.findAllOrderByPrecioAsc();
        model.addAttribute("tarifas", tarifas);
        model.addAttribute("ordenAscendente", true);
        return "tarifas/lista";
    }

    @GetMapping("/ordenar-desc")
    public String listarTarifasOrdenadasPorPrecioDesc(Model model) {
        List<Tarifa> tarifas = tarifaService.findAllOrderByPrecioDesc();
        model.addAttribute("tarifas", tarifas);
        model.addAttribute("ordenDescendente", true);
        return "tarifas/lista";
    }

    @GetMapping("/mas-cara")
    public String verTarifaMasCara(Model model) {
        Optional<Tarifa> tarifa = tarifaService.findTarifaMasCara();
        
        if (tarifa.isPresent()) {
            List<Tarifa> tarifas = List.of(tarifa.get());
            model.addAttribute("tarifas", tarifas);
            model.addAttribute("esMasCara", true);
        } else {
            model.addAttribute("tarifas", List.of());
            model.addAttribute("mensaje", "No hay tarifas registradas");
        }
        
        return "tarifas/lista";
    }

    @GetMapping("/mas-barata")
    public String verTarifaMasBarata(Model model) {
        Optional<Tarifa> tarifa = tarifaService.findTarifaMasBarata();
        
        if (tarifa.isPresent()) {
            List<Tarifa> tarifas = List.of(tarifa.get());
            model.addAttribute("tarifas", tarifas);
            model.addAttribute("esMasBarata", true);
        } else {
            model.addAttribute("tarifas", List.of());
            model.addAttribute("mensaje", "No hay tarifas registradas");
        }
        
        return "tarifas/lista";
    }

    @GetMapping("/estadisticas")
    public String verEstadisticasTarifas(Model model) {
        Double precioPromedio = tarifaService.calcularPrecioPromedio();
        Optional<Tarifa> tarifaMasCara = tarifaService.findTarifaMasCara();
        Optional<Tarifa> tarifaMasBarata = tarifaService.findTarifaMasBarata();
        
        model.addAttribute("precioPromedio", precioPromedio);
        model.addAttribute("tarifaMasCara", tarifaMasCara.orElse(null));
        model.addAttribute("tarifaMasBarata", tarifaMasBarata.orElse(null));
        
        return "tarifas/estadisticas";
    }

    @GetMapping("/{id}/usos")
    public String verUsosTarifa(@PathVariable Long id, Model model) {
        Optional<Tarifa> tarifa = tarifaService.findById(id);
        
        if (tarifa.isPresent()) {
            Long cantidadUsos = tarifaService.countUsosByTarifaId(id);
            model.addAttribute("tarifa", tarifa.get());
            model.addAttribute("cantidadUsos", cantidadUsos);
            return "tarifas/usos";
        } else {
            return "redirect:/tarifas";
        }
    }
}