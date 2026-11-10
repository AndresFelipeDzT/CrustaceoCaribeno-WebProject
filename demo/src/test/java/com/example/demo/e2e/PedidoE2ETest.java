package com.example.demo.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PedidoE2ETest {

    private static final String FRONTEND_URL = "http://localhost:4200";
    private static final String ADMIN_USER = "admin.general";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final long PAUSA_VISUAL_MS = 700;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-notifications", "--disable-extensions");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void pausarParaVer() {
        try {
            Thread.sleep(PAUSA_VISUAL_MS);
        } catch (InterruptedException excepcion) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("La pausa visual fue interrumpida.", excepcion);
        }
    }

    private void escribirCampo(By localizador, String texto) {
        WebElement campo = wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
        campo.clear();
        campo.sendKeys(texto);
        wait.until(d -> texto.equals(d.findElement(localizador).getDomProperty("value")));
        pausarParaVer();
    }

    private void hacerClick(WebElement elemento) {
        new Actions(driver).scrollToElement(elemento).perform();
        wait.until(ExpectedConditions.elementToBeClickable(elemento));
        elemento.click();
        pausarParaVer();
    }

    @Test
    void administradorCreaProductoYVerificaAdicionalesEnElMenu() {
        // Comprueba la página inicial y abre el acceso administrativo.
        driver.get(FRONTEND_URL);
        wait.until(ExpectedConditions.titleContains("Crustáceo Caribeño"));
        assertThat(driver.getTitle()).contains("Crustáceo Caribeño");
        driver.get(FRONTEND_URL + "/admin");

        // Verifica que el acceso rechace una contraseña incorrecta y acepte la válida.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("correo")));
        escribirCampo(By.id("correo"), ADMIN_USER);
        escribirCampo(By.id("password"), "incorrecta");
        hacerClick(driver.findElement(By.className("btn-ingresar")));

        WebElement errorLogin = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("error-message")));
        assertThat(errorLogin.getText()).isNotBlank();

        escribirCampo(By.id("correo"), ADMIN_USER);
        escribirCampo(By.id("password"), ADMIN_PASSWORD);
        hacerClick(driver.findElement(By.className("btn-ingresar")));
        wait.until(ExpectedConditions.urlContains("/pedidos"));

        String administradorId = driver.getCurrentUrl()
                .replaceAll(".*idAdministrador=", "").replaceAll("&.*", "");
        String nombreProducto = "Limonada de prueba";
        String primerAdicional = "Extra de coco de prueba";
        String segundoAdicional = "Extra de limón de prueba";

        // Crea una bebida y confirma que aparezca en la tabla de productos.
        driver.get(FRONTEND_URL + "/comidas/tabla/add?idAdministrador=" + administradorId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nombre")));
        wait.until(d -> d.findElements(By.cssSelector("#categoria option")).size() > 1);
        escribirCampo(By.id("nombre"), nombreProducto);
        escribirCampo(By.id("descripcion"), "Producto creado para la prueba E2E.");
        escribirCampo(By.id("precio"), "23000");
        Select categoriaProducto = new Select(driver.findElement(By.id("categoria")));
        categoriaProducto.selectByVisibleText("Bebida");
        pausarParaVer();
        assertThat(categoriaProducto.getFirstSelectedOption().getText()).isEqualTo("Bebida");
        hacerClick(driver.findElement(By.className("btn-enviar")));

        wait.until(d -> URI.create(d.getCurrentUrl()).getPath().equals("/comidas/tabla")
                || !d.findElements(By.className("alert-danger")).isEmpty()
                || !d.findElements(By.className("error")).isEmpty());
        String rutaActual = URI.create(driver.getCurrentUrl()).getPath();
        if (!rutaActual.equals("/comidas/tabla")) {
            assertThat(driver.findElement(By.className("pagina-agregar")).getText())
                    .doesNotContain("Escribe un nombre válido.", "La descripción es obligatoria.");
        }
        assertThat(rutaActual).isEqualTo("/comidas/tabla");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("tabla-platillos"), nombreProducto));

        // Asocia el primer adicional a la categoría Bebida.
        driver.get(FRONTEND_URL + "/adicionales/tabla/add?idAdministrador=" + administradorId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nombre")));
        wait.until(d -> !d.findElements(By.className("categoria-opcion")).isEmpty());
        escribirCampo(By.id("nombre"), primerAdicional);
        escribirCampo(By.id("precio"), "1800");
        List<WebElement> categorias = driver.findElements(By.className("categoria-opcion"));
        for (WebElement categoria : categorias) {
            if (categoria.getText().contains("Bebida")) {
                WebElement checkbox = categoria.findElement(By.className("categoria-checkbox"));
                hacerClick(checkbox);
            }
        }
        hacerClick(driver.findElement(By.className("btn-guardar")));
        wait.until(d -> URI.create(d.getCurrentUrl()).getPath().equals("/adicionales/tabla"));

        // Abre el detalle de la bebida y comprueba sus adicionales disponibles.
        String pestanaAdministrador = driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB);
        String pestanaMenu = driver.getWindowHandle();
        driver.get(FRONTEND_URL + "/comidas/tarjetas");

        List<WebElement> tarjetas = wait.until(d -> {
            List<WebElement> encontradas = d.findElements(By.className("tarjeta-platillo"));
            return encontradas.isEmpty() ? null : encontradas;
        });
        for (WebElement tarjeta : tarjetas) {
            if (tarjeta.getText().contains(nombreProducto)) {
                WebElement detalle = tarjeta.findElement(By.className("btn-ver-mas"));
                hacerClick(detalle);
                break;
            }
        }
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("tarjeta-plato"), nombreProducto));
        List<WebElement> adicionales = wait.until(d -> {
            List<WebElement> encontrados = d.findElements(By.className("opcion-acompanamiento"));
            return encontrados.size() >= 2 ? encontrados : null;
        });
        assertThat(adicionales.get(0).getText()).contains("Shot de Ron Caribeño");
        assertThat(adicionales.stream().map(WebElement::getText).toList())
                .anyMatch(nombre -> nombre.contains(primerAdicional));

        // Añade otro adicional y verifica que también se muestre en el menú.
        driver.switchTo().window(pestanaAdministrador);
        driver.get(FRONTEND_URL + "/adicionales/tabla/add?idAdministrador=" + administradorId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nombre")));
        wait.until(d -> !d.findElements(By.className("categoria-opcion")).isEmpty());
        escribirCampo(By.id("nombre"), segundoAdicional);
        escribirCampo(By.id("precio"), "1900");
        categorias = driver.findElements(By.className("categoria-opcion"));
        for (WebElement categoria : categorias) {
            if (categoria.getText().contains("Bebida")) {
                WebElement checkbox = categoria.findElement(By.className("categoria-checkbox"));
                hacerClick(checkbox);
            }
        }
        hacerClick(driver.findElement(By.className("btn-guardar")));
        wait.until(d -> URI.create(d.getCurrentUrl()).getPath().equals("/adicionales/tabla"));

        driver.switchTo().window(pestanaMenu);
        driver.navigate().refresh();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("tarjeta-plato"), nombreProducto));
        adicionales = wait.until(d -> {
            List<WebElement> encontrados = d.findElements(By.className("opcion-acompanamiento"));
            return encontrados.size() >= 3 ? encontrados : null;
        });
        List<String> nombresAdicionales = adicionales.stream().map(WebElement::getText).toList();
        assertThat(nombresAdicionales).anyMatch(nombre -> nombre.contains("Shot de Ron Caribeño"));
        assertThat(nombresAdicionales).anyMatch(nombre -> nombre.contains(primerAdicional));
        assertThat(nombresAdicionales).anyMatch(nombre -> nombre.contains(segundoAdicional));
    }

    @Test
    void clienteRealizaPedidoOperadorActualizaEstadoYClienteRevisaHistorial() {
        // Inicia sesión como cliente y agrega dos productos al carrito.
        driver.get(FRONTEND_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("correo")));
        escribirCampo(By.id("correo"), "Juan.Rodriguez@gmail.com");
        escribirCampo(By.id("password"), "1234");
        hacerClick(driver.findElement(By.className("btn-ingresar")));
        wait.until(ExpectedConditions.urlContains("/comidas/tarjetas"));

        String clienteId = driver.getCurrentUrl().replaceAll(".*[?&]id=", "").replaceAll("&.*", "");
        List<WebElement> tarjetas = wait.until(d -> {
            List<WebElement> encontradas = d.findElements(By.className("tarjeta-platillo"));
            return encontradas.size() >= 2 ? encontradas : null;
        });
        String primerProducto = tarjetas.get(0).findElement(By.className("tarjeta-nombre")).getText();
        String segundoProducto = tarjetas.get(1).findElement(By.className("tarjeta-nombre")).getText();

        WebElement agregar = tarjetas.get(0).findElement(By.className("btn-agregar-carrito"));
        hacerClick(agregar);
        wait.until(ExpectedConditions.urlContains("/carrito"));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("carrito-pagina"), primerProducto));

        driver.get(FRONTEND_URL + "/comidas/tarjetas?id=" + clienteId);
        tarjetas = wait.until(d -> {
            List<WebElement> encontradas = d.findElements(By.className("tarjeta-platillo"));
            return encontradas.size() >= 2 ? encontradas : null;
        });
        for (WebElement tarjeta : tarjetas) {
            if (tarjeta.getText().contains(segundoProducto)) {
                agregar = tarjeta.findElement(By.className("btn-agregar-carrito"));
                hacerClick(agregar);
                break;
            }
        }
        wait.until(ExpectedConditions.urlContains("/carrito"));
        wait.until(d -> d.findElements(By.className("carrito-item")).size() == 2);

        // Selecciona adicionales y valida subtotales y total del pedido.
        List<String> adicionalesSeleccionados = new ArrayList<>();
        for (int indiceItem = 0; indiceItem < 2; indiceItem++) {
            List<WebElement> opciones = driver.findElements(By.className("carrito-item"))
                    .get(indiceItem).findElements(By.className("adicional-opcion"));
            assertThat(opciones).hasSizeGreaterThanOrEqualTo(2);
            for (int indiceAdicional = 0; indiceAdicional < 2; indiceAdicional++) {
                WebElement opcion = driver.findElements(By.className("carrito-item"))
                        .get(indiceItem).findElements(By.className("adicional-opcion"))
                        .get(indiceAdicional);
                adicionalesSeleccionados.add(opcion.getText().split("\\R")[0]);
                WebElement checkbox = opcion.findElement(By.className("adicional-checkbox"));
                hacerClick(checkbox);
                final int itemActual = indiceItem;
                final int adicionalActual = indiceAdicional;
                wait.until(d -> d.findElements(By.className("carrito-item"))
                        .get(itemActual).findElements(By.className("adicional-opcion"))
                        .get(adicionalActual).findElement(By.className("adicional-checkbox")).isSelected());
            }
        }

        long sumaSubtotales = 0;
        for (WebElement item : driver.findElements(By.className("carrito-item"))) {
            String precioTexto = item.findElement(By.className("carrito-item-info"))
                    .findElement(By.className("carrito-producto-precio")).getText();
            long precioProducto = Long.parseLong(precioTexto.replaceAll("[^0-9]", ""));
            int cantidad = Integer.parseInt(item.findElement(By.className("cantidad-valor")).getText());
            long precioAdicionales = item.findElements(By.className("adicional-opcion")).stream()
                    .filter(label -> label.findElement(By.className("adicional-checkbox")).isSelected())
                    .mapToLong(label -> Long.parseLong(label.findElement(By.className("adicional-precio"))
                            .getText().replaceAll("[^0-9]", "")))
                    .sum();
            long subtotalEsperado = cantidad * (precioProducto + precioAdicionales);
            long subtotalMostrado = Long.parseLong(item.findElement(By.className("carrito-item-total"))
                    .getText().replaceAll("[^0-9]", ""));
            assertThat(subtotalMostrado).isEqualTo(subtotalEsperado);
            sumaSubtotales += subtotalEsperado;
        }
        long subtotal = Long.parseLong(driver.findElement(By.className("carrito-resumen"))
                .findElements(By.className("resumen-linea")).get(0).getText().replaceAll("[^0-9]", ""));
        long domicilio = Long.parseLong(driver.findElement(By.className("carrito-resumen"))
                .findElements(By.className("resumen-linea")).get(1).getText().replaceAll("[^0-9]", ""));
        long total = Long.parseLong(driver.findElement(By.className("resumen-total"))
                .getText().replaceAll("[^0-9]", ""));
        assertThat(subtotal).isEqualTo(sumaSubtotales);
        assertThat(total).isEqualTo(subtotal + domicilio);

        // Confirma el pedido y revisa que aparezca en el historial del cliente.
        WebElement confirmarPedido = driver.findElement(By.className("btn-realizar-pedido"));
        hacerClick(confirmarPedido);
        WebElement mensajeConfirmacion = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("carrito-alerta")));
        assertThat(mensajeConfirmacion.getText()).contains("Pedido confirmado correctamente.");
        driver.get(FRONTEND_URL + "/perfil?id=" + clienteId);
        WebElement historialActivo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("orders-section")));
        WebElement detalleCliente = historialActivo.findElement(By.className("order-detail-button"));
        hacerClick(detalleCliente);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("order-items")));
        String encabezadoPedido = historialActivo.findElement(By.className("order-card"))
                .findElement(By.className("order-summary")).getText();
        String pedidoId = encabezadoPedido.replaceAll("(?s).*Pedido #(\\d+).*", "$1");
        assertThat(historialActivo.getText()).contains(primerProducto, segundoProducto);

        // Inicia sesión como operador y verifica el contenido del pedido.
        String pestanaCliente = driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(FRONTEND_URL + "/operador");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("correo")));
        escribirCampo(By.id("correo"), "operador.principal");
        escribirCampo(By.id("password"), "operador123");
        hacerClick(driver.findElement(By.className("btn-ingresar")));
        wait.until(ExpectedConditions.urlContains("/operador/pedidos"));
        String operadorId = driver.getCurrentUrl().replaceAll(".*idOperador=", "").replaceAll("&.*", "");

        WebElement pedidoOperador = wait.until(d -> d.findElements(By.className("btn-detalle")).stream()
                .filter(boton -> boton.getAttribute("href").contains("/" + pedidoId + "?"))
                .findFirst().orElse(null));
        assertThat(pedidoOperador.getAttribute("href")).contains("idOperador=" + operadorId);
        hacerClick(pedidoOperador);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("estado")));
        WebElement detallePedido = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("productos-detalle")));
        assertThat(detallePedido.getText()).contains(primerProducto, segundoProducto);
        adicionalesSeleccionados.forEach(adicional ->
                assertThat(detallePedido.getText()).contains(adicional));

        // Avanza el pedido a preparación y confirma el estado desde el perfil del cliente.
        new Select(driver.findElement(By.id("estado"))).selectByIndex(1);
        pausarParaVer();
        hacerClick(driver.findElement(By.className("btn-actualizar")));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("detalle-estado-actual"), "En preparación"));

        driver.switchTo().window(pestanaCliente);
        driver.navigate().refresh();
        historialActivo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("orders-section")));
        wait.until(ExpectedConditions.textToBePresentInElement(historialActivo, "En preparación"));

        String pestanaOperador = driver.getWindowHandles().stream()
                .filter(pestana -> !pestana.equals(pestanaCliente)).findFirst().orElseThrow();
        driver.switchTo().window(pestanaOperador);
        new Select(driver.findElement(By.id("estado"))).selectByIndex(1);
        pausarParaVer();
        Select domiciliario = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("domiciliario"))));
        if (domiciliario.getFirstSelectedOption().getAttribute("value").isBlank()) {
            domiciliario.selectByIndex(1);
            pausarParaVer();
        }
        // Asigna un domiciliario y avanza el pedido hasta entregado.
        hacerClick(driver.findElement(By.className("btn-actualizar")));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("detalle-estado-actual"), "Enviado"));

        new Select(driver.findElement(By.id("estado"))).selectByIndex(1);
        pausarParaVer();
        hacerClick(driver.findElement(By.className("btn-actualizar")));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("detalle-estado-actual"), "En camino"));
        new Select(driver.findElement(By.id("estado"))).selectByIndex(1);
        pausarParaVer();
        hacerClick(driver.findElement(By.className("btn-actualizar")));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("detalle-estado-actual"), "Entregado"));

        // Verifica que el pedido entregado siga disponible en el historial del cliente.
        driver.switchTo().window(pestanaCliente);
        driver.navigate().refresh();
        List<WebElement> seccionesHistorial = wait.until(d -> {
            List<WebElement> secciones = d.findElements(By.className("orders-section"));
            return secciones.size() >= 2 ? secciones : null;
        });
        WebElement pedidosCompletados = seccionesHistorial.get(1);
        wait.until(ExpectedConditions.textToBePresentInElement(pedidosCompletados, "Entregado"));
        WebElement pedidoCompletado = null;
        for (WebElement pedido : pedidosCompletados.findElements(By.className("order-card"))) {
            if (pedido.getText().contains("Pedido #" + pedidoId)) {
                pedidoCompletado = pedido;
                break;
            }
        }
        assertThat(pedidoCompletado).isNotNull();
        assertThat(pedidoCompletado.getText()).contains("Entregado");
        hacerClick(pedidoCompletado.findElement(By.className("order-detail-button")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("order-items")));
        assertThat(pedidoCompletado.getText()).contains(primerProducto, segundoProducto);
    }
}
