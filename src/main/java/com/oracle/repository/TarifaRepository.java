package com.oracle.repository;

import com.oracle.domain.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    
    // Buscar tarifa por tipo
    Optional<Tarifa> findByTipoTarifa(String tipoTarifa);
    
    // Buscar tarifas por rango de precios
    List<Tarifa> findByPrecioBetween(Double precioMinimo, Double precioMaximo);
    
    // Ordenar tarifas por precio ascendente
    List<Tarifa> findAllByOrderByPrecioAsc();
    
    // Ordenar tarifas por precio descendente
    List<Tarifa> findAllByOrderByPrecioDesc();
    
    // Encontrar la tarifa más cara
    @Query("SELECT t FROM Tarifa t WHERE t.precio = (SELECT MAX(t2.precio) FROM Tarifa t2)")
    Optional<Tarifa> findTarifaMasCara();
    
    // Encontrar la tarifa más barata
    @Query("SELECT t FROM Tarifa t WHERE t.precio = (SELECT MIN(t2.precio) FROM Tarifa t2)")
    Optional<Tarifa> findTarifaMasBarata();
    
    // Calcular precio promedio de tarifas
    @Query("SELECT AVG(t.precio) FROM Tarifa t")
    Double calcularPrecioPromedio();
    
    // Contar cuántas veces se ha usado una tarifa específica
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.tarifa.idTarifa = :idTarifa")
    Long countUsosByTarifaId(@Param("idTarifa") Long idTarifa);
    
}
