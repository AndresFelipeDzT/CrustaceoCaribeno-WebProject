package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Domiciliario;
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
        Domiciliario domiciliario = domiciliarioRepository.findById(idDomiciliario).orElse(null);
        if (domiciliario == null) {
            return;
        }
        if (!pedidoRepository.findByDomiciliario(domiciliario).isEmpty()) {
            domiciliario.setDisponible(false);
            domiciliarioRepository.save(domiciliario);
            return;
        }
        domiciliarioRepository.delete(domiciliario);
    }
}
