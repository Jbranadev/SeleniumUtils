package io.github.josecarlosbran.SeleniumUtils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static io.github.josecarlosbran.UtilidadesTest.Utilities.logParrafo;

public class SeleniumUtilsTest {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
    }

    @Test(testName = "Element Exist Google"
            , description = "Verifica que un elemento exista en la pagina de Google")
    public void elementExist() {
        logParrafo("Irá a la pagina principal de Google");
        driver.get("https://www.google.com");
        logParrafo("Verifica que el elemento 'textarea[name='q']' exista en la pagina");
        Assert.assertTrue(SeleniumUtils.elementExist(driver, driver, "textarea[name='q']"),
                "No fue posible encontrar el elemento 'textarea[name='q']' en la pagina," +
                        "valide el identificador del elemento");
    }

    @Test(testName = "SendKeys Element Search Google"
            , description = "Limpia el elemento de busqueda de Google",
            dependsOnMethods = "elementExist")
    public void sendKeysToElement() {
        logParrafo("Escribirá el texto indicado en la barra de busqueda de google");
        WebElement elemento=SeleniumUtils.getElementIfExist(driver, By.xpath("//*[@id=\"APjFqb\"]"));
        Assert.assertTrue(SeleniumUtils.sendKeysToElement(driver,elemento,"Prueba de escritura"));
    }

    @Test(testName = "Clear Element Search Google"
            , description = "Limpia el elemento de busqueda de Google",
            dependsOnMethods = "sendKeysToElement")
    public void clearElement() {
        logParrafo("Limpia el elemento de busqueda de Google");
        Assert.assertTrue(SeleniumUtils.clearElementIfExist(driver, driver, "textarea[name='q']"),
                "No fue posible limpiar el elemento 'textarea[name='q']' en la pagina," +
                        "valide el identificador del elemento");
    }

    @Test(testName = "Should Thread Sleep", description = "Debería dejar el hilo dormido por un momento")
    public void threadSleep() {
        logParrafo("Se debe de tener un tiempo dormido");
        long startTime = System.currentTimeMillis(); // Hora de inicio
        SeleniumUtils.threadslepp(5000);//Dormir el hilo
        long endTime = System.currentTimeMillis(); // Hora de fin
        long duration = endTime - startTime; // Calcular duración
        // Verifica que la duración está dentro de un rango aceptable (por ejemplo, +/- 100 ms)
        Assert.assertTrue(duration >= 5000 && duration <= 5100, "El método no durmió el hilo el tiempo esperado");
    }

    @Test(testName = "KeyPress Using Keys",
            description = "Verifica que se puede presionar una tecla utilizando el objeto Keys",
    dependsOnMethods = "elementExist")
    public void keyPressUsingKeys() {
        logParrafo("Se va a presionar la tecla ENTER utilizando Keys");
        WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"APjFqb\"]"));  // Encuentra la barra de búsqueda
        searchBox.click();  // Da click en la barra de búsqueda para activarla
        searchBox.sendKeys("Selenium WebDriver");  // Escribe algo en la barra de búsqueda

        // Presiona la tecla ENTER
        SeleniumUtils.keyPress(driver, Keys.ENTER);

        // Verifica que el título de la página cambió, indicando que se realizó la búsqueda
        Assert.assertTrue(driver.getTitle().contains("Selenium WebDriver"), "No se realizó la búsqueda");
    }

    @Test(testName = "KeyPress Using ASCII Code",
            description = "Verifica que se puede presionar una tecla utilizando un código ASCII",
    dependsOnMethods = "elementExist")
    public void keyPressUsingAsciiCode() {
        logParrafo("Se va a presionar la tecla 'A' utilizando su código ASCII");
        WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"APjFqb\"]"));  // Encuentra la barra de búsqueda
        searchBox.clear();  // Limpia la barra de búsqueda para comenzar con un campo vacío
        searchBox.click();
        // Presiona la tecla 'A' (código ASCII 65)
        SeleniumUtils.keyPress(driver, 65);

        // Verifica que la barra de búsqueda contiene la letra 'A'
        Assert.assertEquals(searchBox.getAttribute("value"), "A", "La tecla 'A' no fue presionada correctamente");
    }

    @Test(testName = "Fluent Wait Functionality",
            description = "Verifica que el objeto FluentWait funcione correctamente",dependsOnMethods = "elementExist")
    public void testGetFluentWait() {
        logParrafo("Se va a verificar que el objeto FluentWait funcione correctamente");

        int timeduration = 5000;  // 5 segundos de timeout
        int timerepetition = 500;  // 0.5 segundos de polling

        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, timeduration, timerepetition);

        // Prueba que el FluentWait realmente espera un elemento que existe
        WebElement searchBox = wait.until(driver -> driver.findElement(By.name("//*[@id=\"APjFqb\"]")));
        Assert.assertNotNull(searchBox, "El elemento no se encontró dentro del tiempo esperado");

        // Verifica que el elemento es habilitado y visible
        Assert.assertTrue(searchBox.isDisplayed() && searchBox.isEnabled(), "El elemento no está visible o habilitado");
    }





    @Test(testName = "Element Is Disabled - Null Element",
            description = "Verifica que un elemento nulo sea tratado como deshabilitado")
    public void testElementIsDisabled() {
        logParrafo("Se va a verificar que un elemento nulo sea tratado como deshabilitado");

        WebElement nullElement = SeleniumUtils.getElementIfExist(driver,By.xpath("//*[@id=\"xpathFalso\"]"));

        boolean isDisabled = SeleniumUtils.elementIsDisabled(nullElement);

        // Verifica que el método retorne verdadero para un elemento nulo
        Assert.assertTrue(isDisabled, "El elemento nulo no fue reconocido como deshabilitado");
    }
}


