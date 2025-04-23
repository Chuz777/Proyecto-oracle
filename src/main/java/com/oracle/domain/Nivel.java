package com.oracle.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "NIVELES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nivel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_nivel_generator")
    @SequenceGenerator(name = "seq_nivel_generator", sequenceName = "SEQ_NIVEL", allocationSize = 1)
    @Column(name = "ID_NIVEL")
    private Long idNivel;
    
    @Column(name = "NUMERO_NIVEL", nullable = false)
    private Integer numeroNivel;
    
    @Column(name = "CAPACIDAD_NIVEL", nullable = false)
    private Integer capacidadNivel;
    
    @ManyToOne
    @JoinColumn(name = "ID_ESTACIONAMIENTO")
    private Estacionamiento estacionamiento;
    
    @OneToMany(mappedBy = "nivel", cascade = CascadeType.ALL)
    private List<Espacio> espacios;

    public Long getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Long idNivel) {
        this.idNivel = idNivel;
    }

    public Integer getNumeroNivel() {
        return numeroNivel;
    }

    public void setNumeroNivel(Integer numeroNivel) {
        this.numeroNivel = numeroNivel;
    }

    public Integer getCapacidadNivel() {
        return capacidadNivel;
    }

    public void setCapacidadNivel(Integer capacidadNivel) {
        this.capacidadNivel = capacidadNivel;
    }

    public Estacionamiento getEstacionamiento() {
        return estacionamiento;
    }

    public void setEstacionamiento(Estacionamiento estacionamiento) {
        this.estacionamiento = estacionamiento;
    }

    public List<Espacio> getEspacios() {
        return espacios;
    }

    public void setEspacios(List<Espacio> espacios) {
        this.espacios = espacios;
    }
    
    
}
