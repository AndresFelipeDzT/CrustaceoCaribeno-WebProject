// =======================================================================
// SEMANA 9 - DESARROLLO WEB: DIFERENCIA CLAVE DE 'this' EN ARROW FUNCTIONS
// =======================================================================

// Demostración de cómo se comporta el contexto 'this' dentro de métodos y clases

class Animal {
  constructor(nombre) {
    this.nombre = nombre;
  }

  // Función tradicional interna dentro de un método
  // 'this' pierde el contexto del objeto y apunta al contexto global/undefined
  printNameNormal() {
    const funcionInterna = function() {
      // En modo estricto / Node, 'this' es undefined
      try {
        console.log("printNameNormal:", this.nombre);
      } catch (e) {
        console.log("printNameNormal error: 'this' no tiene acceso a 'nombre' porque perdió el contexto.");
      }
    };
    funcionInterna();
  }

  // Arrow function interna dentro de un método
  // Las arrow functions NO crean su propio 'this', sino que heredan el 'this' léxico de la clase
  printNameArrow() {
    const funcionInternaArrow = () => {
      console.log("printNameArrow:", this.nombre); // Funciona correctamente
    };
    funcionInternaArrow();
  }
}

const miPerro = new Animal("perro");

console.log("--- Probando función tradicional ---");
miPerro.printNameNormal();

console.log("\n--- Probando Arrow function ---");
miPerro.printNameArrow();
