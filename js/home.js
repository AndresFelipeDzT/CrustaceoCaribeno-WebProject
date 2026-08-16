/**
 * CRUSTÁCEO CARIBEÑO - JAVASCRIPT ESPECÍFICO DEL HOME (home.js)
 * Interactividad de la Landing Page, animaciones y redirecciones.
 * Estándares: ES6+, sin 'var', comparación estricta (===), funciones const.
 */

// Inicialización de comportamientos de la página principal
const inicializarHome = () => {
  // Manejo de scroll suave para enlaces internos
  const enlacesInternos = document.querySelectorAll('a[href^="#"]');
  enlacesInternos.forEach((enlace) => {
    enlace.addEventListener("click", (evento) => {
      const objetivoId = enlace.getAttribute("href");
      if (objetivoId && objetivoId !== "#") {
        const elementoObjetivo = document.querySelector(objetivoId);
        if (elementoObjetivo) {
          evento.preventDefault();
          elementoObjetivo.scrollIntoView({
            behavior: "smooth",
            block: "start",
          });
        }
      }
    });
  });
};

document.addEventListener("DOMContentLoaded", () => {
  inicializarHome();
});
