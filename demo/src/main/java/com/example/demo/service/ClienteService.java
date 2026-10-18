package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.ClienteDTO;
import com.example.demo.entitys.Cliente;

/**
 * Interfaz que define las operaciones del servicio de clientes.
 */
public interface ClienteService {

    List<Cliente> obtenerTodosLosClientes();

    List<ClienteDTO> obtenerTodosLosClientesDTO();

    Cliente obtenerClientePorId(Long id);

    ClienteDTO obtenerClienteDTOPorId(Long id);

    Cliente guardarCliente(Cliente cliente);

    void actualizarCliente(Long idCliente, Cliente cliente);

    void eliminarCliente(Long id);

    /**
     * Valida las credenciales de un cliente por su nombre o correo.
     * @param nombreOCorreo Nombre de usuario o correo.
     * @param password Contraseña.
     * @return El cliente si las credenciales son válidas, o null si no coinciden.
     */
    Cliente autenticar(String nombreOCorreo, String password);

    /**
     * Realiza el proceso de login completo.
     * @param correo Correo o usuario del cliente.
     * @param password Contraseña del cliente.
     * @return El cliente autenticado exitosamente.
     */
    Cliente login(String correo, String password);
}
