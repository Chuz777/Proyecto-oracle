package com.oracle.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ESTACIONAMIENTOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estacionamiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_estacionamiento_generator")
    @SequenceGenerator(name = "seq_estacionamiento_generator", sequenceName = "SEQ_ESTACIONAMIENTO", allocationSize = 1)
    @Column(name = "ID_ESTACIONAMIENTO")
    private Long idEstacionamiento;
    
    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "DIRECCION", nullable = false, length = 100)
    private String direccion;
    
    @Column(name = "CAPACIDAD_TOTAL", nullable = false)
    private Integer capacidadTotal;
    
    @Column(name = "HORARIO_APERTURA")
    private LocalDateTime horarioApertura;
    
    @Column(name = "HORARIO_CIERRE")
    private LocalDateTime horarioCierre;
    
    @OneToMany(mappedBy = "estacionamiento", cascade = CascadeType.ALL)
    private List<Nivel> niveles;

    public Long getIdEstacionamiento() {
        return idEstacionamiento;
    }

    public void setIdEstacionamiento(Long idEstacionamiento) {
        this.idEstacionamiento = idEstacionamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getCapacidadTotal() {
        return capacidadTotal;
    }

    public void setCapacidadTotal(Integer capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
    }

    public LocalDateTime getHorarioApertura() {
        return horarioApertura;
    }

    public void setHorarioApertura(LocalDateTime horarioApertura) {
        this.horarioApertura = horarioApertura;
    }

    public LocalDateTime getHorarioCierre() {
        return horarioCierre;
    }

    public void setHorarioCierre(LocalDateTime horarioCierre) {
        this.horarioCierre = horarioCierre;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }
    
    
    
}
