package com.oracle.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "TARIFAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tarifa_generator")
    @SequenceGenerator(name = "seq_tarifa_generator", sequenceName = "SEQ_TARIFA", allocationSize = 1)
    @Column(name = "ID_TARIFA")
    private Long idTarifa;
    
    @Column(name = "TIPO_TARIFA", nullable = false, length = 20)
    private String tipoTarifa;
    
    @Column(name = "PRECIO")
    private Double precio;
    
    @OneToMany(mappedBy = "tarifa", cascade = CascadeType.ALL)
    private List<Pago> pagos;

    public Long getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Long idTarifa) {
        this.idTarifa = idTarifa;
    }

    public String getTipoTarifa() {
        return tipoTarifa;
    }

    public void setTipoTarifa(String tipoTarifa) {
        this.tipoTarifa = tipoTarifa;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
    
    
    
}
