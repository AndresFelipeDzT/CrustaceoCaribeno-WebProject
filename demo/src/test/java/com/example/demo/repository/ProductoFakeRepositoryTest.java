package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.entitys.Categoria;
import com.example.demo.entitys.Producto;

@DataJpaTest
class ProductoFakeRepositoryTest {

    @Autowired
    private ProductoFakeRepository productoRepository;

    @Autowired
    private CategoriaFakeRepository categoriaRepository;

    private Categoria entradas;
    private Categoria platosFuertes;

    @BeforeEach
    void prepararDatos() {
        // Arrange
        entradas = categoriaRepository.save(new Categoria("Entradas"));
        platosFuertes = categoriaRepository.save(new Categoria("Platos fuertes"));

        Producto ceviche = new Producto("Ceviche tropical", 28000, "Pescado y cítricos", null);
        ceviche.setCategoria(entradas);
        Producto coctel = new Producto("Cóctel de camarón", 22000, "Camarones frescos", null);
        coctel.setCategoria(entradas);
        Producto pulpo = new Producto("Pulpo a las brasas", 42000, "Pulpo a la parrilla", null);
        pulpo.setCategoria(platosFuertes);
        Producto arroz = new Producto("Arroz con mariscos", 32000, "Arroz y mariscos", null);
        arroz.setCategoria(platosFuertes);
        Producto temporal = new Producto("Plato temporal", 18000, "Producto no disponible", null);
        temporal.setCategoria(entradas);
        temporal.setActivo(false);

        productoRepository.saveAll(List.of(ceviche, coctel, pulpo, arroz, temporal));
    }

    @Test
    void buscarProductosActivos_retornaTodosLosActivos() {
        // Arrange
        // Los productos de prueba se preparan en prepararDatos().

        // Act
        List<Producto> resultado = productoRepository.buscarProductosActivos();

        // Assert
        assertThat(resultado).hasSize(4);
        assertThat(resultado).extracting(Producto::getNombre)
                .containsExactly("Arroz con mariscos", "Ceviche tropical", "Cóctel de camarón",
                        "Pulpo a las brasas");
    }

    @Test
    void buscarActivosPorNombreCategoria_filtraPorCategoriaSinDistinguirMayusculas() {
        // Arrange
        String nombreCategoria = "ENTRADAS";

        // Act
        List<Producto> resultado = productoRepository.buscarActivosPorNombreCategoria(nombreCategoria);

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Producto::getNombre)
                .containsExactly("Ceviche tropical", "Cóctel de camarón");
    }

    @Test
    void buscarActivosEnRangoDePrecio_retornaProductosDentroDelRango() {
        // Arrange
        double precioMinimo = 22000;
        double precioMaximo = 32000;

        // Act
        List<Producto> resultado = productoRepository.buscarActivosEnRangoDePrecio(precioMinimo, precioMaximo);

        // Assert
        assertThat(resultado).hasSize(3);
        assertThat(resultado).extracting(Producto::getNombre)
                .containsExactly("Cóctel de camarón", "Ceviche tropical", "Arroz con mariscos");
    }

    @Test
    void buscarActivosPorNombre_retornaUnSoloCoincidente() {
        // Arrange
        String termino = "PULPO";

        // Act
        List<Producto> resultado = productoRepository.buscarActivosPorNombre(termino);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Pulpo a las brasas");
    }

    @Test
    void buscarActivosDesdePrecio_retornaListaVaciaCuandoNoHayCoincidencias() {
        // Arrange
        double precioMinimo = 50000;

        // Act
        List<Producto> resultado = productoRepository.buscarActivosDesdePrecio(precioMinimo);

        // Assert
        assertThat(resultado).isNotNull().isEmpty();
    }
}
