package io.github.josecarlosbran.SeleniumUtils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
        Assert.assertTrue(SeleniumUtils.ElementoExistente(driver, driver, "textarea[name='q']"),
                "No fue posible encontrar el elemento 'textarea[name='q']' en la pagina," +
                        "valide el identificador del elemento");
    }

    @Test(testName = "SendKeys Element Search Google"
            , description = "Limpia el elemento de busqueda de Google",
            dependsOnMethods = "elementExist")
    public void sendKeysToElement() {
    }

    @Test(testName = "Clear Element Search Google"
            , description = "Limpia el elemento de busqueda de Google",
            dependsOnMethods = "sendKeysToElement")
    public void clearElement() {
        logParrafo("Limpia el elemento de busqueda de Google");
        Assert.assertTrue(SeleniumUtils.LimpiarElementoExistente(driver, driver, "textarea[name='q']"),
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
}
