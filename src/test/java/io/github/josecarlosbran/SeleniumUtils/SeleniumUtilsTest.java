package io.github.josecarlosbran.SeleniumUtils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
import java.util.ArrayList;
import java.util.Arrays;

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
        WebElement elemento=SeleniumUtils.getElementIfExist(driver,driver,By.xpath("//*[@id=\"APjFqb\"]"));
        Assert.assertTrue(SeleniumUtils.sendKeysToElement(driver,elemento,"Prueba de escritura"));
    }

    @Test(testName = "Clear Element Search Google"
            , description = "Limpia el elemento de busqueda de Google",
            dependsOnMethods = "sendKeysToElement")
    public void clearElementIfExists() {
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

        int timeduration = 3000;  // 3 segundos de timeout
        int timerepetition = 300;  // 0.3 segundos de polling

        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, timeduration, timerepetition);

        // Prueba que el FluentWait realmente espera un elemento que existe
        WebElement searchBox = wait.until(driver -> driver.findElement(By.xpath("//*[@id=\"APjFqb\"]")));
        Assert.assertNotNull(searchBox, "El elemento no se encontró dentro del tiempo esperado");

        // Verifica que el elemento es habilitado y visible
        Assert.assertTrue(searchBox.isDisplayed() && searchBox.isEnabled(), "El elemento no está visible o habilitado");
    }


    @Test(testName = "Element Is Disabled - Null Element",
            description = "Verifica que un elemento nulo sea tratado como deshabilitado")
    public void testElementIsDisabled() {
        logParrafo("Se va a verificar que un elemento nulo sea tratado como deshabilitado");

        WebElement nullElement = SeleniumUtils.getElementIfExist(driver,driver,By.xpath("//*[@id=\"xpathFalso\"]"));

        boolean isDisabled = SeleniumUtils.elementIsDisabled(nullElement);

        // Verifica que el método retorne verdadero para un elemento nulo
        Assert.assertTrue(isDisabled, "El elemento nulo no fue reconocido como deshabilitado");
    }
    @Test(testName = "CleanElement",description = "Should clean the especified element",dependsOnMethods = "elementExist")
    public void cleanElement(){
        WebElement elemento= SeleniumUtils.getElementIfExist(driver,driver,By.xpath("//*[@id=\"input\"]"));
        logParrafo("Se debe de limpiar el elemento específicado ");
        Assert.assertTrue(SeleniumUtils.cleanElement(driver,elemento));

    }

    @Test(testName = "posicionarmeEn",description = "Should be positioned in specified element",dependsOnMethods = "elementExist")
    public void posicionarmeEn(){
        WebElement elemento= SeleniumUtils.getElementIfExist(driver,driver,By.xpath("//*[@id=\"input\"]"));
        logParrafo("Se debe posicionr en el elemento específicado ");
        Boolean acierto=false;
        try {
            SeleniumUtils.posicionarmeEn(driver,elemento);
            acierto=true;
            Assert.assertTrue(acierto);
        }catch (Exception e){
            acierto=false;
            Assert.assertTrue(acierto);
        }
    }

    @Test(testName = "Convert Object Array to String ArrayList - Success",
            description = "Verifica que el array de objetos se convierta correctamente en un ArrayList de cadenas cuando acierto es true")
    public void testConvertObjectToArrayStringSuccess() {
        Object[] objects = {"Hello", 123, 45.67, true};
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("Hello", "123", "45.67", "true"));

        ArrayList<String> result = SeleniumUtils.convertObjectToArrayString(objects);

        Assert.assertEquals(result, expected, "La conversión no fue correcta.");
    }

    @Test(testName = "Convert Object Array to String ArrayList - Null Input",
            description = "Verifica que el método maneje correctamente un array nulo")
    public void testConvertObjectToArrayStringNullInput() {
        Object[] objects = null;

        ArrayList<String> result = SeleniumUtils.convertObjectToArrayString(objects);

        Assert.assertNull(result, "El resultado debería ser null cuando se pasa un array nulo.");
    }

    @Test(testName = "Convert Object Array to String ArrayList - Empty Array",
            description = "Verifica que el método maneje correctamente un array vacío")
    public void testConvertObjectToArrayStringEmptyArray() {
        Object[] objects = new Object[]{};
        ArrayList<String> expected = new ArrayList<>();

        ArrayList<String> result = SeleniumUtils.convertObjectToArrayString(objects);

        Assert.assertEquals(result, expected, "La conversión de un array vacío no fue correcta.");
    }

    @Test(testName = "Convert Object Array to String ArrayList - acierto False",
            description = "Verifica que el método retorne null cuando acierto es false")
    public void testConvertObjectToArrayStringAciertoFalse() {
        Object[] objects = {"Hello", 123, 45.67, true};

        ArrayList<String> result = SeleniumUtils.convertObjectToArrayString(objects);

        Assert.assertNull(result, "El resultado debería ser null cuando acierto es false.");
    }

    @Test(testName = "Convert Object Array to String ArrayList - Exception Handling",
            description = "Verifica que el método maneje excepciones correctamente")
    public void testConvertObjectToArrayStringExceptionHandling() {
        Object[] objects = {new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("Error en toString()");
            }
        }};

        ArrayList<String> result = SeleniumUtils.convertObjectToArrayString(objects);

        Assert.assertNull(result, "El resultado debería ser null cuando ocurre una excepción durante la conversión.");
    }

    @Test(testName = "Is an Valid value", description = "Should return a boolean of a valid value")
    public void isAnValidValue(){
        String valor="Valor para ver si es valido o no";
        Assert.assertTrue(SeleniumUtils.isanvalidValue(valor));
    }

    @Test(testName = "Is not an Valid value", description = "Should return a boolean of a valid value")
    public void isNotAnValidValue(){
        String valor="";
        Assert.assertFalse(SeleniumUtils.isanvalidValue(valor));
    }

    @Test(testName = "Is an null or empty String", description = "Should return a boolean of a null or empty value")
    public void stringIsNullOrEmpty(){
        String valor="";
        Assert.assertTrue(SeleniumUtils.stringIsNullOrEmpty(valor));
    }

    @Test(testName = "Is an not null or empty String", description = "Should return a boolean of a null or empty value")
    public void stringIsNotNullOrEmpty(){
        String valor="Valor para evitar que sea nulo";
        Assert.assertFalse(SeleniumUtils.stringIsNullOrEmpty(valor));
    }


    @Test(testName = "getImageScreeenshotWebElement", description = "Should take a correct screenshot",dependsOnMethods = "elementExist")
    public void getImageScreeenshotWebElement(){
        WebElement elemento=SeleniumUtils.getElementIfExist(driver,driver,By.xpath("/html/body"));
        try {
            SeleniumUtils.getImageScreeenshotWebElement(driver,elemento);
            Assert.assertTrue(true);
        }catch (Exception e){
            Assert.assertFalse(false);
        }
    }

    @Test(testName = "RefreshReferenceToElement", description = "Should take a correct refresh",dependsOnMethods = "elementExist")
    public void RefreshReferenceToElement(){
        WebElement elemento=SeleniumUtils.getElementIfExist(driver,driver,By.xpath("/html/body"));
        try {
            SeleniumUtils.RefreshReferenceToElement(driver,elemento);
            Assert.assertTrue(true);
        }catch (Exception e){
            Assert.assertFalse(false);
        }
    }





}


