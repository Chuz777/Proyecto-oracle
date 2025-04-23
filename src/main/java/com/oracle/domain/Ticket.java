package com.oracle.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TICKETS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ticket_generator")
    @SequenceGenerator(name = "seq_ticket_generator", sequenceName = "SEQ_TICKET", allocationSize = 1)
    @Column(name = "ID_TICKETS")
    private Long idTicket;
    
    @Column(name = "FECHA_HORA_ENTRADA")
    private LocalDateTime fechaHoraEntrada;
    
    @Column(name = "FECHA_HORA_SALIDA")
    private LocalDateTime fechaHoraSalida;
    
    @Column(name = "ESTADO", nullable = false, length = 15)
    private String estado;
    
    @ManyToOne
    @JoinColumn(name = "ID_VEHICULO")
    private Vehiculo vehiculo;
    
    @ManyToOne
    @JoinColumn(name = "ID_ESPACIO")
    private Espacio espacio;
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Pago> pagos;

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public LocalDateTime getFechaHoraEntrada() {
        return fechaHoraEntrada;
    }

    public void setFechaHoraEntrada(LocalDateTime fechaHoraEntrada) {
        this.fechaHoraEntrada = fechaHoraEntrada;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Espacio getEspacio() {
        return espacio;
    }

    public void setEspacio(Espacio espacio) {
        this.espacio = espacio;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
    
    
    
}
