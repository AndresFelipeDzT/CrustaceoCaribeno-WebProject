package com.example.demo;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Administrador;
import com.example.demo.entitys.Carrito;
import com.example.demo.entitys.Categoria;
import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.Domiciliario;
import com.example.demo.entitys.ItemPedido;
import com.example.demo.entitys.ItemCarrito;
import com.example.demo.entitys.ItemCarritoAdicional;
import com.example.demo.entitys.ItemPedidoAdicional;
import com.example.demo.entitys.Operador;
import com.example.demo.entitys.ProductoAdicional;
import com.example.demo.entitys.Pedido;
import com.example.demo.entitys.Producto;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.AdministradorRepository;
import com.example.demo.repository.CarritoRepository;
import com.example.demo.repository.ItemCarritoRepository;
import com.example.demo.repository.ItemCarritoAdicionalRepository;
import com.example.demo.repository.ItemPedidoAdicionalRepository;
import com.example.demo.repository.OperadorRepository;
import com.example.demo.repository.ProductoAdicionalRepository;
import com.example.demo.repository.CategoriaFakeRepository;
import com.example.demo.repository.ClienteFakeRepository;
import com.example.demo.repository.DomiciliarioRepository;
import com.example.demo.repository.ItemPedidoRepository;
import com.example.demo.repository.PedidoRepository;
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
    @Autowired
    DomiciliarioRepository domiciliarioRepository;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ItemPedidoRepository itemPedidoRepository;
    @Autowired
    AdicionalRepository adicionalRepository;
    @Autowired
    AdministradorRepository administradorRepository;
    @Autowired
    OperadorRepository operadorRepository;
    @Autowired CarritoRepository carritoRepository;
    @Autowired ItemCarritoRepository itemCarritoRepository;
    @Autowired ItemCarritoAdicionalRepository itemCarritoAdicionalRepository;
    @Autowired ItemPedidoAdicionalRepository itemPedidoAdicionalRepository;
    @Autowired ProductoAdicionalRepository productoAdicionalRepository;

    @Override
    public void run(String... args) throws Exception {

        // Agregar cinco administradores y cinco operadores
        administradorRepository.save(new Administrador("admin.general", "admin123"));
        administradorRepository.save(new Administrador("admin.inventario", "inventario123"));
        administradorRepository.save(new Administrador("admin.ventas", "ventas123"));
        administradorRepository.save(new Administrador("admin.usuarios", "usuarios123"));
        administradorRepository.save(new Administrador("admin.reportes", "reportes123"));

        operadorRepository.save(new Operador("Juan García", "operador.principal", "operador123"));
        operadorRepository.save(new Operador("Ana Velásquez", "operador.cocina", "cocina123"));
        operadorRepository.save(new Operador("Carlos Rodríguez", "operador.despachos", "despachos123"));
        operadorRepository.save(new Operador("María González", "operador.caja", "caja123"));
        operadorRepository.save(new Operador("Luis Martínez", "operador.soporte", "soporte123"));

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
        categoriaRepository.save(new com.example.demo.entitys.Categoria("Especialidades De La Casa"));
        categoriaRepository.save(new com.example.demo.entitys.Categoria("Postre"));
        categoriaRepository.save(new com.example.demo.entitys.Categoria("Bebida"));

        // ==========================================
        // AGREGAR 40 PRODUCTOS
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
            "https://buenazo.cronosmedia.glr.pe/original/2020/09/09/5f58f8c082c2f615f804ffdb.jpg"
        ));

        productoRepository.save(new Producto(
            "Ceviche De Camarón",
            32000.0,
            "Camarones frescos marinados en limón con cebolla morada, cilantro, tomate y un toque de ají.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQN3fqe-rQJ2Xg4wQaDGmzOyouRs8WYSJ8OP9RmdnAoe0ZEiFZKX4V7GBO3&s=10"
        ));

        productoRepository.save(new Producto(
            "Tostadas De Ceviche",
            29000.0,
            "Crujientes tostadas de maíz cubiertas con ceviche fresco, aguacate, cebolla morada y cilantro.",
            "https://familiakitchen.com/wp-content/uploads/2023/05/Tostadas-de-Shrimp-Ceviche-v3.jpg"
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
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5zRcwlCuulf_hAjq_Z3S4n3CDKHzXRcbRMj-caXjReRGiMwlXVjHEHdg&s=10"
        ));

        productoRepository.save(new Producto(
            "Empanadas De Camarón",
            26000.0,
            "Empanadas artesanales rellenas de camarones, queso costeño, cebolla y especias de la casa.",
            "https://cdn7.kiwilimon.com/recetaimagen/33877/960x640/39381.jpg.jpg"
        ));

        productoRepository.save(new Producto(
            "Ceviche De Pulpo",
            34000.0,
            "Pulpo fresco marinado en limón con cebolla morada, cilantro, ají y especias tropicales.",
            "https://gourmet.iprospect.cl/wp-content/uploads/2017/11/cevpul2.jpg-editada.jpg"
        ));

        productoRepository.save(new Producto(
            "Tartar De Pescado",
            33000.0,
            "Pescado fresco cortado en cubos acompañado de aguacate, cebolla morada, limón y salsa cítrica.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSlFS4yQzuTxp1_5PWFwpV5XJK2jKhTq3wWIcejpUHgZ8JhzI3DWAwCp-IC&s=10"
        ));

        productoRepository.save(new Producto(
            "Mariscos A La Vinagreta",
            31000.0,
            "Selección de mariscos frescos acompañados de vinagreta cítrica, cebolla morada, cilantro y limón.",
            "https://selectumgastroplaceres.es/cdn/shop/files/DSCF8083.jpg?v=1764138232&width=1445"
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
            "https://www.cocinadelirante.com/800x600/filters:format(webp):quality(75)/sites/default/files/images/2023/03/mojarra-frita-la-mantequilla.jpg"
        ));

        productoRepository.save(new Producto(
            "Salmón A La Plancha",
            52000.0,
            "Filete de salmón fresco preparado a la plancha con mantequilla de hierbas, limón y vegetales salteados.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTBAE7NVx7VUAATf42Sr7hCyjQ9ogEg35WipqcYfmqDwSk2Bh1BXU2GFxK_&s=10"
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
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9KRt2Jb2cxw0rRBn0YmO466gbPNghubA01qMrzurZ5tRiWHZTlyVHHZw&s=10"
        ));

        productoRepository.save(new Producto(
            "Pescado En Salsa De Coco",
            46000.0,
            "Filete de pescado fresco acompañado de una cremosa salsa de coco, cilantro, ajo y especias caribeñas.",
            "https://commons.wikimedia.org/wiki/Special:Redirect/file/Fish_dish_from_set_of_dinner_on_Osteria_Ristorante_Italiano.jpg"
        ));

        productoRepository.save(new Producto(
            "Camarones A La Parrilla",
            45000.0,
            "Camarones frescos marinados en ajo, limón y hierbas, preparados a la parrilla.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZ8Yw8IKr1bkFQ41XOqdA49ELmHYshkl0bVCwDGkYayzd1pFBc9a7A5_xj&s=10"
        ));

        productoRepository.save(new Producto(
            "Calamares A La Parrilla",
            43000.0,
            "Calamares frescos preparados a la parrilla con ajo, aceite de oliva, limón y hierbas aromáticas.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTEkSJPObVWx-Mod5P4xkim75ZjU5GqyVn30s5Q_q4MdV1wRLLmy9_tM_U3&s=10"
        ));

        productoRepository.save(new Producto(
            "Pulpo A La Parrilla",
            57000.0,
            "Tentáculos de pulpo preparados a la parrilla con aceite de oliva, limón, ajo y hierbas frescas.",
            "https://www.giallozafferano.es/images/195-19599/pulpo-a-la-parrilla_1200x800.jpg"
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
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfpTgHA50f_RRNhTM46NV8rEiNL9jSSBqchh0prnkMUt-_8ZDPYz4Sm-A&s=10"

        ));

        productoRepository.save(new Producto(
            "Arroz Marinero",
            44000.0,
            "Arroz preparado con camarones, calamares y otros frutos del mar, acompañado de un sofrito especial.",
            "https://cdn.colombia.com/gastronomia/2012/07/12/arroz-marinero-2905.jpg"
        ));

        productoRepository.save(new Producto(
            "Risotto De Mariscos",
            50000.0,
            "Risotto cremoso preparado lentamente con caldo de mariscos, camarones, calamares y queso parmesano.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQimG6cMnzrkySp4HOEvMw8H-Oh0oIVf53YGMD6DrbXZZSRO2ZUOCPrmX8&s=10"
        ));

        productoRepository.save(new Producto(
            "Fideuá De Mariscos",
            49000.0,
            "Fideos tostados cocinados en concentrado de mariscos con camarones, calamares y mejillones.",
            "https://imag.bonviveur.com/fideua-de-pescado-y-marisco.jpg"
        ));

        productoRepository.save(new Producto(
            "Parrillada Del Mar",
            65000.0,
            "Selección de pescado, camarones, calamares y otros frutos del mar preparados a la parrilla.",
            "https://www.recetasnestle.com.ec/sites/default/files/srh_recipes/9c0c13b0dde59cf295062dc40f559b9e.jpg"
        ));

        productoRepository.save(new Producto(
            "Festival De Mariscos",
            62000.0,
            "Combinación especial de camarones, pescado, calamares y otros frutos del mar seleccionados por la casa.",
            "https://cloudfront-us-east-1.images.arcpublishing.com/prisaradioco/NK7BO5OKCRD3NB4LYZJBLYB67Q.jpeg"
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
            "https://www.laylita.com/recetas/wp-content/uploads/2012/12/Tarta-cremosa-de-maracuya-o-chinola-1024x683.jpg"
        ));

        productoRepository.save(new Producto(
            "Cheesecake De Maracuyá",
            22000.0,
            "Cheesecake cremoso con cobertura de maracuyá, acompañado de una base crocante de galleta.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxPF3adzUoadO-G_cbYbp6K7okLLU7nT-faUQIkGsv3-CmcOT-2JNB9tkH&s=10"
        ));


        // ==========================================
        // BEBIDAS
        // ==========================================

        productoRepository.save(new Producto(
            "Limonada Natural",
            10000.0,
            "Refrescante limonada preparada con jugo de limón natural, agua y un toque de azúcar.",
            "https://www.sortirambnens.com/wp-content/uploads/2019/02/llimonada-natural-per-a-nens.jpg"
        ));
        productoRepository.save(new Producto(
            "Limonada De Flor De Jamaica",
            14000.0,
            "Refrescante bebida de limón y flor de jamaica con un delicado equilibrio entre dulce y ácido.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsVF92Vejl1lvKfYzKoIH2n9SLCSpPJmA_8MZq0XmcfPw-zRt-40FZ-jvY&s=10"
        ));

        productoRepository.save(new Producto(
            "Agua De Coco",
            12000.0,
            "Refrescante agua de coco natural, ideal para acompañar los sabores tropicales del restaurante.",
            "https://www.eldiario.net/portal/wp-content/uploads/2026/04/NOTA-5-FOTO.webp"
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
        productoRepository.findById(38L).get().setCategoria(categoriaRepository.findById(4L).get());
        productoRepository.findById(39L).get().setCategoria(categoriaRepository.findById(4L).get());
        productoRepository.findById(40L).get().setCategoria(categoriaRepository.findById(4L).get());
        productoRepository.findById(41L).get().setCategoria(categoriaRepository.findById(5L).get());
        productoRepository.findById(42L).get().setCategoria(categoriaRepository.findById(5L).get());
        productoRepository.findById(43L).get().setCategoria(categoriaRepository.findById(5L).get());

        // ==========================================
        // AGREGAR DOMICILIARIOS
        // ==========================================
        domiciliarioRepository.save(Domiciliario.builder()
            .nombre("Carlos Mendoza")
            .celular("3001234567")
            .cedula("1098765432")
            .disponible(true)
            .build());

        domiciliarioRepository.save(Domiciliario.builder()
            .nombre("Brayan Martínez")
            .celular("3119876543")
            .cedula("1087654321")
            .disponible(true)
            .build());

        domiciliarioRepository.save(Domiciliario.builder()
            .nombre("Laura Gómez")
            .celular("3205551234")
            .cedula("1076543210")
            .disponible(false)
            .build());

        domiciliarioRepository.save(Domiciliario.builder()
            .nombre("Mateo Rojas")
            .celular("3156789012")
            .cedula("1065432109")
            .disponible(true)
            .build());

        domiciliarioRepository.save(Domiciliario.builder()
            .nombre("Valentina Torres")
            .celular("3167890123")
            .cedula("1054321098")
            .disponible(true)
            .build());

        // ==========================================
        // AGREGAR PEDIDOS
        // ==========================================
        Cliente cliente1 = clienteRepository.findById(1L).orElse(null);
        Cliente cliente2 = clienteRepository.findById(2L).orElse(null);
        Cliente cliente3 = clienteRepository.findById(3L).orElse(null);
        Cliente cliente4 = clienteRepository.findById(4L).orElse(null);
        Cliente cliente5 = clienteRepository.findById(5L).orElse(null);

        Domiciliario dom1 = domiciliarioRepository.findById(1L).orElse(null);
        Domiciliario dom2 = domiciliarioRepository.findById(2L).orElse(null);
        Domiciliario dom3 = domiciliarioRepository.findById(3L).orElse(null);
        Domiciliario dom4 = domiciliarioRepository.findById(4L).orElse(null);

        Pedido pedido1 = pedidoRepository.save(Pedido.builder()
            .fechaCreacion(LocalDate.now().minusDays(1))
            .fechaEntrega(LocalDate.now().minusDays(1))
            .estado("Entregado")
            .cliente(cliente1)
            .domiciliario(dom1)
            .build());

        Pedido pedido2 = pedidoRepository.save(Pedido.builder()
            .fechaCreacion(LocalDate.now())
            .fechaEntrega(null)
            .estado("En camino")
            .cliente(cliente2)
            .domiciliario(dom2)
            .build());

        Pedido pedido3 = pedidoRepository.save(Pedido.builder()
            .fechaCreacion(LocalDate.now())
            .fechaEntrega(null)
            .estado("En preparación")
            .cliente(cliente3)
            .domiciliario(null)
            .build());

        Pedido pedido4 = pedidoRepository.save(Pedido.builder()
            .fechaCreacion(LocalDate.now().minusDays(2))
            .fechaEntrega(LocalDate.now().minusDays(2))
            .estado("Entregado")
            .cliente(cliente4)
            .domiciliario(dom3)
            .build());

        Pedido pedido5 = pedidoRepository.save(Pedido.builder()
            .fechaCreacion(LocalDate.now())
            .fechaEntrega(null)
            .estado("En preparación")
            .cliente(cliente5)
            .domiciliario(dom4)
            .build());

        // ==========================================
        // AGREGAR ITEMS DE PEDIDO (TABLA PUENTE)
        // ==========================================
        Producto prod1 = productoRepository.findById(1L).orElse(null);  // Aguachile Negro De Camarón
        Producto prod14 = productoRepository.findById(14L).orElse(null); // Pulpo A Las Brasas
        Producto prod25 = productoRepository.findById(25L).orElse(null); // Paella De Mariscos
        Producto prod38 = productoRepository.findById(38L).orElse(null); // Limonada Natural
        Producto prod41 = productoRepository.findById(41L).orElse(null); // Limonada Natural

        // Items para Pedido 1
        itemPedidoRepository.save(ItemPedido.builder()
            .pedido(pedido1)
            .producto(prod14)
            .cantidad(2)
            .precioUnitario(prod14 != null ? prod14.getPrecio() : 58000.0)
            .build());

        itemPedidoRepository.save(ItemPedido.builder()
            .pedido(pedido1)
            .producto(prod38)
            .cantidad(2)
            .precioUnitario(prod38 != null ? prod38.getPrecio() : 10000.0)
            .build());

        // Items para Pedido 2
        itemPedidoRepository.save(ItemPedido.builder()
            .pedido(pedido2)
            .producto(prod25)
            .cantidad(1)
            .precioUnitario(prod25 != null ? prod25.getPrecio() : 52000.0)
            .build());

        itemPedidoRepository.save(ItemPedido.builder()
            .pedido(pedido2)
            .producto(prod1)
            .cantidad(1)
            .precioUnitario(prod1 != null ? prod1.getPrecio() : 36000.0)
            .build());

        // Items para Pedido 3
        itemPedidoRepository.save(ItemPedido.builder()
            .pedido(pedido3)
            .producto(prod1)
            .cantidad(3)
            .precioUnitario(prod1 != null ? prod1.getPrecio() : 36000.0)
            .build());

        // Items para Pedido 4
        itemPedidoRepository.save(ItemPedido.builder()
            .pedido(pedido4)
            .producto(prod14)
            .cantidad(1)
            .precioUnitario(prod14 != null ? prod14.getPrecio() : 58000.0)
            .build());

        // Items para Pedido 5
        itemPedidoRepository.save(ItemPedido.builder()
            .pedido(pedido5)
            .producto(prod41)
            .cantidad(2)
            .precioUnitario(prod41 != null ? prod41.getPrecio() : 10000.0)
            .build());

        // ==========================================
        // AGREGAR ADICIONALES (POR CATEGORÍA)
        // ==========================================
        Categoria catEntrada = categoriaRepository.findById(1L).orElse(null);
        Categoria catPlatoFuerte = categoriaRepository.findById(2L).orElse(null);
        Categoria catEspecialidades = categoriaRepository.findById(3L).orElse(null);
        Categoria catPostre = categoriaRepository.findById(4L).orElse(null);
        Categoria catBebida = categoriaRepository.findById(5L).orElse(null);

        adicionalRepository.save(new Adicional("Patacones con Hogao", 7000.0, catEntrada));
        adicionalRepository.save(new Adicional("Papas de Aguacate Frito", 10000.0, catEntrada));
        adicionalRepository.save(new Adicional("Ensalada Mixta Tostada", 8000.0, catPlatoFuerte));
        adicionalRepository.save(new Adicional("Arroz de Mariscos Extra", 12000.0, catPlatoFuerte));
        adicionalRepository.save(new Adicional("Porción de Arroz con Coco", 9000.0, catEspecialidades));
        adicionalRepository.save(new Adicional("Salsa Tártara de la Casa", 4000.0, catEspecialidades));
        adicionalRepository.save(new Adicional("Bola de Helado de Vainilla", 5000.0, catPostre));
        adicionalRepository.save(new Adicional("Shot de Ron Caribeño", 6000.0, catBebida));

        // Relaciones Producto - Adicional del DER
        Adicional adicional1 = adicionalRepository.findById(1L).orElseThrow();
        Adicional adicional2 = adicionalRepository.findById(2L).orElseThrow();
        Adicional adicional3 = adicionalRepository.findById(3L).orElseThrow();
        Adicional adicional4 = adicionalRepository.findById(4L).orElseThrow();
        Adicional adicional5 = adicionalRepository.findById(5L).orElseThrow();
        productoAdicionalRepository.save(new ProductoAdicional(prod1, adicional1));
        productoAdicionalRepository.save(new ProductoAdicional(prod14, adicional3));
        productoAdicionalRepository.save(new ProductoAdicional(prod25, adicional4));
        productoAdicionalRepository.save(new ProductoAdicional(prod38, adicional2));
        productoAdicionalRepository.save(new ProductoAdicional(prod41, adicional5));

        // Cinco carritos con sus ítems y adicionales seleccionados
        for (long clienteId = 4L; clienteId <= 8L; clienteId++) {
            Cliente cliente = clienteRepository.findById(clienteId).orElseThrow();
            Carrito carrito = carritoRepository.save(new Carrito(cliente));
            Producto producto = productoRepository.findById(clienteId).orElseThrow();
            ItemCarrito itemCarrito = itemCarritoRepository.save(new ItemCarrito(1, carrito, producto));
            Adicional adicional = adicionalRepository.findById(clienteId).orElseThrow();
            itemCarritoAdicionalRepository.save(new ItemCarritoAdicional(itemCarrito, adicional));
        }

        // Cinco adicionales asociados a ítems de pedidos ya generados
        for (long itemPedidoId = 1L; itemPedidoId <= 5L; itemPedidoId++) {
            ItemPedido itemPedido = itemPedidoRepository.findById(itemPedidoId).orElseThrow();
            Adicional adicional = adicionalRepository.findById(itemPedidoId).orElseThrow();
            itemPedidoAdicionalRepository.save(new ItemPedidoAdicional(itemPedido, adicional));
        }
    }   
}
