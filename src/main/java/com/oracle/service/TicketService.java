package com.oracle.service;

import com.oracle.domain.Ticket;
import com.oracle.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Ticket> findByEstado(String estado) {
        return ticketRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Ticket> findByVehiculoId(Long idVehiculo) {
        return ticketRepository.findByVehiculoIdVehiculo(idVehiculo);
    }

    @Transactional(readOnly = true)
    public List<Ticket> findByEspacioId(Long idEspacio) {
        return ticketRepository.findByEspacioIdEspacio(idEspacio);
    }

    @Transactional(readOnly = true)
    public List<Ticket> findByFechaHoraEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ticketRepository.findByFechaHoraEntradaBetween(fechaInicio, fechaFin);
    }

    @Transactional
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Ticket> findTicketsActivosSinSalida() {
        return ticketRepository.findTicketsActivosSinSalida();
    }

    @Transactional(readOnly = true)
    public Double calcularTiempoPromedioPermanencia() {
        return ticketRepository.calcularTiempoPromedioPermanencia();
    }

    @Transactional(readOnly = true)
    public List<Ticket> findTicketsSinPagos() {
        return ticketRepository.findTicketsSinPagos();
    }

    @Transactional(readOnly = true)
    public Long countTicketsByEstadoAndFechas(String estado, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ticketRepository.countTicketsByEstadoAndFechas(estado, fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public List<Ticket> findEntradasDeHoy() {
        return ticketRepository.findEntradasDeHoy();
    }    
}
