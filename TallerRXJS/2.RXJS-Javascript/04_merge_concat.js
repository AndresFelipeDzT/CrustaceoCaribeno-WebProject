// =======================================================================
// SEMANA 9 - 04_MERGE_CONCAT.JS: COMBINACIÓN DE OBSERVABLES (MERGE VS CONCAT)
// =======================================================================

import { interval, merge, concat } from 'rxjs';
import { take, map } from 'rxjs/operators';

// Creamos dos observables con diferentes tiempos de emisión para simular
// dos peticiones asíncronas de red con tiempos de respuesta variables.

const crearObservable1 = () => {
  return interval(150).pipe(
    take(4),
    map(val => `[Obs 1 (rápido)] Mensaje #${val + 1} (${(val + 1) * -2})`)
  );
};

const crearObservable2 = () => {
  return interval(300).pipe(
    take(3),
    map(val => `[Obs 2 (lento)]  Mensaje #${val + 1} (${(val + 1) * 2})`)
  );
};

// =======================================================================
// 1. merge: Emite los valores a medida que van llegando en el tiempo
// =======================================================================
console.log("=== 1. Demostración de 'merge' (intercalado según el tiempo) ===");

const obs1ParaMerge$ = crearObservable1();
const obs2ParaMerge$ = crearObservable2();

merge(obs1ParaMerge$, obs2ParaMerge$).subscribe({
  next: val => console.log("MERGE ->", val),
  complete: () => {
    console.log("MERGE finalizado.");
    ejecutarConcat();
  }
});

// =======================================================================
// 2. concat: Espera a que el primer observable termine antes de iniciar el segundo
// =======================================================================
function ejecutarConcat() {
  console.log("\n=== 2. Demostración de 'concat' (orden secuencial garantizado) ===");

  const obs1ParaConcat$ = crearObservable1();
  const obs2ParaConcat$ = crearObservable2();

  concat(obs1ParaConcat$, obs2ParaConcat$).subscribe({
    next: val => console.log("CONCAT ->", val),
    complete: () => {
      console.log("CONCAT finalizado.");
      mostrarExplicacion();
    }
  });
}

function mostrarExplicacion() {
  console.log(`
--------------------------------------------------------------------------------
CASO DE USO REAL EN APLICACIONES WEB (Explicado en el video de la clase):
- Escenario: 
  Petición 1: Verificar si el usuario inició sesión -> Retorna { sessionId: "123" }
  Petición 2: Cargar datos del perfil -> Requiere 'sessionId' de la Petición 1.

- Con 'merge': 
  Ambas se lanzan concurrentemente. Si la Petición 2 responde primero por latencia,
  'sessionId' es NULL en ese momento y la aplicación lanza un error.

- Con 'concat' / encadenamiento reactivo: 
  Garantizamos que la Petición 1 complete primero, obtenga el 'sessionId', y
  únicamente después se dispare la Petición 2 con el ID válido.
--------------------------------------------------------------------------------
`);
}
