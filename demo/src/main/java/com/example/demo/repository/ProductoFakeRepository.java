package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Categoria;
import com.example.demo.entitys.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación de la falsa base de datos de productos para el Sprint 2.
 * Precarga los 12 platos del menú de Figma con datos realistas del restaurante caribeño.
 *
 * Los productos se almacenan en un HashMap donde la llave es el idProducto,
 * para permitir accesos directos por id sin recorrer toda la colección.
 */
@Repository
public class ProductoFakeRepository {

    /** Mapa en memoria que simula la tabla de comidas en la base de datos.
     *  Llave = idProducto */
    private Map<Integer, Producto> tablaComidas = new HashMap<>();

    /** Contador simple para generar nuevos ids */
    private int secuenciaId = 0;

    /**
     * Constructor que inicializa los datos de prueba quemados.
     */
    public ProductoFakeRepository() {
        cargarDatosQuemados();
    }

   /**
     * Carga inicial de los 12 platos extraídos del diseño de Figma (Entradas y Platos Fuertes).
     * Incluye nombres, precios (COP), descripciones y enlaces directos a las imágenes.
     */
    private void cargarDatosQuemados() {
        // ==========================================
        // ENTRADAS
        // ==========================================
        tablaComidas.put(1, new Producto(
            1,
            "Aguachile Negro De Camarón",
            36000.0,
            "Camarones frescos marinados en zumo de limón con ceniza de chiles habaneros y salsa negra especial de la casa.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzviqiC5iiHmv4J96axL2hNarSuJouJtG45T_qJH4dvKekIAINlFK7KCxB&s=10",
            new Categoria(1, "Entrada")
        ));

        tablaComidas.put(2, new Producto(
            2,
            "Ceviche Tropical",
            32000.0,
            "Cubos de pescado blanco fresco marinados en cítricos con mango biche, maracuyá, cebolla morada y cilantro fresco.",
            "https://www.laylita.com/recetas/wp-content/uploads/2025/02/Ceviche-de-besugo-1024x768.jpg",
            new Categoria(1, "Entrada")
        ));

        tablaComidas.put(3, new Producto(
            3,
            "Tacos Gobernador",
            34000.0,
            "Tortillas de maíz artesanales rellenas de camarones salteados con pimientos, cebolla caramelizada y queso costeño gratinado.",
            "https://cdn-ilddihb.nitrocdn.com/MgqZCGPEMHvMRLsisMUCAIMWvgGMxqaj/assets/images/optimized/rev-0e527e8/www.goya.com/wp-content/uploads/2024/09/tacos-gobernador.jpg",
            new Categoria(1, "Entrada")
        ));

        // ==========================================
        // PLATOS FUERTES
        // ==========================================
        tablaComidas.put(4, new Producto(
            4,
            "Pulpo A Las Brasas",
            58000.0,
            "Tentáculos de pulpo marinados en chimichurri caribeño y especias, asados a la parrilla sobre cama de papas rústicas al pimentón.",
            "https://www.shutterstock.com/image-photo/grilled-octopus-asparagus-served-on-260nw-2483650495.jpg",
            new Categoria(2, "Plato Fuerte")
        ));

        tablaComidas.put(5, new Producto(
            5,
            "Pescado A La Talla",
            52000.0,
            "Pescado fresco abierto en mariposa, marinado con adobo tradicional de chiles dulces y hierbas finas, cocinado a la leña.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8CmGHZaPGTnUMOxwcvO4rRsfFhUZdWfYK8profaljDkVwhoVXCfSa2Vg&s=10",
            new Categoria(2, "Plato Fuerte")
        ));

        tablaComidas.put(6, new Producto(
            6,
            "Cazuela De Mariscos",
            48000.0,
            "Tradicional cazuela con camarones, calamares, pulpo y mejillones en cremosa reducción de leche de coco y especias de la costa.",
            "https://www.cocina-ecuatoriana.com/base/stock/Recipe/cazuela-mixta/cazuela-mixta_web.jpg.webp",
            new Categoria(2, "Plato Fuerte")
        ));

        tablaComidas.put(7, new Producto(
            7,
            "Camarones Al Ajillo",
            42000.0,
            "Camarones salteados al punto en aceite de oliva extra virgen, abundante ajo dorado, vino blanco y perejil fresco picado.",
            "https://especiasmontero.com/wp-content/uploads/2025/05/Camarones-al-ajillo-500x375.jpg",
            new Categoria(2, "Plato Fuerte")
        ));

        tablaComidas.put(8, new Producto(
            8,
            "Salmón En Costra De Hierbas",
            54000.0,
            "Filete de salmón a la plancha cubierto con crujiente costra de finas hierbas y frutos secos, acompañado de vegetales al vapor.",
            "https://gourmet.iprospect.cl/wp-content/uploads/2016/09/Salm%C3%B3n-a-las-finas-hierbas-web.jpg",
            new Categoria(2, "Plato Fuerte")
        ));

        tablaComidas.put(9, new Producto(
            9,
            "Langostinos A La Mantequilla De Ajo",
            62000.0,
            "Langostinos jumbo bañados en mantequilla clarificada con infusión de ajo tostado, limón mandarino y hierbas aromáticas.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ22zmHFu8otBj1tgnHUsdG4sONVQvhxJ-J21SmaYkFEV4G0Lz1pcJc5jPd&s=10",
            new Categoria(2, "Plato Fuerte")
        ));

        tablaComidas.put(10, new Producto(
            10,
            "Arroz Meloso Con Mariscos",
            46000.0,
            "Arroz cremoso cocido a fuego lento en bisque de mariscos con camarones, calamares, almejas y un toque de azafrán caribeño.",
            "https://i.blogs.es/4b3414/arroz_meloso/840_560.jpg",
            new Categoria(3, "Especialidades De La Casa")
        ));

        tablaComidas.put(11, new Producto(
            11,
            "Torre De Mariscos",
            56000.0,
            "Estructura gourmet con capas de ceviche de camarón, pulpo marinado, atún fresco, aguacate cremoso y vinagreta cítrica.",
            "https://media.cocinavital.mx/2022/05/timbal-de-mariscos-receta-1-634x420.jpg",
            new Categoria(3, "Especialidades De La Casa")
        ));

        tablaComidas.put(12, new Producto(
            12,
            "Pasta Frutti Di Mare",
            45000.0,
            "Fettuccine artesanal al dente salteado con frutos del mar en salsa pomodoro rústica de tomates frescos y albahaca.",
            "https://assets.bonappetit.com/photos/57acc5bb1b33404414975193/1:1/w_2560%2Cc_limit/fettuccine-ai-frutti-di-mare.jpg",
            new Categoria(3, "Especialidades De La Casa")
        ));

        // Actualizamos la secuencia para que los próximos ids generados
        // automáticamente (en save) no choquen con los 12 ids quemados.
        secuenciaId = tablaComidas.size();
    }

    public List<Producto> findAll() {
        return new ArrayList<>(tablaComidas.values());
    }

    public Producto findById(int idProducto) {
        return tablaComidas.get(idProducto);
    }

    public Producto save(Producto producto) {
        // Si no viene con un id válido, se le asigna uno nuevo
        if (producto.getIdProducto() <= 0) {
            secuenciaId++;
            producto.setIdProducto(secuenciaId);
        } else if (producto.getIdProducto() > secuenciaId) {
            // Si el id viene especificado, aseguramos que la secuencia no genere colisiones futuras
            secuenciaId = producto.getIdProducto();
        }
        // put() reemplaza si ya existe la llave, o agrega si es nueva
        tablaComidas.put(producto.getIdProducto(), producto);
        return producto;
    }

    public boolean deleteById(int idProducto) {
        return tablaComidas.remove(idProducto) != null;
    }
}