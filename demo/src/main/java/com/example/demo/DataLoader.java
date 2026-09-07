package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.Producto;
import com.example.demo.repository.CategoriaFakeRepository;
import com.example.demo.repository.ClienteFakeRepository;
import com.example.demo.repository.ProductoFakeRepository;

import jakarta.transaction.Transactional;

@Component 
@Transactional 
public class DataLoader implements CommandLineRunner {

    @Autowired 
    ClienteFakeRepository clienteRepository;
    @Autowired 
    CategoriaFakeRepository categoriaRepository;
    @Autowired 
    ProductoFakeRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {

        //Agregar 10 clientes
        clienteRepository.save(new Cliente("Juan","Rodriguez","Juan.Rodriguez@gmail.com","33034534","Av carrera 45 # 34-56","1234"));
        clienteRepository.save(new Cliente("María", "Gómez", "maria.gomez@gmail.com", "3104567890", "Calle 100 # 15-23", "5678"));
        clienteRepository.save(new Cliente("Carlos", "López", "carlos.lopez@hotmail.com", "3209876543", "Carrera 7 # 72-10", "4321"));
        clienteRepository.save(new Cliente("Ana", "Martínez", "ana.martinez@yahoo.com", "3152345678", "Diagonal 45 # 12-89", "9876"));
        clienteRepository.save(new Cliente("Luis", "García", "luis.garcia@outlook.com", "3007654321", "Avenida Calle 26 # 68-90", "2468"));
        clienteRepository.save(new Cliente("Diana", "Sánchez", "diana.sanchez@gmail.com", "3123450987", "Transversal 23 # 45-67", "1357"));
        clienteRepository.save(new Cliente("Andrés", "Pérez", "andres.perez@hotmail.com", "3187654321", "Calle 134 # 45A-12", "8765"));
        clienteRepository.save(new Cliente("Laura", "Ramírez", "laura.ramirez@gmail.com", "3214560987", "Carrera 50 # 80-45", "3690"));
        clienteRepository.save(new Cliente("Jorge", "Herrera", "jorge.herrera@outlook.com", "3149876543", "Avenida Boyacá # 53-22", "1590"));
        clienteRepository.save(new Cliente("Sofía", "Castro", "sofia.castro@yahoo.com", "3165432109", "Circular 4 # 71-15", "7531"));

        //Agregar 5 categorias
        categoriaRepository.save(new com.example.demo.entitys.Categoria("Entrada"));
        categoriaRepository.save(new com.example.demo.entitys.Categoria("Plato Fuerte"));
        categoriaRepository.save(new com.example.demo.entitys.Categoria("Especialidad de la Casa"));
        categoriaRepository.save(new com.example.demo.entitys.Categoria("Postre"));
        categoriaRepository.save(new com.example.demo.entitys.Categoria("Bebida"));

        // ==========================================
        // AGREGAR 50 PRODUCTOS
        // ==========================================   
        // ==========================================
        // ENTRADAS
        // ==========================================
        productoRepository.save(new Producto(
            "Aguachile Negro De Camarón",
            36000.0,
            "Camarones frescos marinados en zumo de limón con ceniza de chiles habaneros y salsa negra especial de la casa.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzviqiC5iiHmv4J96axL2hNarSuJouJtG45T_qJH4dvKekIAINlFK7KCxB&s=10"
        ));

        productoRepository.save(new Producto(
            "Ceviche Tropical",
            32000.0,
            "Cubos de pescado blanco fresco marinados en cítricos con mango biche, maracuyá, cebolla morada y cilantro fresco.",
            "https://www.laylita.com/recetas/wp-content/uploads/2025/02/Ceviche-de-besugo-1024x768.jpg"
        ));

        productoRepository.save(new Producto(
            "Tacos Gobernador",
            34000.0,
            "Tortillas de maíz artesanales rellenas de camarones salteados con pimientos, cebolla caramelizada y queso costeño gratinado.",
            "https://cdn-ilddihb.nitrocdn.com/MgqZCGPEMHvMRLsisMUCAIMWvgGMxqaj/assets/images/optimized/rev-0e527e8/www.goya.com/wp-content/uploads/2024/09/tacos-gobernador.jpg"
        ));
        productoRepository.save(new Producto(
            "Ceviche Mixto",
            35000.0,
            "Combinación de pescado blanco, camarones, calamares y pulpo marinados en limón con cebolla morada y cilantro.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Ceviche_de_pescado.JPG"
        ));

        productoRepository.save(new Producto(
            "Ceviche De Camarón",
            32000.0,
            "Camarones frescos marinados en limón con cebolla morada, cilantro, tomate y un toque de ají.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Ceviche_from_Ecuador.jpeg"
        ));

        productoRepository.save(new Producto(
            "Tostadas De Ceviche",
            29000.0,
            "Crujientes tostadas de maíz cubiertas con ceviche fresco, aguacate, cebolla morada y cilantro.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Ceviche_from_Ecuador.jpeg"
        ));

        productoRepository.save(new Producto(
            "Calamares Fritos",
            28000.0,
            "Anillos de calamar frescos apanados y fritos hasta quedar dorados y crujientes, acompañados de salsa tártara.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Fried_calamares.jpg"
        ));

        productoRepository.save(new Producto(
            "Cóctel De Mariscos",
            32000.0,
            "Camarones y frutos del mar servidos en salsa especial con aguacate, limón, cebolla y cilantro fresco.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Shrimp_dish_at_Korean_restaurant.jpg"
        ));

        productoRepository.save(new Producto(
            "Empanadas De Camarón",
            26000.0,
            "Empanadas artesanales rellenas de camarones, queso costeño, cebolla y especias de la casa.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Shrimp_dish_at_a_restaurant_in_Jersey_City.JPG"
        ));

        productoRepository.save(new Producto(
            "Ceviche De Pulpo",
            34000.0,
            "Pulpo fresco marinado en limón con cebolla morada, cilantro, ají y especias tropicales.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Ceviche_de_pescado.JPG"
        ));

        productoRepository.save(new Producto(
            "Tartar De Pescado",
            33000.0,
            "Pescado fresco cortado en cubos acompañado de aguacate, cebolla morada, limón y salsa cítrica.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Ceviche_de_pescado.JPG"
        ));

        productoRepository.save(new Producto(
            "Camarones Al Ajillo De Entrada",
            30000.0,
            "Camarones salteados con ajo, mantequilla, perejil y un toque de limón, servidos como entrada.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Shrimp_dish_at_Korean_restaurant.jpg"
        ));

        productoRepository.save(new Producto(
            "Mariscos A La Vinagreta",
            31000.0,
            "Selección de mariscos frescos acompañados de vinagreta cítrica, cebolla morada, cilantro y limón.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Shrimp_dish_at_a_restaurant_in_Jersey_City.JPG"
        ));


        // ==========================================
        // PLATOS FUERTES
        // ==========================================


        productoRepository.save(new Producto(
            "Pulpo A Las Brasas",
            58000.0,
            "Tentáculos de pulpo marinados en chimichurri caribeño y especias, asados a la parrilla sobre cama de papas rústicas al pimentón.",
            "https://www.shutterstock.com/image-photo/grilled-octopus-asparagus-served-on-260nw-2483650495.jpg"
        ));

        productoRepository.save(new Producto(
            "Pescado A La Talla",
            52000.0,
            "Pescado fresco abierto en mariposa, marinado con adobo tradicional de chiles dulces y hierbas finas, cocinado a la leña.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8CmGHZaPGTnUMOxwcvO4rRsfFhUZdWfYK8profaljDkVwhoVXCfSa2Vg&s=10"
        ));

        productoRepository.save(new Producto(
            "Cazuela De Mariscos",
            48000.0,
            "Tradicional cazuela con camarones, calamares, pulpo y mejillones en cremosa reducción de leche de coco y especias de la costa.",
            "https://www.cocina-ecuatoriana.com/base/stock/Recipe/cazuela-mixta/cazuela-mixta_web.jpg.webp"
        ));

        productoRepository.save(new Producto(
            "Camarones Al Ajillo",
            42000.0,
            "Camarones salteados al punto en aceite de oliva extra virgen, abundante ajo dorado, vino blanco y perejil fresco picado.",
            "https://especiasmontero.com/wp-content/uploads/2025/05/Camarones-al-ajillo-500x375.jpg"
        ));

        productoRepository.save(new Producto(
            "Salmón En Costra De Hierbas",
            54000.0,
            "Filete de salmón a la plancha cubierto con crujiente costra de finas hierbas y frutos secos, acompañado de vegetales al vapor.",
            "https://gourmet.iprospect.cl/wp-content/uploads/2016/09/Salm%C3%B3n-a-las-finas-hierbas-web.jpg"
        ));

        productoRepository.save(new Producto(
            "Langostinos A La Mantequilla De Ajo",
            62000.0,
            "Langostinos jumbo bañados en mantequilla clarificada con infusión de ajo tostado, limón mandarino y hierbas aromáticas.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ22zmHFu8otBj1tgnHUsdG4sONVQvhxJ-J21SmaYkFEV4G0Lz1pcJc5jPd&s=10"
        ));

        productoRepository.save(new Producto(
            "Mojarra Frita",
            40000.0,
            "Mojarra fresca frita hasta quedar dorada y crocante, acompañada de arroz con coco, ensalada y patacones.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Fish_dish_from_set_of_dinner_on_Osteria_Ristorante_Italiano.jpg"
        ));

        productoRepository.save(new Producto(
            "Salmón A La Plancha",
            52000.0,
            "Filete de salmón fresco preparado a la plancha con mantequilla de hierbas, limón y vegetales salteados.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Salmon_dish.jpg"
        ));

        productoRepository.save(new Producto(
            "Salmón En Salsa De Limón",
            55000.0,
            "Filete de salmón sellado acompañado de una cremosa salsa de limón, ajo, mantequilla y hierbas frescas.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/SalmonDish.jpg"
        ));

        productoRepository.save(new Producto(
            "Pescado A La Parrilla",
            44000.0,
            "Filete de pescado blanco cocinado a la parrilla con limón, ajo, perejil y vegetales frescos.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Fish_dish_from_set_of_dinner_on_Osteria_Ristorante_Italiano.jpg"
        ));

        productoRepository.save(new Producto(
            "Pescado En Salsa De Coco",
            46000.0,
            "Filete de pescado fresco acompañado de una cremosa salsa de coco, cilantro, ajo y especias caribeñas.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Fish_dish_from_set_of_dinner_on_Osteria_Ristorante_Italiano.jpg"
        ));

        productoRepository.save(new Producto(
            "Camarones Al Ajillo",
            42000.0,
            "Camarones salteados con abundante ajo, mantequilla, vino blanco, limón y perejil fresco.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Shrimp_dish_at_Korean_restaurant.jpg"
        ));

        productoRepository.save(new Producto(
            "Camarones A La Parrilla",
            45000.0,
            "Camarones frescos marinados en ajo, limón y hierbas, preparados a la parrilla.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Shrimp_dish_at_a_restaurant_in_Jersey_City.JPG"
        ));

        productoRepository.save(new Producto(
            "Calamares A La Parrilla",
            43000.0,
            "Calamares frescos preparados a la parrilla con ajo, aceite de oliva, limón y hierbas aromáticas.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/A_calamari.jpg"
        ));

        productoRepository.save(new Producto(
            "Pulpo A La Parrilla",
            57000.0,
            "Tentáculos de pulpo preparados a la parrilla con aceite de oliva, limón, ajo y hierbas frescas.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/A_calamari.jpg"
        ));

        productoRepository.save(new Producto(
            "Langostinos Al Ajillo",
            60000.0,
            "Langostinos frescos salteados con ajo, mantequilla, limón, vino blanco y perejil.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Shrimp_dish_at_Korean_restaurant.jpg"
        ));


        // ==========================================
        // ESPECIALIDADES DE LA CASA
        // ==========================================

        productoRepository.save(new Producto(
            "Paella De Mariscos",
            52000.0,
            "Arroz tradicional preparado con camarones, calamares, mejillones y especias, terminado con limón fresco.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Paella_seafood.JPG"
        ));

        productoRepository.save(new Producto(
            "Paella Especial De La Casa",
            58000.0,
            "Paella preparada con arroz, camarones, calamares, mejillones y una selección especial de mariscos frescos.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Seafoods_paella.jpg"

        ));

        productoRepository.save(new Producto(
            "Arroz Marinero",
            44000.0,
            "Arroz preparado con camarones, calamares y otros frutos del mar, acompañado de un sofrito especial.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Seafoods_paella.jpg"
        ));

        productoRepository.save(new Producto(
            "Risotto De Mariscos",
            50000.0,
            "Risotto cremoso preparado lentamente con caldo de mariscos, camarones, calamares y queso parmesano.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Seafood_Risotto.jpg"
        ));

        productoRepository.save(new Producto(
            "Pasta Frutti Di Mare",
            47000.0,
            "Pasta artesanal acompañada de camarones, calamares y otros frutos del mar en salsa de tomate y hierbas.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Seafood_pasta.jpg"
        ));

        productoRepository.save(new Producto(
            "Fideuá De Mariscos",
            49000.0,
            "Fideos tostados cocinados en concentrado de mariscos con camarones, calamares y mejillones.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Seafood_pasta.jpg"
        ));

        productoRepository.save(new Producto(
            "Parrillada Del Mar",
            65000.0,
            "Selección de pescado, camarones, calamares y otros frutos del mar preparados a la parrilla.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Shrimp_dish_at_Korean_restaurant.jpg"
        ));

        productoRepository.save(new Producto(
            "Festival De Mariscos",
            62000.0,
            "Combinación especial de camarones, pescado, calamares y otros frutos del mar seleccionados por la casa.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Shrimp_dish_at_a_restaurant_in_Jersey_City.JPG"
        ));
        productoRepository.save(new Producto(
            "Arroz Meloso Con Mariscos",
            46000.0,
            "Arroz cremoso cocido a fuego lento en bisque de mariscos con camarones, calamares, almejas y un toque de azafrán caribeño.",
            "https://i.blogs.es/4b3414/arroz_meloso/840_560.jpg"
        ));

        productoRepository.save(new Producto(
            "Torre De Mariscos",
            56000.0,
            "Estructura gourmet con capas de ceviche de camarón, pulpo marinado, atún fresco, aguacate cremoso y vinagreta cítrica.",
            "https://media.cocinavital.mx/2022/05/timbal-de-mariscos-receta-1-634x420.jpg"
        ));

        productoRepository.save(new Producto(
            "Pasta Frutti Di Mare",
            45000.0,
            "Fettuccine artesanal al dente salteado con frutos del mar en salsa pomodoro rústica de tomates frescos y albahaca.",
            "https://assets.bonappetit.com/photos/57acc5bb1b33404414975193/1:1/w_2560%2Cc_limit/fettuccine-ai-frutti-di-mare.jpg"
        ));


        // ==========================================
        // POSTRES
        // ==========================================

        productoRepository.save(new Producto(
            "Flan De Coco",
            18000.0,
            "Suave flan de coco bañado en caramelo, con una textura cremosa y delicado sabor tropical.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Coconut_Flan.jpg"
        ));

        productoRepository.save(new Producto(
            "Tarta De Maracuyá",
            20000.0,
            "Deliciosa tarta cremosa de maracuyá con base crujiente y un intenso sabor tropical.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Tarta_de_maracuy%C3%A1.jpg"
        ));

        productoRepository.save(new Producto(
            "Cheesecake De Maracuyá",
            22000.0,
            "Cheesecake cremoso con cobertura de maracuyá, acompañado de una base crocante de galleta.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Calca_Peru-_Maracuya_Cheesecake.jpg"
        ));

        productoRepository.save(new Producto(
            "Torta De Maracuyá",
            19000.0,
            "Suave torta de maracuyá con crema tropical y delicada cobertura de fruta fresca.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Passion_Fruit_Cake_%28Tarta_de_Maracuy%C3%A1%29.jpg"
        ));

        productoRepository.save(new Producto(
            "Tarta Tropical De Maracuyá",
            21000.0,
            "Tarta artesanal de maracuyá con textura cremosa y un equilibrado contraste entre dulce y ácido.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Passion_Fruit_Cake_of_Argentina_%28Tarta_de_Maracuy%C3%A1%29.jpg"
        ));


        // ==========================================
        // BEBIDAS
        // ==========================================

        productoRepository.save(new Producto(
            "Limonada Natural",
            10000.0,
            "Refrescante limonada preparada con jugo de limón natural, agua y un toque de azúcar.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Lemonade.png"
        ));

        productoRepository.save(new Producto(
            "Limonada Clásica",
            12000.0,
            "Limonada fresca preparada al momento con limones naturales y hielo.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Classic_Lemonade.jpg"
        ));

        productoRepository.save(new Producto(
            "Limonada De La Casa",
            13000.0,
            "Limonada artesanal preparada con limón natural, hielo y el toque especial de la casa.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Homemade_Lemonade.jpg"
        ));

        productoRepository.save(new Producto(
            "Limonada De Flor De Jamaica",
            14000.0,
            "Refrescante bebida de limón y flor de jamaica con un delicado equilibrio entre dulce y ácido.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Hibiscus_Lemonade_Drink.jpg"
        ));

        productoRepository.save(new Producto(
            "Agua De Coco",
            12000.0,
            "Refrescante agua de coco natural, ideal para acompañar los sabores tropicales del restaurante.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Coconut_water_from_above.jpg"
        ));


        //Agregar categorias a los productos
        productoRepository.findById(1L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(2L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(3L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(4L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(5L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(6L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(7L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(8L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(9L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(10L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(11L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(12L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(13L).get().setCategoria(categoriaRepository.findById(1L).get());
        productoRepository.findById(14L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(15L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(16L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(17L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(18L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(19L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(20L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(21L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(22L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(23L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(24L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(25L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(26L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(27L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(28L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(29L).get().setCategoria(categoriaRepository.findById(2L).get());
        productoRepository.findById(30L).get().setCategoria(categoriaRepository.findById(3L).get());
        productoRepository.findById(31L).get().setCategoria(categoriaRepository.findById(3L).get());
        productoRepository.findById(32L).get().setCategoria(categoriaRepository.findById(3L).get());
        productoRepository.findById(33L).get().setCategoria(categoriaRepository.findById(3L).get());
        productoRepository.findById(34L).get().setCategoria(categoriaRepository.findById(3L).get());
        productoRepository.findById(35L).get().setCategoria(categoriaRepository.findById(3L).get());
        productoRepository.findById(36L).get().setCategoria(categoriaRepository.findById(3L).get());
        productoRepository.findById(37L).get().setCategoria(categoriaRepository.findById(3L).get());
        productoRepository.findById(38L).get().setCategoria(categoriaRepository.findById(3L).get());
        productoRepository.findById(39L).get().setCategoria(categoriaRepository.findById(3L).get());
        productoRepository.findById(40L).get().setCategoria(categoriaRepository.findById(3L).get());
        productoRepository.findById(41L).get().setCategoria(categoriaRepository.findById(4L).get());
        productoRepository.findById(42L).get().setCategoria(categoriaRepository.findById(4L).get());
        productoRepository.findById(43L).get().setCategoria(categoriaRepository.findById(4L).get());
        productoRepository.findById(44L).get().setCategoria(categoriaRepository.findById(4L).get());
        productoRepository.findById(45L).get().setCategoria(categoriaRepository.findById(4L).get());
        productoRepository.findById(46L).get().setCategoria(categoriaRepository.findById(5L).get());
        productoRepository.findById(47L).get().setCategoria(categoriaRepository.findById(5L).get());
        productoRepository.findById(48L).get().setCategoria(categoriaRepository.findById(5L).get());
        productoRepository.findById(49L).get().setCategoria(categoriaRepository.findById(5L).get());
        productoRepository.findById(50L).get().setCategoria(categoriaRepository.findById(5L).get());
    }   
}
