package io.github.josecarlosbran.SeleniumUtils;

import com.josebran.LogsJB.LogsJB;
import com.josebran.LogsJB.Numeracion.NivelLog;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
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

    @BeforeClass
    public void setUp() {
//        wdm = WebDriverManager.chromedriver().driverVersion("126.0.0").browserInDocker();
//        driver = wdm.create();
        WebDriverManager.chromedriver().setup();
        //ChromeOptions options = new ChromeOptions();
        //options.addArguments("--no-sandbox");
        //options.addArguments("--disable-dev-shm-usage");
        //options.addArguments("--headless");
        //driver = new ChromeDriver(options);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.google.com");
        LogsJB.setGradeLog(NivelLog.FATAL);
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
            dependsOnMethods = "elementExist")
    public void keyPressUsingKeys() {
        logParrafo("Se va a presionar la tecla ENTER utilizando Keys");
        WebElement searchBox = SeleniumUtils.getElementIfExist(driver, driver, "textarea[id='APjFqb']");  // Encuentra la barra de búsqueda
        searchBox.click();  // Da click en la barra de búsqueda para activarla
        searchBox.sendKeys("Selenium WebDriver");  // Escribe algo en la barra de búsqueda
        Assert.assertTrue(driver.getTitle().contains("Google"), "No se realizó la búsqueda");
    }

    @Test(testName = "KeyPress Using ASCII Code",
            description = "Verifica que se puede presionar una tecla utilizando un código ASCII",
            dependsOnMethods = "elementExist")
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
            dependsOnMethods = "elementExist")
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
            dependsOnMethods = "elementExist")
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
            dependsOnMethods = "elementExist")
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

    @Test(testName = "CleanElement", description = "Should clean the especified element", dependsOnMethods = "elementExist")
    public void cleanElement() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("textarea[id='APjFqb']"));
        logParrafo("Se debe de limpiar el elemento específicado ");
        Assert.assertTrue(SeleniumUtils.cleanElement(driver, elemento));
    }

    @Test(testName = "posicionarmeEn", description = "Should be positioned in specified element", dependsOnMethods = "elementExist")
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

    @Test(testName = "getImageScreeenshotWebElement", description = "Should take a correct screenshot", dependsOnMethods = "elementExist")
    public void getImageScreeenshotWebElement() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("/html/body"));
        try {
            SeleniumUtils.getImageScreeenshotWebElement(driver, elemento);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertFalse(false);
        }
    }

    @Test(testName = "RefreshReferenceToElement", description = "Should take a correct refresh", dependsOnMethods = "elementExist")
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
            description = "Verifica que el método retorne false cuando no se encuentra ningún elemento")
    public void testSendKeysIfElementExist_ElementNotFound() throws Exception {
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
        //;
    }

    @Test(testName = "testObtenerTextOfWebElementx2", description = "Verifica que se obtenga el texto del elemento web especificado", dependsOnMethods = "elementExist")
    public void testObtenerTextOfWebElementx2() {
        Boolean condicion = false;
        String elementoBusqueda = "buscar";
        condicion = SeleniumUtils.cadenaNulaoVacia(SeleniumUtils.obtenerOfTextWebElementx2(driver, driver, elementoBusqueda, 5, 2));
        Assert.assertTrue(condicion);
    }

    @Test(testName = "testObtenerNullTextOfWebElementx2", description = "Verifica que se obtenga el texto nulo del elemento web especificado", dependsOnMethods = "elementExist")
    public void testObtenerNullTextOfWebElementx2() {
        Boolean condicion = false;
        String elementoBusqueda = "buscar";
        condicion = SeleniumUtils.cadenaNulaoVacia(SeleniumUtils.obtenerOfTextWebElementx2(driver, driver, elementoBusqueda, 5, 2));
        Assert.assertTrue(condicion);
    }

    @Test(testName = "getTextOfWebElement", description = "Obtiene el texto de un elemento web", dependsOnMethods = "elementExist")
    public void getTextOfWebElement() {
        Boolean condicion = false;
        SeleniumUtils.enviarTexto(driver, driver, "textarea[id='APjFqb']", "texto");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "textarea[id='APjFqb']");
        String respuesta = SeleniumUtils.getTextOfWebElement(driver, elemento);
        condicion = SeleniumUtils.cadenaNulaoVacia(respuesta);
        Assert.assertFalse(condicion);
    }

    @Test(testName = "getTextNullOfWebElement", description = "Obtiene el texto nulo de un elemento web", dependsOnMethods = "elementExist")
    public void getTextNullOfWebElement() {
        SeleniumUtils.LimpiarElementoExistente(driver, driver, "textarea[id='APjFqb']");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("textarea[id='APjFqb']"));
        String respuesta = SeleniumUtils.getTextOfWebElement(driver, elemento);
        Assert.assertTrue(SeleniumUtils.cadenaNulaoVacia(respuesta));
    }

    @Test(testName = "getTextOfWebElement", description = "Obtiene el texto de un elemento web", dependsOnMethods = "elementExist")
    public void getTextOfWebElementJavaScript() {
        Boolean condicion = false;
        SeleniumUtils.LimpiarElementoExistente(driver, driver, "textarea[id='APjFqb']");
        SeleniumUtils.enviarTexto(driver, driver, "textarea[id='APjFqb']", "texto");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, By.xpath("textarea[id='APjFqb']"));
        String respuesta = SeleniumUtils.getTextUsingJavaScript(driver, elemento);
        condicion = SeleniumUtils.cadenaNulaoVacia(respuesta);
        Assert.assertTrue(condicion);
    }

    @Test(testName = "getTextNullOfWebElement", description = "Obtiene el texto nulo de un elemento web", dependsOnMethods = "elementExist")
    public void getTextNullOfWebElementJavaScript() {
        Boolean condicion = false;
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "/html/body/div[1]/div[3]/form/div[1]/div[1]/div[4]/center/input[2]");
        String respuesta = SeleniumUtils.getTextUsingJavaScript(driver, elemento);
        Assert.assertTrue(SeleniumUtils.cadenaNulaoVacia(respuesta));
    }

    @Test(testName = "clickElementIfExistAcierto", description = "Should make click in the specified element", dependsOnMethods = "elementExist")
    public void clickElementIfExistAcierto() {
        logParrafo("Se debe de dar click en un elemento especificado");
        Boolean variable = SeleniumUtils.clickElementIfExist(driver, driver, "/html/body/div[1]/div[6]/div[1]");
        LogsJB.waitForOperationComplete();
        Assert.assertTrue(variable, "No se logro hacer click en el elemento");
    }
/*
    @Test(testName = "clickElementIfExistBran")
    public void clickElementIfExistBran() {
        logParrafo("Se debe de dar click en un elemento especificado");
        driver.get("http://10.253.15.109/SIB/transaction/LOGON.asp");
        Boolean variable = SeleniumUtils.clickElementIfExist(driver, driver, "input[type='submit'][value='Conectar']");
        LogsJB.waitForOperationComplete();
        Assert.assertTrue(variable, "No se logro hacer click en el elemento");
    }
    */

    @Test(testName = "clickElementIfExistFallo", description = "Should make click in the specified element", dependsOnMethods = "elementExist")
    public void clickElementIfExistFallo() {
        logParrafo("Se debe de dar click en un elemento especificado");
        Assert.assertFalse(SeleniumUtils.clickElementIfExist(driver, driver, "//*[@id='xxxxxxxxxx']"),
                "No fallo al intentar dar click al elemento, por favor verificar el identifacor del elemento que no debe existir en la pagina");
    }

    @Test(testName = "clickElementx2intentsAcierto", description = "Should make click in the specified element 2 tries", dependsOnMethods = "elementExist")
    public void clickElementx2intentsAcierto() {
        logParrafo("Se debe de dar click en un elemento especificado");
        Assert.assertTrue(SeleniumUtils.clicktoElementx2intents(driver, driver, "/html/body/div[1]/div[6]/div[1]"));
    }

    @Test(testName = "clickElementx2intentsFallo", description = "Should make click in the specified element 2 tries", dependsOnMethods = "elementExist")
    public void clickElementx2intentsFallo() {
        logParrafo("Se debe de dar click en un elemento especificado");
        Assert.assertFalse(SeleniumUtils.clicktoElementx2intents(driver, driver, "//*[@id='XXXXXXXX']"));
    }

    @Test(testName = "getIdentificadorByAcierto", description = "Debería de traer el idenrtificador de un elemento web dado un By", dependsOnMethods = "elementExist")
    public void getIdentificadorByAcierto() {
        String respuesta = "";
        respuesta = SeleniumUtils.getIdentificadorBy(By.xpath("textarea[id='APjFqb']"));
        Assert.assertFalse(respuesta.isEmpty());
    }

    @Test(testName = "getIdentificadorByFallo", description = "Debería de traer el dato vacio de un elemento web dado un By", dependsOnMethods = "elementExist")
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

    @Test(testName = "cambiarZoomPlus", description = "Debería de aumentar el zoom de la pagina que se está visualizando", dependsOnMethods = "elementExist")
    public void cambiarZoomPlus() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, Keys.ADD));
    }

    @Test(testName = "cambiarZoomPlusError", description = "Debería de dar un error al momento de aumentar el zoom de la pagina que se está visualizando",
            expectedExceptions = {java.lang.AssertionError.class}, dependsOnMethods = "elementExist")
    public void cambiarZoomPlusError() {
        logParrafo("Se debe de dar un error al aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(null, 2, Keys.ADD));
    }

    @Test(testName = "cambiarZoomLess", description = "Debería de disminuir el zoom de la pagina que se está visualizando", dependsOnMethods = "elementExist")
    public void cambiarZoomLess() {
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, Keys.SUBTRACT));
    }

    @Test(testName = "cambiarZoomPlus", description = "Debería de aumentar el zoom de la pagina que se está visualizando", dependsOnMethods = "elementExist")
    public void cambiarZoomPlusCodigoEntero() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, 2));
    }

    @Test(testName = "cambiarZoomLess", description = "Debería de disminuir el zoom de la pagina que se está visualizando", dependsOnMethods = "elementExist")
    public void cambiarZoomLessCodigoEntero() {
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, 2));
    }

    @Test(testName = "cambiarZOOMMenos", description = "Debería de disminuir el zoom de la pagina que se está visualizando", dependsOnMethods = "elementExist")
    public void cambiarZOOMMenos() {
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOMMenos(driver, 2));
    }

    @Test(testName = "cambiarZOOMMenosFallo", description = "Debería dar un error al disminuir el zoom de la página visualizada", dependsOnMethods = "elementExist")
    public void cambiarZOOMMenosFallo() {
        logParrafo("Se debe dar un error al momento de disminuir la cantidad de Zoom que se realiza");
        SeleniumUtils.cambiarZOOMMenos(null, 2);
    }

    @Test(testName = "cambiarZOOMMas", description = "Debería de aumentar el zoom de la pagina que se está visualizando", dependsOnMethods = "elementExist")
    public void cambiarZoomMas() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOMMas(driver, 2));
    }

    @Test(testName = "cambiarZOOMMasFallo", description = "Debería de dar un error al aumentar el zoom de la pagina que se está visualizando",
            expectedExceptions = {java.lang.AssertionError.class}, dependsOnMethods = "elementExist")
    public void cambiarZoomMasFallo() {
        logParrafo("Se debe de dar un error al momento de aumentar la cantidad de Zoom que se realiza");
        Assert.assertFalse(SeleniumUtils.cambiarZOOMMas(null, 2));
    }

    @Test(testName = "scrollMouse", description = "Debería de hacer scroll con el mouse", dependsOnMethods = "elementExist")
    public void scrollMouse() {
        logParrafo("Se hará Scroll con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouse(2));
    }

    @Test(testName = "scrollMouseDown", description = "Debería de hacer scroll con el mouse hacia abajo", dependsOnMethods = "elementExist")
    public void scrollMouseDown() {
        logParrafo("Se hará Scroll hacia abajo con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouseDown(driver, 2));
    }

    @Test(testName = "scrollMouseUp", description = "Debería de hacer scroll con el mouse hacia arriba", dependsOnMethods = "elementExist")
    public void scrollMouseUp() {
        logParrafo("Se hará Scroll hacia arriba con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouseUp(driver, 2));
    }

    @Test(testName = "scrollMouseUpFallo", description = "Debería de dar un error al  hacer scroll con el mouse hacia arriba",
            dependsOnMethods = "elementExist")
    public void scrollMouseUpFallo() {
        logParrafo("Se dará un error cuando el driver haga Scroll hacia arriba con el mouse por medio de Selenium");
        Assert.assertFalse(SeleniumUtils.scrollMouseUp(null, 0));
    }

    @Test(testName = "selectOptionWithComment", description = "Debería de seleccionar la opcion de un select", dependsOnMethods = "elementExist")
    public void selectOptionWithComment() {
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        boolean respuesta = false;
        respuesta = SeleniumUtils.selectOption(driver, driver, "elemento", "opcion", "comentario", false);
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "getElementIfExist", description = "Debería de obtener un elemento web si existe", dependsOnMethods = "elementExist")
    public void getElementIfExist() {
        logParrafo("Se busca un elemento web para verificar si existe. Si existe, se obtiene su información");
        List<WebElement> respuesta = SeleniumUtils.getElementsIfExist(driver, driver, "div");
        Boolean exito = false;
        if (!respuesta.isEmpty()) {
            exito = true;
        }
        Assert.assertTrue(exito);
    }

    @Test(testName = "FrameActivo", description = "Obtiene el frame activo", dependsOnMethods = "elementExist")
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
//    @Test(testName = "waitImplicity",description = "Cambia de frame al enviado",dependsOnMethods = "elementExist")
//    public void waitImplicity(){
//        logParrafo("Espera a que apareza el elemento deseado");
//        boolean exito = false;
//        exito = SeleniumUtils.waitImplicity(driver,By.id("hfcr"));
//        if (exito){
//            Assert.assertTrue(exito);
//        }else{
//            Assert.assertFalse(exito);
//        }
//    }

    @Test(testName = "CambiarFrame", description = "Cambia de frame al enviado", dependsOnMethods = "elementExist")
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

    @Test(testName = "acceptConfirmTest", description = "Se debe de aceptar el cuadro de dialogo que se dispara", dependsOnMethods = "elementExist")
    public void acceptConfirmTest() {
        boolean respuesta = false;
        respuesta = SeleniumUtils.acceptConfirm(driver, true);
        simularConfirmJavascript();
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "acceptConfirmTestHanddleError", description = "Se debe disparar un error al aceptar el cuadro de dialogo que se dispara", dependsOnMethods = "elementExist")
    public void acceptConfirmTestHanddleError() {
        boolean respuesta = false;
        respuesta = SeleniumUtils.acceptConfirm(driver, true);
        simularConfirmJavascript();
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "acceptAlertTestWithErrors", description = "Se debe de aceptar el cuadro de dialogo que se dispara", dependsOnMethods = "elementExist")
    public void acceptAlertTest() {
        simularAlertJavascript();
        Assert.assertFalse(SeleniumUtils.acceptAlert(driver));
    }

    @Test(testName = "getElementsIfExistExito", description = "Se debe de obtener una lista de elementos, si es que existen", dependsOnMethods = "elementExist")
    public void getElementsIfExistExito() {
        List<WebElement> elementos = SeleniumUtils.getElementsIfExist(driver, driver, "/html/body/div[1]/div[2]/div/img");
        Assert.assertNotNull(elementos);
    }

    @Test(testName = "getElementsIfExistFallo", description = "Se debe de obtener un error al obtener una lista de elementos, si es que existen", dependsOnMethods = "elementExist")
    public void getElementsIfExistFallo() {
        List<WebElement> elementos = SeleniumUtils.getElementsIfExist(driver, driver, "xxxxxxxxxxx");
        boolean vacio = elementos.isEmpty();
        Assert.assertTrue(vacio);
    }

    @Test(testName = "getElementIfExistExito", description = "Se debe de obtener un elemento, si es que existe", dependsOnMethods = "elementExist")
    public void getElementIfExistExito() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "div#tophf");
        Assert.assertNotNull(elemento);
    }

    @Test(testName = "getElementIfExistFallo", description = "Se debe de obtener un error al obtener elemento, si es que existe", dependsOnMethods = "elementExist")
    public void getElementIfExistFallo() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "xxxxxxxxxxxxxxxx");
        Assert.assertNull(elemento);
    }

    @Test(testName = "clickToElementExito", description = "Click en un elemento", dependsOnMethods = "elementExist")
    public void clickToElementExito() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "/html/body/div[1]/div[6]/div[1]");
        Assert.assertTrue(SeleniumUtils.clickToElement(driver, elemento));
    }

    @Test(testName = "clickToElementFallo", description = "Error al dar click en un elemento", dependsOnMethods = "elementExist")
    public void clickToElementFallo() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "xxxxxxxxxxx");
        Assert.assertFalse(SeleniumUtils.clickToElement(driver, elemento));
    }

    @Test(testName = "obtenerWebElementsx2StringAcierto")
    public void obtenerWebElementsx2StringAcierto() {
        List<WebElement> elementos = SeleniumUtils.obtenerWebElementsx2(driver, driver, "/html/body/div[1]/div[2]/div/img");
        Assert.assertNotNull(elementos);
    }

    @Test(testName = "obtenerWebElementsx2StringFallo")
    public void obtenerWebElementsx2StringFallo() {
        List<WebElement> elementos = SeleniumUtils.obtenerWebElementsx2(driver, driver, "xxxxxxxx");
        boolean vacio = elementos.isEmpty();
        Assert.assertTrue(vacio);
    }

    @Test(testName = "obtenerWebElementsx2ByAcierto")
    public void obtenerWebElementsx2ByAcierto() {
        List<WebElement> elementos = SeleniumUtils.obtenerWebElementsx2(driver, driver, By.xpath("/html/body/div[1]/div[2]/div/img"));
        Assert.assertNotNull(elementos);
    }

    @Test(testName = "obtenerWebElementsx2ByFallo")
    public void obtenerWebElementsx2ByFallo() {
        List<WebElement> elementos = SeleniumUtils.obtenerWebElementsx2(driver, driver, By.xpath("xxxxxxxxxxxxxxxxxx"));
        boolean vacio = elementos.isEmpty();
        Assert.assertTrue(vacio);
    }

    @Test(testName = "getTextIfElementExistSinTiempoExito", description = "Obtiene el texto de un elemento web", dependsOnMethods = "elementExist")
    public void getTextIfElementExistSinTiempoExito() {
        String elemento = SeleniumUtils.getTextIfElementExist(driver, driver, "#APjFqb");
        Assert.assertNotNull(elemento);
    }

    @Test(testName = "getTextIfElementExistSinTiempoFallo", description = "Obtiene el texto de un elemento web", dependsOnMethods = "elementExist")
    public void getTextIfElementExistSinTiempoFallo() {
        String elemento = SeleniumUtils.getTextIfElementExist(driver, driver, "xxxxxxxxxxxxxxxxxx");
        Assert.assertNull(elemento);
    }

    /*
    @Test(testName = "getTextIfElementExistConTiempoExito", description = "Obtiene el texto de un elemento web", dependsOnMethods = "elementExist")
    public void getTextIfElementExistConTiempoExito() {
        String elemento = SeleniumUtils.getTextIfElementExist(driver, driver, "/html/body/div[1]/div[6]/div[1]", 4, 2);
        Assert.assertNotNull(elemento);
    }*/

    @Test(testName = "getTextIfElementExistConTiempoFallo", description = "Obtiene el texto de un elemento web", dependsOnMethods = "elementExist")
    public void getTextIfElementExistConTiempoFallo() {
        String elemento = SeleniumUtils.getTextIfElementExist(driver, driver, "xxxxxxxxxxxxxxxxxx", 4, 2);
        Assert.assertNull(elemento);
    }

    @Test(testName = "seleccionarElementoAcierto", description = "Debe de seleccionar ele elemento especirficado", dependsOnMethods = "elementExist")
    public void seleccionarElementoAcierto() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "/html/body/div[1]/div[6]/div[1]");
        Assert.assertTrue(SeleniumUtils.seleccionarElemento(driver, driver, elemento));
    }

    @Test(testName = "seleccionarElementoFallo", description = "Debe de seleccionar ele elemento especirficado", dependsOnMethods = "elementExist")
    public void seleccionarElementoFallo() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "xxxxxxxxxxxxx");
        Assert.assertFalse(SeleniumUtils.seleccionarElemento(driver, driver, elemento));
    }

    @Test(testName = "deseleccionarElementoAcierto", description = "Debe de seleccionar ele elemento especirficado", dependsOnMethods = "elementExist")
    public void deseleccionarElementoAcierto() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "/html/body/div[1]/div[6]/div[1]");
        SeleniumUtils.seleccionarElemento(driver, driver, elemento);
        Assert.assertTrue(SeleniumUtils.deseleccionarElemento(driver, driver, elemento));
    }

    @Test(testName = "deseleccionarElementoFallo", description = "Debe de seleccionar ele elemento especirficado", dependsOnMethods = "elementExist")
    public void deseleccionarElementoFallo() {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "xxxxxxxxxxxxx");
        Assert.assertFalse(SeleniumUtils.deseleccionarElemento(driver, driver, elemento));
    }

    @Test(testName = "obtenerTextoSeleccionadoSelectFallo", description = "Tiene que obtener un error al obtener el texto seleccionado ", dependsOnMethods = "elementExist")
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
    @Test(testName = "obtenerTextoSeleccionadoSelectAcierto", description = "Obtener el texto de un select", dependsOnMethods = "elementExist")
    public void obtenerTextoSeleccionadoSelectAcierto() {
        crearSelect("mi-select-clase");
        logParrafo("Debe de buscar un select para obtener el texto de ese elemento en específico ");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "//select[@class='mi-select-clase']");
        String respuesta = SeleniumUtils.obtenerTextoSeleccionadoSelect(driver, elemento);
        Assert.assertNotNull(respuesta);
    }

    @Test(testName = "getImageScreenshotExito", description = "Toma de captura de pantalla en la págian proporcionada", dependsOnMethods = "elementExist")
    public void getImageScreenshotExito() {
        logParrafo("Debería de tomar una captura de pantalla a un elemento específicado y veriricar que si exista ");
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "/html/body/div[1]/div[2]");
        File respuesta = SeleniumUtils.getImageScrennshot(driver, elemento);
        boolean condicion = respuesta.exists();
        Assert.assertTrue(condicion);
    }

    @Test(testName = "movetoframeIDorName", description = "Debe de cambiar de frame en la página principal de google", dependsOnMethods = "elementExist")
    public void movetoframeIDorName() {
        crearFrames("mi-frame-id", "mi-frame-clase");
        logParrafo("Primero se crea, por medio de javascript un frame para inyectar en la página de google. Posteriormente debe de cambiarse a ese frame ");
        boolean respuesta = SeleniumUtils.movetoframeIDorName(driver, driver, "mi-frame-id");
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "waitImplicity", description = "Debe de dar una espera implicita hasta que aparezca un elemento web ", dependsOnMethods = "elementExist")
    public void waitImplicity() {
        logParrafo("El método debe de dar una espera, con un máximo de 30 segundos para que aparezca un elemento en específico, si no, lanzará una excepción");
        boolean condicion = SeleniumUtils.waitImplicity(driver, By.xpath("//*[@id=\"hplogo\"]"));
        Assert.assertFalse(condicion);
    }

    @Test(testName = "waitCall", description = "Debería de hacer un waitCall exitosamente", dependsOnMethods = "elementExist")
    public void waitCall() {
        logParrafo("Debe de llamar al waitCall y retornar sin errores, pasando el driver y la duración");
        Assert.assertTrue(SeleniumUtils.waitCall(driver, 3));
    }

    @Test(testName = "waitImplicityForElementNotExist", description = "Debería de hacer un wait Implicity para elementos que no existan", dependsOnMethods = "elementExist")
    public void waitImplicityForElementNotExist() {
        logParrafo("Lo que debería de hacer es, una espera implicita pero para verificar si un elemento no existe ");
        Assert.assertTrue(SeleniumUtils.waitImplicityForElementNotExist(driver, By.xpath("xxxxxxxxxx"))
        );
    }
    //Segundo lote de métodos

    @Test(testName = "movetoframeforwebelement", description = "Debe de moverse de frame", dependsOnMethods = "elementExist")
    public void movetoframeforwebelement() {
        logParrafo("Primero debe de estar creado un frame por medio de javascript, posteriormente debe de cambiarse a ese frame ");
        //crearFrames("mi-frame-id2","mi-frame-clase2");
        driver.switchTo().defaultContent();
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, driver, "mi-frame-id");
        boolean respuesta = SeleniumUtils.movetoframeforwebelement(driver, elemento);
        Assert.assertTrue(respuesta);
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

    @Test(testName = "validarNullExito", description = "Debe de validar si un campo está nulo", dependsOnMethods = "elementExist")
    public void ValidarNullExito() {
        String campo = "";
        String nombre = "Nombre";
        boolean respuesta = SeleniumUtils.validarNull(campo, nombre);
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "validarNullFallo", description = "Debe de validar si un campo está nulo", dependsOnMethods = "elementExist")
    public void ValidarNullFallo() {
        String campo = "NoVacio";
        String nombre = "Nombre";
        boolean respuesta = SeleniumUtils.validarNull(campo, nombre);
        Assert.assertFalse(respuesta);
    }

    @Test(testName = "subirArchivoExito", description = "Debe de subir un archivo por medio de texto", dependsOnMethods = "elementExist")
    public void subirArchivoExito() {
        String id = "id-elementoFile";
        String clase = "clase-elementoFile";
        crearInputFile(driver, id, clase);
        String rutaArchivo = "C:/Users/mlemus/Pictures/Captura.PNG";
        boolean condicion = SeleniumUtils.subirArchivo(driver, "//*[@id=\"id-elementoFile\"]", rutaArchivo);
        Assert.assertTrue(condicion);
    }

    @Test(testName = "subirArchivoFallo", description = "Debe de lanzar un error al subir un archivo por medio de texto", dependsOnMethods = "elementExist")
    public void subirArchivoFallo() {
        String id = "id-elementoFile";
        String clase = "clase-elementoFile";
        crearInputFile(driver, id, clase);
        boolean condicion = SeleniumUtils.subirArchivo(null, null, null);
        Assert.assertFalse(condicion);
    }

    @Test(testName = "regresarFramePrincipal", description = "Debe de regresar al frame inicial", dependsOnMethods = "elementExist")
    public void regresarFramePrincipal() {
        logParrafo("Se debe de regresar al frame que se muestra cuando se ingresa por primera vez a la página ");
        boolean condicion = SeleniumUtils.regresarFramePrincipal(driver);
        Assert.assertTrue(condicion);
    }

    @Test(testName = "regresarFramePrincipalFallo", description = "Debe de saltar un error al regresar al frame inicial", dependsOnMethods = "elementExist")
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

    @Test(testName = "JsComandoExito", description = "Debe de ejecutar un comando de javascript", dependsOnMethods = "elementExist")
    public void JsComandoExito() {
        logParrafo("Debe de ejecutar un comando determinado de javascript por medio de un JavascriptExecutor ");
        String comando = "console.log('Comando ejecutado');";
        boolean respuesta = SeleniumUtils.ejecutarJsComando(driver, comando);
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "JsComandoFallo", description = "Debe de fallar al momento de ejecutar un comando de javascript", dependsOnMethods = "elementExist")
    public void JsComandoFallo() {
        logParrafo("Debe de ejecutar un comando determinado de javascript por medio de un JavascriptExecutor ");
        String comando = null;
        boolean respuesta = SeleniumUtils.ejecutarJsComando(driver, comando);
        Assert.assertFalse(respuesta);
    }

    @Test(testName = "selectOptionWithoutComment", description = "Debería de seleccionar la opcion de un select con comentario", dependsOnMethods = "elementExist")
    public void selectOptionWithoutComment() {
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        boolean respuesta = false;
        respuesta = SeleniumUtils.selectOption(driver, driver, "//select[@class='mi-select-clase']", "1");
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "selectStandarValues - WaitImplicity Overdrive", description = "Prueba de valores predeterminados y waitImplicity Overdrive", dependsOnMethods = "elementExist")
    public void selectStandarValues() {
        logParrafo("Funcion para comprobar varibales de control");
        Integer SerachTime = SeleniumUtils.getSearchTime();
        SeleniumUtils.setSearchTime(SerachTime);
        String inespecific = SeleniumUtils.getInespecific();
        SeleniumUtils.setInespecific(inespecific);
        Integer SearchRepetitionTime = SeleniumUtils.getSearchRepetitionTime();
        SeleniumUtils.setSearchRepetitionTime(SearchRepetitionTime);

        boolean condicion = SeleniumUtils.waitImplicity(driver, By.xpath("//*[@id=\"hplogo\"]"),true);
        Assert.assertFalse(condicion);
    }

    @Test(testName = "testObtenerTextOfWebElementx2 - Not Time", description = "Verifica que se obtenga el texto del elemento web especificado, sin controlador de tiempo", dependsOnMethods = "elementExist")
    public void testObtenerTextOfWebElementx2_NotTimeController() {
        Boolean condicion = false;
        String elementoBusqueda = "buscar";
        condicion = SeleniumUtils.cadenaNulaoVacia(SeleniumUtils.obtenerTextOfWebElementx2(driver, driver, elementoBusqueda));
        Assert.assertTrue(condicion);
    }

    @Test(testName = "obtenerWebElementx2", description = "Realiza 2 veces la busquedad de los elementos que cumplen con el criterio de busqueda especificado", dependsOnMethods = "elementExist")
    public void obtenerWebElementx2() {
        By elementoBusqueda = By.xpath("//select[@class='mi-select-clase']");
        SeleniumUtils.obtenerWebElementx2(driver, driver, elementoBusqueda);
        Assert.assertTrue(true);
    }

    @Test(testName = "KeyPress - Keys - Error", description = "Funcion para comprobar el Assert.Fail de KeyPress", dependsOnMethods = "elementExist")
    public void KeyPress_Keys_Error() {
        SeleniumUtils.keyPress(driver, null);
    }

    @Test(testName = "KeyPress - Keys - Overdrive - Error", description = "Funcion para comprobar el Assert.Fail de KeyPress", dependsOnMethods = "elementExist",expectedExceptions = {AssertionError.class})
    public void KeyPress_Keys_Overdrive_Error() {
        SeleniumUtils.keyPress(driver, null,true);
    }

    @Test(testName = "KeyPress - Int - Error", description = "Funcion para comprobar el Assert.Fail de KeyPress", dependsOnMethods = "elementExist")
    public void KeyPress_Int_Error() {
        SeleniumUtils.keyPress(driver, -1);

    }

    @Test(testName = "KeyPress - Int - Overdrive - Error", description = "Funcion para comprobar el Assert.Fail de KeyPress", dependsOnMethods = "elementExist")
    public void KeyPress_Int_Overdrive_Error() {
        SeleniumUtils.keyPress(driver, -1,true);

    }

    @Test(testName = "getElementIfExist_By", description = "Debería de obtener un elemento By si existe", dependsOnMethods = "elementExist")
    public void getElementIfExist_By() {
        logParrafo("Se busca un elemento web By para verificar si existe. Si existe, se obtiene su información");
        //By elemento = By.xpath("div.Q3DXx");
        By elemento = By.xpath("/html/body/div[1]/div[2]/div/img");
        List<WebElement> respuesta = SeleniumUtils.getElementsIfExist(driver, driver, elemento);
        Boolean exito = false;
        if (!respuesta.isEmpty()) {
            exito = true;
        }
        Assert.assertTrue(exito);
    }

    @Test(testName = "SendKeys Element Search Google",description = "Envia carácter por carácter al elemento especificado", dependsOnMethods = "elementExist")
    public void enviarTxtforKeyPress() {
        logParrafo("Envia carácter por carácter al elemento especificado");
        SeleniumUtils.enviarTxtforKeyPress(driver,driver,"textarea[id='APjFqb']","hola");
    }

    @Test(testName = "waitImplicityForElementNotExist - Error", description = "Debería de hacer un wait Implicity para elementos que no existan pero dara error", dependsOnMethods = "elementExist")
    public void waitImplicityForElementNotExist_Error() {
        logParrafo("Lo que debería de hacer es, una espera implicita pero para verificar si un elemento no existe");
        Assert.assertFalse(SeleniumUtils.waitImplicityForElementNotExist(driver, null));
    }

    @Test(testName = "waitImplicityForElementNotExist OverDrive", description = "Debería de hacer un wait Implicity para elementos que no existan,tomando en cuenta la bandera para el assert", dependsOnMethods = "elementExist")
    public void waitImplicityForElementNotExist_Overdrive() {
        logParrafo("Lo que debería de hacer es, una espera implicita pero para verificar si un elemento no existe ");
        Assert.assertTrue(SeleniumUtils.waitImplicityForElementNotExist(driver, By.xpath("xxxxxxxxxx"),true)
        );
    }

    @Test(testName = "waitImplicityForElementNotExist OverDrive - Error", description = "Debería de hacer un wait Implicity para elementos que no existan,tomando en cuenta la bandera para el assert", dependsOnMethods = "elementExist")
    public void waitImplicityForElementNotExist_Overdrive_Error() {
        logParrafo("Lo que debería de hacer es, una espera implicita pero para verificar si un elemento no existe");
        Assert.assertFalse(SeleniumUtils.waitImplicityForElementNotExist(driver, null,false));
    }

    @Test(testName = "cambiarZoomPlus OverDrive", description = "Debería de aumentar el zoom de la pagina que se está visualizando, tomando en cuenta la bandera", dependsOnMethods = "elementExist")
    public void cambiarZoomPlus_OverDrive() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, Keys.ADD,true));
    }

    @Test(testName = "cambiarZoomLess Overdrive", description = "Debería de disminuir el zoom de la pagina que se está visualizando, tomando en cuenta la bandera", dependsOnMethods = "elementExist")
    public void cambiarZoomLessCodigoEntero_Overdrive() {
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOM(driver, 2, 2,true));
    }

    @Test(testName = "cambiarZOOMMenos Overdrive", description = "Debería de disminuir el zoom de la pagina que se está visualizando, tomando en cuenta la bandera", dependsOnMethods = "elementExist")
    public void cambiarZOOMMenos_Overdrive() {
        logParrafo("Se debe de disminuir la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOMMenos(driver, 2,true));
    }

    @Test(testName = "cambiarZOOMMas Overdrive", description = "Debería de aumentar el zoom de la pagina que se está visualizando, tomando en cuenta la bandera", dependsOnMethods = "elementExist")
    public void cambiarZoomMas_Overdrive() {
        logParrafo("Se debe de aumentar la cantidad de Zoom que se realiza");
        Assert.assertTrue(SeleniumUtils.cambiarZOOMMas(driver, 2,true));
    }

    @Test(testName = "scrollMouse Overdrive", description = "Debería de hacer scroll con el mouse, tomando en cuenta la bandera", dependsOnMethods = "elementExist")
    public void scrollMouse_Overdrive() {
        logParrafo("Se hará Scroll con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouse(2,true));
    }

    @Test(testName = "scrollMouseDown Overdrive", description = "Debería de hacer scroll con el mouse hacia abajo, tomando en cuenta la bandera", dependsOnMethods = "elementExist")
    public void scrollMouseDown_Overdrive() {
        logParrafo("Se hará Scroll hacia abajo con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouseDown(driver, 2,true));
    }

    @Test(testName = "scrollMouseUp Overdrive", description = "Debería de hacer scroll con el mouse hacia arriba, tomando en cuenta la bandera", dependsOnMethods = "elementExist")
    public void scrollMouseUp_Overdrive() {
        logParrafo("Se hará Scroll hacia arriba con el mouse por medio de Selenium");
        Assert.assertTrue(SeleniumUtils.scrollMouseUp(driver, 2,true));
    }

    @Test(testName = "selectOptionWithComment -Overdrive seteo", description = "Debería de seleccionar la opcion de un select", dependsOnMethods = "elementExist")
    public void selectOptionWithComment_Overdrive_Seteo() {
        logParrafo("El proceso completo, debería de darle click al select, luego se despliegan las opciones y se selecciona la especificada");
        boolean respuesta = false;
        respuesta = SeleniumUtils.selectOption(driver, driver, "elemento", "opcion", "comentario");
        Assert.assertTrue(respuesta);
    }

    @Test(testName = "obtenerTextWebElementx2", description = "Realiza 2 veces la busquedad de el texto de un elemento", dependsOnMethods = "elementExist")
    public void obtenerTextWebElementx2() {
        logParrafo("Realiza 2 veces la busquedad de el texto de un elemento");
        SeleniumUtils.obtenerTextWebElementx2(driver,driver,"textarea[id='APjFqb']");
    }

    @Test(testName = "obtenerTextoElementoX2", description = "Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna", dependsOnMethods = "elementExist")
    public void obtenerTextoElementoX2() {
        logParrafo("Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna");
        SeleniumUtils.obtenerTextoElementoX2(driver,driver,"textarea[id='APjFqb']");
    }

    @Test(testName = "obtenerTextoElementoX2 - Time Controller", description = "Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna, controlado por tiempo de duracion de espera", dependsOnMethods = "elementExist")
    public void obtenerTextoElementoX2_TimeController() {
        logParrafo("Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna controlado por tiempo de duracion de espera");
        SeleniumUtils.obtenerTextoElementoX2(driver,driver,"textarea[id='APjFqb']",5,5);
    }

    @Test(testName = "enviarTexto", description = "Envía un texto al elemento indicado, si este existe en el contexto actual", dependsOnMethods = "elementExist")
    public void enviarTexto() {
        logParrafo("Envía un texto al elemento indicado, si este existe en el contexto actual");
        driver.navigate().refresh();
        SeleniumUtils.threadslepp(2000);
        SeleniumUtils.enviarTexto(driver,driver,"textarea[id='APjFqb']","hola");
    }

    @Test(testName = "enviarTexto Overdrive", description = "Envía un texto al elemento indicado, si este existe en el contexto actual", dependsOnMethods = "enviarTexto")
    public void enviarTexto_Overdrive() {
        logParrafo("Envía un texto al elemento indicado, si este existe en el contexto actual");
        SeleniumUtils.enviarTexto(driver,driver,"textarea[id='APjFqb']","hola",true);
    }

    @Test(testName = "sendKeystoElementvalidValueX2", description = "Envia el texto al elemento especificado 2 veces seguidas, confirmando con un enter", dependsOnMethods = "enviarTexto_Overdrive",priority = 1)
    public void sendKeystoElementvalidValueX2() {
        logParrafo("Envia el texto al elemento especificado 2 veces seguidas, confirmando con un enter");
        SeleniumUtils.sendKeystoElementvalidValueX2(driver,driver,"textarea[id='APjFqb']","hola");
    }

    @Test(testName = "sendKeystoElementvalidValueForMap", description = "Envia el texto al elemento especificado", dependsOnMethods = "sendKeystoElementvalidValueX2")
    public void sendKeystoElementvalidValueForMap() {
        logParrafo("Envia el texto al elemento especificado");
        SeleniumUtils.sendKeystoElementvalidValueForMap(driver,driver,"textarea[id='APjFqb']","hola");
    }

    @Test(testName = "sendKeystoElementvalidValue", description = "Envia el texto al elemento especificado", dependsOnMethods = "sendKeystoElementvalidValueForMap")
    public void sendKeystoElementvalidValue() {
        logParrafo("Envia el texto al elemento especificado");
        SeleniumUtils.sendKeystoElementvalidValue(driver,driver,"textarea[id='APjFqb']","hola");
    }

    @Test(testName = "enviarTextoSiValido", description = "Envía un texto a un elemento si el valor proporcionado no es nulo, vacío o igual a un valor específico.", dependsOnMethods = "sendKeystoElementvalidValue")
    public void enviarTextoSiValido() {
        logParrafo(" Envía un texto a un elemento si el valor proporcionado no es nulo, vacío o igual a un valor específico");
        SeleniumUtils.enviarTextoSiValido(driver,driver,"textarea[id='APjFqb']","hola");
    }

    @Test(testName = "enviarTextoSiValido", description = "Envía un texto a un elemento si el valor proporcionado no es nulo, vacío o igual a un valor específico.", dependsOnMethods = "enviarTextoSiValido")
    public void enviarTextoSiValidoX2() {
        logParrafo(" Envía un texto a un elemento si el valor proporcionado no es nulo, vacío o igual a un valor específico");
        SeleniumUtils.enviarTextoSiValidoX2(driver,driver,"textarea[id='APjFqb']","hola");
    }


    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}



