package com.oracle.controller;

import com.oracle.domain.Pago;
import com.oracle.domain.Ticket;
import com.oracle.domain.Tarifa;
import com.oracle.domain.Empleado;
import com.oracle.service.PagoService;
import com.oracle.service.TicketService;
import com.oracle.service.TarifaService;
import com.oracle.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final TicketService ticketService;
    private final TarifaService tarifaService;
    private final EmpleadoService empleadoService;

    @Autowired
    public PagoController(PagoService pagoService, TicketService ticketService, 
                            TarifaService tarifaService, EmpleadoService empleadoService) {
        this.pagoService = pagoService;
        this.ticketService = ticketService;
        this.tarifaService = tarifaService;
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public String listarPagos(Model model) {
        List<Pago> pagos = pagoService.findAll();
        model.addAttribute("pagos", pagos);
        return "pagos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoPagoForm(Model model) {
        model.addAttribute("pago", new Pago());
        model.addAttribute("tickets", ticketService.findTicketsActivosSinSalida());
        model.addAttribute("tarifas", tarifaService.findAll());
        model.addAttribute("empleados", empleadoService.findAll());
        model.addAttribute("titulo", "Nuevo Pago");
        return "pagos/form";
    }

    @GetMapping("/nuevo/{idTicket}")
    public String nuevoPagoParaTicket(@PathVariable Long idTicket, Model model) {
        Optional<Ticket> ticket = ticketService.findById(idTicket);
        if (ticket.isPresent()) {
            Pago pago = new Pago();
            pago.setTicket(ticket.get());
            pago.setFechaPago(LocalDate.now());
            
            model.addAttribute("pago", pago);
            model.addAttribute("tickets", List.of(ticket.get()));
            model.addAttribute("tarifas", tarifaService.findAll());
            model.addAttribute("empleados", empleadoService.findAll());
            model.addAttribute("titulo", "Nuevo Pago para Ticket #" + idTicket);
            return "pagos/form";
        } else {
            return "redirect:/pagos/nuevo";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarPagoForm(@PathVariable Long id, Model model) {
        Optional<Pago> pago = pagoService.findById(id);
        if (pago.isPresent()) {
            model.addAttribute("pago", pago.get());
            model.addAttribute("tickets", ticketService.findAll());
            model.addAttribute("tarifas", tarifaService.findAll());
            model.addAttribute("empleados", empleadoService.findAll());
            model.addAttribute("titulo", "Editar Pago");
            return "pagos/form";
        } else {
            return "redirect:/pagos";
        }
    }

    @PostMapping("/guardar")
    public String guardarPago(@ModelAttribute Pago pago, RedirectAttributes flash) {
        try {
            // Si es un pago nuevo y no tiene fecha, establecemos la fecha actual
            if (pago.getIdPago() == null && pago.getFechaPago() == null) {
                pago.setFechaPago(LocalDate.now());
            }
            
            // Si tiene tarifa pero no tiene monto, calculamos el monto según la tarifa
            if (pago.getTarifa() != null && pago.getMontoTotal() == null) {
                Optional<Tarifa> tarifa = tarifaService.findById(pago.getTarifa().getIdTarifa());
                if (tarifa.isPresent()) {
                    pago.setMontoTotal(tarifa.get().getPrecio());
                }
            }
            
            pagoService.save(pago);
            
            // Actualizar el estado del ticket a "pagado" si es necesario
            if (pago.getTicket() != null) {
                Optional<Ticket> ticket = ticketService.findById(pago.getTicket().getIdTicket());
                if (ticket.isPresent()) {
                    Ticket ticketActual = ticket.get();
                    ticketActual.setEstado("pagado");
                    ticketService.save(ticketActual);
                }
            }
            
            flash.addFlashAttribute("mensaje", "Pago guardado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar el pago: " + e.getMessage());
        }
        return "redirect:/pagos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPago(@PathVariable Long id, RedirectAttributes flash) {
        try {
            pagoService.deleteById(id);
            flash.addFlashAttribute("mensaje", "Pago eliminado con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el pago: " + e.getMessage());
        }
        return "redirect:/pagos";
    }

    @GetMapping("/ticket/{idTicket}")
    public String listarPagosPorTicket(@PathVariable Long idTicket, Model model) {
        List<Pago> pagos = pagoService.findByTicketId(idTicket);
        Optional<Ticket> ticket = ticketService.findById(idTicket);
        
        model.addAttribute("pagos", pagos);
        if (ticket.isPresent()) {
            model.addAttribute("ticket", ticket.get());
        }
        
        return "pagos/lista";
    }

    @GetMapping("/tarifa/{idTarifa}")
    public String listarPagosPorTarifa(@PathVariable Long idTarifa, Model model) {
        List<Pago> pagos = pagoService.findByTarifaId(idTarifa);
        Optional<Tarifa> tarifa = tarifaService.findById(idTarifa);
        
        model.addAttribute("pagos", pagos);
        if (tarifa.isPresent()) {
            model.addAttribute("tarifa", tarifa.get());
            model.addAttribute("tipoTarifa", tarifa.get().getTipoTarifa());
        }
        
        return "pagos/lista";
    }

    @GetMapping("/empleado/{idEmpleado}")
    public String listarPagosPorEmpleado(@PathVariable Long idEmpleado, Model model) {
        List<Pago> pagos = pagoService.findByEmpleadoId(idEmpleado);
        Optional<Empleado> empleado = empleadoService.findById(idEmpleado);
        
        model.addAttribute("pagos", pagos);
        if (empleado.isPresent()) {
            model.addAttribute("empleado", empleado.get());
            model.addAttribute("nombreEmpleado", 
                              empleado.get().getNombre() + " " + empleado.get().getApellido());
        }
        
        return "pagos/lista";
    }

    @GetMapping("/fecha/{fecha}")
    public String listarPagosPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, 
            Model model) {
        
        List<Pago> pagos = pagoService.findByFechaPago(fecha);
        model.addAttribute("pagos", pagos);
        model.addAttribute("fecha", fecha);
        
        return "pagos/lista";
    }

    @GetMapping("/rango-fechas")
    public String listarPagosPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {
        
        List<Pago> pagos = pagoService.findByFechaPagoBetween(fechaInicio, fechaFin);
        model.addAttribute("pagos", pagos);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        
        return "pagos/lista";
    }

    @GetMapping("/ingresos-dia/{fecha}")
    public String verIngresosPorDia(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, 
            Model model) {
        
        Double ingresos = pagoService.calcularIngresosPorDia(fecha);
        List<Pago> pagos = pagoService.findByFechaPago(fecha);
        
        model.addAttribute("fecha", fecha);
        model.addAttribute("ingresos", ingresos != null ? ingresos : 0.0);
        model.addAttribute("cantidadPagos", pagos.size());
        model.addAttribute("pagos", pagos);
        
        return "pagos/ingresos-dia";
    }

    @GetMapping("/ingresos-rango")
    public String verIngresosEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {
        
        Double ingresos = pagoService.calcularIngresosEntreFechas(fechaInicio, fechaFin);
        List<Pago> pagos = pagoService.findByFechaPagoBetween(fechaInicio, fechaFin);
        
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("ingresos", ingresos != null ? ingresos : 0.0);
        model.addAttribute("cantidadPagos", pagos.size());
        model.addAttribute("pagos", pagos);
        
        return "pagos/ingresos-rango";
    }

    @GetMapping("/vehiculo/{idVehiculo}")
    public String listarPagosPorVehiculo(@PathVariable Long idVehiculo, Model model) {
        List<Pago> pagos = pagoService.findPagosByVehiculoId(idVehiculo);
        model.addAttribute("pagos", pagos);
        model.addAttribute("idVehiculo", idVehiculo);
        return "pagos/lista";
    }

    @GetMapping("/estadisticas/tipo-tarifa")
    public String verEstadisticasPorTipoTarifa(Model model) {
        List<Object[]> estadisticas = pagoService.obtenerEstadisticasPorTipoTarifa();
        model.addAttribute("estadisticas", estadisticas);
        return "pagos/estadisticas-tipo-tarifa";
    }

    @GetMapping("/estadisticas/mes-anio")
    public String verEstadisticasPorMesYAnio(Model model) {
        List<Object[]> estadisticas = pagoService.obtenerEstadisticasPorMesYAnio();
        model.addAttribute("estadisticas", estadisticas);
        return "pagos/estadisticas-mes-anio";
    }
}