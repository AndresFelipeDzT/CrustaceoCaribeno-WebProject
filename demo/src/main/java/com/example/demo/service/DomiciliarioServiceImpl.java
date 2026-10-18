package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Domiciliario;
import com.example.demo.errors.DomiciliarioNotFoundException;
import com.example.demo.repository.DomiciliarioRepository;
import com.example.demo.repository.PedidoRepository;
import jakarta.transaction.Transactional;

@Service
public class DomiciliarioServiceImpl implements DomiciliarioService {

    @Autowired
    private DomiciliarioRepository domiciliarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public List<Domiciliario> obtenerTodosLosDomiciliarios() {
        return domiciliarioRepository.findAll();
    }

    @Override
    public Domiciliario obtenerDomiciliarioPorId(Long idDomiciliario) {
        if (idDomiciliario == null) {
            throw new IllegalArgumentException("El ID del domiciliario es obligatorio.");
        }
        return domiciliarioRepository.findById(idDomiciliario)
                .orElseThrow(() -> new DomiciliarioNotFoundException(idDomiciliario));
    }

    @Override
    public Domiciliario guardarDomiciliario(Domiciliario domiciliario) {
        if (domiciliario == null) {
            throw new IllegalArgumentException("Los datos del domiciliario no pueden ser nulos.");
        }
        return domiciliarioRepository.save(domiciliario);
    }

    @Override
    public Domiciliario actualizarDomiciliario(Long idDomiciliario, Domiciliario domiciliario) {
        if (domiciliario == null) {
            throw new IllegalArgumentException("Los datos del domiciliario no pueden ser nulos.");
        }

        Domiciliario domiciliarioExistente = obtenerDomiciliarioPorId(idDomiciliario);
        if (domiciliario.getNombre() != null) {
            domiciliarioExistente.setNombre(domiciliario.getNombre());
        }
        if (domiciliario.getCelular() != null) {
            domiciliarioExistente.setCelular(domiciliario.getCelular());
        }
        if (domiciliario.getCedula() != null) {
            domiciliarioExistente.setCedula(domiciliario.getCedula());
        }
        if (domiciliario.getImagenURL() != null) {
            domiciliarioExistente.setImagenURL(domiciliario.getImagenURL());
        }
        if (domiciliario.getDisponible() != null) {
            domiciliarioExistente.setDisponible(domiciliario.getDisponible());
        }
        return domiciliarioRepository.save(domiciliarioExistente);
    }

    @Override
    public Domiciliario cambiarDisponibilidad(Long idDomiciliario, Boolean disponible) {
        if (disponible == null) {
            throw new IllegalArgumentException("El estado de disponibilidad es obligatorio.");
        }
        Domiciliario domiciliario = obtenerDomiciliarioPorId(idDomiciliario);
        domiciliario.setDisponible(disponible);
        return domiciliarioRepository.save(domiciliario);
    }

    @Override
    @Transactional
    public void eliminarDomiciliario(Long idDomiciliario) {
        Domiciliario domiciliario = obtenerDomiciliarioPorId(idDomiciliario);
        pedidoRepository.findByDomiciliario(domiciliario).forEach(pedido -> pedido.setDomiciliario(null));
        domiciliarioRepository.delete(domiciliario);
    }
}
