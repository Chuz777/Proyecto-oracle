package com.oracle.service;

import com.oracle.domain.Tarifa;
import com.oracle.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TarifaService {
    
    private final TarifaRepository tarifaRepository;

    @Autowired
    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    @Transactional(readOnly = true)
    public List<Tarifa> findAll() {
        return tarifaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Tarifa> findById(Long id) {
        return tarifaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Tarifa> findByTipoTarifa(String tipoTarifa) {
        return tarifaRepository.findByTipoTarifa(tipoTarifa);
    }

    @Transactional(readOnly = true)
    public List<Tarifa> findByPrecioBetween(Double precioMinimo, Double precioMaximo) {
        return tarifaRepository.findByPrecioBetween(precioMinimo, precioMaximo);
    }

    @Transactional(readOnly = true)
    public List<Tarifa> findAllOrderByPrecioAsc() {
        return tarifaRepository.findAllByOrderByPrecioAsc();
    }

    @Transactional(readOnly = true)
    public List<Tarifa> findAllOrderByPrecioDesc() {
        return tarifaRepository.findAllByOrderByPrecioDesc();
    }

    @Transactional
    public Tarifa save(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    @Transactional
    public void deleteById(Long id) {
        tarifaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Tarifa> findTarifaMasCara() {
        return tarifaRepository.findTarifaMasCara();
    }

    @Transactional(readOnly = true)
    public Optional<Tarifa> findTarifaMasBarata() {
        return tarifaRepository.findTarifaMasBarata();
    }

    @Transactional(readOnly = true)
    public Double calcularPrecioPromedio() {
        return tarifaRepository.calcularPrecioPromedio();
    }

    @Transactional(readOnly = true)
    public Long countUsosByTarifaId(Long idTarifa) {
        return tarifaRepository.countUsosByTarifaId(idTarifa);
    }
    
}
