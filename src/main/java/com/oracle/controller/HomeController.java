package com.oracle.controller;

import com.oracle.domain.Nivel;
import com.oracle.domain.Ticket;
import com.oracle.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final ClienteService clienteService;
    private final VehiculoService vehiculoService;
    private final EspacioService espacioService;
    private final TicketService ticketService;
    private final NivelService nivelService;

    @Autowired
    public HomeController(
            ClienteService clienteService,
            VehiculoService vehiculoService,
            EspacioService espacioService,
            TicketService ticketService,
            NivelService nivelService) {
        this.clienteService = clienteService;
        this.vehiculoService = vehiculoService;
        this.espacioService = espacioService;
        this.ticketService = ticketService;
        this.nivelService = nivelService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Obtener datos para el dashboard
        
        long clientesRegistrados = clienteService.findAll().size();
        List<Ticket> ticketsActivos = ticketService.findTicketsActivosSinSalida();
        List<Nivel> niveles = nivelService.findAll();
        
        
        
        // Preparar datos para gráfico de ocupación por nivel
        List<Map<String, Object>> nivelesOcupacion = new ArrayList<>();
        for (Nivel nivel : niveles) {
            Map<String, Object> nivelData = new HashMap<>();
            nivelData.put("numeroNivel", nivel.getNumeroNivel());
            
            // Calcular porcentaje de ocupación
            Double porcentaje = nivelService.calcularPorcentajeOcupacion(nivel.getIdNivel());
            // Si es null (posiblemente porque no hay espacios), establecerlo a 0
            nivelData.put("porcentajeOcupacion", porcentaje != null ? porcentaje : 0);
            
            nivelesOcupacion.add(nivelData);
        }
        
        // Obtener tickets recientes (últimas 5 entradas)
        List<Ticket> ticketsRecientes = ticketService.findByFechaHoraEntradaBetween(
                LocalDateTime.now().minusHours(24), LocalDateTime.now());
        
        // Si hay más de 5, solo mostrar los primeros 5
        if (ticketsRecientes.size() > 5) {
            ticketsRecientes = ticketsRecientes.subList(0, 5);
        }
        
        // Calcular total de espacios disponibles (suma de todos los niveles)
        long espaciosDisponibles = 0;
        for (Nivel nivel : niveles) {
            Long disponibles = nivelService.countEspaciosDisponiblesByNivelId(nivel.getIdNivel());
            if (disponibles != null) {
                espaciosDisponibles += disponibles;
            }
        }
        
        // Obtener vehículos estacionados
        List<?> vehiculosEstacionados = vehiculoService.findVehiculosEstacionados();
        
        // Agregar atributos al modelo para la vista
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("espaciosDisponibles", espaciosDisponibles);
        model.addAttribute("vehiculosEstacionados", vehiculosEstacionados.size());
        model.addAttribute("clientesRegistrados", clientesRegistrados);
        model.addAttribute("ticketsActivos", ticketsActivos.size());
        model.addAttribute("niveles", nivelesOcupacion);
        model.addAttribute("ticketsRecientes", ticketsRecientes);
        
        return "index";
    }
}