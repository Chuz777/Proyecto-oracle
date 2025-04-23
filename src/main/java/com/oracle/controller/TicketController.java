package com.oracle.controller;

import com.oracle.domain.Ticket;
import com.oracle.domain.Vehiculo;
import com.oracle.domain.Espacio;
import com.oracle.service.TicketService;
import com.oracle.service.VehiculoService;
import com.oracle.service.EspacioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final VehiculoService vehiculoService;
    private final EspacioService espacioService;

    @Autowired
    public TicketController(TicketService ticketService, VehiculoService vehiculoService, EspacioService espacioService) {
        this.ticketService = ticketService;
        this.vehiculoService = vehiculoService;
        this.espacioService = espacioService;
    }

    @GetMapping
    public String listarTickets(Model model) {
        List<Ticket> tickets = ticketService.findAll();
        model.addAttribute("tickets", tickets);
        return "tickets/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("vehiculos", vehiculoService.findAll());
        model.addAttribute("espacios", espacioService.findByEstado("disponible"));
        model.addAttribute("titulo", "Nuevo Ticket");
        return "tickets/form";
    }

    @GetMapping("/editar/{id}")
    public String editarTicketForm(@PathVariable Long id, Model model) {
        Optional<Ticket> ticket = ticketService.findById(id);
        if (ticket.isPresent()) {
            model.addAttribute("ticket", ticket.get());
            model.addAttribute("vehiculos", vehiculoService.findAll());
            // Para edición incluimos todos los espacios, no solo los disponibles
            model.addAttribute("espacios", espacioService.findAll());
            model.addAttribute("titulo", "Editar Ticket");
            return "tickets/form";
        } else {
            return "redirect:/tickets";
        }
    }

    @PostMapping("/guardar")
    public String guardarTicket(@ModelAttribute Ticket ticket, RedirectAttributes flash) {
        try {
            // Si es un ticket nuevo, establecemos la fecha y hora actual como entrada
            if (ticket.getIdTicket() == null) {
                ticket.setFechaHoraEntrada(LocalDateTime.now());
                
                // Además, actualizamos el estado del espacio a 'ocupado'
                if (ticket.getEspacio() != null) {
                    Optional<Espacio> espacio = espacioService.findById(ticket.getEspacio().getIdEspacio());
                    if (espacio.isPresent()) {
                        Espacio espacioActual = espacio.get();
                        espacioActual.setEstado("ocupado");
                        espacioService.save(espacioActual);
                    }
                }
            }
            
            // Si el ticket se está cerrando (se establece fecha de salida), actualizamos el estado del espacio
            if (ticket.getFechaHoraSalida() != null && ticket.getEspacio() != null) {
                Optional<Espacio> espacio = espacioService.findById(ticket.getEspacio().getIdEspacio());
                if (espacio.isPresent()) {
                    Espacio espacioActual = espacio.get();
                    espacioActual.setEstado("disponible");
                    espacioService.save(espacioActual);
                }
            }
            
            ticketService.save(ticket);
            flash.addFlashAttribute("mensaje", "Ticket guardado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar el ticket: " + e.getMessage());
        }
        return "redirect:/tickets";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTicket(@PathVariable Long id, RedirectAttributes flash) {
        try {
            // Antes de eliminar el ticket, verificamos si necesitamos liberar el espacio
            Optional<Ticket> ticket = ticketService.findById(id);
            if (ticket.isPresent() && ticket.get().getEspacio() != null && ticket.get().getFechaHoraSalida() == null) {
                Optional<Espacio> espacio = espacioService.findById(ticket.get().getEspacio().getIdEspacio());
                if (espacio.isPresent()) {
                    Espacio espacioActual = espacio.get();
                    espacioActual.setEstado("disponible");
                    espacioService.save(espacioActual);
                }
            }
            
            ticketService.deleteById(id);
            flash.addFlashAttribute("mensaje", "Ticket eliminado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el ticket. Puede tener registros asociados.");
        }
        return "redirect:/tickets";
    }

    @GetMapping("/estado/{estado}")
    public String listarTicketsPorEstado(@PathVariable String estado, Model model) {
        List<Ticket> tickets = ticketService.findByEstado(estado);
        model.addAttribute("tickets", tickets);
        model.addAttribute("estadoSeleccionado", estado);
        return "tickets/lista";
    }

    @GetMapping("/vehiculo/{idVehiculo}")
    public String listarTicketsPorVehiculo(@PathVariable Long idVehiculo, Model model) {
        List<Ticket> tickets = ticketService.findByVehiculoId(idVehiculo);
        Optional<Vehiculo> vehiculo = vehiculoService.findById(idVehiculo);
        
        model.addAttribute("tickets", tickets);
        if (vehiculo.isPresent()) {
            model.addAttribute("vehiculo", vehiculo.get());
            model.addAttribute("placaVehiculo", vehiculo.get().getNumeroPlaca());
        }
        
        return "tickets/lista";
    }

    @GetMapping("/espacio/{idEspacio}")
    public String listarTicketsPorEspacio(@PathVariable Long idEspacio, Model model) {
        List<Ticket> tickets = ticketService.findByEspacioId(idEspacio);
        Optional<Espacio> espacio = espacioService.findById(idEspacio);
        
        model.addAttribute("tickets", tickets);
        if (espacio.isPresent()) {
            model.addAttribute("espacio", espacio.get());
            model.addAttribute("detalleEspacio", "Nivel: " + espacio.get().getNivel().getNumeroNivel() + 
                              ", Espacio: " + espacio.get().getNumeroEspacio());
        }
        
        return "tickets/lista";
    }

    @GetMapping("/rango-fechas")
    public String listarTicketsPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            Model model) {
        
        List<Ticket> tickets = ticketService.findByFechaHoraEntradaBetween(fechaInicio, fechaFin);
        model.addAttribute("tickets", tickets);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        
        return "tickets/lista";
    }

    @GetMapping("/activos")
    public String listarTicketsActivos(Model model) {
        List<Ticket> tickets = ticketService.findTicketsActivosSinSalida();
        model.addAttribute("tickets", tickets);
        model.addAttribute("mostrandoActivos", true);
        return "tickets/lista";
    }

    @GetMapping("/sin-pagos")
    public String listarTicketsSinPagos(Model model) {
        List<Ticket> tickets = ticketService.findTicketsSinPagos();
        model.addAttribute("tickets", tickets);
        model.addAttribute("mostrandoSinPagos", true);
        return "tickets/lista";
    }

    @GetMapping("/entradas-hoy")
    public String listarEntradasDeHoy(Model model) {
        List<Ticket> tickets = ticketService.findEntradasDeHoy();
        model.addAttribute("tickets", tickets);
        model.addAttribute("mostrandoEntradasHoy", true);
        return "tickets/lista";
    }

    @GetMapping("/estadisticas")
    public String verEstadisticasTickets(Model model) {
        Double tiempoPromedioPermanencia = ticketService.calcularTiempoPromedioPermanencia();
        Long ticketsActivosCount = (long) ticketService.findTicketsActivosSinSalida().size();
        
        // Podríamos obtener otras estadísticas si las necesitamos
        
        model.addAttribute("tiempoPromedioPermanencia", tiempoPromedioPermanencia);
        model.addAttribute("ticketsActivosCount", ticketsActivosCount);
        
        return "tickets/estadisticas";
    }
}