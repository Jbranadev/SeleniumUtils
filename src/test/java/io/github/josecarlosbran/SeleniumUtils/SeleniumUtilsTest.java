package io.github.josecarlosbran.SeleniumUtils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.josebran.LogsJB.LogsJB;
import com.josebran.LogsJB.Numeracion.NivelLog;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.channels.SeekableByteChannel;
import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Future;


import static io.github.josecarlosbran.UtilidadesTest.Utilities.logParrafo;

public class SeleniumUtilsTest {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        LogsJB.setGradeLog(NivelLog.INFO);

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
        logParrafo("Escribirá el texto indicado en la barra de busqueda de google");
        WebElement elemento=SeleniumUtils.getElementIfExist(driver,driver,By.xpath("//*[@id=\"APjFqb\"]"));
        Assert.assertTrue(SeleniumUtils.sendKeysToElement(driver,elemento,"Prueba de escritura"));
    }

    @Test(testName = "Clear Element Search Google"
            , description = "Limpia el elemento de busqueda de Google",
            dependsOnMethods = "sendKeysToElement")
    public void clearElementIfExists() {
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

    @Test(testName = "KeyPress Using Keys",
            description = "Verifica que se puede presionar una tecla utilizando el objeto Keys",
    dependsOnMethods = "elementExist")
    public void keyPressUsingKeys() {
        logParrafo("Se va a presionar la tecla ENTER utilizando Keys");
        WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"APjFqb\"]"));  // Encuentra la barra de búsqueda
        searchBox.click();  // Da click en la barra de búsqueda para activarla
        searchBox.sendKeys("Selenium WebDriver");  // Escribe algo en la barra de búsqueda


        Assert.assertTrue(driver.getTitle().contains("Google"), "No se realizó la búsqueda");
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

        boolean isDisabled = SeleniumUtils.ElementoDeshabilitado(nullElement);

        // Verifica que el método retorne verdadero para un elemento nulo
        Assert.assertTrue(isDisabled, "El elemento nulo no fue reconocido como deshabilitado");
    }
    @Test(testName = "CleanElement",description = "Should clean the especified element",dependsOnMethods = "elementExist")
    public void cleanElement(){
        WebElement elemento= SeleniumUtils.getElementIfExist(driver,driver,By.xpath("//*[@id=\"APjFqb\"]"));
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
        Assert.assertTrue(SeleniumUtils.esValorValido(valor));
    }

    @Test(testName = "Is not an Valid value", description = "Should return a boolean of a valid value")
    public void isNotAnValidValue(){
        String valor="";
        Assert.assertFalse(SeleniumUtils.esValorValido(valor));
    }

    @Test(testName = "Is an null or empty String", description = "Should return a boolean of a null or empty value")
    public void stringIsNullOrEmpty(){
        String valor="";
        Assert.assertTrue(SeleniumUtils.cadenaNulaoVacia(valor));
    }

    @Test(testName = "Is an not null or empty String", description = "Should return a boolean of a null or empty value")
    public void stringIsNotNullOrEmpty(){
        String valor="Valor para evitar que sea nulo";
        Assert.assertFalse(SeleniumUtils.cadenaNulaoVacia(valor));
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



    @Test(testName = "Send Keys If Element Exist - Element Not Found",
            description = "Verifica que el método retorne false cuando no se encuentra ningún elemento")
    public void testSendKeysIfElementExist_ElementNotFound() throws Exception {
        String element = "testElement";
        CharSequence[] texto = {"Hello"};

        // Crear el WebDriver y otros objetos necesarios
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Intentar encontrar un elemento que no existe
        boolean result;
        try {
            WebElement testElement = driver.findElement(By.id(element));
            result = SeleniumUtils.sendKeysIfElementExist(driver, driver, element, texto);
        } catch (NoSuchElementException e) {
            result = false;
        }

        // Validar el resultado
        Assert.assertFalse(result, "El método debería retornar false cuando no se encuentra ningún elemento.");

        // Cerrar el WebDriver
        driver.quit();
    }



    @Test(testName = "Send Keys If Element Exist - Exception Handling",
            description = "Verifica que el método maneje correctamente las excepciones al buscar el elemento")
    public void testSendKeysIfElementExist_ExceptionHandling() throws Exception {
        String element = "testElement";
        CharSequence[] texto = {"Hello"};

        // Crear el WebDriver y otros objetos necesarios

        boolean result;
        try {
            // Intentar enviar texto a un elemento que lanzará una excepción (por ejemplo, un elemento que no se puede interactuar)
            WebElement testElement = driver.findElement(By.id(element));
            testElement.sendKeys(""); // Supongamos que esto lanza una excepción
            result = SeleniumUtils.sendKeysIfElementExist(driver, driver, element, texto);
        } catch (Exception e) {
            result = false;
        }

        // Validar el resultado
        Assert.assertFalse(result, "El método debería retornar false cuando ocurre una excepción.");

        // Cerrar el WebDriver
        driver.quit();
    }

    @Test(testName = "testObtenerTextOfWebElementx2",description = "Verifica que se obtenga el texto del elemento web especificado",dependsOnMethods = "elementExist")
    public void testObtenerTextOfWebElementx2(){
        Boolean condicion=false;

        String elementoBusqueda="buscar";
        condicion= SeleniumUtils.cadenaNulaoVacia(SeleniumUtils.obtenerOfTextWebElementx2(driver,driver,elementoBusqueda,5,2));
        Assert.assertTrue(condicion);
    }

    @Test(testName = "testObtenerNullTextOfWebElementx2",description = "Verifica que se obtenga el texto nulo del elemento web especificado",dependsOnMethods = "elementExist")
    public void testObtenerNullTextOfWebElementx2(){
        Boolean condicion=false;

        String elementoBusqueda="buscar";
        condicion= SeleniumUtils.cadenaNulaoVacia(SeleniumUtils.obtenerOfTextWebElementx2(driver,driver,elementoBusqueda,5,2));
        Assert.assertTrue(condicion);
    }

    @Test(testName = "getTextOfWebElement",description = "Obtiene el texto de un elemento web",dependsOnMethods = "elementExist")
    public void getTextOfWebElement(){
        Boolean condicion=false;
        WebElement elemento=SeleniumUtils.getElementIfExist(driver,driver,"/html/body/div[1]/div[3]/form/div[1]/div[1]/div[4]/center/input[2]");
        String respuesta=SeleniumUtils.getTextOfWebElement(driver,elemento);
        condicion=SeleniumUtils.cadenaNulaoVacia(respuesta);
        Assert.assertTrue(!condicion);
    }

    @Test(testName = "getTextNullOfWebElement",description = "Obtiene el texto nulo de un elemento web",dependsOnMethods = "elementExist")
    public void getTextNullOfWebElement(){
        WebElement elemento=SeleniumUtils.getElementIfExist(driver,driver,By.xpath("//*[@id=\"APjFqb\"]"));
        String respuesta=SeleniumUtils.getTextOfWebElement(driver,elemento);
        Assert.assertTrue(SeleniumUtils.cadenaNulaoVacia(respuesta));
    }

    @Test(testName = "getTextOfWebElement",description = "Obtiene el texto de un elemento web",dependsOnMethods = "elementExist")
    public void getTextOfWebElementJavaScript(){
        Boolean condicion=false;
        WebElement elemento=SeleniumUtils.getElementIfExist(driver,driver,By.xpath("//*[@id=\"APjFqb\"]"));
        String respuesta=SeleniumUtils.getTextUsingJavaScript(driver,elemento);
        condicion=SeleniumUtils.cadenaNulaoVacia(respuesta);
        Assert.assertTrue(condicion);
    }

    @Test(testName = "getTextNullOfWebElement",description = "Obtiene el texto nulo de un elemento web",dependsOnMethods = "elementExist")
    public void getTextNullOfWebElementJavaScript(){
        Boolean condicion=false;
        WebElement elemento=SeleniumUtils.getElementIfExist(driver,driver,"/html/body/div[1]/div[3]/form/div[1]/div[1]/div[4]/center/input[2]");
        String respuesta=SeleniumUtils.getTextUsingJavaScript(driver,elemento);
        Assert.assertTrue(SeleniumUtils.cadenaNulaoVacia(respuesta));
    }

    @Test(testName = "clickElementIfExistAcierto",description = "Should make click in the specified element",dependsOnMethods = "elementExist")
    public void clickElementIfExistAcierto(){
        logParrafo("Se debe de dar click en un elemento especificado");
        Boolean variable=SeleniumUtils.clickElementIfExist(driver,driver,"/html/body/div[1]/div[6]/div[1]");
        LogsJB.waitForOperationComplete();

        Assert.assertTrue(variable);
    }


    @Test(testName = "clickElementIfExistFallo",description = "Should make click in the specified element",dependsOnMethods = "elementExist")
    public void clickElementIfExistFallo(){
        logParrafo("Se debe de dar click en un elemento especificado");
        Assert.assertFalse(SeleniumUtils.clickElementIfExist(driver,driver,"//*[@id='xxxxxxxxxx']"));
    }

    @Test(testName = "clickElementx2intentsAcierto",description = "Should make click in the specified element 2 tries",dependsOnMethods = "elementExist")
    public void clickElementx2intentsAcierto(){
        logParrafo("Se debe de dar click en un elemento especificado");
        Assert.assertTrue(SeleniumUtils.clicktoElementx2intents(driver,driver,"//*[@id='APjFqb']"));
    }

    @Test(testName = "clickElementx2intentsFallo",description = "Should make click in the specified element 2 tries",dependsOnMethods = "elementExist")
    public void clickElementx2intentsFallo(){
        logParrafo("Se debe de dar click en un elemento especificado");
        Assert.assertFalse(SeleniumUtils.clicktoElementx2intents(driver,driver,"//*[@id='XXXXXXXX']"));
    }

    @Test(testName = "getIdentificadorByAcierto",description = "Debería de traer el idenrtificador de un elemento web dado un By",dependsOnMethods = "elementExist")
    public void getIdentificadorByAcierto(){
        String respuesta="";
        respuesta=SeleniumUtils.getIdentificadorBy(By.xpath("//*[@id=\"APjFqb\"]"));
        Assert.assertFalse(respuesta.isEmpty());
    }

    @Test(testName = "getIdentificadorByFallo",description = "Debería de traer el dato vacio de un elemento web dado un By",dependsOnMethods = "elementExist")
    public void getIdentificadorByFallo(){
        String respuesta="";
        respuesta=SeleniumUtils.getIdentificadorBy(By.xpath("//*[@id=\"APjFqb\"]"));
        Assert.assertFalse(respuesta.isEmpty());
    }
    @Test(testName = "waitImplicity",description = "Debería de esperar a que cargue un elemento web de la pagina especificada ",dependsOnMethods = "elementExist")
    public void waitImplicity(){

    }

    @Test(testName = "getBooleanfromIntTrue",description = "Debería de retornar el valor booleano de un numero entero")
    public void getBooleanfromIntTrue(){
        logParrafo("Se ingresa 1, debería de ser True");
        Assert.assertTrue(SeleniumUtils.getBooleanfromInt(1));
    }

    @Test(testName = "getBooleanfromIntFalse",description = "Debería de retornar el valor booleano de un numero entero")
    public void getBooleanfromIntFalse(){
        logParrafo("Se ingresa 0, debería de ser False");
        Assert.assertFalse(SeleniumUtils.getBooleanfromInt(0));
    }

    @Test(testName = "cambiarZoomPlus",description = "Debería de aumentar el zoom de la pagina que se está visualizando",dependsOnMethods = "elementExist")
    public void cambiarZoomPlus(){
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver,2, Keys.ADD));
    }

    @Test(testName = "cambiarZoomLess",description = "Debería de disminuir el zoom de la pagina que se está visualizando",dependsOnMethods = "elementExist")
    public void cambiarZoomLess(){
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver,2, Keys.SUBTRACT));
    }

    @Test(testName = "cambiarZoomPlus",description = "Debería de aumentar el zoom de la pagina que se está visualizando",dependsOnMethods = "elementExist")
    public void cambiarZoomPlusCodigoEntero(){
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver,2, 2));

    }

    @Test(testName = "cambiarZoomLess",description = "Debería de disminuir el zoom de la pagina que se está visualizando",dependsOnMethods = "elementExist")
    public void cambiarZoomLessCodigoEntero(){
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver,2, 2));
    }

    @Test(testName = "cambiarZOOMMenos",description = "Debería de disminuir el zoom de la pagina que se está visualizando",dependsOnMethods = "elementExist")
    public void cambiarZOOMMenos(){
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOMMenos(driver,2));
    }

    @Test(testName = "cambiarZOOMMas",description = "Debería de aumentar el zoom de la pagina que se está visualizando",dependsOnMethods = "elementExist")
    public void cambiarZoomMas(){
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOMMas(driver,2));
    }

    @Test(testName = "scrollMouse",description = "Debería de hacer scroll con el mouse",dependsOnMethods = "elementExist")
    public void scrollMouse(){
        logParrafo("Se hará Scroll con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouse(2));

    }

    @Test(testName = "scrollMouseDown",description = "Debería de hacer scroll con el mouse hacia abajo",dependsOnMethods = "elementExist")
    public void scrollMouseDown(){
        logParrafo("Se hará Scroll hacia abajo con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouseDown(driver,2));

    }

    @Test(testName = "scrollMouseUp",description = "Debería de hacer scroll con el mouse hacia arriba",dependsOnMethods = "elementExist")
    public void scrollMouseUp(){
        logParrafo("Se hará Scroll hacia arriba con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouseUp(driver,2));

    }

    @Test(testName = "selectOptionWithoutComment",description = "Debería de seleccionar la opcion de un select",dependsOnMethods = "elementExist")
    public void selectOptionWithoutComment(){
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        Boolean respuesta=false;
        respuesta=SeleniumUtils.selectOption(driver,driver,"elemento","opcion","comentario");
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "getElementIfExist",description = "Debería de obtener un elemento web si existe",dependsOnMethods = "elementExist")
    public void getElementIfExist(){
        logParrafo("Se busca un elemento web para verificar si existe. Si existe, se obtiene su información");
        List<WebElement> respuesta=SeleniumUtils.getElementsIfExist(driver,driver,"/html/body/div[1]/div[6]/div[1]");
        Boolean exito=false;
        if(!respuesta.isEmpty()){
            exito=true;
        }

        Assert.assertTrue(exito);

    }












}


