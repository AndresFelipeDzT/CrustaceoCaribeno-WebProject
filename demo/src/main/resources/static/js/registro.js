/**
 * Crustáceo Caribeño - Script de validación para la pantalla de Signup / Registro.
 */
document.addEventListener("DOMContentLoaded", () => {
  const formRegistro = document.getElementById("formRegistro");
  const txtNombreCompleto = document.getElementById("txtNombreCompleto");
  const txtCorreo = document.getElementById("txtCorreo");
  const txtTelefono = document.getElementById("txtTelefono");
  const txtPassword = document.getElementById("txtPassword");
  const alertaJs = document.getElementById("alertaJs");

  if (!formRegistro) return;

  const validateEmail = (email) => {
    return String(email)
      .toLowerCase()
      .match(
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      );
  };

  formRegistro.addEventListener("submit", (e) => {
    alertaJs.classList.add("d-none");
    alertaJs.textContent = "";

    const nombre = txtNombreCompleto.value.trim();
    const correo = txtCorreo.value.trim();
    const telefono = txtTelefono.value.trim();
    const password = txtPassword.value.trim();

    if (nombre.length < 2) {
      e.preventDefault();
      mostrarAlerta("Por favor ingresa tu nombre completo.");
      txtNombreCompleto.focus();
      return;
    }

    if (!validateEmail(correo)) {
      e.preventDefault();
      mostrarAlerta("Por favor ingresa un correo electrónico válido.");
      txtCorreo.focus();
      return;
    }

    if (telefono.length < 7) {
      e.preventDefault();
      mostrarAlerta("Por favor ingresa un número de teléfono válido.");
      txtTelefono.focus();
      return;
    }

    if (password.length < 4) {
      e.preventDefault();
      mostrarAlerta("La contraseña debe tener al menos 4 caracteres.");
      txtPassword.focus();
      return;
    }
  });

  function mostrarAlerta(mensaje) {
    alertaJs.textContent = mensaje;
    alertaJs.classList.remove("d-none");
  }
});
