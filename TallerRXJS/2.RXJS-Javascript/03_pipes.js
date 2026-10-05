// =======================================================================
// SEMANA 9 - 03_PIPES.JS: TRANSFORMACIÓN Y FILTRADO CON PIPES Y OPERADORES
// =======================================================================

import { interval, range } from 'rxjs';
import { take, map, filter, reduce } from 'rxjs/operators';

// Un Pipe es una tubería por donde pasan los datos emitidos por un observable.
// Cada operador en la tubería puede filtrar, transformar o acumular la información.

console.log("=== 1. Pipe con 'take(5)' (limita emisiones y completa) ===");
// Sin 'take', interval emitiría indefinidamente
const intervalTake$ = interval(100).pipe(
  take(5)
);

intervalTake$.subscribe({
  next: val => console.log("[take(5)]:", val),
  complete: () => console.log("[take(5) completado]")
});

// Esperamos a que termine el intervalo para continuar con los siguientes ejemplos
setTimeout(() => {
  console.log("\n=== 2. Pipe con 'take' y 'map' ===");
  // Multiplica cada valor emitido por 2
  range(0, 5).pipe(
    map(val => val * 2)
  ).subscribe(val => console.log("[map(*2)]:", val)); // 0, 2, 4, 6, 8

  console.log("\n=== 3. Comparación: El orden de los operadores en el Pipe IMPORTA ===");

  // ORDEN A: Primero take(5), luego filter(pares), luego map(*2)
  // Flujo: [0, 1, 2, 3, 4] -> Filtrar pares: [0, 2, 4] -> Multiplicar * 2: [0, 4, 8]
  console.log("-> Orden A: take(5) -> filter(pares) -> map(*2):");
  range(0, 10).pipe(
    take(5),
    filter(x => x % 2 === 0),
    map(x => x * 2)
  ).subscribe(val => console.log("   Orden A valor:", val));

  // ORDEN B: Primero filter(pares), luego take(5), luego map(*2)
  // Flujo: De todos los números toma los primeros 5 que sean pares: [0, 2, 4, 6, 8] -> Multiplica * 2: [0, 4, 8, 12, 16]
  console.log("\n-> Orden B: filter(pares) -> take(5) -> map(*2):");
  range(0, 10).pipe(
    filter(x => x % 2 === 0),
    take(5),
    map(x => x * 2)
  ).subscribe(val => console.log("   Orden B valor:", val));

  console.log("\n=== 4. Pipe con operador 'reduce' ===");
  // Acumula la suma de las emisiones que pasan por el pipe
  // Flujo: take(5) -> filter(pares) -> map(*2) -> [0, 4, 8] -> reduce suma total = 12
  range(0, 10).pipe(
    take(5),
    filter(x => x % 2 === 0),
    map(x => x * 2),
    reduce((acumulador, actual) => acumulador + actual, 0)
  ).subscribe(total => {
    console.log("[reduce total acumulado]:", total); // 12
  });
}, 700);
