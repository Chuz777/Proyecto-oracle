package com.oracle.repository;

import com.oracle.domain.Espacio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspacioRepository extends JpaRepository <Espacio, Long> {
    
    // Buscar espacios por nivel
    List<Espacio> findByNivelIdNivel(Long idNivel);
    
    // Buscar espacios por estado (disponible, ocupado, mantenimiento, etc.)
    List<Espacio> findByEstado(String estado);
    
    // Buscar espacio específico en un nivel
    Optional<Espacio> findByNivelIdNivelAndNumeroEspacio(Long idNivel, Integer numeroEspacio);
    
    // Buscar espacios por ubicación
    List<Espacio> findByUbicacionContainingIgnoreCase(String ubicacion);
    
    // Contar espacios disponibles por nivel
    @Query("SELECT COUNT(e) FROM Espacio e WHERE e.nivel.idNivel = :idNivel AND e.estado = 'DISPONIBLE'")
    Long countEspaciosDisponiblesByNivel(@Param("idNivel") Long idNivel);
    
    // Encontrar el próximo espacio disponible en un nivel
    @Query("SELECT e FROM Espacio e WHERE e.nivel.idNivel = :idNivel AND e.estado = 'DISPONIBLE' ORDER BY e.numeroEspacio ASC")
    List<Espacio> findPrimerEspacioDisponible(@Param("idNivel") Long idNivel);
    
    // Obtener distribución de estados de espacios por nivel
    @Query("SELECT e.estado, COUNT(e) FROM Espacio e WHERE e.nivel.idNivel = :idNivel GROUP BY e.estado")
    List<Object[]> estadisticasDeEspaciosPorNivel(@Param("idNivel") Long idNivel);
    
    // Buscar espacios con mayor frecuencia de uso
    @Query("SELECT e.idEspacio, COUNT(t) as usos FROM Espacio e JOIN e.tickets t GROUP BY e.idEspacio ORDER BY usos DESC")
    List<Object[]> findEspaciosMasUtilizados();

}
