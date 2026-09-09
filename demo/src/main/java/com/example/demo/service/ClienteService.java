package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entitys.Cliente;

/**
 * Interfaz que define las operaciones del servicio de clientes.
 */
public interface ClienteService {

    List<Cliente> obtenerTodosLosClientes();

    Cliente obtenerClientePorId(Long id);

    Cliente guardarCliente(Cliente cliente);

    void eliminarCliente(Long id);

    /**
     * Valida las credenciales de un cliente por su nombre o correo.
     * @param nombreOCorreo Nombre de usuario o correo.
     * @param password Contraseña.
     * @return El cliente si las credenciales son válidas, o null si no coinciden.
     */
    Cliente autenticar(String nombreOCorreo, String password);

    /**
     * Realiza el proceso de login completo, validando entradas y credenciales.
     * Lanza IllegalArgumentException si las credenciales son inválidas o faltan datos.
     * @param correo Correo o usuario del cliente.
     * @param password Contraseña del cliente.
     * @return El cliente autenticado exitosamente.
     */
    Cliente login(String correo, String password);

    /**
     * Verifica si un correo ya se encuentra registrado en el sistema.
     * @param correo Correo electrónico a validar.
     * @return true si el correo ya existe, false en caso contrario.
     */
    boolean existeCorreo(String correo);

    /**
     * Busca un cliente por su correo electrónico.
     * @param correo Correo electrónico del cliente.
     * @return Optional con el cliente si existe.
     */
    Optional<Cliente> buscarPorCorreo(String correo);
}
