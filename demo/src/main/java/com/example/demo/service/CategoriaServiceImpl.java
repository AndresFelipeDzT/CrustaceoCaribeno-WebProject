package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Categoria;
import com.example.demo.repository.CategoriaFakeRepository;
import com.example.demo.repository.ProductoFakeRepository;

/**
 * Implementación de la capa de servicio para categorias de platos.
 * Maneja la lógica de negocio y se comunica con el repositorio.
 */
@Service
public class CategoriaServiceImpl implements CategoriaService{

    @Autowired
    CategoriaFakeRepository categoriaRepository;

    @Override
    public List<Categoria> obtenerTodasLasCategorias(){
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerCategoriaPorNombre(String nombre){
        return categoriaRepository.findByName(nombre);
    }
}
