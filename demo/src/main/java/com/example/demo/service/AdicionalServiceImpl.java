package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Categoria;
import com.example.demo.repository.AdicionalRepository;

@Service
public class AdicionalServiceImpl implements AdicionalService {

    @Autowired
    AdicionalRepository adicionalRepository;

    @Override
    public List<Adicional> obtenerTodosLosAdicionales() {
        return adicionalRepository.findAll();
    }

    @Override
    public Adicional obtenerAdicionalPorId(Long idAdicional) {
        return adicionalRepository.findById(idAdicional).orElse(null);
    }

    @Override
    public List<Adicional> obtenerAdicionalesPorCategoria(Categoria categoria) {
        if (categoria == null) {
            return adicionalRepository.findAll();
        }
        return adicionalRepository.findByCategoria(categoria);
    }

    @Override
    public Adicional guardarAdicional(Adicional adicional) {
        return adicionalRepository.save(adicional);
    }

    @Override
    public void eliminarAdicional(Long idAdicional) {
        adicionalRepository.deleteById(idAdicional);
    }
}
