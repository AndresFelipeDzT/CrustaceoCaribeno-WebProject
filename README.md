# Taller de RxJS (Semana 9) - Desarrollo Web
**Pontificia Universidad Javeriana**  
**Profesor:** Juan Sebastián Angarita Torres  

---

## 📌 Descripción del Proyecto
Este proyecto corresponde a la entrega oficial evaluable del **Taller de RxJS**. El objetivo es consumir de manera asíncrona y reactiva la API pública [DummyJSON](https://dummyjson.com/) mediante **Angular** y operadores de **RxJS**, demostrando el flujo de datos dependientes (Usuario ➔ Publicaciones ➔ Comentarios) sin recargar la página y sin condiciones de carrera.

---

## 🚀 Requerimientos Oficiales Cumplidos (100% + Puntos Extra)

| Requisito Oficial | Implementación | Estado |
| :--- | :--- | :---: |
| **Sin enrutamiento** | Toda la aplicación funciona en una única vista principal (`App`). | ✅ Cumplido |
| **API DummyJSON** | Endpoints oficiales: `/users/filter?key=username=...`, `/posts/user/{userId}`, `/comments/post/{postId}`. | ✅ Cumplido |
| **Sección 1: Buscador** | Input reactivo para ingresar el `username`, botón de búsqueda y accesos rápidos de prueba. | ✅ Cumplido |
| **Sección 2: Datos de Usuario** | Componente hijo independiente `UserCardComponent` que recibe la información mediante `@Input() user`. | ✅ Cumplido |
| **Sección 3: Posts y Comentarios** | Componente hijo independiente `PostListComponent` que recibe los posts y comentarios anidados mediante `@Input() posts`. | ✅ Cumplido |
| **Carita Muy Feliz 1: Reacciones con Íconos** | Conteo de reacciones por post (likes/dislikes) representadas con íconos vectoriales de **Bootstrap Icons** (`bi-hand-thumbs-up-fill`, `bi-hand-thumbs-down-fill`). | ⭐ Puntos Extra |
| **Carita Muy Feliz 2: Nombre de Autor en Comentarios** | Cada comentario muestra el nombre completo del autor (`comment.user.fullName`) y sus likes. | ⭐ Puntos Extra |
| **Manejo de Errores** | Si el usuario no existe, se muestra un banner de alerta en pantalla y se limpia la información anterior. | ✅ Cumplido |
| **Diseño y Estilos** | Maquetación con **Bootstrap 5.3.3** y **Bootstrap Icons 1.11.3**. | ✅ Cumplido |

---

## 🏗️ Arquitectura y Estructura del Código

Siguiendo las buenas prácticas explicadas por el profesor, la lógica se separó en modelos, servicios dedicados y componentes hijos:

```text
3.Angular-RXJS/
├── src/
│   ├── app/
│   │   ├── models/                    # Modelos e interfaces tipadas
│   │   │   ├── user.model.ts          # Interface User, Address, Company
│   │   │   ├── post.model.ts          # Interface Post, PostReactions
│   │   │   └── comment.model.ts       # Interface Comment, CommentUser
│   │   │
│   │   ├── services/                  # Servicios HTTP modulares (1 por entidad)
│   │   │   ├── user.service.ts        # GET /users/filter?key=username={username}
│   │   │   ├── post.service.ts        # GET /posts/user/{userId}
│   │   │   └── comment.service.ts     # GET /comments/post/{postId}
│   │   │
│   │   ├── components/                # Componentes hijos para Secciones 2 y 3
│   │   │   ├── user-card/             # Sección 2: @Input() user (Tarjeta de perfil)
│   │   │   └── post-list/             # Sección 3: @Input() posts (Posts + comentarios)
│   │   │
│   │   ├── app.ts                     # Componente principal: orquestación con RxJS
│   │   ├── app.html                   # Sección 1 (Buscador) y renderizado de hijos
│   │   ├── app.css                    # Estilos complementarios
│   │   └── app.config.ts              # provideHttpClient() configurado
│   └── index.html                     # CDNs de Bootstrap 5 y Bootstrap Icons
```

---

## ⚡ ¿Cómo funciona la consulta reactiva con RxJS? (`app.ts`)

Para evitar suscripciones anidadas (*callback hell*) y condiciones de carrera, el componente principal orquesta los 3 servicios usando tuberías (`pipe`) y operadores reactivos:

1. **`concatMap`:** Recibe el usuario desde `UserService`. Si el usuario no existe, corta el flujo y emite el error. Si existe, espera a tener su `user.id` y dispara la petición de publicaciones en `PostService`.
2. **`forkJoin`:** Por cada publicación encontrada, lanza en paralelo las peticiones a `CommentService` para traer sus comentarios respectivos y los fusiona en el objeto `post.comments`.
3. **`catchError`:** Captura errores de red o usuario no encontrado, limpia los estados y muestra el mensaje amigable al usuario.
4. **`subscribe`:** Un único punto de suscripción final que actualiza las variables que se envían a los componentes hijos vía `@Input`.

---

## 💻 Instrucciones para Ejecutar

### 1. Prerrequisitos
* Tener instalado **Node.js** (versión 18 o superior).

### 2. Instalación de dependencias
Abre una terminal en esta carpeta y ejecuta:
```bash
npm install
```

### 3. Iniciar el servidor de desarrollo
```bash
npm start
```
*(O alternativamente: `npx ng serve`)*

Abre tu navegador en:
```
http://localhost:4200/
```

---

## 🧪 Casos de Prueba Recomendados

En la parte superior de la página encontrarás botones de prueba rápida para verificar los diferentes flujos:

1. **Usuario con posts y comentarios (Éxito completo):**
   * Haz clic en el botón **`emilys`** o escribe `emilys` en el buscador.
   * *Resultado:* Carga el perfil con foto, datos de contacto, sus publicaciones con etiquetas, cantidad de reacciones con íconos de pulgar arriba/abajo, y cada comentario con el nombre completo de su autor.
2. **Otro usuario válido:**
   * Haz clic en **`michaelw`** o escribe `michaelw`.
3. **Usuario con múltiples publicaciones:**
   * Haz clic en **`lillians`** o escribe `lillians`.
4. **Manejo de Error (Usuario inexistente):**
   * Haz clic en **`atuny0 (no existe)`** o escribe cualquier texto al azar (ej. `usuario_invalido_xyz`).
   * *Resultado:* No se muestran tarjetas de datos y aparece la alerta roja: *"El nombre de usuario '...' no existe en el sistema."*
