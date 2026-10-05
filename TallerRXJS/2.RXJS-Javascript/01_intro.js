// =======================================================================
// SEMANA 9 - 01_INTRO.JS: FUNDAMENTOS DE RXJS Y PATRÓN PUBLISH/SUBSCRIBE
// =======================================================================

import { Observable } from 'rxjs';

// Dos partes esenciales del patrón:
// 1. Observable: Es quien emite / produce los mensajes. Por convención termina en '$'.
// 2. Observer: Es quien recibe los mensajes y reacciona ante ellos con funciones.

console.log("=== 1. Creación básica de un Observable ===");

const observable$ = new Observable(subscriber => {
  // El observable define qué datos emitir cuando alguien se suscribe
  subscriber.next("Hola");
  subscriber.next("mundo");
  subscriber.complete(); // Cierra el flujo de emisión
  subscriber.next("Este mensaje ya no se emitirá porque se completó");
});

// El Observer es un objeto JavaScript con 3 funciones posibles:
// - next: se ejecuta cada vez que llega un nuevo dato
// - error: se ejecuta si ocurre una excepción
// - complete: se ejecuta cuando el flujo finaliza exitosamente
const observer = {
  next: (mensaje) => console.log("[Observer 1] Recibiendo mensaje:", mensaje),
  error: (err) => console.error("[Observer 1] Error:", err),
  complete: () => console.log("[Observer 1] Flujo completado con éxito")
};

// Suscripción: Une el observable con el observador
console.log("--- Antes de suscribir ---");
const subscription = observable$.subscribe(observer);
console.log("--- Después de suscribir ---\n");

// =======================================================================
// 2. Múltiples observadores para el mismo Observable
// =======================================================================
console.log("=== 2. Múltiples observadores ===");

const observer2 = {
  // Lo único obligatorio para un observador es el método next
  next: (mensaje) => console.log(`[Observer 2 - Notificación]: <<${mensaje}>>`)
};

observable$.subscribe(observer2);

// =======================================================================
// 3. Un observador suscrito a múltiples Observables
// =======================================================================
console.log("\n=== 3. Suscripción a múltiples Observables ===");

const observableNumero$ = new Observable(subscriber => {
  subscriber.next(100);
  subscriber.next(200);
  subscriber.next(300);
  subscriber.complete();
});

// observer2 puede suscribirse tanto a 'observable$' como a 'observableNumero$'
observableNumero$.subscribe(observer2);

/*
  -----------------------------------------------------------------------
  NOTA CONCEPTUAL (Explicada en el video):
  ¿Por qué necesitamos esto en aplicaciones Web (Frontend con Backend)?
  1. El navegador carga el HTML.
  2. En el HTML hay un elemento, por ejemplo: "Total de resultados: {{ totalResultados }}".
  3. Si calculamos 'totalResultados' antes de que el Backend responda, será NULL/undefined y la web falla.
  4. Con programación reactiva:
     - El componente se SUSCRIBE a la petición del Backend (Observable).
     - Cuando el Backend responde, la función 'next' recibe los datos.
     - En ese momento exacto se actualiza el modelo y la vista muestra la información.
  -----------------------------------------------------------------------
*/
