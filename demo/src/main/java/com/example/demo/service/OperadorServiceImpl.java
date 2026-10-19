package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Operador;
import com.example.demo.repository.OperadorRepository;

@Service
public class OperadorServiceImpl implements OperadorService {

    @Autowired
    private OperadorRepository operadorRepository;

    @Override
    public List<Operador> obtenerTodosLosOperadores() {
        return operadorRepository.findAll();
    }

    @Override
    public Operador obtenerOperadorPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del operador es obligatorio.");
        }
        return operadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el operador con ID " + id + "."));
    }

    @Override
    public Operador guardarOperador(Operador operador) {
        if (operador == null) {
            throw new IllegalArgumentException("Los datos del operador no pueden ser nulos.");
        }
        return operadorRepository.save(operador);
    }

    @Override
    public void actualizarOperador(Long id, Operador operador) {
        if (operador == null) {
            throw new IllegalArgumentException("Los datos del operador no pueden ser nulos.");
        }

        Operador operadorExistente = obtenerOperadorPorId(id);
        if (operador.getNombre() != null && !operador.getNombre().isBlank()) {
            operadorExistente.setNombre(operador.getNombre());
        }
        if (operador.getImagenURL() != null) {
            operadorExistente.setImagenURL(operador.getImagenURL());
        }
        if (operador.getUsuario() != null && !operador.getUsuario().isBlank()) {
            operadorExistente.setUsuario(operador.getUsuario());
        }
        if (operador.getContrasena() != null && !operador.getContrasena().isBlank()) {
            operadorExistente.setContrasena(operador.getContrasena());
        }
        operadorRepository.save(operadorExistente);
    }

    @Override
    public void eliminarOperador(Long id) {
        operadorRepository.delete(obtenerOperadorPorId(id));
    }
}
