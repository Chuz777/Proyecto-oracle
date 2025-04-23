package com.oracle.repository;

import com.oracle.domain.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    
    // Buscar vehículo por número de placa
    Optional<Vehiculo> findByNumeroPlaca(String numeroPlaca);
    
    // Buscar vehículos por cliente
    List<Vehiculo> findByClienteIdCliente(Long idCliente);
    
    // Buscar vehículos por tipo
    List<Vehiculo> findByTipoVehiculo(String tipoVehiculo);

    // Buscar vehículos por tipo de energía
    List<Vehiculo> findByTipoEnergia(String tipoEnergia);
    
    // Buscar vehículos actualmente estacionados (con tickets activos)
    @Query("SELECT v FROM Vehiculo v JOIN v.tickets t WHERE t.estado = 'ACTIVO'")
    List<Vehiculo> findVehiculosEstacionados();
    
    // Contar número de veces que un vehículo ha usado el estacionamiento
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.vehiculo.idVehiculo = ?1")
    Long countVisitasByVehiculoId(Long idVehiculo);
    
    // Buscar vehículos que nunca han usado el estacionamiento
    @Query("SELECT v FROM Vehiculo v WHERE NOT EXISTS (SELECT t FROM Ticket t WHERE t.vehiculo = v)")
    List<Vehiculo> findVehiculosSinRegistroDeUso();
}
