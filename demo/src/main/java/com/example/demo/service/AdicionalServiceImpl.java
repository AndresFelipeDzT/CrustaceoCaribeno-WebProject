package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Categoria;
import com.example.demo.errors.AdicionalNotFoundException;
import com.example.demo.errors.CategoriaNotFoundException;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaFakeRepository;

@Service
public class AdicionalServiceImpl implements AdicionalService {

    @Autowired
    private AdicionalRepository adicionalRepository;

    @Autowired
    private CategoriaFakeRepository categoriaRepository;

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
    public List<AdicionalDTO> obtenerAdicionalesDTO(Long categoriaId) {
        List<Adicional> adicionales;
        if (categoriaId == null) {
            adicionales = obtenerTodosLosAdicionales();
        } else {
            Categoria categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new CategoriaNotFoundException(categoriaId));
            adicionales = obtenerAdicionalesPorCategoria(categoria);
        }
        return adicionales.stream().map(this::aDTO).toList();
    }

    @Override
    public AdicionalDTO obtenerAdicionalDTOPorId(Long idAdicional) {
        return aDTO(obtenerAdicionalPorId(idAdicional));
    }

    @Override
    public Adicional guardarAdicional(Adicional adicional) {
        if (adicional == null) {
            throw new IllegalArgumentException("Los datos del adicional no pueden ser nulos.");
        }
        return adicionalRepository.save(adicional);
    }

    @Override
    public void actualizarAdicional(Long idAdicional, Adicional adicional) {
        if (adicional == null) {
            throw new IllegalArgumentException("Los datos del adicional no pueden ser nulos.");
        }
        Adicional adicionalExistente = obtenerAdicionalPorId(idAdicional);
        adicional.setIdAdicional(idAdicional);
        adicional.setActivo(adicionalExistente.isActivo());
        adicionalRepository.save(adicional);
    }

    @Override
    public void eliminarAdicional(Long idAdicional) {
        Adicional adicional = obtenerAdicionalPorId(idAdicional);
        adicional.setActivo(false);
        adicionalRepository.save(adicional);
    }

    private AdicionalDTO aDTO(Adicional adicional) {
        return new AdicionalDTO(
                adicional.getIdAdicional(),
                adicional.getNombre(),
                adicional.getPrecio(),
                adicional.isActivo());
    }
}
