
package service.impl;

import com.oracle.entity.Vehiculo;
import com.oracle.repository.VehiculoRepository;
import com.oracle.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculoServiceImpl implements VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Override
    public void guardarVehiculo(Vehiculo vehiculo) {
        vehiculoRepository.save(vehiculo);
    }

    @Override
    public List<Vehiculo> obtenerTodos() {
        return vehiculoRepository.findAll();
    }
}