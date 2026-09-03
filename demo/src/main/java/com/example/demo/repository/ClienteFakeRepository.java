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

    public Optional<Cliente> findById(Integer id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(tablaClientes.get(id));
    }

    public Optional<Cliente> findByCorreo(String correo) {
        if (correo == null) return Optional.empty();
        String correoBuscado = correo.trim();
        for (Cliente c : tablaClientes.values()) {
            if (c.getCorreo() != null && c.getCorreo().equalsIgnoreCase(correoBuscado)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public Optional<Cliente> findByNombreOrCorreo(String criterio) {
        if (criterio == null) return Optional.empty();
        String criterioBuscado = criterio.trim();
        for (Cliente c : tablaClientes.values()) {
            if ((c.getNombreCompleto() != null && c.getNombreCompleto().equalsIgnoreCase(criterioBuscado)) ||
                (c.getCorreo() != null && c.getCorreo().equalsIgnoreCase(criterioBuscado))) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
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

    public boolean deleteById(Integer id) {
        if (id == null) return false;
        return tablaClientes.remove(id) != null;
    }
}
