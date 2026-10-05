// =======================================================================
// SEMANA 9 - DESARROLLO WEB: REPASO DE FUNCIONES EN JAVASCRIPT
// =======================================================================

// 1. Declaración tradicional con la palabra clave 'function'
function sumar(a, b) {
  return a + b;
}

console.log("Resultado suma inicial:", sumar(1, 2)); // 3

// Problema en JavaScript: Las funciones declaradas con 'function' pueden ser
// redefinidas accidentalmente en cualquier parte del código (hoisting y sobreescritura)
function sumar(a, b) {
  return a + b + 2;
}

console.log("Resultado tras redefinir sumar accidentalmente:", sumar(1, 2)); // 5

// 2. Buena práctica: Asignar a una constante para evitar redefiniciones
const restar = function(a, b) {
  return a - b;
};

console.log("Resultado restar:", restar(5, 2)); // 3
// restar = function(a, b) { ... }; // ERROR: Assignment to constant variable.

// 3. Arrow Functions (Funciones flecha)
// Desaparece la palabra clave 'function'
const multiplicar = (a, b) => {
  return a * b;
};

console.log("Resultado multiplicar:", multiplicar(3, 4)); // 12

// Regla 1: Retorno implícito en una sola línea (no requiere llaves ni 'return')
const dividir = (a, b) => a / b;
console.log("Resultado dividir:", dividir(10, 2)); // 5

// Regla 2: Un único parámetro no requiere paréntesis obligatorios
const duplicar = a => a * 2;
console.log("Resultado duplicar:", duplicar(8)); // 16

// Regla 3: Cero parámetros SÍ requieren paréntesis obligatorios
const imprimirMensaje = () => console.log("Hola desde Arrow Function sin parámetros");
imprimirMensaje();

// 4. Funciones de orden superior (pasar funciones como parámetros)
const operacionFinal = (operacion1, operacion2, a, b) => {
  const resultado1 = operacion1(a, b);
  const resultadoFinal = operacion2(resultado1, 10);
  return resultadoFinal;
};

console.log("Operación pasando funciones:", operacionFinal(sumar, restar, 5, 5));

// 5. Funciones anónimas pasadas en el momento de la llamada
// Muy comunes en eventos de UI (ej. botones, listeners, suscripciones reactivas)
const operacion3 = (callback, a, b) => {
  return callback(a, b);
};

// Pasamos una arrow function anónima que calcula la potencia (a ** b)
const resultadoPotencia = operacion3((x, y) => x ** y, 2, 3);
console.log("Resultado potencia (función anónima):", resultadoPotencia); // 8
