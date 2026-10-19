package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.AdicionalRequestDTO;
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
    public List<Adicional> obtenerTodosLosAdicionalesIncluyendoInactivos() {
        return adicionalRepository.findAll();
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
    public List<Adicional> obtenerAdicionalesPorCategoriaIncluyendoInactivos(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría es obligatoria.");
        }
        return adicionalRepository.findByCategoriasContaining(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdicionalDTO> obtenerAdicionalesDTO(boolean incluirInactivos, Long categoriaId) {
        List<Adicional> adicionales;
        if (categoriaId != null) {
            Categoria categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new CategoriaNotFoundException(categoriaId));
            adicionales = incluirInactivos
                    ? obtenerAdicionalesPorCategoriaIncluyendoInactivos(categoria)
                    : obtenerAdicionalesPorCategoria(categoria);
        } else {
            adicionales = incluirInactivos
                    ? obtenerTodosLosAdicionalesIncluyendoInactivos()
                    : obtenerTodosLosAdicionales();
        }
        return adicionales.stream().map(this::aDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdicionalDTO obtenerAdicionalDTOPorId(Long idAdicional) {
        return aDTO(obtenerAdicionalPorId(idAdicional));
    }

    @Override
    @Transactional
    public Adicional guardarAdicional(AdicionalRequestDTO datos) {
        validarDatos(datos);
        Adicional adicional = new Adicional(datos.getNombre(), datos.getPrecio());
        adicional = adicionalRepository.save(adicional);
        actualizarCategorias(adicional, datos.getCategoriaIds());
        return adicional;
    }

    @Override
    @Transactional
    public void actualizarAdicional(Long idAdicional, AdicionalRequestDTO datos) {
        validarDatos(datos);
        Adicional adicional = obtenerAdicionalPorId(idAdicional);
        adicional.setNombre(datos.getNombre());
        adicional.setPrecio(datos.getPrecio());
        adicionalRepository.save(adicional);
        actualizarCategorias(adicional, datos.getCategoriaIds());
    }

    private void validarDatos(AdicionalRequestDTO datos) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos del adicional no pueden ser nulos.");
        }
        if (datos.getCategoriaIds() == null) {
            throw new IllegalArgumentException("La lista de categorías es obligatoria.");
        }
    }

    private void actualizarCategorias(Adicional adicional, List<Long> categoriaIds) {
        if (adicional.getCategorias() == null) {
            adicional.setCategorias(new ArrayList<>());
        }
        List<Categoria> categoriasActuales = new ArrayList<>(adicional.getCategorias());
        for (Categoria categoria : categoriasActuales) {
            if (categoria.getAdicionales() == null) {
                categoria.setAdicionales(new ArrayList<>());
            }
            categoria.getAdicionales().removeIf(
                    adicionalRelacionado -> adicional.getIdAdicional().equals(adicionalRelacionado.getIdAdicional()));
            categoriaRepository.save(categoria);
        }
        adicional.getCategorias().clear();

        List<Long> categoriasProcesadas = new ArrayList<>();
        for (Long categoriaId : categoriaIds) {
            if (categoriaId == null) {
                throw new IllegalArgumentException("Los IDs de categoría no pueden ser nulos.");
            }
            if (categoriasProcesadas.contains(categoriaId)) {
                continue;
            }
            categoriasProcesadas.add(categoriaId);
            Categoria categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new CategoriaNotFoundException(categoriaId));
            if (categoria.getAdicionales() == null) {
                categoria.setAdicionales(new ArrayList<>());
            }
            categoria.getAdicionales().add(adicional);
            adicional.getCategorias().add(categoria);
            categoriaRepository.save(categoria);
        }
    }

    @Override
    public void eliminarAdicional(Long idAdicional) {
        Adicional adicional = obtenerAdicionalPorId(idAdicional);
        adicional.setActivo(false);
        adicionalRepository.save(adicional);
    }

    @Override
    public void activarAdicional(Long idAdicional) {
        Adicional adicional = obtenerAdicionalPorId(idAdicional);
        adicional.setActivo(true);
        adicionalRepository.save(adicional);
    }

    private AdicionalDTO aDTO(Adicional adicional) {
        return new AdicionalDTO(
                adicional.getIdAdicional(),
                adicional.getNombre(),
                adicional.getPrecio(),
                adicional.isActivo(),
                adicional.getCategorias().stream().map(Categoria::getIdCategoria).toList());
    }
}
