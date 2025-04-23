package com.oracle.repository;

import com.oracle.domain.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

@Repository
public interface NivelRepository extends JpaRepository <Nivel, Long> {

    // Buscar niveles por estacionamiento
    List<Nivel> findByEstacionamientoIdEstacionamiento(Long idEstacionamiento);
    
    // Buscar un nivel específico en un estacionamiento
    Optional<Nivel> findByEstacionamientoIdEstacionamientoAndNumeroNivel(Long idEstacionamiento, Integer numeroNivel);
    
    // Buscar niveles con capacidad mayor a un valor dado
    List<Nivel> findByCapacidadNivelGreaterThanEqual(Integer capacidadMinima);
    
    // Contar espacios disponibles en un nivel
    @Query("SELECT COUNT(e) FROM Espacio e WHERE e.nivel.idNivel = ?1 AND e.estado = 'DISPONIBLE'")
    Long countEspaciosDisponiblesByNivelId(Long idNivel);
    
    // Calcular porcentaje de ocupación de un nivel
    @Query(value = "select ((select count(*) from espacios e where e.id_nivel=:idNivel and e.estado='OCUPADO')*100.0/(select n.capacidad_nivel from niveles n where n.id_nivel=:idNivel)) as porcentaje from dual", nativeQuery = true)
Double calcularPorcentajeOcupacion(@Param("idNivel") Long idNivel);
    
    // Buscar niveles con espacios disponibles
    @Query("SELECT DISTINCT n FROM Nivel n JOIN n.espacios e WHERE e.estado = 'DISPONIBLE'")
    List<Nivel> findNivelesConEspaciosDisponibles();
    
}
