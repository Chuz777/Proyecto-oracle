package com.oracle.repository;

import com.oracle.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Buscar cliente por nombre y apellido
    Optional<Cliente> findByNombreAndApellido(String nombre, String apellido);
    
    // Buscar clientes por apellido
    List<Cliente> findByApellidoContainingIgnoreCase(String apellido);
    
    // Buscar cliente por teléfono
    Optional<Cliente> findByTelefono(String telefono);
    
    // Buscar clientes con vehículos de cierto tipo
    @Query("SELECT DISTINCT c FROM Cliente c JOIN c.vehiculos v WHERE v.tipoVehiculo = ?1")
    List<Cliente> findClientesConTipoVehiculo(String tipoVehiculo);
    
    // Contar cuántos vehículos tiene un cliente
    @Query("SELECT COUNT(v) FROM Vehiculo v WHERE v.cliente.idCliente = ?1")
    Long countVehiculosByClienteId(Long idCliente);
    
    // Buscar clientes que tengan tickets activos
    @Query("SELECT DISTINCT c FROM Cliente c JOIN c.vehiculos v JOIN v.tickets t WHERE t.estado = 'ACTIVO'")
    List<Cliente> findClientesConTicketsActivos();
    
}
