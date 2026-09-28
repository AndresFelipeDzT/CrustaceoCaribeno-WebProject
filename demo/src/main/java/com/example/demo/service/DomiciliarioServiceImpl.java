package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Domiciliario;
import com.example.demo.errors.DomiciliarioNotFoundException;
import com.example.demo.repository.DomiciliarioRepository;
import com.example.demo.repository.PedidoRepository;

@Service
public class DomiciliarioServiceImpl implements DomiciliarioService {

    @Autowired
    private DomiciliarioRepository domiciliarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void eliminarDomiciliario(Long idDomiciliario) {
        if (idDomiciliario == null) {
            throw new IllegalArgumentException("El ID del domiciliario es obligatorio.");
        }
        Domiciliario domiciliario = domiciliarioRepository.findById(idDomiciliario)
                .orElseThrow(() -> new DomiciliarioNotFoundException(idDomiciliario));
        if (!pedidoRepository.findByDomiciliario(domiciliario).isEmpty()) {
            domiciliario.setDisponible(false);
            domiciliarioRepository.save(domiciliario);
            return;
        }
        domiciliarioRepository.delete(domiciliario);
    }
}
