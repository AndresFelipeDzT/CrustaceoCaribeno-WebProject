package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.entitys.Cliente;

// Pruebas de repositorio para las operaciones CRUD de Cliente.

@DataJpaTest
@ActiveProfiles("test")
public class ClienteRepositoryTest {

    @Autowired
    private ClienteFakeRepository clienteRepository;

    // Carga cuatro clientes iniciales de prueba antes de ejecutar cada test
    @BeforeEach
    void setUp() {
        clienteRepository.save(new Cliente(
                "Juan1",
                "Pérez",
                "juan1@gmail.com",
                "3001111111",
                "Calle 1 # 10-10",
                "clave1"));
        clienteRepository.save(new Cliente(
                "Juan2",
                "Gómez",
                "juan2@gmail.com",
                "3002222222",
                "Calle 2 # 20-20",
                "clave2"));
        clienteRepository.save(new Cliente(
                "Juan3",
                "López",
                "juan3@gmail.com",
                "3003333333",
                "Calle 3 # 30-30",
                "clave3"));
        clienteRepository.save(new Cliente(
                "Juan4",
                "Martínez",
                "juan4@gmail.com",
                "3004444444",
                "Calle 4 # 40-40",
                "clave4"));
    }

    // Prueba CRUD - Guardar un nuevo cliente
    @Test
    public void clienteRepository_save_cliente() {
        // 1. Arrange
        Cliente cliente = new Cliente(
                "Juan",
                "Rodríguez",
                "juan.prueba@gmail.com",
                "3001234567",
                "Calle 45 # 12-34",
                "clave123");

        // 2. Act
        Cliente savedCliente = clienteRepository.save(cliente);

        // 3. Assert
        Assertions.assertThat(savedCliente).isNotNull();
    }

    // Prueba CRUD - Listar todos los clientes
    @Test
    public void clienteRepository_findAll_notEmptyList() {
        // 1. Arrange
        Cliente cliente1 = new Cliente(
                "Ana",
                "Ríos",
                "ana.prueba@gmail.com",
                "3101234567",
                "Carrera 10 # 5-20",
                "clave123");
        Cliente cliente2 = new Cliente(
                "Luis",
                "Castro",
                "luis.prueba@gmail.com",
                "3201234567",
                "Carrera 15 # 8-30",
                "clave456");

        // 2. Act
        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);
        List<Cliente> clientes = clienteRepository.findAll();

        // 3. Assert
        Assertions.assertThat(clientes).isNotNull();
        Assertions.assertThat(clientes.size()).isEqualTo(6);
        Assertions.assertThat(clientes.size()).isGreaterThan(0);
    }

    // Prueba CRUD - Buscar un cliente existente por ID
    @Test
    public void clienteRepository_findById_cliente() {
        // 1. Arrange
        Long index = clienteRepository.findByNombre("Juan1").get().getIdCliente();

        // 2. Act
        Optional<Cliente> cliente = clienteRepository.findById(index);

        // 3. Assert
        Assertions.assertThat(cliente).isPresent();
        Assertions.assertThat(cliente.get().getNombre()).isEqualTo("Juan1");
    }

    // Prueba CRUD - Buscar un cliente con un ID inexistente
    @Test
    public void clienteRepository_findById_findWrongIndex() {
        // 1. Arrange
        Long index = -1L;

        // 2. Act
        Optional<Cliente> cliente = clienteRepository.findById(index);

        // 3. Assert
        Assertions.assertThat(cliente).isEmpty();
    }

    // Prueba CRUD - Eliminar un cliente por ID
    @Test
    public void clienteRepository_deleteById_emptyCliente() {
        // 1. Arrange
        Long index = clienteRepository.findByNombre("Juan2").get().getIdCliente();

        // 2. Act
        clienteRepository.deleteById(index);

        // 3. Assert
        Assertions.assertThat(clienteRepository.findById(index)).isEmpty();
    }

    // Prueba CRUD - Buscar un cliente por nombre
    @Test
    public void clienteRepository_findByNombre_cliente() {
        // 1. Arrange
        String nombre = "Juan1";

        // 2. Act
        Cliente cliente = clienteRepository.findByNombre(nombre).orElse(null);

        // 3. Assert
        Assertions.assertThat(cliente).isNotNull();
    }

    // Prueba CRUD - Actualizar el nombre de un cliente
    @Test
    public void clienteRepository_updateByName_cliente() {
        // 1. Arrange
        String nombre = "Juan1";

        // 2. Act
        Cliente cliente = clienteRepository.findByNombre(nombre).get();
        cliente.setNombre("Modificado");
        Cliente updated = clienteRepository.save(cliente);

        // 3. Assert
        Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(updated.getNombre()).isEqualTo("Modificado");
    }
}