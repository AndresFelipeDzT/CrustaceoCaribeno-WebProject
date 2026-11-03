package com.example.demo.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("default")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:e2e;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class PedidoE2ETest {

    private static final String FRONTEND_URL = "http://localhost:4200";
    private static final String ADMIN_USER = "admin.general";
    private static final String ADMIN_PASSWORD = "admin123";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu", "--window-size=1440,1000");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void loginAdministrador_redirigeAlListadoDePedidos() throws InterruptedException {
        iniciarSesionComoAdministrador();

        assertThat(driver.getCurrentUrl()).contains("/pedidos");
        assertThat(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".tabla-contenedor table"))).isDisplayed()).isTrue();
        Thread.sleep(10000);
    }

    @Test
    void abrirDetalleDesdeListado_muestraInformacionDelPedido() throws InterruptedException {
        driver.get(FRONTEND_URL + "/pedidos?idAdministrador=1");
        String textoTabla = wait.until(driver -> {
            try {
                WebElement tabla = driver.findElement(By.cssSelector(".tabla-contenedor table"));
                String texto = tabla.getText();
                return texto.contains("En preparación") || texto.contains("En camino") ? texto : null;
            } catch (StaleElementReferenceException exception) {
                return null;
            }
        });

        assertThat(textoTabla).containsAnyOf("En preparación", "En camino");

        WebElement botonDetalles = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".btn-detalle")));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", botonDetalles);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botonDetalles);

        WebElement detalle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".productos-detalle")));
        assertThat(detalle.getText()).isNotBlank();
        Thread.sleep(10000);
    }

    @Test
    void actualizarEstadoDePedido_muestraMensajeDeConfirmacion() throws InterruptedException {
        abrirPedidoEnPreparacion();

        WebElement estado = wait.until(ExpectedConditions.elementToBeClickable(By.id("estado")));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = 'Entregado';"
                        + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                estado);
        Thread.sleep(10000);
        WebElement actualizar = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".btn-actualizar")));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", actualizar);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", actualizar);

        driver.get(FRONTEND_URL + "/pedidos/3?idAdministrador=1");
        Select estadoActualizado = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("estado"))));
        assertThat(estadoActualizado.getFirstSelectedOption().getText()).isEqualTo("Entregado");
        Thread.sleep(10000);
    }

    @Test
    void pedidoInexistente_muestraMensajeDeError() throws InterruptedException {
        driver.get(FRONTEND_URL + "/pedidos/999999?idAdministrador=1");

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".pedidos-alerta.error[role='alert']")));

        assertThat(error.getText()).contains("El pedido con ID 999999 no fue encontrado.");
        Thread.sleep(10000);
    }

    private void iniciarSesionComoAdministrador() {
        driver.get(FRONTEND_URL + "/admin");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("correo")))
                .sendKeys(ADMIN_USER);
        driver.findElement(By.id("password")).sendKeys(ADMIN_PASSWORD);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/pedidos"));
    }

    private void abrirPedidoEnPreparacion() {
        driver.get(FRONTEND_URL + "/pedidos/3?idAdministrador=1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("estado")));
    }
}
