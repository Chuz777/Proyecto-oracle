package com.oracle.service;

import com.oracle.domain.Nivel;
import com.oracle.repository.NivelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NivelService {
    
    private final NivelRepository nivelRepository;

    @Autowired
    public NivelService(NivelRepository nivelRepository) {
        this.nivelRepository = nivelRepository;
    }

    @Transactional(readOnly = true)
    public List<Nivel> findAll() {
        return nivelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Nivel> findById(Long id) {
        return nivelRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Nivel> findByEstacionamientoId(Long idEstacionamiento) {
        return nivelRepository.findByEstacionamientoIdEstacionamiento(idEstacionamiento);
    }

    @Transactional(readOnly = true)
    public Optional<Nivel> findByEstacionamientoAndNumeroNivel(Long idEstacionamiento, Integer numeroNivel) {
        return nivelRepository.findByEstacionamientoIdEstacionamientoAndNumeroNivel(idEstacionamiento, numeroNivel);
    }

    @Transactional
    public Nivel save(Nivel nivel) {
        return nivelRepository.save(nivel);
    }

    @Transactional
    public void deleteById(Long id) {
        nivelRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Nivel> findByCapacidadMinima(Integer capacidadMinima) {
        return nivelRepository.findByCapacidadNivelGreaterThanEqual(capacidadMinima);
    }

    @Transactional(readOnly = true)
    public Long countEspaciosDisponiblesByNivelId(Long idNivel) {
        return nivelRepository.countEspaciosDisponiblesByNivelId(idNivel);
    }

    @Transactional(readOnly = true)
    public Double calcularPorcentajeOcupacion(Long idNivel) {
        return nivelRepository.calcularPorcentajeOcupacion(idNivel);
    }

    @Transactional(readOnly = true)
    public List<Nivel> findNivelesConEspaciosDisponibles() {
        return nivelRepository.findNivelesConEspaciosDisponibles();
    }
}
