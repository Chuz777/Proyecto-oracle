package com.oracle.controller;

import com.oracle.domain.Empleado;
import com.oracle.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    
    private final EmpleadoService empleadoService;
    
    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }
    
    @GetMapping
    public ResponseEntity<List<Empleado>> getAllEmpleados() {
        List<Empleado> empleados = empleadoService.findAll();
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Empleado> getEmpleadoById(@PathVariable Long id) {
        Optional<Empleado> empleado = empleadoService.findById(id);
        return empleado.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<Empleado> getEmpleadoByNombreAndApellido(
            @RequestParam String nombre, 
            @RequestParam String apellido) {
        Optional<Empleado> empleado = empleadoService.findByNombreAndApellido(nombre, apellido);
        return empleado.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/cargo/{cargo}")
    public ResponseEntity<List<Empleado>> getEmpleadosByCargo(@PathVariable String cargo) {
        List<Empleado> empleados = empleadoService.findByCargo(cargo);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }
    
    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Empleado>> getEmpleadosByApellido(@PathVariable String apellido) {
        List<Empleado> empleados = empleadoService.findByApellido(apellido);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/cantidad-pagos")
    public ResponseEntity<Long> countPagosByEmpleadoId(@PathVariable Long id) {
        Long cantidad = empleadoService.countPagosByEmpleadoId(id);
        return new ResponseEntity<>(cantidad, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/monto-total-procesado")
    public ResponseEntity<Double> calcularMontoTotalProcesado(@PathVariable Long id) {
        Double montoTotal = empleadoService.calcularMontoTotalProcesado(id);
        return new ResponseEntity<>(montoTotal != null ? montoTotal : 0.0, HttpStatus.OK);
    }
    
    @GetMapping("/pagos-entre-fechas")
    public ResponseEntity<List<Empleado>> findEmpleadosConPagosEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Empleado> empleados = empleadoService.findEmpleadosConPagosEntreFechas(fechaInicio, fechaFin);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }
    
    @GetMapping("/mas-pagos-procesados")
    public ResponseEntity<List<Object[]>> findEmpleadosConMasPagosProcesados() {
        List<Object[]> empleados = empleadoService.findEmpleadosConMasPagosProcesados();
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<Empleado> createEmpleado(@RequestBody Empleado empleado) {
        Empleado nuevoEmpleado = empleadoService.save(empleado);
        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> updateEmpleado(@PathVariable Long id, @RequestBody Empleado empleado) {
        Optional<Empleado> empleadoExistente = empleadoService.findById(id);
        if (empleadoExistente.isPresent()) {
            empleado.setIdEmpleado(id);
            Empleado empleadoActualizado = empleadoService.save(empleado);
            return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long id) {
        Optional<Empleado> empleado = empleadoService.findById(id);
        if (empleado.isPresent()) {
            empleadoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}