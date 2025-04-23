package com.oracle.repository;

import com.oracle.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    // Buscar tickets por estado
    List<Ticket> findByEstado(String estado);
    
    // Buscar tickets por vehículo
    List<Ticket> findByVehiculoIdVehiculo(Long idVehiculo);
    
    // Buscar tickets por espacio
    List<Ticket> findByEspacioIdEspacio(Long idEspacio);
    
    // Buscar tickets por rango de fechas
    List<Ticket> findByFechaHoraEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar tickets activos sin salida registrada
    @Query("SELECT t FROM Ticket t WHERE t.estado = 'ACTIVO' AND t.fechaHoraSalida IS NULL")
    List<Ticket> findTicketsActivosSinSalida();
    
    // Calcular tiempo de estancia promedio
    @Query(value = "SELECT AVG((t.fecha_hora_salida - t.fecha_hora_entrada) * 86400) " +
       "FROM ticket t WHERE t.estado = 'CERRADO' AND t.fecha_hora_salida IS NOT NULL", 
       nativeQuery = true)
Double calcularTiempoPromedioPermanencia();
    
    // Buscar tickets sin pagos registrados
    @Query("SELECT t FROM Ticket t WHERE t.estado = 'CERRADO' AND NOT EXISTS " +
           "(SELECT p FROM Pago p WHERE p.ticket = t)")
    List<Ticket> findTicketsSinPagos();
    
    // Contar tickets por estado y rango de fechas
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.estado = :estado " +
           "AND t.fechaHoraEntrada BETWEEN :fechaInicio AND :fechaFin")
    Long countTicketsByEstadoAndFechas(@Param("estado") String estado, 
                                      @Param("fechaInicio") LocalDateTime fechaInicio, 
                                      @Param("fechaFin") LocalDateTime fechaFin);
    
    // Encontrar entradas de hoy
    @Query("SELECT t FROM Ticket t WHERE TO_CHAR(t.fechaHoraEntrada, 'YYYY-MM-DD') = TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD')")
List<Ticket> findEntradasDeHoy();
    
}
