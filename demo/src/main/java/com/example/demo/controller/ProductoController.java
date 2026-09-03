package com.example.demo.controller;
import com.example.demo.entitys.Categoria;
import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.Producto;
import com.example.demo.repository.CategoriaFakeRepository;
import com.example.demo.service.CategoriaService;
import com.example.demo.service.ClienteService;
import com.example.demo.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ClienteService clienteService;

    /**
     * Muestra todas las comidas en formato de tabla (/comidas/tabla).
     */
    // localhost:8080/comidas/tabla
    @GetMapping("/tabla")
    public String listarComidasTabla(@RequestParam(value = "id", required = false) Integer id, Model model,
            HttpSession session) {
        if (id == null) {
            id = (Integer) session.getAttribute("clienteId");
        }
        if (id == null) {
            return "redirect:/login";
        }

        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) {
            return "redirect:/login";
        }
        List<Producto> lista = productoService.obtenerTodosLosProductos();
        model.addAttribute("comidas", lista);
        model.addAttribute("cliente", cliente);
        return "comidas-tabla";
        
    }

    /**
     * Muestra todas las comidas en formato de tarjetas agrupadas por categoría (/comidas/tarjetas).
     */
    // localhost:8080/comidas/tarjetas
    @GetMapping("/tarjetas")
    public String listarComidasTarjetas(@RequestParam(value = "id", required = false) Integer id, Model model,
            HttpSession session) {
        if (id == null) {
            id = (Integer) session.getAttribute("clienteId");
        }
        if (id == null) {
            return "redirect:/login";
        }

        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) {
            return "redirect:/login";
        }
        List<Producto> lista = productoService.obtenerTodosLosProductos();
        model.addAttribute("comidas", lista);
        model.addAttribute("cliente", cliente);

        // Agrupación limpia para las secciones del diseño
        List<Producto> entradas = lista.stream()
            .filter(p -> "Entrada".equalsIgnoreCase(p.getCategoria().getNombreCategoria()) || (p.getIdProducto() >= 1 && p.getIdProducto() <= 3))
            .toList();
        List<Producto> platosFuertes = lista.stream()
            .filter(p -> "Plato Fuerte".equalsIgnoreCase(p.getCategoria().getNombreCategoria()) || (p.getIdProducto() >= 4 && p.getIdProducto() <= 9))
            .toList();
        List<Producto> especialidades = lista.stream()
            .filter(p -> "Especialidades De La Casa".equalsIgnoreCase(p.getCategoria().getNombreCategoria()) || (p.getIdProducto() >= 10 && p.getIdProducto() <= 12))
            .toList();

        model.addAttribute("entradas", entradas);
        model.addAttribute("platosFuertes", platosFuertes);
        model.addAttribute("especialidades", especialidades);
        return "comidas-tarjetas";
    }


    /**
     * Muestra la información de una sola comida por ID (/comidas/detalle/{id}).
     */
    // localhost:8080/comidas/detalle/1
    @GetMapping("/detalle/{id}")
    public String verDetalleComida(@PathVariable("id") int id, Model model) {
        Producto comida = productoService.obtenerProductoPorId(id);
        if (comida == null) {
            return "redirect:/comidas/tarjetas";
        }
        model.addAttribute("comida", comida);
        return "comida-detalle";
    }

    // localhost:8080/comidas/tabla/add
    @GetMapping("/tabla/add")
    public String mostrarFormularioAgregar(Model model) {
        Producto producto = new Producto(null, "", 0, "", "", null);
        model.addAttribute("plato",producto);
        model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
        return "comida-agregar";
    }

    @PostMapping("tabla/add")
    public String agregarProducto(@ModelAttribute("plato") Producto producto, @RequestParam("nombreCategoria") String nombreCategoria) {
    
        Categoria categoria = categoriaService.obtenerCategoriaPorNombre(nombreCategoria);
        producto.setCategoria(categoria);
        productoService.guardarProducto(producto);
        return "redirect:/comidas/tabla";
    }
    

    // localhost:8080/comidas/tabla/delete/{id}
    @GetMapping("/tabla/delete/{id}")
    public String eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return "redirect:/comidas/tabla";
    }

    // localhost:8080/comidas/tabla/update/{id}
    @GetMapping("tabla/update/{id}")
    public String actualizarProducto(@PathVariable Integer id, Model model) {

        Producto producto = productoService.obtenerProductoPorId(id);
        model.addAttribute("plato",producto);
        model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
        return "comida-agregar";
    }
    
    
}