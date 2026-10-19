package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entitys.Operador;
import com.example.demo.service.OperadorService;

@RestController
@RequestMapping("/api/operadores")
@CrossOrigin(origins = "http://localhost:4200")
public class OperadorApiController {

    @Autowired
    private OperadorService operadorService;

    @GetMapping
    public List<Operador> listar() {
        return operadorService.obtenerTodosLosOperadores();
    }

    @GetMapping("/{id}")
    public Operador obtener(@PathVariable Long id) {
        return operadorService.obtenerOperadorPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody Operador operador) {
        operadorService.guardarOperador(operador);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable Long id, @RequestBody Operador operador) {
        operadorService.actualizarOperador(id, operador);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        operadorService.eliminarOperador(id);
    }
}
