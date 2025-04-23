package com.oracle.repository;

import com.oracle.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
    // Buscar empleado por nombre y apellido
    Optional<Empleado> findByNombreAndApellido(String nombre, String apellido);
    
    // Buscar empleados por cargo
    List<Empleado> findByCargo(String cargo);
    
    // Buscar empleados por apellido que contenga cierto texto
    List<Empleado> findByApellidoContainingIgnoreCase(String apellido);
    
    // Contar cuántos pagos ha procesado un empleado
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.empleado.idEmpleado = :idEmpleado")
    Long countPagosByEmpleadoId(@Param("idEmpleado") Long idEmpleado);
    
    // Calcular el monto total de pagos procesados por un empleado
    @Query("SELECT SUM(p.montoTotal) FROM Pago p WHERE p.empleado.idEmpleado = :idEmpleado")
    Double calcularMontoTotalProcesado(@Param("idEmpleado") Long idEmpleado);
    
    // Encontrar empleados con pagos procesados en un rango de fechas
    @Query("SELECT DISTINCT e FROM Empleado e JOIN e.pagos p WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin")
    List<Empleado> findEmpleadosConPagosEntreFechas(
            @Param("fechaInicio") LocalDate fechaInicio, 
            @Param("fechaFin") LocalDate fechaFin);
    
    // Encontrar al empleado que más pagos ha procesado
    @Query("SELECT e.idEmpleado, COUNT(p) as totalPagos FROM Empleado e JOIN e.pagos p " +
           "GROUP BY e.idEmpleado ORDER BY totalPagos DESC")
    List<Object[]> findEmpleadosConMasPagosProcesados();
    
}
