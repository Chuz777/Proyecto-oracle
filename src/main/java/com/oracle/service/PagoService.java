package com.oracle.service;


import com.oracle.domain.Pago;
import com.oracle.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {
    
    private final PagoRepository pagoRepository;

    @Autowired
    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Transactional(readOnly = true)
    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pago> findById(Long id) {
        return pagoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Pago> findByTicketId(Long idTicket) {
        return pagoRepository.findByTicketIdTicket(idTicket);
    }

    @Transactional(readOnly = true)
    public List<Pago> findByTarifaId(Long idTarifa) {
        return pagoRepository.findByTarifaIdTarifa(idTarifa);
    }

    @Transactional(readOnly = true)
    public List<Pago> findByEmpleadoId(Long idEmpleado) {
        return pagoRepository.findByEmpleadoIdEmpleado(idEmpleado);
    }

    @Transactional(readOnly = true)
    public List<Pago> findByFechaPago(LocalDate fechaPago) {
        return pagoRepository.findByFechaPago(fechaPago);
    }

    @Transactional(readOnly = true)
    public List<Pago> findByFechaPagoBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoRepository.findByFechaPagoBetween(fechaInicio, fechaFin);
    }

    @Transactional
    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }

    @Transactional
    public void deleteById(Long id) {
        pagoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Double calcularIngresosPorDia(LocalDate fecha) {
        return pagoRepository.calcularIngresosPorDia(fecha);
    }

    @Transactional(readOnly = true)
    public Double calcularIngresosEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoRepository.calcularIngresosEntreFechas(fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public List<Pago> findPagosByVehiculoId(Long idVehiculo) {
        return pagoRepository.findPagosByVehiculoId(idVehiculo);
    }

    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorTipoTarifa() {
        return pagoRepository.obtenerEstadisticasPorTipoTarifa();
    }

    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorMesYAnio() {
        return pagoRepository.obtenerEstadisticasPorMesYAnio();
    }
}
