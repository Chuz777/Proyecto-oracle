package com.oracle.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "VEHICULOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vehiculo_generator")
    @SequenceGenerator(name = "seq_vehiculo_generator", sequenceName = "SEQ_VEHICULO", allocationSize = 1)
    @Column(name = "ID_VEHICULO")
    private Long idVehiculo;
    
    @Column(name = "NUMERO_PLACA", nullable = false, length = 12)
    private String numeroPlaca;
    
    @Column(name = "TIPO_VEHICULO", nullable = false, length = 20)
    private String tipoVehiculo;
    
    @Column(name = "TIPO_ENERGIA", nullable = false, length = 20)
    private String tipoEnergia;
    
    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;
    
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getNumeroPlaca() {
        return numeroPlaca;
    }

    public void setNumeroPlaca(String numeroPlaca) {
        this.numeroPlaca = numeroPlaca;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getTipoEnergia() {
        return tipoEnergia;
    }

    public void setTipoEnergia(String tipoEnergia) {
        this.tipoEnergia = tipoEnergia;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    
    
}
