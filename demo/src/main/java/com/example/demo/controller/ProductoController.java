package com.example.demo.controller;
import com.example.demo.entitys.Producto;
import com.example.demo.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controlador Spring MVC encargado de gestionar las peticiones web relacionadas con los platos.
 * Cumple los requerimientos del Sprint 2:
 * 1. Mostrar comidas en formato de tabla.
 * 2. Mostrar comidas en formato de tarjetas.
 * 3. Mostrar el detalle de una sola comida.
 */
@Controller
// localhost:8080/comidas
@RequestMapping("/comidas")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    /**
     * Muestra todas las comidas en formato de tabla (/comidas/tabla).
     */
    // localhost:8080/comidas/tabla
    @GetMapping("/tabla")
    public String listarComidasTabla(Model model) {
        List<Producto> lista = productoService.obtenerTodosLosProductos();
        model.addAttribute("comidas", lista);
        return "comidas-tabla";
    }

    /**
     * Muestra todas las comidas en formato de tarjetas agrupadas por categoría (/comidas/tarjetas).
     */
    // localhost:8080/comidas/tarjetas
    @GetMapping("/tarjetas")
    public String listarComidasTarjetas(Model model) {
        List<Producto> lista = productoService.obtenerTodosLosProductos();
        model.addAttribute("comidas", lista);

        // Agrupación limpia para las secciones del diseño
        List<Producto> entradas = lista.stream()
            .filter(p -> "Entrada".equalsIgnoreCase(p.getCategoria()) || (p.getIdProducto() >= 1 && p.getIdProducto() <= 3))
            .toList();
        List<Producto> platosFuertes = lista.stream()
            .filter(p -> "Plato Fuerte".equalsIgnoreCase(p.getCategoria()) || (p.getIdProducto() >= 4 && p.getIdProducto() <= 9))
            .toList();
        List<Producto> especialidades = lista.stream()
            .filter(p -> "Especialidades De La Casa".equalsIgnoreCase(p.getCategoria()) || (p.getIdProducto() >= 10 && p.getIdProducto() <= 12))
            .toList();

        model.addAttribute("entradas", entradas);
        model.addAttribute("platosFuertes", platosFuertes);
        model.addAttribute("especialidades", especialidades);
        return "comidas-tarjetas";
    }


    /**
     * Muestra la información de una sola comida por ID (/comidas/detalle/{id}).
     */
    // localhost:8080/comidas//detalle/1
    @GetMapping("/detalle/{id}")
    public String verDetalleComida(@PathVariable("id") int id, Model model) {
        Producto comida = productoService.obtenerProductoPorId(id);
        if (comida == null) {
            return "redirect:/comidas/tarjetas";
        }
        model.addAttribute("comida", comida);
        return "comida-detalle";
    }
}