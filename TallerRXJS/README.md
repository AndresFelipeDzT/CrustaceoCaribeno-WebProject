# Taller Semana 9: Programación Reactiva con RxJS y Angular

Este taller contiene la implementación práctica paso a paso realizada en la **Clase 9** de Desarrollo Web (Pontificia Universidad Javeriana), cubriendo desde los fundamentos de funciones en JavaScript hasta el encadenamiento reactivo de peticiones HTTP en Angular.

---

## 📁 Estructura del Taller

```text
TallerRXJS/
├── 1.Funciones/
│   ├── package.json
│   ├── funciones.js          # Declaraciones, Hoisting, Arrow functions y callbacks
│   └── arrow_vs_normal.js    # Demostración del 'this' léxico en Arrow functions vs tradicionales
├── 2.RXJS-Javascript/
│   ├── package.json          # ES Module con dependencia rxjs
│   ├── 01_intro.js           # Observable ($), Observer, suscripción y flujo asíncrono
│   ├── 02_observable.js      # Operadores de creación (of, from, range, handmadeFrom, interval)
│   ├── 03_pipes.js           # Tuberías (pipe, take, map, filter, reduce) e impacto del orden
│   └── 04_merge_concat.js    # Combinación de flujos: merge (tiempo) vs concat (secuencial)
└── 3.Angular-RXJS/
    ├── src/app/
    │   ├── models/
    │   │   ├── user.ts       # Modelo de Usuario para JSONPlaceholder
    │   │   └── post.ts       # Modelo de Publicación
    │   ├── app.ts            # Componente con lógica de encadenamiento usando mergeMap
    │   ├── app.html          # Vista con formulario reactivo y tarjetas condicionales
    │   └── app.css           # Estilos limpios y organizados
    └── ...
```

---

## 🚀 Cómo ejecutar cada parte

### 1. Repaso de Funciones (`1.Funciones`)
```bash
cd "1.Funciones"
node funciones.js
node arrow_vs_normal.js
```

### 2. Programación Reactiva con RxJS en Node (`2.RXJS-Javascript`)
```bash
cd "2.RXJS-Javascript"
node 01_intro.js
node 02_observable.js
node 03_pipes.js
node 04_merge_concat.js
```

### 3. Proyecto Angular con RxJS (`3.Angular-RXJS`)
```bash
cd "3.Angular-RXJS"
npm start
# Abre en tu navegador: http://localhost:4200
```

---

## 🧠 Conceptos Clave Demostrados

1. **Patrón Publish/Subscribe (Observable / Observer)**:
   - El **Observable** emite flujos de datos (`next`, `error`, `complete`).
   - El **Observer** es un objeto que define los métodos que reaccionan a dichas emisiones.
   - En el Frontend, prácticamente nunca creamos observables manualmente; nos suscribimos a las peticiones del `HttpClient`.

2. **Impacto del orden en los Pipes**:
   - `take(5) -> filter(pares) -> map(*2)` produce 3 datos (`[0, 4, 8]`).
   - `filter(pares) -> take(5) -> map(*2)` produce 5 datos (`[0, 4, 8, 12, 16]`).

3. **Resolución de Dependencias Asíncronas con `mergeMap`**:
   - Evita la condición de carrera: Si llamamos a la API de posts antes de que la API de usuarios retorne, `usuario.id` es `null`.
   - Con `.pipe(mergeMap(...))`, Angular espera la respuesta del usuario, extrae su ID y dispara la petición de sus publicaciones en secuencia ordenada.
