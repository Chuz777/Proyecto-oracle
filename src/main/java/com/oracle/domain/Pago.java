package com.oracle.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "PAGOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pago_generator")
    @SequenceGenerator(name = "seq_pago_generator", sequenceName = "SEQ_PAGO", allocationSize = 1)
    @Column(name = "ID_PAGO")
    private Long idPago;
    
    @Column(name = "FECHA_PAGO")
    private LocalDate fechaPago;
    
    @Column(name = "MONTO_TOTAL")
    private Double montoTotal;
    
    @ManyToOne
    @JoinColumn(name = "ID_TICKETS")
    private Ticket ticket;
    
    @ManyToOne
    @JoinColumn(name = "ID_TARIFA")
    private Tarifa tarifa;
    
    @ManyToOne
    @JoinColumn(name = "ID_EMPLEADO")
    private Empleado empleado;

    public Long getIdPago() {
        return idPago;
    }

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    
    
}
