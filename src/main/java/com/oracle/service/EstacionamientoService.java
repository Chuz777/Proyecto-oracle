package com.oracle.service;

import com.oracle.domain.Estacionamiento;
import com.oracle.repository.EstacionamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstacionamientoService {
    
    private final EstacionamientoRepository estacionamientoRepository;

    @Autowired
    public EstacionamientoService(EstacionamientoRepository estacionamientoRepository) {
        this.estacionamientoRepository = estacionamientoRepository;
    }

    @Transactional(readOnly = true)
    public List<Estacionamiento> findAll() {
        return estacionamientoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Estacionamiento> findById(Long id) {
        return estacionamientoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Estacionamiento> findByNombre(String nombre) {
        return estacionamientoRepository.findByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public List<Estacionamiento> findByDireccion(String direccion) {
        return estacionamientoRepository.findByDireccionContainingIgnoreCase(direccion);
    }

    @Transactional
    public Estacionamiento save(Estacionamiento estacionamiento) {
        return estacionamientoRepository.save(estacionamiento);
    }

    @Transactional
    public void deleteById(Long id) {
        estacionamientoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Estacionamiento> findByCapacidadMinima(Integer capacidadMinima) {
        return estacionamientoRepository.findByCapacidadTotalGreaterThanEqual(capacidadMinima);
    }

    @Transactional(readOnly = true)
    public List<Estacionamiento> findEstacionamientosConEspaciosDisponibles() {
        return estacionamientoRepository.findEstacionamientosConEspaciosDisponibles();
    }

    @Transactional(readOnly = true)
    public Long countNivelesByEstacionamientoId(Long idEstacionamiento) {
        return estacionamientoRepository.countNivelesByEstacionamientoId(idEstacionamiento);
    }
    
    /**
     * Calcula el porcentaje de capacidad completada (niveles vs capacidad total)
     * @param idEstacionamiento ID del estacionamiento
     * @param capacidadActual Capacidad actual del estacionamiento
     * @return Porcentaje de capacidad completada (0-100)
     */
    @Transactional(readOnly = true)
    public Integer calcularPorcentajeCapacidadCompletada(Long idEstacionamiento, Integer capacidadActual) {
        Optional<Estacionamiento> estacionamientoOpt = findById(idEstacionamiento);
        if (estacionamientoOpt.isPresent() && capacidadActual != null) {
            Integer capacidadTotal = estacionamientoOpt.get().getCapacidadTotal();
            if (capacidadTotal > 0) {
                return (capacidadActual * 100) / capacidadTotal;
            }
        }
        return 0;
    }
}