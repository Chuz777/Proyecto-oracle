package com.oracle.service;

import com.oracle.domain.Vehiculo;
import com.oracle.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {
    
    private final VehiculoRepository vehiculoRepository;

    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> findAll() {
        return vehiculoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Vehiculo> findById(Long id) {
        return vehiculoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Vehiculo> findByNumeroPlaca(String numeroPlaca) {
        return vehiculoRepository.findByNumeroPlaca(numeroPlaca);
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> findByClienteId(Long idCliente) {
        return vehiculoRepository.findByClienteIdCliente(idCliente);
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> findByTipoVehiculo(String tipoVehiculo) {
        return vehiculoRepository.findByTipoVehiculo(tipoVehiculo);
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> findByTipoEnergia(String tipoEnergia) {
        return vehiculoRepository.findByTipoEnergia(tipoEnergia);
    }

    @Transactional
    public Vehiculo save(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public void deleteById(Long id) {
        vehiculoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> findVehiculosEstacionados() {
        return vehiculoRepository.findVehiculosEstacionados();
    }

    @Transactional(readOnly = true)
    public Long countVisitasByVehiculoId(Long idVehiculo) {
        return vehiculoRepository.countVisitasByVehiculoId(idVehiculo);
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> findVehiculosSinRegistroDeUso() {
        return vehiculoRepository.findVehiculosSinRegistroDeUso();
    }
    
}
