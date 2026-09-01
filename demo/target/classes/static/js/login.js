/**
 * Crustáceo Caribeño - Script de validación para la pantalla de Login.
 */
document.addEventListener("DOMContentLoaded", () => {
  const formLogin = document.getElementById("formLogin");
  const txtNombre = document.getElementById("txtNombre");
  const txtPassword = document.getElementById("txtPassword");
  const alertaJs = document.getElementById("alertaJs");

  if (!formLogin) return;

  formLogin.addEventListener("submit", (e) => {
    alertaJs.classList.add("d-none");
    alertaJs.textContent = "";

    const nombre = txtNombre.value.trim();
    const password = txtPassword.value.trim();

    if (nombre === "") {
      e.preventDefault();
      mostrarAlerta("Por favor ingresa tu nombre de usuario.");
      txtNombre.focus();
      return;
    }

    if (password === "") {
      e.preventDefault();
      mostrarAlerta("Por favor ingresa tu contraseña.");
      txtPassword.focus();
      return;
    }

    if (password.length < 3) {
      e.preventDefault();
      mostrarAlerta("La contraseña debe tener al menos 3 caracteres.");
      txtPassword.focus();
      return;
    }
  });

  function mostrarAlerta(mensaje) {
    alertaJs.textContent = mensaje;
    alertaJs.classList.remove("d-none");
  }
});
