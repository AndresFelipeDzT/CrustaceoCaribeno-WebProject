package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Categoria;
import com.example.demo.errors.AdicionalNotFoundException;
import com.example.demo.repository.AdicionalRepository;

@Service
public class AdicionalServiceImpl implements AdicionalService {

    @Autowired
    private AdicionalRepository adicionalRepository;

    @Override
    public List<Adicional> obtenerTodosLosAdicionales() {
        return adicionalRepository.findByActivoTrue();
    }

    @Override
    public Adicional obtenerAdicionalPorId(Long idAdicional) {
        if (idAdicional == null) {
            throw new IllegalArgumentException("El ID del adicional es obligatorio.");
        }
        return adicionalRepository.findById(idAdicional)
                .orElseThrow(() -> new AdicionalNotFoundException(idAdicional));
    }

    @Override
    public List<Adicional> obtenerAdicionalesPorCategoria(Categoria categoria) {
        if (categoria == null) {
            return obtenerTodosLosAdicionales();
        }
        return adicionalRepository.findByCategoriasContainingAndActivoTrue(categoria);
    }

    @Override
    public Adicional guardarAdicional(Adicional adicional) {
        if (adicional == null) {
            throw new IllegalArgumentException("Los datos del adicional no pueden ser nulos.");
        }
        return adicionalRepository.save(adicional);
    }

    @Override
    public void eliminarAdicional(Long idAdicional) {
        Adicional adicional = obtenerAdicionalPorId(idAdicional);
        adicional.setActivo(false);
        adicionalRepository.save(adicional);
    }
}
