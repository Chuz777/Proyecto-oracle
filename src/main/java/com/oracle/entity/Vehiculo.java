package com.oracle.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Data;
import model.Cliente;

@Entity
@Data
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehiculo;

    private String placa;
    private String marca;
    private String modelo;
    private String color;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
}