/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import com.oracle.entity.Vehiculo;
import java.util.List;

public interface VehiculoService {
    void guardarVehiculo(Vehiculo vehiculo);
    List<Vehiculo> obtenerTodos();
}