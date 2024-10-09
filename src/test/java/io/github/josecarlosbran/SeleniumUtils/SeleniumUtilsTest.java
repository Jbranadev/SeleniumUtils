package io.github.josecarlosbran.SeleniumUtils;

import com.josebran.LogsJB.LogsJB;
import com.josebran.LogsJB.Numeracion.NivelLog;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.josecarlosbran.UtilidadesTest.Utilities.logParrafo;

public class SeleniumUtilsTest {
    WebDriver driver;
    WebDriverManager wdm;

    /**
     * El método sirve para enviar archivos por medio de la escritura cuando se presente un elemento de tipo input:File
     *
     * @param id          el nombre de id que recibirá nuestro input File
     * @param nombreClase el nombre de clase que recibirá nuestro input File
     */
    public static void crearInputFile(WebDriver driver, String id, String nombreClase) {
        String comando = "var input = document.createElement('input');" +
                "input.type = 'file';" +  // Establecer el tipo de input como file
                "input.id = '" + id + "';" +  // Asignar el ID al input
                "input.className = '" + nombreClase + "';" +  // Asignar una clase al input
                "document.body.appendChild(input);";  // Añadir el input al cuerpo del documento
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(comando);
    }

    public static void eliminarElementoPorId(WebDriver driver, String id) {
        String comando = "var element = document.getElementById('" + id + "');" +
                "if (element) { element.parentNode.removeChild(element); }";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(comando);
    }

    @BeforeClass
    public void setUp() throws IllegalAccessException {
//        wdm = WebDriverManager.chromedriver().driverVersion("126.0.0").browserInDocker();
//        driver = wdm.create();
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        //options.addArguments("--headless");
        driver = new ChromeDriver(options);
        //driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.google.com");
        LogsJB.setGradeLog(NivelLog.DEBUG);
        SeleniumUtils.setSearchTime(1000);
        SeleniumUtils.setSearchRepetitionTime(50);
    }

    @Test(testName = "Element Exist Google"
            , description = "Verifica que un elemento exista en la pagina de Google")
    public void elementExist() {
        logParrafo("Irá a la pagina principal de Google");
        driver.get("https://www.google.com");
        logParrafo("Verifica que el elemento 'textarea[id='APjFqb']' exista en la pagina");
        Assert.assertTrue(SeleniumUtils.ElementoExistente(driver, driver, "textarea[id='APjFqb']"),
                "No fue posible encontrar el elemento 'textarea[id='APjFqb']' en la pagina," +
                        "valide el identificador del elemento");
    }

    @Test(testName = "SendKeys Element Search Google"
            , description = "Limpia el elemento de busqueda de Google",
            dependsOnMethods = "elementExist")
    public void sendKeysToElement() {
        logParrafo("Escribirá el texto indicado en la barra de busqueda de google");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("textarea[id='APjFqb']"));
        Assert.assertTrue(SeleniumUtils.sendKeysToElement(driver, elemento, "Prueba de escritura"));
    }

    @Test(testName = "Clear Element Search Google"
            , description = "Limpia el elemento de busqueda de Google",
            dependsOnMethods = "sendKeysToElement")
    public void clearElementIfExists() {
        logParrafo("Limpia el elemento de busqueda de Google");
        Assert.assertTrue(SeleniumUtils.LimpiarElementoExistente(driver, driver, "textarea[id='APjFqb']"),
                "No fue posible limpiar el elemento 'textarea[id='APjFqb']' en la pagina," +
                        "valide el identificador del elemento");
    }

    @Test(testName = "Should Thread Sleep", description = "Debería dejar el hilo dormido por un momento")
    public void threadSleep() {
        logParrafo("Se debe de tener un tiempo dormido");
        long startTime = System.currentTimeMillis(); // Hora de inicio
        SeleniumUtils.threadslepp(1000);//Dormir el hilo
        long endTime = System.currentTimeMillis(); // Hora de fin
        long duration = endTime - startTime; // Calcular duración
        // Verifica que la duración está dentro de un rango aceptable (por ejemplo, +/- 100 ms)
        Assert.assertTrue(duration >= 1000 && duration <= 1100, "El método no durmió el hilo el tiempo esperado");
    }

    @Test(testName = "KeyPress Using Keys",
            description = "Verifica que se puede presionar una tecla utilizando el objeto Keys",
            dependsOnMethods = "clearElementIfExists")
    public void keyPressUsingKeys() {
        logParrafo("Se va a presionar la tecla ENTER utilizando Keys");
        WebElement searchBox = SeleniumUtils.getElementIfExist(driver, driver, "textarea[id='APjFqb']");  // Encuentra la barra de búsqueda
        searchBox.click();  // Da click en la barra de búsqueda para activarla
        searchBox.sendKeys("Selenium WebDriver");  // Escribe algo en la barra de búsqueda
        Assert.assertTrue(driver.getTitle().contains("Google"), "No se realizó la búsqueda");
    }

    @Test(testName = "KeyPress Using ASCII Code",
            description = "Verifica que se puede presionar una tecla utilizando un código ASCII",
            dependsOnMethods = "keyPressUsingKeys")
    public void keyPressUsingAsciiCode() {
        logParrafo("Se va a presionar la tecla 'A' utilizando su código ASCII");
        WebElement searchBox = SeleniumUtils.getElementIfExist(driver, driver, "textarea[id='APjFqb']");  // Encuentra la barra de búsqueda
        searchBox.clear();  // Limpia la barra de búsqueda para comenzar con un campo vacío
        searchBox.click();
        // Presiona la tecla 'A' (código ASCII 65)
        SeleniumUtils.keyPress(driver, 65);
        // Verifica que la barra de búsqueda contiene la letra 'A'
        Assert.assertEquals(SeleniumUtils.getTextOfWebElement(driver, searchBox), "A", "La tecla 'A' no fue presionada correctamente");
    }

    @Test(testName = "KeyPress Using Keys",
            description = "Verifica que se puede presionar una tecla utilizando Keys",
            dependsOnMethods = "keyPressUsingAsciiCode")
    public void KeyPressUsingKeys() {
        logParrafo("Se va a presionar la tecla '-->' utilizando su Keys ");
        WebElement searchBox = SeleniumUtils.getElementIfExist(driver, driver, "textarea[id='APjFqb']");  // Encuentra la barra de búsqueda
        searchBox.clear();  // Limpia la barra de búsqueda para comenzar con un campo vacío
        searchBox.click();
        // Presiona la tecla '-->'
        SeleniumUtils.keyPress(driver, Keys.ARROW_RIGHT);
        // Verifica que la barra de búsqueda contiene la letra '-->'
        Assert.assertEquals(searchBox.getAttribute("value"), "", "La tecla '-->' no fue presionada correctamente");
    }

    @Test(testName = "KeyPressUsingKeysAssertControl",
            description = "Verifica que se puede presionar una tecla utilizando Keys y la variable de controlAssert",
            dependsOnMethods = "KeyPressUsingKeys")
    public void KeyPressUsingKeysAssertControl() {
        logParrafo("Se va a presionar la tecla '-->' utilizando su Keys ");
        WebElement searchBox = SeleniumUtils.getElementIfExist(driver, driver, "textarea[id='APjFqb']");  // Encuentra la barra de búsqueda
        searchBox.clear();  // Limpia la barra de búsqueda para comenzar con un campo vacío
        searchBox.click();
        // Presiona la tecla '-->'
        SeleniumUtils.keyPress(driver, Keys.ARROW_RIGHT, true);
        // Verifica que la barra de búsqueda contiene la letra '-->'
        Assert.assertEquals(searchBox.getAttribute("value"), "", "La tecla '-->' no fue presionada correctamente");
    }

    @Test(testName = "KeyPress Using ASCII Code Assert control",
            description = "Verifica que se puede presionar una tecla utilizando un código ASCII con la variable de control true",
            dependsOnMethods = "KeyPressUsingKeysAssertControl")
    public void keyPressUsingAsciiCodeAssertControl() {
        logParrafo("Se va a presionar la tecla 'A' utilizando su código ASCII");
        WebElement searchBox = SeleniumUtils.getElementIfExist(driver, driver, "textarea[id='APjFqb']");  // Encuentra la barra de búsqueda
        searchBox.clear();  // Limpia la barra de búsqueda para comenzar con un campo vacío
        searchBox.click();
        // Presiona la tecla 'A' (código ASCII 65)
        SeleniumUtils.keyPress(driver, 65, true);
        // Verifica que la barra de búsqueda contiene la letra 'A'
        Assert.assertEquals(searchBox.getAttribute("value"), "A", "La tecla 'A' no fue presionada correctamente");
    }

    @Test(testName = "Element Is Disabled - Null Element",
            description = "Verifica que un elemento nulo sea tratado como deshabilitado")
    public void testElementIsDisabled() {
        logParrafo("Se va a verificar que un elemento nulo sea tratado como deshabilitado");
        WebElement nullElement = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("//*[@id=\"xpathFalso\"]"));
        boolean isDisabled = SeleniumUtils.ElementoDeshabilitado(nullElement);
        // Verifica que el método retorne verdadero para un elemento nulo
        Assert.assertTrue(isDisabled, "El elemento nulo no fue reconocido como deshabilitado");
    }

    @Test(testName = "CleanElement", description = "Should clean the especified element",
            dependsOnMethods = "keyPressUsingAsciiCodeAssertControl")
    public void cleanElement() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("textarea[id='APjFqb']"));
        logParrafo("Se debe de limpiar el elemento específicado ");
        Assert.assertTrue(SeleniumUtils.cleanElement(driver, elemento));
    }

    @Test(testName = "CleanElement_Nulo", description = "Should clean the especified element",
            dependsOnMethods = "cleanElement")
    public void cleanElement_Nulo() {
        Assert.assertFalse(SeleniumUtils.cleanElement(driver, null));
    }

    @Test(testName = "CleanElement_Error", description = "Should clean the especified element",
            dependsOnMethods = "cleanElement_Nulo")
    public void cleanElement_Error() {
        // Crear un nuevo div y añadirlo al cuerpo del documento
        String createDivScript = "var div = document.createElement('div');" +
                "div.id = 'myDiv';" +
                "document.body.appendChild(div);";
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript(createDivScript);
        // Establecer el textContent del div
        String setTextContentScript = "document.getElementById('myDiv').textContent = 'Texto de prueba';";
        jsExecutor.executeScript(setTextContentScript);
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.id("myDiv"));
        Assert.assertTrue(SeleniumUtils.cleanElement(driver, elemento));
    }

    @Test(testName = "posicionarmeEn", description = "Should be positioned in specified element",
            dependsOnMethods = "cleanElement_Error")
    public void posicionarmeEn() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("//*[@id=\"input\"]"));
        logParrafo("Se debe posicionr en el elemento específicado ");
        Boolean acierto = false;
        try {
            SeleniumUtils.posicionarmeEn(driver, elemento);
            acierto = true;
            Assert.assertTrue(acierto);
        } catch (Exception e) {
            acierto = false;
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
    public void isAnValidValue() {
        String valor = "Valor para ver si es valido o no";
        Assert.assertTrue(SeleniumUtils.esValorValido(valor));
    }

    @Test(testName = "Is not an Valid value", description = "Should return a boolean of a valid value")
    public void isNotAnValidValue() {
        String valor = "";
        Assert.assertFalse(SeleniumUtils.esValorValido(valor));
    }

    @Test(testName = "Is an null or empty String", description = "Should return a boolean of a null or empty value")
    public void stringIsNullOrEmpty() {
        String valor = "";
        Assert.assertTrue(SeleniumUtils.cadenaNulaoVacia(valor));
    }

    @Test(testName = "Is an not null or empty String", description = "Should return a boolean of a null or empty value")
    public void stringIsNotNullOrEmpty() {
        String valor = "Valor para evitar que sea nulo";
        Assert.assertFalse(SeleniumUtils.cadenaNulaoVacia(valor));
    }

    @Test(testName = "getImageScreeenshotWebElement", description = "Should take a correct screenshot",
            dependsOnMethods = "posicionarmeEn")
    public void getImageScreeenshotWebElement() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("/html/body"));
        try {
            SeleniumUtils.getImageScreeenshotWebElement(elemento);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertFalse(false);
        }
    }

    @Test(testName = "getImageScreeenshotWebElement_Error", description = "Should take a correct screenshot",
            dependsOnMethods = "getImageScreeenshotWebElement")
    public void getImageScreeenshotWebElement_Error() {
        try {
            SeleniumUtils.getImageScreeenshotWebElement(null);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertFalse(false);
        }
    }

    @Test(testName = "RefreshReferenceToElement", description = "Should take a correct refresh",
            dependsOnMethods = "getImageScreeenshotWebElement_Error")
    public void RefreshReferenceToElement() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("/html/body"));
        try {
            SeleniumUtils.RefreshReferenceToElement(driver, elemento);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertFalse(false);
        }
    }

    @Test(testName = "Send Keys If Element Exist - Element Not Found",
            description = "Verifica que el método retorne false cuando no se encuentra ningún elemento",
            dependsOnMethods = "RefreshReferenceToElement")
    public void testSendKeysIfElementExist_ElementNotFound() {
        String element = "testElement";
        CharSequence[] texto = {"Hello"};
        // Crear el WebDriver y otros objetos necesarios
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Intentar encontrar un elemento que no existe
        boolean result;
        try {
            result = SeleniumUtils.sendKeysIfElementExist(driver, driver, element, texto);
        } catch (NoSuchElementException e) {
            result = false;
        }
        // Validar el resultado
        Assert.assertFalse(result, "El método debería retornar false cuando no se encuentra ningún elemento.");
        // Cerrar el WebDriver
        //;
    }

    @Test(testName = "Send Keys If Element Exist - Exception Handling",
            description = "Verifica que el método maneje correctamente las excepciones al buscar el elemento",
            dependsOnMethods = "testSendKeysIfElementExist_ElementNotFound")
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
        //;
    }

    @Test(testName = "testObtenerTextOfWebElementx2", description = "Verifica que se obtenga el texto del elemento web especificado"
            , dependsOnMethods = "testSendKeysIfElementExist_ExceptionHandling")
    public void testObtenerTextOfWebElementx2() {
        Boolean condicion = false;
        String elementoBusqueda = "buscar";
        condicion = SeleniumUtils.cadenaNulaoVacia(SeleniumUtils.obtenerOfTextWebElementx2(driver, driver, elementoBusqueda, 5, 2));
        Assert.assertTrue(condicion);
    }

    @Test(testName = "testObtenerNullTextOfWebElementx2",
            description = "Verifica que se obtenga el texto nulo del elemento web especificado",
            dependsOnMethods = "testObtenerTextOfWebElementx2")
    public void testObtenerNullTextOfWebElementx2() {
        Boolean condicion = false;
        String elementoBusqueda = "buscar";
        condicion = SeleniumUtils.cadenaNulaoVacia(SeleniumUtils.obtenerOfTextWebElementx2(driver, driver, elementoBusqueda, 5, 2));
        Assert.assertTrue(condicion);
    }

    @Test(testName = "getTextOfWebElement", description = "Obtiene el texto de un elemento web",
            dependsOnMethods = "testObtenerNullTextOfWebElementx2")
    public void getTextOfWebElement() {
        Boolean condicion = false;
        SeleniumUtils.enviarTexto(driver, driver, "textarea[id='APjFqb']", "texto");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "textarea[id='APjFqb']");
        String respuesta = SeleniumUtils.getTextOfWebElement(driver, elemento);
        condicion = SeleniumUtils.cadenaNulaoVacia(respuesta);
        Assert.assertFalse(condicion);
    }

    @Test(testName = "getTextNullOfWebElement", description = "Obtiene el texto nulo de un elemento web",
            dependsOnMethods = "getTextOfWebElement")
    public void getTextNullOfWebElement() {
        SeleniumUtils.LimpiarElementoExistente(driver, driver, "textarea[id='APjFqb']");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("textarea[id='APjFqb']"));
        String respuesta = SeleniumUtils.getTextOfWebElement(driver, elemento);
        Assert.assertTrue(SeleniumUtils.cadenaNulaoVacia(respuesta));
    }

    @Test(testName = "getTextNullOfWebElement_ObjetoNull", description = "Obtiene el texto nulo de un elemento web",
            dependsOnMethods = "getTextNullOfWebElement")
    public void getTextNullOfWebElement_ObjetoNull() {
        SeleniumUtils.LimpiarElementoExistente(driver, driver, "textarea[id='APjFqb']");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("textarea[id='APjFqb']"));
        String respuesta = SeleniumUtils.getTextOfWebElement(driver, null);
        Assert.assertTrue(SeleniumUtils.cadenaNulaoVacia(respuesta));
    }

    @Test(testName = "getTextOfWebElement", description = "Obtiene el texto de un elemento web",
            dependsOnMethods = "getTextNullOfWebElement")
    public void getTextOfWebElementJavaScript() {
        Boolean condicion = false;
        SeleniumUtils.LimpiarElementoExistente(driver, driver, "textarea[id='APjFqb']");
        SeleniumUtils.enviarTexto(driver, driver, "textarea[id='APjFqb']", "texto");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("textarea[id='APjFqb']"));
        String respuesta = SeleniumUtils.getTextUsingJavaScript(driver, elemento);
        condicion = SeleniumUtils.cadenaNulaoVacia(respuesta);
        Assert.assertTrue(condicion);
    }

    @Test(testName = "getTextNullOfWebElement", description = "Obtiene el texto nulo de un elemento web",
            dependsOnMethods = "getTextOfWebElementJavaScript")
    public void getTextNullOfWebElementJavaScript() {
        Boolean condicion = false;
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "/html/body/div[1]/div[3]/form/div[1]/div[1]/div[4]/center/input[2]");
        String respuesta = SeleniumUtils.getTextUsingJavaScript(driver, elemento);
        Assert.assertTrue(SeleniumUtils.cadenaNulaoVacia(respuesta));
    }

    @Test(testName = "clickElementIfExistAcierto", description = "Should make click in the specified element",
            dependsOnMethods = "getTextNullOfWebElementJavaScript")
    public void clickElementIfExistAcierto() {
        logParrafo("Se debe de dar click en un elemento especificado");
        Boolean variable = SeleniumUtils.clickElementIfExist(driver, driver, "textarea[id='APjFqb']");
        Assert.assertTrue(variable, "No se logro hacer click en el elemento");
    }

    @Test(testName = "clickElementIfExistFallo", description = "Should make click in the specified element",
            dependsOnMethods = "clickElementIfExistAcierto")
    public void clickElementIfExistFallo() {
        logParrafo("Se debe de dar click en un elemento especificado");
        Assert.assertFalse(SeleniumUtils.clickElementIfExist(driver, driver, "//*[@id='xxxxxxxxxx']"),
                "No fallo al intentar dar click al elemento, por favor verificar el identifacor del elemento que no debe existir en la pagina");
    }

    @Test(testName = "clickElementx2intentsAcierto", description = "Should make click in the specified element 2 tries",
            dependsOnMethods = "clickElementIfExistFallo")
    public void clickElementx2intentsAcierto() {
        logParrafo("Se debe de dar click en un elemento especificado");
        Assert.assertTrue(SeleniumUtils.clicktoElementx2intents(driver, driver, "textarea[id='APjFqb']"));
    }

    @Test(testName = "clickElementx2intentsFallo", description = "Should make click in the specified element 2 tries",
            dependsOnMethods = "clickElementx2intentsAcierto")
    public void clickElementx2intentsFallo() {
        logParrafo("Se debe de dar click en un elemento especificado");
        Assert.assertFalse(SeleniumUtils.clicktoElementx2intents(driver, driver, "//*[@id='XXXXXXXX']"));
    }

    @Test(testName = "getIdentificadorByAcierto", description = "Debería de traer el idenrtificador de un elemento web dado un By",
            dependsOnMethods = "clickElementx2intentsFallo")
    public void getIdentificadorByAcierto() {
        String respuesta = "";
        respuesta = SeleniumUtils.getIdentificadorBy(By.xpath("textarea[id='APjFqb']"));
        Assert.assertFalse(respuesta.isEmpty());
    }

    @Test(testName = "getIdentificadorByFallo", description = "Debería de traer el dato vacio de un elemento web dado un By",
            dependsOnMethods = "getIdentificadorByAcierto")
    public void getIdentificadorByFallo() {
        String respuesta = "";
        respuesta = SeleniumUtils.getIdentificadorBy(By.xpath("textarea[id='APjFqb']"));
        Assert.assertFalse(respuesta.isEmpty());
    }

    @Test(testName = "getBooleanfromIntTrue", description = "Debería de retornar el valor booleano de un numero entero")
    public void getBooleanfromIntTrue() {
        logParrafo("Se ingresa 1, debería de ser True");
        Assert.assertTrue(SeleniumUtils.getBooleanfromInt(1));
    }

    @Test(testName = "getBooleanfromIntFalse", description = "Debería de retornar el valor booleano de un numero entero")
    public void getBooleanfromIntFalse() {
        logParrafo("Se ingresa 0, debería de ser False");
        Assert.assertFalse(SeleniumUtils.getBooleanfromInt(0));
    }

    @Test(testName = "cambiarZoomPlus", description = "Debería de aumentar el zoom de la pagina que se está visualizando",
            dependsOnMethods = "getIdentificadorByFallo")
    public void cambiarZoomPlus() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, Keys.ADD));
    }

    @Test(testName = "cambiarZoomPlusError", description = "Debería de dar un error al momento de aumentar el zoom de la pagina que se está visualizando",
            expectedExceptions = {AssertionError.class}, dependsOnMethods = "cambiarZoomPlus")
    public void cambiarZoomPlusError() {
        logParrafo("Se debe de dar un error al aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(null, 2, Keys.ADD));
    }

    @Test(testName = "cambiarZoomLess", description = "Debería de disminuir el zoom de la pagina que se está visualizando",
            dependsOnMethods = "cambiarZoomPlusError")
    public void cambiarZoomLess() {
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, Keys.SUBTRACT));
    }

    @Test(testName = "cambiarZoomPlus", description = "Debería de aumentar el zoom de la pagina que se está visualizando",
            dependsOnMethods = "cambiarZoomLess")
    public void cambiarZoomPlusCodigoEntero() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, 2));
    }

    @Test(testName = "cambiarZoomLess", description = "Debería de disminuir el zoom de la pagina que se está visualizando",
            dependsOnMethods = "cambiarZoomPlusCodigoEntero")
    public void cambiarZoomLessCodigoEntero() {
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, 2));
    }

    @Test(testName = "cambiarZoomPlusCodigoEntero Error", description = "Debería de aumentar el zoom de la pagina que se está visualizando", dependsOnMethods = "elementExist")
    public void cambiarZoomPlusCodigoEntero_Error() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertFalse(SeleniumUtils.cambiarZOOM(null, 5, 3000));
    }

    @Test(testName = "cambiarZOOMMenos", description = "Debería de disminuir el zoom de la pagina que se está visualizando",
            dependsOnMethods = "cambiarZoomLessCodigoEntero")
    public void cambiarZOOMMenos() {
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOMMenos(driver, 2));
    }

    @Test(testName = "cambiarZOOMMenosFallo", description = "Debería dar un error al disminuir el zoom de la página visualizada",
            dependsOnMethods = "cambiarZOOMMenos")
    public void cambiarZOOMMenosFallo() {
        logParrafo("Se debe dar un error al momento de disminuir la cantidad de Zoom que se realiza");
        SeleniumUtils.cambiarZOOMMenos(null, 2);
    }

    @Test(testName = "cambiarZOOMMas", description = "Debería de aumentar el zoom de la pagina que se está visualizando",
            dependsOnMethods = "cambiarZOOMMenosFallo")
    public void cambiarZoomMas() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOMMas(driver, 2));
    }

    @Test(testName = "cambiarZOOMMasFallo", description = "Debería de dar un error al aumentar el zoom de la pagina que se está visualizando",
            dependsOnMethods = "cambiarZoomMas")
    public void cambiarZoomMasFallo() {
        logParrafo("Se debe de dar un error al momento de aumentar la cantidad de Zoom que se realiza");
        Assert.assertFalse(SeleniumUtils.cambiarZOOMMas(null, 2));
    }

    @Test(testName = "scrollMouseDown", description = "Debería de hacer scroll con el mouse hacia abajo",
            dependsOnMethods = "cambiarZoomMasFallo")
    public void scrollMouseDown() {
        logParrafo("Se hará Scroll hacia abajo con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouseDown(driver, 2));
    }

    @Test(testName = "scrollMouseDown Fallo", description = "Debería de hacer scroll con el mouse hacia abajo", dependsOnMethods = "elementExist")
    public void scrollMouseDown_Fallo() {
        logParrafo("Se hará Scroll hacia abajo con el mouse por medio de Selenium");
        Assert.assertFalse(SeleniumUtils.scrollMouseDown(null, 2));
    }

    @Test(testName = "scrollMouseUp", description = "Debería de hacer scroll con el mouse hacia arriba",
            dependsOnMethods = "scrollMouseDown")
    public void scrollMouseUp() {
        logParrafo("Se hará Scroll hacia arriba con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouseUp(driver, 2));
    }

    @Test(testName = "scrollMouseUpFallo", description = "Debería de dar un error al  hacer scroll con el mouse hacia arriba",
            dependsOnMethods = "scrollMouseUp")
    public void scrollMouseUpFallo() {
        logParrafo("Se dará un error cuando el driver haga Scroll hacia arriba con el mouse por medio de Selenium");
        Assert.assertFalse(SeleniumUtils.scrollMouseUp(null, 0));
    }

    @Test(testName = "selectOptionWithComment", description = "Debería de seleccionar la opcion de un select",
            dependsOnMethods = "scrollMouseUpFallo")
    public void selectOptionWithComment() {
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        boolean respuesta = false;
        respuesta = SeleniumUtils.selectOption(driver, driver, "elemento", "opcion", false);
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "getElementIfExist", description = "Debería de obtener un elemento web si existe",
            dependsOnMethods = "selectOptionWithComment")
    public void getElementIfExist() {
        logParrafo("Se busca un elemento web para verificar si existe. Si existe, se obtiene su información");
        List<WebElement> respuesta = SeleniumUtils.getElementsIfExist(driver, driver, "div");
        Boolean exito = false;
        if (!respuesta.isEmpty()) {
            exito = true;
        }
        Assert.assertTrue(exito);
    }

    @Test(testName = "FrameActivo", description = "Obtiene el frame activo", dependsOnMethods = "getElementIfExist")
    public void FrameActivo() {
        logParrafo("Se busca un elemento web FRAME a traves de JS para verificar si esta activo");
        boolean exito = false;
        exito = SeleniumUtils.FrameActivo(driver);
        if (exito) {
            Assert.assertTrue(exito);
        } else {
            Assert.assertFalse(exito);
        }
    }

    @Test(testName = "CambiarFrame", description = "Cambia de frame al enviado", dependsOnMethods = "FrameActivo")
    public void CambiarFrame() {
        logParrafo("Se busca un elemento web FRAME y se coloca como activo");
        boolean exito = false;
        exito = SeleniumUtils.movetoframeIDorName(driver, driver, "hfcr");
        if (exito) {
            Assert.assertTrue(exito);
        } else {
            Assert.assertFalse(exito);
        }
    }

    public void simularConfirmJavascript() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("confirm('hola')");
    }

    public void simularAlertJavascript() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("alert('hola')");
    }

    @Test(testName = "acceptConfirmTest", description = "Se debe de aceptar el cuadro de dialogo que se dispara",
            dependsOnMethods = "CambiarFrame")
    public void acceptConfirmTest() {
        boolean respuesta = false;
        respuesta = SeleniumUtils.acceptConfirm(driver, true);
        simularConfirmJavascript();
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "acceptConfirmTestHanddleError", description = "Se debe disparar un error al aceptar el cuadro de dialogo que se dispara",
            dependsOnMethods = "acceptConfirmTest")
    public void acceptConfirmTestHanddleError() {
        boolean respuesta = false;
        respuesta = SeleniumUtils.acceptConfirm(driver, true);
        simularConfirmJavascript();
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "acceptAlertTestWithErrors", description = "Se debe de aceptar el cuadro de dialogo que se dispara",
            dependsOnMethods = "acceptConfirmTestHanddleError")
    public void acceptAlertTest() {
        SeleniumUtils.threadslepp(250);
        simularAlertJavascript();
        Assert.assertTrue(SeleniumUtils.acceptAlert(driver));
    }

    @Test(testName = "getElementsIfExistExito", description = "Se debe de obtener una lista de elementos, si es que existen",
            dependsOnMethods = "acceptAlertTest")
    public void getElementsIfExistExito() {
        List<WebElement> elementos = SeleniumUtils.getElementsIfExist(driver, driver, "//img");
        Assert.assertNotNull(elementos);
    }

    @Test(testName = "getElementsIfExistFallo", description = "Se debe de obtener un error al obtener una lista de elementos, si es que existen",
            dependsOnMethods = "getElementsIfExistExito")
    public void getElementsIfExistFallo() {
        List<WebElement> elementos = SeleniumUtils.getElementsIfExist(driver, driver, "xxxxxxxxxxx");
        boolean vacio = elementos.isEmpty();
        Assert.assertTrue(vacio);
    }

    @Test(testName = "getElementIfExistExito", description = "Se debe de obtener un elemento, si es que existe",
            dependsOnMethods = "getElementsIfExistFallo")
    public void getElementIfExistExito() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "div#tophf");
        Assert.assertNotNull(elemento);
    }

    @Test(testName = "getElementIfExistFallo", description = "Se debe de obtener un error al obtener elemento, si es que existe",
            dependsOnMethods = "getElementIfExistExito")
    public void getElementIfExistFallo() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "xxxxxxxxxxxxxxxx");
        Assert.assertNull(elemento);
    }

    @Test(testName = "clickToElementExito", description = "Click en un elemento",
            dependsOnMethods = "getElementIfExistFallo")
    public void clickToElementExito() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "textarea[id='APjFqb']");
        Assert.assertTrue(SeleniumUtils.clickToElement(driver, elemento));
    }

    @Test(testName = "clickToElementFallo", description = "Error al dar click en un elemento",
            dependsOnMethods = "clickToElementExito")
    public void clickToElementFallo() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "xxxxxxxxxxx");
        Assert.assertFalse(SeleniumUtils.clickToElement(driver, elemento));
    }

    @Test(testName = "obtenerWebElementsx2StringAcierto", dependsOnMethods = "clickToElementFallo")
    public void obtenerWebElementsx2StringAcierto() {
        List<WebElement> elementos = SeleniumUtils.obtenerWebElementsx2(driver, driver, "//img");
        Assert.assertNotNull(elementos);
    }

    @Test(testName = "obtenerWebElementsx2StringFallo", dependsOnMethods = "obtenerWebElementsx2StringAcierto")
    public void obtenerWebElementsx2StringFallo() {
        List<WebElement> elementos = SeleniumUtils.obtenerWebElementsx2(driver, driver, "xxxxxxxx");
        boolean vacio = elementos.isEmpty();
        Assert.assertTrue(vacio);
    }

    @Test(testName = "obtenerWebElementsx2ByAcierto", dependsOnMethods = "obtenerWebElementsx2StringFallo")
    public void obtenerWebElementsx2ByAcierto() {
        List<WebElement> elementos = SeleniumUtils.obtenerWebElementsx2(driver, driver, By.xpath("//img"));
        Assert.assertNotNull(elementos);
    }

    @Test(testName = "obtenerWebElementsx2ByFallo", dependsOnMethods = "obtenerWebElementsx2ByAcierto")
    public void obtenerWebElementsx2ByFallo() {
        List<WebElement> elementos = SeleniumUtils.obtenerWebElementsx2(driver, driver, By.xpath("xxxxxxxxxxxxxxxxxx"));
        boolean vacio = elementos.isEmpty();
        Assert.assertTrue(vacio);
    }

    @Test(testName = "getTextIfElementExistSinTiempoExito", description = "Obtiene el texto de un elemento web",
            dependsOnMethods = "obtenerWebElementsx2ByFallo")
    public void getTextIfElementExistSinTiempoExito() {
        String elemento = SeleniumUtils.getTextIfElementExist(driver, driver, "#APjFqb");
        Assert.assertNotNull(elemento);
    }

    @Test(testName = "getTextIfElementExistSinTiempoFallo", description = "Obtiene el texto de un elemento web",
            dependsOnMethods = "getTextIfElementExistSinTiempoExito")
    public void getTextIfElementExistSinTiempoFallo() {
        String elemento = SeleniumUtils.getTextIfElementExist(driver, driver, "xxxxxxxxxxxxxxxxxx");
        Assert.assertNull(elemento);
    }

    @Test(testName = "getTextIfElementExistConTiempoFallo", description = "Obtiene el texto de un elemento web",
            dependsOnMethods = "getTextIfElementExistSinTiempoFallo")
    public void getTextIfElementExistConTiempoFallo() {
        String elemento = SeleniumUtils.getTextIfElementExist(driver, driver, "xxxxxxxxxxxxxxxxxx", 4, 2);
        Assert.assertNull(elemento);
    }

    @Test(testName = "seleccionarElementoAcierto", description = "Debe de seleccionar ele elemento especirficado",
            dependsOnMethods = "getTextIfElementExistConTiempoFallo")
    public void seleccionarElementoAcierto() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "/html/body/div[1]/div[6]/div[1]");
        Assert.assertTrue(SeleniumUtils.seleccionarElemento(driver, driver, elemento));
    }

    @Test(testName = "seleccionarElementoFallo", description = "Debe de seleccionar ele elemento especirficado",
            dependsOnMethods = "seleccionarElementoAcierto")
    public void seleccionarElementoFallo() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "xxxxxxxxxxxxx");
        Assert.assertFalse(SeleniumUtils.seleccionarElemento(driver, driver, elemento));
    }

    @Test(testName = "deseleccionarElementoAcierto", description = "Debe de seleccionar ele elemento especirficado",
            dependsOnMethods = "seleccionarElementoFallo")
    public void deseleccionarElementoAcierto() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "/html/body/div[1]/div[6]/div[1]");
        SeleniumUtils.seleccionarElemento(driver, driver, elemento);
        Assert.assertTrue(SeleniumUtils.deseleccionarElemento(driver, driver, elemento));
    }

    @Test(testName = "seleccionarElemento_IF", description = "Debe de seleccionar ele elemento especirficado",
            dependsOnMethods = "deseleccionarElementoAcierto")
    public void seleccionarElemento_IF() {
        ((JavascriptExecutor) driver).executeScript("var nuevoElemento = document.createElement('div');" +
                "nuevoElemento.id = 'Colores2';" +
                "nuevoElemento.style.width = '100px';" +
                "nuevoElemento.style.height = '50px';" +
                "nuevoElemento.style.backgroundColor = 'rgba(164, 9, 32, 1)';" +
                "nuevoElemento.style.color = 'white';" +
                "nuevoElemento.style.display = 'flex';" +
                "nuevoElemento.style.alignItems = 'center';" +
                "nuevoElemento.style.justifyContent = 'center';" +
                "nuevoElemento.innerText = 'Este es un elemento';" +
                "document.body.appendChild(nuevoElemento);");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.id("Colores2"));
        Assert.assertTrue(SeleniumUtils.seleccionarElemento(driver, driver, elemento));
    }

    @Test(testName = "deseleccionarElementoFallo", description = "Debe de seleccionar ele elemento especirficado",
            dependsOnMethods = "seleccionarElemento_IF")
    public void deseleccionarElementoFallo() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "xxxxxxxxxxxxx");
        Assert.assertFalse(SeleniumUtils.deseleccionarElemento(driver, driver, elemento));
    }

    @Test(testName = "deseleccionarElementoFallo_IF", description = "Debe de seleccionar ele elemento especirficado",
            dependsOnMethods = "deseleccionarElementoFallo")
    public void deseleccionarElementoFallo_IF() {
        ((JavascriptExecutor) driver).executeScript("var nuevoElemento = document.createElement('div');" +
                "nuevoElemento.id = 'Colores';" +
                "nuevoElemento.style.width = '200px';" +
                "nuevoElemento.style.height = '100px';" +
                "nuevoElemento.style.backgroundColor = 'rgba(46, 152, 9, 1)';" +
                "nuevoElemento.style.color = 'white';" +
                "nuevoElemento.style.display = 'flex';" +
                "nuevoElemento.style.alignItems = 'center';" +
                "nuevoElemento.style.justifyContent = 'center';" +
                "nuevoElemento.innerText = 'Este es un elemento';" +
                "document.body.appendChild(nuevoElemento);");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.id("Colores"));
        Assert.assertTrue(SeleniumUtils.deseleccionarElemento(driver, driver, elemento));
    }

    @Test(testName = "obtenerTextoSeleccionadoSelectFallo", description = "Tiene que obtener un error al obtener el texto seleccionado ",
            dependsOnMethods = "deseleccionarElementoFallo_IF")
    public void obtenerTextoSeleccionadoSelectFallo() {
        logParrafo("La prueba debería de obtener el texto de algun elemento seleccionado existente en la página de interés");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "/html/body/div[1]/div[6]/div[1]");
        String respuesta = SeleniumUtils.obtenerTextoSeleccionadoSelect(driver, elemento);
        boolean exito = respuesta.contains("fatal");
        Assert.assertTrue(exito);
    }

    /**
     * Método que, por medio de javascript, genera un WebObject Select para poder probar los métodos
     *
     * @param nombreClase Es el nombre de clase que recibirá el objeto que estemos creando
     */
    public void crearSelect(String nombreClase) {
        String comando = "var select = document.createElement('select');" +
                "select.className = '" + nombreClase + "';" +  // Añade la clase al select
                "select.id = '" + nombreClase + "';" +  // Asignar el ID al input
                "var options = ['Opción 1', 'Opción 2', 'Opción 3', 'Opción 4'];" +
                "options.forEach(function(optionText) {" +
                "    var option = document.createElement('option');" +
                "    option.value = optionText.toLowerCase().replace(/\\s+/g, '-');" +
                "    option.text = optionText;" +
                "    select.appendChild(option);" +
                "});" +
                "document.body.appendChild(select);";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(comando);
    }

    /**
     * Método que, por medio de javascript, genera un WebObject de tipo frame, para poder probar los métodos.
     *
     * @param id          nombre del id que recibe nuestro frame
     * @param nombreClase nombre de clase que recibe nuestro frame
     */
    public void crearFrames(String id, String nombreClase) {
        String comando = "var iframe = document.createElement('iframe');" +
                "iframe.id = '" + id + "';" +  // Añadir un id al iframe
                "iframe.className = '" + nombreClase + "';" +  // Añadir una clase al iframe
                "iframe.src = 'https://www.google.com';" +  // Establecer la fuente del iframe
                "iframe.width = '600';" +  // Ancho del iframe
                "iframe.height = '400';" +  // Altura del iframe
                "document.body.appendChild(iframe);";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(comando);
    }

    //Falta obtenerTextoSeleccionadoSelectAcierto
    @Test(testName = "obtenerTextoSeleccionadoSelectAcierto", description = "Obtener el texto de un select",
            dependsOnMethods = "obtenerTextoSeleccionadoSelectFallo")
    public void obtenerTextoSeleccionadoSelectAcierto() {
        crearSelect("mi-select-clase");
        SeleniumUtils.threadslepp(200);
        logParrafo("Debe de buscar un select para obtener el texto de ese elemento en específico ");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "//select[@class='mi-select-clase']");
        String respuesta = SeleniumUtils.obtenerTextoSeleccionadoSelect(driver, elemento);
        Assert.assertNotNull(respuesta);
        eliminarElementoPorId(driver, "mi-select-clase");
    }

    @Test(testName = "getImageScreenshotExito", description = "Toma de captura de pantalla en la págian proporcionada",
            dependsOnMethods = "obtenerTextoSeleccionadoSelectAcierto")
    public void getImageScreenshotExito() {
        logParrafo("Debería de tomar una captura de pantalla a un elemento específicado y veriricar que si exista ");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "/html/body/div[1]/div[2]");
        File respuesta = SeleniumUtils.getImageScrennshot(driver, elemento);
        boolean condicion = respuesta.exists();
        Assert.assertTrue(condicion);
    }

    @Test(testName = "movetoframeIDorName", description = "Debe de cambiar de frame en la página principal de google",
            dependsOnMethods = "getImageScreenshotExito")
    public void movetoframeIDorName() {
        driver.switchTo().defaultContent();
        crearFrames("mi-frame-id", "mi-frame-clase");
        SeleniumUtils.threadslepp(200);
        logParrafo("Primero se crea, por medio de javascript un frame para inyectar en la página de google. Posteriormente debe de cambiarse a ese frame ");
        boolean respuesta = SeleniumUtils.movetoframeIDorName(driver, driver, "mi-frame-id");
        Assert.assertTrue(respuesta);
        driver.switchTo().defaultContent();
        eliminarElementoPorId(driver, "mi-frame-id");
    }

    //Segundo lote de métodos
    @Test(testName = "movetoframeforwebelement", description = "Debe de moverse de frame",
            dependsOnMethods = "movetoframeIDorName")
    public void movetoframeforwebelement() {
        logParrafo("Primero debe de estar creado un frame por medio de javascript, posteriormente debe de cambiarse a ese frame ");
        driver.switchTo().defaultContent();
        crearFrames("mi-frame-id", "mi-frame-clase");
        Assert.assertTrue(SeleniumUtils.movetoframeforwebelement(driver, SeleniumUtils.obtenerWebElementx2(driver, driver, "mi-frame-id")));
        driver.switchTo().defaultContent();
        eliminarElementoPorId(driver, "mi-frame-id");
    }

    @Test(testName = "NormalizarExito", description = "Debe de retornar el string en mayusculas y normalizado")
    public void NormalizarExito() {
        logParrafo("Se le ingresa un texto sin normalizar, y el método debe de retornar un String normalizado y en mayusculas");
        String stringSinNormalizar = "café";
        String normalizado = SeleniumUtils.Normalizar(stringSinNormalizar);
        Assert.assertEquals(normalizado, "CAFA");
    }

    @Test(testName = "NormalizarFallo", description = "Debe de retornar un error en el string en mayusculas y normalizado")
    public void NormalizarFallo() {
        logParrafo("Se le ingresa un texto sin normalizar, y el método debe de retornar un String normalizado y en mayusculas");
        String stringSinNormalizar = "café";
        String normalizado = SeleniumUtils.Normalizar(stringSinNormalizar);
        Assert.assertNotEquals(normalizado, "xxxx");
    }

    @Test(testName = "validarNullExito", description = "Debe de validar si un campo está nulo",
            dependsOnMethods = "movetoframeIDorName")
    public void ValidarNullExito() {
        String campo = "";
        String nombre = "Nombre";
        boolean respuesta = SeleniumUtils.validarNull(campo, nombre);
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "validarNullFallo", description = "Debe de validar si un campo está nulo",
            dependsOnMethods = "ValidarNullExito")
    public void ValidarNullFallo() {
        String campo = "NoVacio";
        String nombre = "Nombre";
        boolean respuesta = SeleniumUtils.validarNull(campo, nombre);
        Assert.assertFalse(respuesta);
    }

    @Test(testName = "subirArchivoExito", description = "Debe de subir un archivo por medio de texto",
            dependsOnMethods = "ValidarNullFallo")
    public void subirArchivoExito() {
        String id = "id-elementoFile";
        String clase = "clase-elementoFile";
        crearInputFile(driver, id, clase);
        SeleniumUtils.threadslepp(200);
        String rutaArchivo = "C:/Users/mlemus/Pictures/Captura.PNG";
        boolean condicion = SeleniumUtils.subirArchivo(driver, "//*[@id=\"id-elementoFile\"]", rutaArchivo);
        Assert.assertTrue(condicion);
        eliminarElementoPorId(driver, id);
    }

    @Test(testName = "subirArchivoFallo", description = "Debe de lanzar un error al subir un archivo por medio de texto",
            dependsOnMethods = "subirArchivoExito")
    public void subirArchivoFallo() {
        String id = "id-elementoFile";
        String clase = "clase-elementoFile";
        crearInputFile(driver, id, clase);
        SeleniumUtils.threadslepp(200);
        boolean condicion = SeleniumUtils.subirArchivo(null, null, null);
        Assert.assertFalse(condicion);
        eliminarElementoPorId(driver, id);
    }

    @Test(testName = "regresarFramePrincipal", description = "Debe de regresar al frame inicial",
            dependsOnMethods = "subirArchivoFallo")
    public void regresarFramePrincipal() {
        logParrafo("Se debe de regresar al frame que se muestra cuando se ingresa por primera vez a la página ");
        boolean condicion = SeleniumUtils.regresarFramePrincipal(driver);
        Assert.assertTrue(condicion);
    }

    @Test(testName = "regresarFramePrincipalFallo", description = "Debe de saltar un error al regresar al frame inicial",
            dependsOnMethods = "regresarFramePrincipal")
    public void regresarFramePrincipalFallo() {
        logParrafo("Se debe de dar un fallo al momento de regresar al frame que se muestra cuando se ingresa por primera vez a la página ");
        boolean condicion = SeleniumUtils.regresarFramePrincipal(null);
        Assert.assertFalse(condicion);
    }

    @Test(testName = "convertirIpDecimalAHexadecimal", description = "Debe de retornar la terminal de una direccion ip")
    public void convertirIpDecimalAHexadecimal() {
        logParrafo("Se debe de convertir cada uno de los módulos de la direccion ip xxx.xxx.xxx.xxx a hexadecimal y concatenarlos para retornar la terminal ");
        String ip = "127.0.0.1";
        String terminal = SeleniumUtils.convertirIpDecimalAHexadecimal(ip);
        Assert.assertEquals(terminal, "7F000001");
    }

    @Test(testName = "JsComandoExito", description = "Debe de ejecutar un comando de javascript",
            dependsOnMethods = "regresarFramePrincipalFallo")
    public void JsComandoExito() {
        logParrafo("Debe de ejecutar un comando determinado de javascript por medio de un JavascriptExecutor ");
        String comando = "console.log('Comando ejecutado');";
        SeleniumUtils.ejecutarJsComando(driver, comando, null);
    }

    @Test(testName = "JsComandoFallo", description = "Debe de fallar al momento de ejecutar un comando de javascript",
            dependsOnMethods = "JsComandoExito")
    public void JsComandoFallo() {
        logParrafo("Debe de ejecutar un comando determinado de javascript por medio de un JavascriptExecutor ");
        String comando = null;
        SeleniumUtils.ejecutarJsComando(driver, comando, null);
    }

    public void generarSelect() {
        String createSelectScript =
                "var select = document.createElement('select');" +
                        "select.id = 'miSelect';" +
                        "var option1 = document.createElement('option');" +
                        "option1.value = 'HOLA';" +
                        "option1.text = 'Opcion 1';" +
                        "var option2 = document.createElement('option');" +
                        "option2.value = 'ADIOS';" +
                        "option2.text = 'Opcion 2';" +
                        "select.appendChild(option1);" +
                        "select.appendChild(option2);" +
                        "document.body.appendChild(select);";
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript(createSelectScript);
        SeleniumUtils.threadslepp(250);
    }

    @Test(testName = "selectOptionWithoutComment", description = "Debería de seleccionar la opcion de un select con comentario",
            dependsOnMethods = "JsComandoFallo")
    public void selectOptionWithoutComment() {
        generarSelect();
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        Assert.assertTrue(SeleniumUtils.selectOption(driver, driver, "miSelect", "1"));
    }

    @Test(testName = "selectOptionWithoutComment_NumberFormatException", description = "Debería de seleccionar la opcion de un select con comentario",
            dependsOnMethods = "selectOptionWithoutComment")
    public void selectOptionWithoutComment_NumberFormatException() {
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        Assert.assertTrue(SeleniumUtils.selectOption(driver, driver, "miSelect", "HOLA"));
    }

    @Test(testName = "selectOptionWithoutComment_Exception_Ex", description = "Debería de seleccionar la opcion de un select con comentario",
            dependsOnMethods = "selectOptionWithoutComment_NumberFormatException")
    public void selectOptionWithoutComment_Exception_Ex() {
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        Assert.assertFalse(SeleniumUtils.selectOption(driver, driver, "miSelect", "5"));
    }

    @Test(testName = "selectOptionWithoutComment_Exception_Ex", description = "Debería de seleccionar la opcion de un select con comentario lanzando una bandera assertfail",
            dependsOnMethods = "selectOptionWithoutComment_Exception_Ex"
            , expectedExceptions = {AssertionError.class})
    public void selectOptionWithoutComment_Exception_ExAssertFail() {
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        Assert.assertFalse(SeleniumUtils.selectOption(driver, driver, "miSelect", "5", true));
    }

    @Test(testName = "selectOptionWithoutComment_Exception_E", description = "Debería de seleccionar la opcion de un select con comentario",
            dependsOnMethods = "selectOptionWithoutComment_Exception_ExAssertFail")
    public void selectOptionWithoutComment_Exception_E() {
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        Assert.assertFalse(SeleniumUtils.selectOption(driver, driver, "miSelect", "NO HAY"));
    }

    @Test(testName = "selectOptionWithoutComment_Error", description = "Debería de seleccionar la opcion de un select con comentario con error",
            dependsOnMethods = "elementExist")
    public void selectOptionWithoutComment_Error() {
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        boolean respuesta = false;
        respuesta = SeleniumUtils.selectOption(driver, driver, "miElementoSelect", "opcionInvalida");
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "selectStandarValues - WaitImplicity Overdrive", description = "Prueba de valores predeterminados y waitImplicity Overdrive",
            dependsOnMethods = "selectOptionWithoutComment_Exception_E")
    public void selectStandarValues() throws IllegalAccessException {
        logParrafo("Funcion para comprobar varibales de control");
        Integer SerachTime = SeleniumUtils.getSearchTime();
        SeleniumUtils.setSearchTime(SerachTime);
        String inespecific = SeleniumUtils.getInespecific();
        SeleniumUtils.setInespecific(inespecific);
        Integer SearchRepetitionTime = SeleniumUtils.getSearchRepetitionTime();
        SeleniumUtils.setSearchRepetitionTime(SearchRepetitionTime);
        boolean condicion = SeleniumUtils.waitImplicity(driver, By.xpath("//*[@id=\"hplogo\"]"));
        Assert.assertFalse(condicion);
    }

    @Test(testName = "testObtenerTextOfWebElementx2 - Not Time", description = "Verifica que se obtenga el texto del elemento web especificado, sin controlador de tiempo"
            , dependsOnMethods = "selectStandarValues")
    public void testObtenerTextOfWebElementx2_NotTimeController() {
        Boolean condicion = false;
        String elementoBusqueda = "buscar";
        condicion = SeleniumUtils.cadenaNulaoVacia(SeleniumUtils.obtenerTextOfWebElementx2(driver, driver, elementoBusqueda));
        Assert.assertTrue(condicion);
    }

    @Test(testName = "obtenerWebElementx2", description = "Realiza 2 veces la busquedad de los elementos que cumplen con el criterio de busqueda especificado",
            dependsOnMethods = "testObtenerTextOfWebElementx2_NotTimeController")
    public void obtenerWebElementx2() {
        By elementoBusqueda = By.xpath("//select[@class='mi-select-clase']");
        SeleniumUtils.obtenerWebElementx2(driver, driver, elementoBusqueda);
        Assert.assertTrue(true);
    }

    @Test(testName = "obtenerWebElementx2_String", description = "Realiza 2 veces la busquedad de los elementos que cumplen con el criterio de busqueda especificado",
            dependsOnMethods = "obtenerWebElementx2")
    public void obtenerWebElementx2_String() {
        SeleniumUtils.obtenerWebElementx2(driver, driver, "//select[@class='mi-select-clase']");
        Assert.assertTrue(true);
    }

    @Test(testName = "KeyPress - Keys - Error", description = "Funcion para comprobar el Assert.Fail de KeyPress",
            dependsOnMethods = "obtenerWebElementx2_String")
    public void KeyPress_Keys_Error() {
        SeleniumUtils.keyPress(driver, null);
    }

    @Test(testName = "KeyPress - Keys - Overdrive - Error", description = "Funcion para comprobar el Assert.Fail de KeyPress",
            dependsOnMethods = "KeyPress_Keys_Error", expectedExceptions = {AssertionError.class})
    public void KeyPress_Keys_Overdrive_Error() {
        SeleniumUtils.keyPress(driver, null, true);
    }

    @Test(testName = "KeyPress - Int - Error", description = "Funcion para comprobar el Assert.Fail de KeyPress",
            dependsOnMethods = "KeyPress_Keys_Overdrive_Error")
    public void KeyPress_Int_Error() {
        SeleniumUtils.keyPress(null, -1);
    }

    @Test(testName = "KeyPress - Int - Overdrive - Error", description = "Funcion para comprobar el Assert.Fail de KeyPress",
            dependsOnMethods = "KeyPress_Int_Error")
    public void KeyPress_Int_Overdrive_Error() {
        SeleniumUtils.keyPress(null, -1, false);
    }

    @Test(testName = "KeyPress - Int - Overdrive - Error", description = "Funcion para comprobar el Assert.Fail de KeyPress",
            dependsOnMethods = "KeyPress_Int_Overdrive_Error")
    public void KeyPress_Int_Overdrive_Error2() {
        SeleniumUtils.keyPress(null, 65, false);
    }

    @Test(testName = "getElementIfExist_By", description = "Debería de obtener un elemento By si existe",
            dependsOnMethods = "KeyPress_Int_Overdrive_Error2")
    public void getElementIfExist_By() {
        logParrafo("Se busca un elemento web By para verificar si existe. Si existe, se obtiene su información");
        driver.switchTo().defaultContent();
        //By elemento = By.xpath("div.Q3DXx");
        By elemento = By.tagName("div");
        List<WebElement> respuesta = SeleniumUtils.obtenerWebElementsx2(driver, driver, elemento);
        Boolean exito = false;
        if (!respuesta.isEmpty()) {
            exito = true;
        }
        Assert.assertTrue(exito);
    }

    @Test(testName = "SendKeys Element Search Google", description = "Envia carácter por carácter al elemento especificado",
            dependsOnMethods = "getElementIfExist_By")
    public void enviarTxtforKeyPress() {
        logParrafo("Envia carácter por carácter al elemento especificado");
        SeleniumUtils.enviarTxtforKeyPress(driver, driver, "textarea[id='APjFqb']", "hola");
    }

    @Test(testName = "waitImplicityForElementNotExist - Error", description = "Debería de hacer un wait Implicity para elementos que no existan pero dara error",
            dependsOnMethods = "enviarTxtforKeyPress")
    public void waitImplicityForElementNotExist_Error() {
        logParrafo("Lo que debería de hacer es, una espera implicita pero para verificar si un elemento no existe");
        Assert.assertFalse(SeleniumUtils.waitImplicityForElementNotExist(driver, null));
    }

    @Test(testName = "waitImplicityForElementNotExist OverDrive",
            description = "Debería de hacer un wait Implicity para elementos que no existan,tomando en cuenta la bandera para el assert",
            dependsOnMethods = "waitImplicityForElementNotExist_Error")
    public void waitImplicityForElementNotExist_Overdrive() {
        logParrafo("Lo que debería de hacer es, una espera implicita pero para verificar si un elemento no existe ");
        Assert.assertTrue(SeleniumUtils.waitImplicityForElementNotExist(driver, By.xpath("xxxxxxxxxx"), true)
        );
    }

    @Test(testName = "waitImplicityForElementNotExist OverDrive - Error",
            description = "Debería de hacer un wait Implicity para elementos que no existan,tomando en cuenta la bandera para el assert",
            dependsOnMethods = "waitImplicityForElementNotExist_Overdrive")
    public void waitImplicityForElementNotExist_Overdrive_Error() {
        logParrafo("Lo que debería de hacer es, una espera implicita pero para verificar si un elemento no existe");
        Assert.assertFalse(SeleniumUtils.waitImplicityForElementNotExist(driver, null, false));
    }

    @Test(testName = "cambiarZoomPlus OverDrive", description = "Debería de aumentar el zoom de la pagina que se está visualizando, tomando en cuenta la bandera",
            dependsOnMethods = "waitImplicityForElementNotExist_Overdrive_Error")
    public void cambiarZoomPlus_OverDrive() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, Keys.ADD, true));
    }

    @Test(testName = "cambiarZoomPlus OverDrive Error", description = "Debería de aumentar el zoom de la pagina que se está visualizando, tomando en cuenta la bandera", dependsOnMethods = "elementExist")
    public void cambiarZoomPlus_OverDrive_Error() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertFalse(SeleniumUtils.cambiarZOOM(driver, 2, null, false));
    }

    @Test(testName = "cambiarZoomLess Overdrive", description = "Debería de disminuir el zoom de la pagina que se está visualizando, tomando en cuenta la bandera",
            dependsOnMethods = "cambiarZoomPlus_OverDrive")
    public void cambiarZoomLessCodigoEntero_Overdrive() {
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, 2, true));
    }

    @Test(testName = "cambiarZoomLess Overdrive Fallo", description = "Debería de disminuir el zoom de la pagina que se está visualizando, tomando en cuenta la bandera", dependsOnMethods = "elementExist")
    public void cambiarZoomLessCodigoEntero_Overdrive_Fallo() {
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertFalse(SeleniumUtils.cambiarZOOM(null, 5, 3000, false));
    }

    @Test(testName = "cambiarZOOMMenos Overdrive", description = "Debería de disminuir el zoom de la pagina que se está visualizando, tomando en cuenta la bandera",
            dependsOnMethods = "cambiarZoomLessCodigoEntero_Overdrive")
    public void cambiarZOOMMenos_Overdrive() {
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOMMenos(driver, 2, true));
    }

    @Test(testName = "cambiarZOOMMas Overdrive", description = "Debería de aumentar el zoom de la pagina que se está visualizando, tomando en cuenta la bandera",
            dependsOnMethods = "cambiarZOOMMenos_Overdrive")
    public void cambiarZoomMas_Overdrive() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOMMas(driver, 2, true));
    }

    @Test(testName = "scrollMouseDown Overdrive", description = "Debería de hacer scroll con el mouse hacia abajo, tomando en cuenta la bandera",
            dependsOnMethods = "cambiarZoomMas_Overdrive")
    public void scrollMouseDown_Overdrive() {
        logParrafo("Se hará Scroll hacia abajo con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouseDown(driver, 2, true));
    }

    @Test(testName = "scrollMouseDown Overdrive Fallo", description = "Debería de hacer scroll con el mouse hacia abajo, tomando en cuenta la bandera", dependsOnMethods = "elementExist")
    public void scrollMouseDown_Overdrive_Fallo() {
        logParrafo("Se hará Scroll hacia abajo con el mouse por medio de Selenium");
        Assert.assertFalse(SeleniumUtils.scrollMouseDown(null, 2, false));
    }

    @Test(testName = "scrollMouseUp Overdrive", description = "Debería de hacer scroll con el mouse hacia arriba, tomando en cuenta la bandera",
            dependsOnMethods = "scrollMouseDown_Overdrive")
    public void scrollMouseUp_Overdrive() {
        logParrafo("Se hará Scroll hacia arriba con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouseUp(driver, 2, true));
    }

    @Test(testName = "scrollMouseUp Overdrive Fallo", description = "Debería de hacer scroll con el mouse hacia arriba, tomando en cuenta la bandera", dependsOnMethods = "elementExist")
    public void scrollMouseUp_Overdrive_Fallo() {
        logParrafo("Se hará Scroll hacia arriba con el mouse por medio de Selenium");
        Assert.assertFalse(SeleniumUtils.scrollMouseUp(null, 2, false));
    }

    @Test(testName = "selectOptionWithComment -Overdrive seteo", description = "Debería de seleccionar la opcion de un select",
            dependsOnMethods = "scrollMouseUp_Overdrive")
    public void selectOptionWithComment_Overdrive_Seteo() {
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        boolean respuesta = false;
        respuesta = SeleniumUtils.selectOption(driver, driver, "elemento", "opcion", false);
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "obtenerTextWebElementx2", description = "Realiza 2 veces la busquedad de el texto de un elemento",
            dependsOnMethods = "selectOptionWithComment_Overdrive_Seteo")
    public void obtenerTextWebElementx2() {
        logParrafo("Realiza 2 veces la busquedad de el texto de un elemento");
        SeleniumUtils.obtenerTextWebElementx2(driver, driver, "textarea[id='APjFqb']");
    }

    @Test(testName = "obtenerTextoElementoX2",
            description = "Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna",
            dependsOnMethods = "obtenerTextWebElementx2")
    public void obtenerTextoElementoX2() {
        logParrafo("Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna");
        SeleniumUtils.obtenerTextoElementoX2(driver, driver, "textarea[id='APjFqb']");
    }

    @Test(testName = "obtenerTextoElementoX2_Fallo",
            description = "Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna",
            dependsOnMethods = "obtenerTextoElementoX2")
    public void obtenerTextoElementoX2_Fallo() {
        logParrafo("Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna");
        String validar = SeleniumUtils.obtenerTextoElementoX2(driver, driver, null);

        if (validar == null){
            Assert.assertFalse(false);
        }else{
            Assert.assertTrue(true);
        }
    }

    @Test(testName = "obtenerTextoElementoX2 - Time Controller",
            description = "Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna, controlado por tiempo de duracion de espera",
            dependsOnMethods = "obtenerTextoElementoX2_Fallo")
    public void obtenerTextoElementoX2_TimeController() {
        logParrafo("Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna controlado por tiempo de duracion de espera");
        SeleniumUtils.obtenerTextoElementoX2(driver, driver, "textarea[id='APjFqb']", 1500, 50);
    }

    @Test(testName = "obtenerTextoElementoX2_TimeController_Fallo",
            description = "Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna, controlado por tiempo de duracion de espera",
            dependsOnMethods = "obtenerTextoElementoX2_TimeController")
    public void obtenerTextoElementoX2_TimeController_Fallo() {
        logParrafo("Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna controlado por tiempo de duracion de espera");
        String validar = SeleniumUtils.obtenerTextoElementoX2(driver, driver, null, 1500, 50);
        if (validar == null){
            Assert.assertFalse(false);
        }else{
            Assert.assertTrue(true);
        }
    }

    @Test(testName = "enviarTexto", description = "Envía un texto al elemento indicado, si este existe en el contexto actual",
            dependsOnMethods = "obtenerTextoElementoX2_TimeController_Fallo")
    public void enviarTexto() {
        logParrafo("Envía un texto al elemento indicado, si este existe en el contexto actual");
        driver.navigate().refresh();
        SeleniumUtils.threadslepp(2000);
        SeleniumUtils.enviarTexto(driver, driver, "textarea[id='APjFqb']", "hola");
    }

    @Test(testName = "enviarTexto Overdrive", description = "Envía un texto al elemento indicado, si este existe en el contexto actual",
            dependsOnMethods = "enviarTexto")
    public void enviarTexto_Overdrive() {
        logParrafo("Envía un texto al elemento indicado, si este existe en el contexto actual");
        SeleniumUtils.enviarTexto(driver, driver, "textarea[id='APjFqb']", "hola");
    }

    @Test(testName = "enviarTexto Overdrive_Error", description = "Envía un texto al elemento indicado, si este existe en el contexto actual",
            dependsOnMethods = "enviarTexto")
    public void enviarTexto_Overdrive_Error() {
        logParrafo("Envía un texto al elemento indicado, si este existe en el contexto actual");
        SeleniumUtils.enviarTexto(driver, driver, "Fallo", "hola");
    }

    @Test(testName = "sendKeystoElementvalidValueX2", description = "Envia el texto al elemento especificado 2 veces seguidas, confirmando con un enter",
            dependsOnMethods = "enviarTexto_Overdrive", priority = 1)
    public void sendKeystoElementvalidValueX2() {
        logParrafo("Envia el texto al elemento especificado 2 veces seguidas, confirmando con un enter");
        SeleniumUtils.sendKeystoElementvalidValueX2(driver, driver, "textarea[id='APjFqb']", "hola");
    }

    @Test(testName = "sendKeystoElementvalidValue", description = "Envia el texto al elemento especificado",
            dependsOnMethods = "sendKeystoElementvalidValueX2")
    public void sendKeystoElementvalidValue() {
        logParrafo("Envia el texto al elemento especificado");
        SeleniumUtils.sendKeystoElementvalidValue(driver, driver, "textarea[id='APjFqb']", "hola");
    }

    @Test(testName = "enviarTextoSiValido", description = "Envía un texto a un elemento si el valor proporcionado no es nulo, vacío o igual a un valor específico.",
            dependsOnMethods = "sendKeystoElementvalidValue")
    public void enviarTextoSiValido() {
        logParrafo(" Envía un texto a un elemento si el valor proporcionado no es nulo, vacío o igual a un valor específico");
        SeleniumUtils.enviarTextoSiValido(driver, driver, "textarea[id='APjFqb']", "hola");
    }

    @Test(testName = "enviarTextoSiValido", description = "Envía un texto a un elemento si el valor proporcionado no es nulo, vacío o igual a un valor específico.",
            dependsOnMethods = "enviarTextoSiValido")
    public void enviarTextoSiValidoX2() {
        logParrafo(" Envía un texto a un elemento si el valor proporcionado no es nulo, vacío o igual a un valor específico");
        SeleniumUtils.enviarTextoSiValidoX2(driver, driver, "textarea[id='APjFqb']", "hola");
    }

    @Test(testName = "limpiarTextUsingJavaScript_2", dependsOnMethods = "enviarTextoSiValidoX2")
    public void limpiarTextUsingJavaScript_2() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("textarea[id='APjFqb']"));
        Assert.assertTrue(SeleniumUtils.limpiarTextUsingJavaScript(driver, elemento));
    }

    @Test(testName = "enviarTexto2", description = "Envía texto a un elemento web, intentando hasta dos veces, y maneja cualquier excepción que pueda ocurrir."
            , dependsOnMethods = "limpiarTextUsingJavaScript_2")
    public void enviarTexto2() {
        logParrafo("Envía texto a un elemento web, intentando hasta dos veces, y maneja cualquier excepción que pueda ocurrir");
        boolean resultado = SeleniumUtils.enviarTexto(driver, driver, "q", "Texto de prueba", "Texto de prueba 2");
    }

    @Test(testName = "enviarTexto2_Error", description = "Envía texto a un elemento web, intentando hasta dos veces, y maneja cualquier excepción que pueda ocurrir."
            , dependsOnMethods = "enviarTexto2")
    public void enviarTexto2_Error() {
        logParrafo("Envía texto a un elemento web, intentando hasta dos veces, y maneja cualquier excepción que pueda ocurrir");
        boolean resultado = SeleniumUtils.enviarTexto(driver, driver, "NotElementExist", "Texto de prueba", "Texto de prueba 2");
    }

    @Test(testName = "sendKeystoElementx2intents", description = "Trata de envíar el texto al elemento especificado en mas de una ocasión",
            dependsOnMethods = "enviarTexto2_Error")
    public void sendKeystoElementx2intents() {
        logParrafo("Trata de envíar el texto al elemento especificado en mas de una ocasión");
        Assert.assertTrue(SeleniumUtils.sendKeystoElementx2intents(driver, driver, "q", "Texto de prueba", "Texto de prueba 2"));
    }

    @Test(testName = "sendKeystoElementx2intents_Fallo", description = "Trata de envíar el texto al elemento especificado en mas de una ocasión",
            dependsOnMethods = "sendKeystoElementx2intents")
    public void sendKeystoElementx2intents_Fallo() {
        logParrafo("Trata de envíar el texto al elemento especificado en mas de una ocasión");
        Assert.assertFalse(SeleniumUtils.sendKeystoElementx2intents(driver, driver, null, "Texto de prueba", "Texto de prueba 2"));
    }

    @Test(testName = "moverATabAnterior",
            description = "Cambia el foco a una pestaña anterior en el navegador, según el identificador de la pestaña",
            dependsOnMethods = "sendKeystoElementx2intents_Fallo")
    public void moverATabAnterior() {
        logParrafo("Cambia el foco a una pestaña anterior en el navegador, según el identificador de la pestaña");
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://www.wikipedia.org");
        String SecondTab = driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://www.w3schools.com/html/html_form_input_types.asp");
        String thirdTab = driver.getWindowHandle();
        SeleniumUtils.moverATabAnterior(driver, SecondTab);
        SeleniumUtils.moverATabAnterior(driver, thirdTab);
    }

    @Test(testName = "limpiarTextUsingJavaScript_3", dependsOnMethods = "moverATabAnterior")
    public void limpiarTextUsingJavaScript_3() {
        WebElement textElement = driver.findElement(By.xpath("//h1")); // Busca un encabezado
        boolean result = SeleniumUtils.limpiarTextUsingJavaScript(driver, textElement);
        Assert.assertTrue(result, "Se esperaba que la función retornara true al limpiar un elemento con texto.");
    }

    @Test(testName = "limpiarTextUsingJavaScript_3", dependsOnMethods = "limpiarTextUsingJavaScript_3")
    public void testLimpiarTextUsingJavaScript_4() {
        // Caso 3: Limpiar un elemento que ya está vacío
        WebElement emptyElement = driver.findElement(By.xpath("//*[@id='main']/input[1]")); // Asegúrate de que haya un elemento vacío en tu HTML, este es un ejemplo.
        boolean result = SeleniumUtils.limpiarTextUsingJavaScript(driver, emptyElement);
        Assert.assertTrue(result, "Se esperaba que la función retornara true para un elemento vacío.");
    }

    @Test(testName = "getElementByLocator", dependsOnMethods = "testLimpiarTextUsingJavaScript_4")
    public void getElementByLocator() {
        WebElement elemento = null;
        elemento = SeleniumUtils.getElementByLocator(driver, "xpath", "//*[@id='main']/input[1]");
        elemento = SeleniumUtils.getElementByLocator(driver, "css selector", "#main > input[type=text]:nth-child(17)");
        elemento = SeleniumUtils.getElementByLocator(driver, "id", "midcontentadcontainer");
        elemento = SeleniumUtils.getElementByLocator(driver, "tag name", "h1");
        elemento = SeleniumUtils.getElementByLocator(driver, "link text", "HTML Form Elements");
        elemento = SeleniumUtils.getElementByLocator(driver, "partial link text", "Elements");
        elemento = SeleniumUtils.getElementByLocator(driver, "class name", "w3-button");
        elemento = SeleniumUtils.getElementByLocator(driver, "name", "viewport");
        elemento = SeleniumUtils.getElementByLocator(driver, "invalid", "dummy");
    }

    @Test(testName = "clickElementIfExist", dependsOnMethods = "getElementByLocator")
    public void clickElementIfExist() {
        WebElement elemento = null;
        SeleniumUtils.clickElementIfExist(driver, driver, "//*[@id='main']/input[1]");
        SeleniumUtils.clickElementIfExist(driver, driver, "#main > input[type=text]:nth-child(17)");
        SeleniumUtils.clickElementIfExist(driver, driver, "midcontentadcontainer");
        SeleniumUtils.clickElementIfExist(driver, driver, "h1");
        SeleniumUtils.clickElementIfExist(driver, driver, "HTML Form Elements");
        SeleniumUtils.clickElementIfExist(driver, driver, "Elements");
        SeleniumUtils.clickElementIfExist(driver, driver, "w3-button");
        SeleniumUtils.clickElementIfExist(driver, driver, "viewport");
        SeleniumUtils.clickElementIfExist(driver, driver, "dummy");
    }

    @Test(testName = "switchFrame", description = "Función para cambiar el contexto del WebDriver para interactuar con un marco (frame)",
            dependsOnMethods = "clickElementIfExist")
    public void switchFrame() {
        logParrafo("Función para cambiar el contexto del WebDriver para interactuar con un marco (frame)");
        SeleniumUtils.switchFrame(driver, driver, "/html/body/iframe[1]");
    }

    @Test(testName = "sendKeystoElementvalidValueForMap", description = "Envia el texto al elemento especificado",
            dependsOnMethods = "switchFrame")
    public void sendKeystoElementvalidValueForMap() {
        logParrafo("Envia el texto al elemento especificado");
        SeleniumUtils.sendKeystoElementvalidValueForMap(driver, driver, "textarea[id='APjFqb']", "hola");
    }

    @Test(testName = "waitImplicity", description = "Debe de dar una espera implicita hasta que aparezca un elemento web ",
            dependsOnMethods = "sendKeystoElementvalidValueForMap")
    public void waitImplicity() {
        logParrafo("El método debe de dar una espera, con un máximo de 30 segundos para que aparezca un elemento en específico, si no, lanzará una excepción");
        boolean condicion = SeleniumUtils.waitImplicity(driver, By.xpath("//*[@id=\"hplogo\"]"));
        Assert.assertFalse(condicion);
    }

    @Test(testName = "waitImplicityForElementNotExist", description = "Debería de hacer un wait Implicity para elementos que no existan",
            dependsOnMethods = "waitImplicity")
    public void waitImplicityForElementNotExist() {
        logParrafo("Lo que debería de hacer es, una espera implicita pero para verificar si un elemento no existe ");
        Assert.assertTrue(SeleniumUtils.waitImplicityForElementNotExist(driver, By.xpath("xxxxxxxxxx"))
        );
    }

    public void  SimularPromt() {
        // Inyectar el código JavaScript para mostrar un prompt
        String script = "var resultado = prompt('Introduce un mensaje:');" +
                "if (resultado !== null) { alert('Has ingresado: ' + resultado); }";
        ((JavascriptExecutor) driver).executeScript(script);
    }

    @Test(testName = "handlePrompt", description = "Permite Aceptar las Alertas emergentes por medio de la definición estándar de W3C de los navegadores",
            dependsOnMethods = "waitImplicityForElementNotExist")
    public void handlePrompt() {
        SimularPromt();
        Assert.assertFalse(SeleniumUtils.handlePrompt(driver, "Texto"));
    }

    @Test(testName = "handlePrompt_Error", description = "Permite Aceptar las Alertas emergentes por medio de la definición estándar de W3C de los navegadores con error",
            dependsOnMethods = "handlePrompt")
    public void handlePrompt_Error() {
        SimularPromt();
        Assert.assertFalse(SeleniumUtils.handlePrompt(null, null));
    }

    @Test(testName = "obtenerTextWebElementx2_Tiempos", description = "Realiza 2 veces la busquedad de el texto de un elemento",
            dependsOnMethods = "handlePrompt_Error")
    public void obtenerTextWebElementx2_Tiempos() {
        logParrafo("Realiza 2 veces la busquedad de el texto de un elemento");
        SeleniumUtils.obtenerTextWebElementx2(driver, driver, "textarea[id='APjFqb']", 5, 5);
    }

    @Test(testName = "getElementsIfExist", dependsOnMethods = "obtenerTextWebElementx2_Tiempos")
    public void getElementsIfExist() {
        By elemento = By.xpath("textarea[id='APjFqb']");
        List<WebElement> elementos = SeleniumUtils.getElementsIfExist(driver, driver, elemento);
        Assert.assertNotNull(elementos);
    }

    @Test(testName = "setFieldValue", dependsOnMethods = "getElementsIfExist")
    public void setFieldValue() throws IllegalAccessException {
        SeleniumUtils.setFieldValue("searchRepetitionTime", 50);
    }

    @Test(testName = "getFluentWait", dependsOnMethods = "setFieldValue")
    public void getFluentWait() {
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
    }

    @Test(testName = "limpiarTextUsingJavaScript", dependsOnMethods = "getFluentWait")
    public void limpiarTextUsingJavaScript() {
        boolean result;
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("textarea[id='APjFqb']"));
        result = SeleniumUtils.limpiarTextUsingJavaScript(driver, elemento);
        if (result) {
            Assert.assertTrue(result);
        } else {
            Assert.assertFalse(result);
        }
    }

    @Test(testName = "limpiarTextUsingJavaScript_Value", dependsOnMethods = "limpiarTextUsingJavaScript")
    public void limpiarTextUsingJavaScript_Value() {
        // Crear un nuevo input y añadirlo al cuerpo del documento
        String createInputScript = "var input = document.createElement('input');" +
                "input.type = 'text';" +
                "input.id = 'myInput';" +
                "document.body.appendChild(input);";
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript(createInputScript);
        // Establecer el valor del input
        String setValueScript = "document.getElementById('myInput').value = 'Texto de prueba';";
        jsExecutor.executeScript(setValueScript);
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.id("myInput"));
        Assert.assertTrue(SeleniumUtils.limpiarTextUsingJavaScript(driver, elemento));
    }

    @Test(testName = "acceptAlertTest_E", description = "Se debe de aceptar el cuadro de dialogo que se dispara",
            dependsOnMethods = "limpiarTextUsingJavaScript_Value")
    public void acceptAlertTest_E() {
        SeleniumUtils.threadslepp(250);
        Assert.assertTrue(SeleniumUtils.acceptAlert(null));
    }

//    @AfterTest
//    public void AfterTest() {
//        LogsJB.waitForOperationComplete();
//    }

    @AfterClass
    public void tearDown() {
        LogsJB.waitForOperationComplete();
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }
}



