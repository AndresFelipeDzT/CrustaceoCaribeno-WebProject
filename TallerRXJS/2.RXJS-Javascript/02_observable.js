// =======================================================================
// SEMANA 9 - 02_OBSERVABLE.JS: MÉTODOS DE CREACIÓN DE OBSERVABLES EN RXJS
// =======================================================================

import { of, from, range, interval, Observable } from 'rxjs';

// En lugar de usar siempre 'new Observable', RxJS provee operadores de creación
// que simplifican emitir flujos de datos comunes.

// 1. Operador 'of': Emite los argumentos pasados secuencialmente y luego completa
console.log("=== 1. Operador 'of' ===");
const of$ = of("Hello", "World", "desde", "RxJS");
of$.subscribe(value => console.log("[of]:", value));

// 2. Operador 'from': Convierte un arreglo o iterable en un flujo de emisiones
console.log("\n=== 2. Operador 'from' ===");
const arrayDatos = ["Angular", "Spring Boot", "RxJS", "PostgreSQL"];
const from$ = from(arrayDatos);

from$.subscribe({
  next: (item) => console.log("[from item]:", item),
  complete: () => console.log("[from completado]")
});

// 3. Operador 'range': Emite un rango de números (inicio, cantidad)
console.log("\n=== 3. Operador 'range' ===");
const range$ = range(0, 5); // Emite 0, 1, 2, 3, 4
range$.subscribe(num => console.log("[range]:", num));

// 4. Creación artesanal de 'from' (handmadeFrom)
// Demostración didáctica: Comprender que 'from' es una función constructora de Observable
console.log("\n=== 4. Operador artesanal 'handmadeFrom' ===");

const handmadeFrom = (arreglo) => {
  return new Observable(subscriber => {
    for (const elemento of arreglo) {
      subscriber.next(elemento);
    }
    subscriber.complete();
  });
};

const customFrom$ = handmadeFrom(["Dato artesanal 1", "Dato artesanal 2"]);
customFrom$.subscribe({
  next: (val) => console.log("[handmadeFrom]:", val),
  complete: () => console.log("[handmadeFrom completado]")
});

// 5. Operador 'interval': Emite valores secuenciales (0, 1, 2, ...) cada intervalo de tiempo
console.log("\n=== 5. Operador 'interval' (controlado) ===");
const interval$ = interval(100); // Emite cada 100ms

const subInterval = interval$.subscribe(valor => {
  console.log("[interval cada 100ms]:", valor);
  // Cancelamos la suscripción tras 5 emisiones para no bloquear la consola
  if (valor >= 4) {
    subInterval.unsubscribe();
    console.log("[interval desuscrito]");
  }
});
