package com.oracle.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "EMPLEADOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_empleado_generator")
    @SequenceGenerator(name = "seq_empleado_generator", sequenceName = "SEQ_EMPLEADO", allocationSize = 1)
    @Column(name = "ID_EMPLEADO")
    private Long idEmpleado;
    
    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "APELLIDO", nullable = false, length = 50)
    private String apellido;
    
    @Column(name = "CARGO", nullable = false, length = 50)
    private String cargo;
    
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    private List<Pago> pagos;

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
    
    
    
}
