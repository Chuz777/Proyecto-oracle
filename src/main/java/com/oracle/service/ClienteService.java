package com.oracle.service;

import com.oracle.domain.Cliente;
import com.oracle.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findByNombreAndApellido(String nombre, String apellido) {
        return clienteRepository.findByNombreAndApellido(nombre, apellido);
    }

    @Transactional(readOnly = true)
    public List<Cliente> findByApellido(String apellido) {
        return clienteRepository.findByApellidoContainingIgnoreCase(apellido);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findByTelefono(String telefono) {
        return clienteRepository.findByTelefono(telefono);
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Cliente> findClientesConTipoVehiculo(String tipoVehiculo) {
        return clienteRepository.findClientesConTipoVehiculo(tipoVehiculo);
    }

    @Transactional(readOnly = true)
    public Long countVehiculosByClienteId(Long idCliente) {
        return clienteRepository.countVehiculosByClienteId(idCliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> findClientesConTicketsActivos() {
        return clienteRepository.findClientesConTicketsActivos();
    }
    
}
