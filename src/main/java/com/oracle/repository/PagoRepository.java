package com.oracle.repository;

import com.oracle.domain.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    
    // Buscar pagos por ticket
    List<Pago> findByTicketIdTicket(Long idTicket);
    
    // Buscar pagos por tarifa
    List<Pago> findByTarifaIdTarifa(Long idTarifa);
    
    // Buscar pagos por empleado
    List<Pago> findByEmpleadoIdEmpleado(Long idEmpleado);
    
    // Buscar pagos por fecha
    List<Pago> findByFechaPago(LocalDate fechaPago);
    
    // Buscar pagos en un rango de fechas
    List<Pago> findByFechaPagoBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    // Calcular el total de ingresos en un día
    @Query("SELECT SUM(p.montoTotal) FROM Pago p WHERE p.fechaPago = :fecha")
    Double calcularIngresosPorDia(@Param("fecha") LocalDate fecha);
    
    // Calcular el total de ingresos en un rango de fechas
    @Query("SELECT SUM(p.montoTotal) FROM Pago p WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin")
    Double calcularIngresosEntreFechas(
            @Param("fechaInicio") LocalDate fechaInicio, 
            @Param("fechaFin") LocalDate fechaFin);
    
    // Obtener pagos por vehículo
    @Query("SELECT p FROM Pago p JOIN p.ticket t JOIN t.vehiculo v WHERE v.idVehiculo = :idVehiculo")
    List<Pago> findPagosByVehiculoId(@Param("idVehiculo") Long idVehiculo);
    
    // Obtener estadísticas de pagos por tipo de tarifa
    @Query("SELECT t.tipoTarifa, COUNT(p), SUM(p.montoTotal) FROM Pago p JOIN p.tarifa t " +
           "GROUP BY t.tipoTarifa ORDER BY SUM(p.montoTotal) DESC")
    List<Object[]> obtenerEstadisticasPorTipoTarifa();
    
    // Obtener total de pagos por mes y año
    @Query("SELECT FUNCTION('MONTH', p.fechaPago), FUNCTION('YEAR', p.fechaPago), " +
           "COUNT(p), SUM(p.montoTotal) FROM Pago p " +
           "GROUP BY FUNCTION('YEAR', p.fechaPago), FUNCTION('MONTH', p.fechaPago) " +
           "ORDER BY FUNCTION('YEAR', p.fechaPago), FUNCTION('MONTH', p.fechaPago)")
    List<Object[]> obtenerEstadisticasPorMesYAnio();
    
}
