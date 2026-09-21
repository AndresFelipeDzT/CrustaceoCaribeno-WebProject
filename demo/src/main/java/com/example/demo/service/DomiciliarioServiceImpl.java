package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entitys.Domiciliario;
import com.example.demo.repository.DomiciliarioRepository;
import com.example.demo.repository.PedidoRepository;

@Service
public class DomiciliarioServiceImpl implements DomiciliarioService {
    private final DomiciliarioRepository domiciliarioRepository;
    private final PedidoRepository pedidoRepository;

    public DomiciliarioServiceImpl(DomiciliarioRepository domiciliarioRepository, PedidoRepository pedidoRepository) {
        this.domiciliarioRepository = domiciliarioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void eliminarDomiciliario(Long idDomiciliario) {
        Domiciliario domiciliario = domiciliarioRepository.findById(idDomiciliario).orElseThrow();
        if (!pedidoRepository.findByDomiciliario(domiciliario).isEmpty()) {
            domiciliario.setDisponible(false);
            domiciliarioRepository.save(domiciliario);
            return;
        }
        domiciliarioRepository.delete(domiciliario);
    }
}
