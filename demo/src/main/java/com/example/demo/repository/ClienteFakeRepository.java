package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Cliente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repositorio falso en memoria para almacenar clientes.
 * Contiene datos iniciales sin tildes para pruebas rápidas.
 */
@Repository
public class ClienteFakeRepository {

    private Map<Integer, Cliente> tablaClientes = new HashMap<>();
    private int secuenciaId = 0;

    public ClienteFakeRepository() {
        cargarDatosQuemados();
    }

    private void cargarDatosQuemados() {
        save(new Cliente(null, "Felipe", "felipe@correo.com", "3001234567", "Calle 10 # 20-30", "1234"));
        save(new Cliente(null, "Maria Gomez", "maria@correo.com", "3109876543", "Carrera 15 # 45-12", "abcd"));
        save(new Cliente(null, "Carlos Perez", "carlos@correo.com", "3201122334", "Avenida 30 # 18-50", "pass123"));
    }

    public List<Cliente> findAll() {
        return new ArrayList<>(tablaClientes.values());
    }

    public Optional<Cliente> findById(int id) {
        return Optional.ofNullable(tablaClientes.get(id));
    }

    public Optional<Cliente> findByNombreOrCorreo(String criterio) {
        if (criterio == null) return Optional.empty();
        String criterioLower = criterio.trim().toLowerCase();
        
        return tablaClientes.values().stream()
            .filter(c -> (c.getNombreCompleto() != null && c.getNombreCompleto().toLowerCase().equals(criterioLower)) ||
                         (c.getCorreo() != null && c.getCorreo().toLowerCase().equals(criterioLower)))
            .findFirst();
    }

    public Cliente save(Cliente cliente) {
        if (cliente.getIdCliente() == null) {
            secuenciaId++;
            cliente.setIdCliente(secuenciaId);
        } else if (cliente.getIdCliente() > secuenciaId) {
            secuenciaId = cliente.getIdCliente();
        }
        tablaClientes.put(cliente.getIdCliente(), cliente);
        return cliente;
    }

    public boolean deleteById(int id) {
        return tablaClientes.remove(id) != null;
    }
}
