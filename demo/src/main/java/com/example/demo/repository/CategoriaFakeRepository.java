package com.example.demo.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Categoria;
import com.example.demo.entitys.Producto;


/**
 * Implementación de la falsa base de datos de categorías
 * Los productos se almacenan en un HashMap donde la llave es el idCategoria,
 * para permitir accesos directos por id.
 */
@Repository
public class CategoriaFakeRepository {
    
    /* Mapa Hash -> Llave = idCategoria */
    private Map<Integer, Categoria> tablaCategorias = new HashMap<>();

    /**
     * Constructor que inicializa los datos de prueba quemados.
     */
    public CategoriaFakeRepository() {

        tablaCategorias.put(1, new Categoria(
            1,
            "Entrada"
        ));
        tablaCategorias.put(2, new Categoria(
            2,
            "Plato Fuerte"

        ));
        tablaCategorias.put(3, new Categoria(
            3,
            "Especialidades De La Casa"
        ));
    }

    public List<Categoria> findAll() {
        return new ArrayList<>(tablaCategorias.values());
    }

    public Categoria findById(int idCategoria) {
        return tablaCategorias.get(idCategoria);
    }
}
