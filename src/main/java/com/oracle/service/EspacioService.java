package com.oracle.service;

import com.oracle.domain.Espacio;
import com.oracle.repository.EspacioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EspacioService {
    
    private final EspacioRepository espacioRepository;

    @Autowired
    public EspacioService(EspacioRepository espacioRepository) {
        this.espacioRepository = espacioRepository;
    }

    @Transactional(readOnly = true)
    public List<Espacio> findAll() {
        return espacioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Espacio> findById(Long id) {
        return espacioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Espacio> findByNivelId(Long idNivel) {
        return espacioRepository.findByNivelIdNivel(idNivel);
    }

    @Transactional(readOnly = true)
    public List<Espacio> findByEstado(String estado) {
        return espacioRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public Optional<Espacio> findByNivelIdAndNumeroEspacio(Long idNivel, Integer numeroEspacio) {
        return espacioRepository.findByNivelIdNivelAndNumeroEspacio(idNivel, numeroEspacio);
    }

    @Transactional(readOnly = true)
    public List<Espacio> findByUbicacion(String ubicacion) {
        return espacioRepository.findByUbicacionContainingIgnoreCase(ubicacion);
    }

    @Transactional
    public Espacio save(Espacio espacio) {
        return espacioRepository.save(espacio);
    }

    @Transactional
    public void deleteById(Long id) {
        espacioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long countEspaciosDisponiblesByNivel(Long idNivel) {
        return espacioRepository.countEspaciosDisponiblesByNivel(idNivel);
    }

    @Transactional(readOnly = true)
    public List<Espacio> findPrimerEspacioDisponible(Long idNivel) {
        return espacioRepository.findPrimerEspacioDisponible(idNivel);
    }

    @Transactional(readOnly = true)
    public List<Object[]> estadisticasDeEspaciosPorNivel(Long idNivel) {
        return espacioRepository.estadisticasDeEspaciosPorNivel(idNivel);
    }

    @Transactional(readOnly = true)
    public List<Object[]> findEspaciosMasUtilizados() {
        return espacioRepository.findEspaciosMasUtilizados();
    }
}
