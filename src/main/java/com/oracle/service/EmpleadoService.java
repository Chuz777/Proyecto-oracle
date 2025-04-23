package com.oracle.service;

import com.oracle.domain.Empleado;
import com.oracle.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {
    
    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Transactional(readOnly = true)
    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Empleado> findById(Long id) {
        return empleadoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Empleado> findByNombreAndApellido(String nombre, String apellido) {
        return empleadoRepository.findByNombreAndApellido(nombre, apellido);
    }

    @Transactional(readOnly = true)
    public List<Empleado> findByCargo(String cargo) {
        return empleadoRepository.findByCargo(cargo);
    }

    @Transactional(readOnly = true)
    public List<Empleado> findByApellido(String apellido) {
        return empleadoRepository.findByApellidoContainingIgnoreCase(apellido);
    }

    @Transactional
    public Empleado save(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Transactional
    public void deleteById(Long id) {
        empleadoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long countPagosByEmpleadoId(Long idEmpleado) {
        return empleadoRepository.countPagosByEmpleadoId(idEmpleado);
    }

    @Transactional(readOnly = true)
    public Double calcularMontoTotalProcesado(Long idEmpleado) {
        return empleadoRepository.calcularMontoTotalProcesado(idEmpleado);
    }

    @Transactional(readOnly = true)
    public List<Empleado> findEmpleadosConPagosEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return empleadoRepository.findEmpleadosConPagosEntreFechas(fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public List<Object[]> findEmpleadosConMasPagosProcesados() {
        return empleadoRepository.findEmpleadosConMasPagosProcesados();
    }
    
}
