package com.oracle.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ESPACIOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Espacio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_espacio_generator")
    @SequenceGenerator(name = "seq_espacio_generator", sequenceName = "SEQ_ESPACIO", allocationSize = 1)
    @Column(name = "ID_ESPACIO")
    private Long idEspacio;
    
    @Column(name = "NUMERO_ESPACIO", nullable = false)
    private Integer numeroEspacio;
    
    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;
    
    @Column(name = "UBICACION", nullable = false, length = 50)
    private String ubicacion;
    
    @ManyToOne
    @JoinColumn(name = "ID_NIVEL")
    private Nivel nivel;
    
    @OneToMany(mappedBy = "espacio", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    public Long getIdEspacio() {
        return idEspacio;
    }

    public void setIdEspacio(Long idEspacio) {
        this.idEspacio = idEspacio;
    }

    public Integer getNumeroEspacio() {
        return numeroEspacio;
    }

    public void setNumeroEspacio(Integer numeroEspacio) {
        this.numeroEspacio = numeroEspacio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    
    
    
}
