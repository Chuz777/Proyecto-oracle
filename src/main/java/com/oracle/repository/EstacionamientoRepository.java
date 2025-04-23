package com.oracle.repository;

import com.oracle.domain.Estacionamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstacionamientoRepository extends JpaRepository<Estacionamiento, Long> {
    
    // Buscar estacionamiento por nombre
    Optional<Estacionamiento> findByNombre(String nombre);
    
    // Buscar estacionamientos por dirección (parcial)
    List<Estacionamiento> findByDireccionContainingIgnoreCase(String direccion);
    
    // Buscar estacionamientos con capacidad mayor a un valor dado
    List<Estacionamiento> findByCapacidadTotalGreaterThanEqual(Integer capacidadMinima);
    
    // Consulta personalizada para obtener estacionamientos con espacios disponibles
    @Query("SELECT e FROM Estacionamiento e JOIN e.niveles n JOIN n.espacios esp WHERE esp.estado = 'DISPONIBLE' GROUP BY e HAVING COUNT(esp) > 0")
    List<Estacionamiento> findEstacionamientosConEspaciosDisponibles();
    
    // Contar niveles de un estacionamiento
    @Query("SELECT COUNT(n) FROM Nivel n WHERE n.estacionamiento.idEstacionamiento = ?1")
    Long countNivelesByEstacionamientoId(Long idEstacionamiento);
    
}
