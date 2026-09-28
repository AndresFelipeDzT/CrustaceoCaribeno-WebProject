import { Injectable } from '@angular/core';
import { Adicional } from '../models/adicional.model';
import { Comida } from '../models/comida.model';

@Injectable({ providedIn: 'root' })
export class ComidaService {
  private readonly storageKey = 'restaurante.comidas';
  private adicionales: Adicional[] = [
    { idAdicional: 1, nombre: 'Patacones con Hogao', precio: 7000, activo: true, categorias: [] },
    { idAdicional: 2, nombre: 'Papas de Aguacate Frito', precio: 10000, activo: true, categorias: [] },
    { idAdicional: 3, nombre: 'Ensalada Mixta Tostada', precio: 8000, activo: true, categorias: [] },
    { idAdicional: 4, nombre: 'Arroz de Mariscos Extra', precio: 12000, activo: true, categorias: [] },
    { idAdicional: 5, nombre: 'Porción de Arroz con Coco', precio: 9000, activo: true, categorias: [] },
    { idAdicional: 6, nombre: 'Salsa Tártara de la Casa', precio: 4000, activo: true, categorias: [] },
    { idAdicional: 7, nombre: 'Bola de Helado de Vainilla', precio: 5000, activo: true, categorias: [] },
    { idAdicional: 8, nombre: 'Shot de Ron Caribeño', precio: 6000, activo: true, categorias: [] }
  ];

  private readonly adicionalesPorCategoria: Record<string, number[]> = {
    'Entrada': [1, 2, 6],
    'Plato Fuerte': [1, 3, 4, 5, 6],
    'Especialidades De La Casa': [3, 5, 6, 8],
    'Postre': [7],
    'Bebida': [8]
  };

  // Datos iniciales del menú de Spring Boot; se conservan en memoria como en el ejemplo del profesor.
  private comidas: Comida[] = [
  {
    "id": 1,
    "nombre": "Aguachile Negro De Camar\u00f3n",
    "descripcion": "Camarones frescos marinados en zumo de lim\u00f3n con ceniza de chiles habaneros y salsa negra especial de la casa.",
    "precio": 36000.0,
    "categoria": "Entrada",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzviqiC5iiHmv4J96axL2hNarSuJouJtG45T_qJH4dvKekIAINlFK7KCxB&s=10",
    "activo": true
  },
  {
    "id": 2,
    "nombre": "Ceviche Tropical",
    "descripcion": "Cubos de pescado blanco fresco marinados en c\u00edtricos con mango biche, maracuy\u00e1, cebolla morada y cilantro fresco.",
    "precio": 32000.0,
    "categoria": "Entrada",
    "imagenURL": "https://www.laylita.com/recetas/wp-content/uploads/2025/02/Ceviche-de-besugo-1024x768.jpg",
    "activo": true
  },
  {
    "id": 3,
    "nombre": "Tacos Gobernador",
    "descripcion": "Tortillas de ma\u00edz artesanales rellenas de camarones salteados con pimientos, cebolla caramelizada y queso coste\u00f1o gratinado.",
    "precio": 34000.0,
    "categoria": "Entrada",
    "imagenURL": "https://cdn-ilddihb.nitrocdn.com/MgqZCGPEMHvMRLsisMUCAIMWvgGMxqaj/assets/images/optimized/rev-0e527e8/www.goya.com/wp-content/uploads/2024/09/tacos-gobernador.jpg",
    "activo": true
  },
  {
    "id": 4,
    "nombre": "Ceviche Mixto",
    "descripcion": "Combinaci\u00f3n de pescado blanco, camarones, calamares y pulpo marinados en lim\u00f3n con cebolla morada y cilantro.",
    "precio": 35000.0,
    "categoria": "Entrada",
    "imagenURL": "https://buenazo.cronosmedia.glr.pe/original/2020/09/09/5f58f8c082c2f615f804ffdb.jpg",
    "activo": true
  },
  {
    "id": 5,
    "nombre": "Ceviche De Camar\u00f3n",
    "descripcion": "Camarones frescos marinados en lim\u00f3n con cebolla morada, cilantro, tomate y un toque de aj\u00ed.",
    "precio": 32000.0,
    "categoria": "Entrada",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQN3fqe-rQJ2Xg4wQaDGmzOyouRs8WYSJ8OP9RmdnAoe0ZEiFZKX4V7GBO3&s=10",
    "activo": true
  },
  {
    "id": 6,
    "nombre": "Tostadas De Ceviche",
    "descripcion": "Crujientes tostadas de ma\u00edz cubiertas con ceviche fresco, aguacate, cebolla morada y cilantro.",
    "precio": 29000.0,
    "categoria": "Entrada",
    "imagenURL": "https://familiakitchen.com/wp-content/uploads/2023/05/Tostadas-de-Shrimp-Ceviche-v3.jpg",
    "activo": true
  },
  {
    "id": 7,
    "nombre": "Calamares Fritos",
    "descripcion": "Anillos de calamar frescos apanados y fritos hasta quedar dorados y crujientes, acompa\u00f1ados de salsa t\u00e1rtara.",
    "precio": 28000.0,
    "categoria": "Entrada",
    "imagenURL": "https://commons.wikimedia.org/wiki/Special:Redirect/file/Fried_calamares.jpg",
    "activo": true
  },
  {
    "id": 8,
    "nombre": "C\u00f3ctel De Mariscos",
    "descripcion": "Camarones y frutos del mar servidos en salsa especial con aguacate, lim\u00f3n, cebolla y cilantro fresco.",
    "precio": 32000.0,
    "categoria": "Entrada",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5zRcwlCuulf_hAjq_Z3S4n3CDKHzXRcbRMj-caXjReRGiMwlXVjHEHdg&s=10",
    "activo": true
  },
  {
    "id": 9,
    "nombre": "Empanadas De Camar\u00f3n",
    "descripcion": "Empanadas artesanales rellenas de camarones, queso coste\u00f1o, cebolla y especias de la casa.",
    "precio": 26000.0,
    "categoria": "Entrada",
    "imagenURL": "https://cdn7.kiwilimon.com/recetaimagen/33877/960x640/39381.jpg.jpg",
    "activo": true
  },
  {
    "id": 10,
    "nombre": "Ceviche De Pulpo",
    "descripcion": "Pulpo fresco marinado en lim\u00f3n con cebolla morada, cilantro, aj\u00ed y especias tropicales.",
    "precio": 34000.0,
    "categoria": "Entrada",
    "imagenURL": "https://gourmet.iprospect.cl/wp-content/uploads/2017/11/cevpul2.jpg-editada.jpg",
    "activo": true
  },
  {
    "id": 11,
    "nombre": "Tartar De Pescado",
    "descripcion": "Pescado fresco cortado en cubos acompa\u00f1ado de aguacate, cebolla morada, lim\u00f3n y salsa c\u00edtrica.",
    "precio": 33000.0,
    "categoria": "Entrada",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSlFS4yQzuTxp1_5PWFwpV5XJK2jKhTq3wWIcejpUHgZ8JhzI3DWAwCp-IC&s=10",
    "activo": true
  },
  {
    "id": 12,
    "nombre": "Mariscos A La Vinagreta",
    "descripcion": "Selecci\u00f3n de mariscos frescos acompa\u00f1ados de vinagreta c\u00edtrica, cebolla morada, cilantro y lim\u00f3n.",
    "precio": 31000.0,
    "categoria": "Entrada",
    "imagenURL": "https://selectumgastroplaceres.es/cdn/shop/files/DSCF8083.jpg?v=1764138232&width=1445",
    "activo": true
  },
  {
    "id": 13,
    "nombre": "Pulpo A Las Brasas",
    "descripcion": "Tent\u00e1culos de pulpo marinados en chimichurri caribe\u00f1o y especias, asados a la parrilla sobre cama de papas r\u00fasticas al piment\u00f3n.",
    "precio": 58000.0,
    "categoria": "Entrada",
    "imagenURL": "https://www.shutterstock.com/image-photo/grilled-octopus-asparagus-served-on-260nw-2483650495.jpg",
    "activo": true
  },
  {
    "id": 14,
    "nombre": "Pescado A La Talla",
    "descripcion": "Pescado fresco abierto en mariposa, marinado con adobo tradicional de chiles dulces y hierbas finas, cocinado a la le\u00f1a.",
    "precio": 52000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8CmGHZaPGTnUMOxwcvO4rRsfFhUZdWfYK8profaljDkVwhoVXCfSa2Vg&s=10",
    "activo": true
  },
  {
    "id": 15,
    "nombre": "Cazuela De Mariscos",
    "descripcion": "Tradicional cazuela con camarones, calamares, pulpo y mejillones en cremosa reducci\u00f3n de leche de coco y especias de la costa.",
    "precio": 48000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://www.cocina-ecuatoriana.com/base/stock/Recipe/cazuela-mixta/cazuela-mixta_web.jpg.webp",
    "activo": true
  },
  {
    "id": 16,
    "nombre": "Camarones Al Ajillo",
    "descripcion": "Camarones salteados al punto en aceite de oliva extra virgen, abundante ajo dorado, vino blanco y perejil fresco picado.",
    "precio": 42000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://especiasmontero.com/wp-content/uploads/2025/05/Camarones-al-ajillo-500x375.jpg",
    "activo": true
  },
  {
    "id": 17,
    "nombre": "Salm\u00f3n En Costra De Hierbas",
    "descripcion": "Filete de salm\u00f3n a la plancha cubierto con crujiente costra de finas hierbas y frutos secos, acompa\u00f1ado de vegetales al vapor.",
    "precio": 54000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://gourmet.iprospect.cl/wp-content/uploads/2016/09/Salm%C3%B3n-a-las-finas-hierbas-web.jpg",
    "activo": true
  },
  {
    "id": 18,
    "nombre": "Langostinos A La Mantequilla De Ajo",
    "descripcion": "Langostinos jumbo ba\u00f1ados en mantequilla clarificada con infusi\u00f3n de ajo tostado, lim\u00f3n mandarino y hierbas arom\u00e1ticas.",
    "precio": 62000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ22zmHFu8otBj1tgnHUsdG4sONVQvhxJ-J21SmaYkFEV4G0Lz1pcJc5jPd&s=10",
    "activo": true
  },
  {
    "id": 19,
    "nombre": "Mojarra Frita",
    "descripcion": "Mojarra fresca frita hasta quedar dorada y crocante, acompa\u00f1ada de arroz con coco, ensalada y patacones.",
    "precio": 40000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://www.cocinadelirante.com/800x600/filters:format(webp):quality(75)/sites/default/files/images/2023/03/mojarra-frita-la-mantequilla.jpg",
    "activo": true
  },
  {
    "id": 20,
    "nombre": "Salm\u00f3n A La Plancha",
    "descripcion": "Filete de salm\u00f3n fresco preparado a la plancha con mantequilla de hierbas, lim\u00f3n y vegetales salteados.",
    "precio": 52000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTBAE7NVx7VUAATf42Sr7hCyjQ9ogEg35WipqcYfmqDwSk2Bh1BXU2GFxK_&s=10",
    "activo": true
  },
  {
    "id": 21,
    "nombre": "Salm\u00f3n En Salsa De Lim\u00f3n",
    "descripcion": "Filete de salm\u00f3n sellado acompa\u00f1ado de una cremosa salsa de lim\u00f3n, ajo, mantequilla y hierbas frescas.",
    "precio": 55000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://commons.wikimedia.org/wiki/Special:Redirect/file/SalmonDish.jpg",
    "activo": true
  },
  {
    "id": 22,
    "nombre": "Pescado A La Parrilla",
    "descripcion": "Filete de pescado blanco cocinado a la parrilla con lim\u00f3n, ajo, perejil y vegetales frescos.",
    "precio": 44000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9KRt2Jb2cxw0rRBn0YmO466gbPNghubA01qMrzurZ5tRiWHZTlyVHHZw&s=10",
    "activo": true
  },
  {
    "id": 23,
    "nombre": "Pescado En Salsa De Coco",
    "descripcion": "Filete de pescado fresco acompa\u00f1ado de una cremosa salsa de coco, cilantro, ajo y especias caribe\u00f1as.",
    "precio": 46000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://commons.wikimedia.org/wiki/Special:Redirect/file/Fish_dish_from_set_of_dinner_on_Osteria_Ristorante_Italiano.jpg",
    "activo": true
  },
  {
    "id": 24,
    "nombre": "Camarones A La Parrilla",
    "descripcion": "Camarones frescos marinados en ajo, lim\u00f3n y hierbas, preparados a la parrilla.",
    "precio": 45000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZ8Yw8IKr1bkFQ41XOqdA49ELmHYshkl0bVCwDGkYayzd1pFBc9a7A5_xj&s=10",
    "activo": true
  },
  {
    "id": 25,
    "nombre": "Calamares A La Parrilla",
    "descripcion": "Calamares frescos preparados a la parrilla con ajo, aceite de oliva, lim\u00f3n y hierbas arom\u00e1ticas.",
    "precio": 43000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTEkSJPObVWx-Mod5P4xkim75ZjU5GqyVn30s5Q_q4MdV1wRLLmy9_tM_U3&s=10",
    "activo": true
  },
  {
    "id": 26,
    "nombre": "Pulpo A La Parrilla",
    "descripcion": "Tent\u00e1culos de pulpo preparados a la parrilla con aceite de oliva, lim\u00f3n, ajo y hierbas frescas.",
    "precio": 57000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://www.giallozafferano.es/images/195-19599/pulpo-a-la-parrilla_1200x800.jpg",
    "activo": true
  },
  {
    "id": 27,
    "nombre": "Langostinos Al Ajillo",
    "descripcion": "Langostinos frescos salteados con ajo, mantequilla, lim\u00f3n, vino blanco y perejil.",
    "precio": 60000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://commons.wikimedia.org/wiki/Special:Redirect/file/Shrimp_dish_at_Korean_restaurant.jpg",
    "activo": true
  },
  {
    "id": 28,
    "nombre": "Paella De Mariscos",
    "descripcion": "Arroz tradicional preparado con camarones, calamares, mejillones y especias, terminado con lim\u00f3n fresco.",
    "precio": 52000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://commons.wikimedia.org/wiki/Special:Redirect/file/Paella_seafood.JPG",
    "activo": true
  },
  {
    "id": 29,
    "nombre": "Paella Especial De La Casa",
    "descripcion": "Paella preparada con arroz, camarones, calamares, mejillones y una selecci\u00f3n especial de mariscos frescos.",
    "precio": 58000.0,
    "categoria": "Plato Fuerte",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfpTgHA50f_RRNhTM46NV8rEiNL9jSSBqchh0prnkMUt-_8ZDPYz4Sm-A&s=10",
    "activo": true
  },
  {
    "id": 30,
    "nombre": "Arroz Marinero",
    "descripcion": "Arroz preparado con camarones, calamares y otros frutos del mar, acompa\u00f1ado de un sofrito especial.",
    "precio": 44000.0,
    "categoria": "Especialidades De La Casa",
    "imagenURL": "https://cdn.colombia.com/gastronomia/2012/07/12/arroz-marinero-2905.jpg",
    "activo": true
  },
  {
    "id": 31,
    "nombre": "Risotto De Mariscos",
    "descripcion": "Risotto cremoso preparado lentamente con caldo de mariscos, camarones, calamares y queso parmesano.",
    "precio": 50000.0,
    "categoria": "Especialidades De La Casa",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQimG6cMnzrkySp4HOEvMw8H-Oh0oIVf53YGMD6DrbXZZSRO2ZUOCPrmX8&s=10",
    "activo": true
  },
  {
    "id": 32,
    "nombre": "Fideu\u00e1 De Mariscos",
    "descripcion": "Fideos tostados cocinados en concentrado de mariscos con camarones, calamares y mejillones.",
    "precio": 49000.0,
    "categoria": "Especialidades De La Casa",
    "imagenURL": "https://imag.bonviveur.com/fideua-de-pescado-y-marisco.jpg",
    "activo": true
  },
  {
    "id": 33,
    "nombre": "Parrillada Del Mar",
    "descripcion": "Selecci\u00f3n de pescado, camarones, calamares y otros frutos del mar preparados a la parrilla.",
    "precio": 65000.0,
    "categoria": "Especialidades De La Casa",
    "imagenURL": "https://www.recetasnestle.com.ec/sites/default/files/srh_recipes/9c0c13b0dde59cf295062dc40f559b9e.jpg",
    "activo": true
  },
  {
    "id": 34,
    "nombre": "Festival De Mariscos",
    "descripcion": "Combinaci\u00f3n especial de camarones, pescado, calamares y otros frutos del mar seleccionados por la casa.",
    "precio": 62000.0,
    "categoria": "Especialidades De La Casa",
    "imagenURL": "https://cloudfront-us-east-1.images.arcpublishing.com/prisaradioco/NK7BO5OKCRD3NB4LYZJBLYB67Q.jpeg",
    "activo": true
  },
  {
    "id": 35,
    "nombre": "Arroz Meloso Con Mariscos",
    "descripcion": "Arroz cremoso cocido a fuego lento en bisque de mariscos con camarones, calamares, almejas y un toque de azafr\u00e1n caribe\u00f1o.",
    "precio": 46000.0,
    "categoria": "Especialidades De La Casa",
    "imagenURL": "https://i.blogs.es/4b3414/arroz_meloso/840_560.jpg",
    "activo": true
  },
  {
    "id": 36,
    "nombre": "Torre De Mariscos",
    "descripcion": "Estructura gourmet con capas de ceviche de camar\u00f3n, pulpo marinado, at\u00fan fresco, aguacate cremoso y vinagreta c\u00edtrica.",
    "precio": 56000.0,
    "categoria": "Especialidades De La Casa",
    "imagenURL": "https://media.cocinavital.mx/2022/05/timbal-de-mariscos-receta-1-634x420.jpg",
    "activo": true
  },
  {
    "id": 37,
    "nombre": "Pasta Frutti Di Mare",
    "descripcion": "Fettuccine artesanal al dente salteado con frutos del mar en salsa pomodoro r\u00fastica de tomates frescos y albahaca.",
    "precio": 45000.0,
    "categoria": "Especialidades De La Casa",
    "imagenURL": "https://assets.bonappetit.com/photos/57acc5bb1b33404414975193/1:1/w_2560%2Cc_limit/fettuccine-ai-frutti-di-mare.jpg",
    "activo": true
  },
  {
    "id": 38,
    "nombre": "Flan De Coco",
    "descripcion": "Suave flan de coco ba\u00f1ado en caramelo, con una textura cremosa y delicado sabor tropical.",
    "precio": 18000.0,
    "categoria": "Postre",
    "imagenURL": "https://commons.wikimedia.org/wiki/Special:Redirect/file/Coconut_Flan.jpg",
    "activo": true
  },
  {
    "id": 39,
    "nombre": "Tarta De Maracuy\u00e1",
    "descripcion": "Deliciosa tarta cremosa de maracuy\u00e1 con base crujiente y un intenso sabor tropical.",
    "precio": 20000.0,
    "categoria": "Postre",
    "imagenURL": "https://www.laylita.com/recetas/wp-content/uploads/2012/12/Tarta-cremosa-de-maracuya-o-chinola-1024x683.jpg",
    "activo": true
  },
  {
    "id": 40,
    "nombre": "Cheesecake De Maracuy\u00e1",
    "descripcion": "Cheesecake cremoso con cobertura de maracuy\u00e1, acompa\u00f1ado de una base crocante de galleta.",
    "precio": 22000.0,
    "categoria": "Postre",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxPF3adzUoadO-G_cbYbp6K7okLLU7nT-faUQIkGsv3-CmcOT-2JNB9tkH&s=10",
    "activo": true
  },
  {
    "id": 41,
    "nombre": "Limonada Natural",
    "descripcion": "Refrescante limonada preparada con jugo de lim\u00f3n natural, agua y un toque de az\u00facar.",
    "precio": 10000.0,
    "categoria": "Bebida",
    "imagenURL": "https://www.sortirambnens.com/wp-content/uploads/2019/02/llimonada-natural-per-a-nens.jpg",
    "activo": true
  },
  {
    "id": 42,
    "nombre": "Limonada De Flor De Jamaica",
    "descripcion": "Refrescante bebida de lim\u00f3n y flor de jamaica con un delicado equilibrio entre dulce y \u00e1cido.",
    "precio": 14000.0,
    "categoria": "Bebida",
    "imagenURL": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsVF92Vejl1lvKfYzKoIH2n9SLCSpPJmA_8MZq0XmcfPw-zRt-40FZ-jvY&s=10",
    "activo": true
  },
  {
    "id": 43,
    "nombre": "Agua De Coco",
    "descripcion": "Refrescante agua de coco natural, ideal para acompa\u00f1ar los sabores tropicales del restaurante.",
    "precio": 12000.0,
    "categoria": "Bebida",
    "imagenURL": "https://www.eldiario.net/portal/wp-content/uploads/2026/04/NOTA-5-FOTO.webp",
    "activo": true
  }
];

  constructor() {
    const comidasGuardadas = this.leerComidasGuardadas();
    if (comidasGuardadas) {
      this.comidas = comidasGuardadas;
    }
  }

  private leerComidasGuardadas(): Comida[] | null {
    try {
      if (typeof localStorage === 'undefined') return null;
      const guardadas = localStorage.getItem(this.storageKey);
      if (!guardadas) return null;
      const comidas = JSON.parse(guardadas) as Comida[];
      return Array.isArray(comidas) ? comidas : null;
    } catch {
      return null;
    }
  }

  private guardarComidas(): void {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.storageKey, JSON.stringify(this.comidas));
      }
    } catch {
      // Si el navegador bloquea el almacenamiento, los cambios siguen en memoria.
    }
  }

  // La tabla muestra todos los productos, igual que la tabla de Spring Boot.
  getComidas(): Comida[] {
    return this.comidas;
  }

  getComidasActivas(): Comida[] {
    return this.comidas.filter(comida => comida.activo !== false);
  }

  getAdicionalesPorCategoria(categoria: string): Adicional[] {
    const idsDisponibles = this.adicionalesPorCategoria[categoria] ?? [];
    return this.adicionales.filter(adicional => idsDisponibles.includes(adicional.idAdicional));
  }

  getComidaById(id: number): Comida | undefined {
    return this.comidas.find(comida => comida.id === id);
  }

  addComida(comida: Omit<Comida, 'id'>): void {
    const nextId = Math.max(0, ...this.comidas.map(item => item.id)) + 1;
    this.comidas.push({ ...comida, id: nextId, activo: true });
    this.guardarComidas();
  }

  updateComida(id: number, comida: Omit<Comida, 'id'>): void {
    const index = this.comidas.findIndex(item => item.id === id);
    if (index >= 0) {
      const activo = this.comidas[index].activo !== false;
      this.comidas[index] = { ...this.comidas[index], ...comida, id, activo };
      this.guardarComidas();
    }
  }

  desactivarComida(id: number): void {
    const comida = this.getComidaById(id);
    if (comida) {
      comida.activo = false;
      this.guardarComidas();
    }
  }

  activarComida(id: number): void {
    const comida = this.getComidaById(id);
    if (comida) {
      comida.activo = true;
      this.guardarComidas();
    }
  }

  // Conserva el método que usa la tabla; eliminar equivale a desactivar el producto.
  deleteComida(id: number): void {
    this.desactivarComida(id);
  }
}
