package io.github.josecarlosbran.SeleniumUtils;

import com.josebran.LogsJB.LogsJB;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Path;
import java.text.Normalizer;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

public class SeleniumUtils {
    @Getter(AccessLevel.PACKAGE)
    static ExecutorService seleniumEjecutor = Executors.newCachedThreadPool();
    private static String inespecific = "N/E";
    private static Integer searchTime = 2500;
    private static Integer searchRepetitionTime = 50;

    // Métodos para obtener los valores actuales
    public static String getInespecific() {
        return inespecific;
    }

    // Método para cambiar el valor de 'inespecific' usando Reflection
    public static void setInespecific(String newInespecific) throws IllegalAccessException {
        setFieldValue("inespecific", newInespecific);
    }

    // Metodo para obtener el valor searchTime
    public static Integer getSearchTime() {
        return searchTime;
    }

    // Método para cambiar el valor de 'searchTime' usando Reflection
    public static void setSearchTime(Integer newSearchTime) throws IllegalAccessException {
        setFieldValue("searchTime", newSearchTime);
    }

    // Metodo para obtener el valor searchRepetitionTime
    public static Integer getSearchRepetitionTime() {
        return searchRepetitionTime;
    }

    // Método para cambiar el valor de 'searchRepetitionTime' usando Reflection
    public static void setSearchRepetitionTime(Integer newSearchRepetitionTime) throws IllegalAccessException {
        setFieldValue("searchRepetitionTime", newSearchRepetitionTime);
    }

    // Método privado que maneja la lógica de cambio de cualquier campo usando Reflection
    public static void setFieldValue(String fieldName, Object newValue) throws IllegalAccessException {
        FieldUtils.writeDeclaredStaticField(SeleniumUtils.class, fieldName, newValue, true);
    }

    /***
     * Método que presiona una tecla del teclado simulado por selenium
     * @param driver Driver que manipula el navegador y realiza las acciones
     * @param codigo Clave de la tecla que se desea presionar
     */
    public static void keyPress(WebDriver driver, Keys codigo) {
        keyPress(driver, codigo, false);
    }

    /***
     * Método que presiona una tecla del teclado simulado por selenium
     * @param driver Driver que manipula el navegador y realiza las acciones
     * @param codigo Clave de la tecla que se desea presionar
     * @param controlAssert Variable booleana para controlar si se ejecuta el Assert.fail o no
     */
    public static void keyPress(WebDriver driver, Keys codigo, boolean controlAssert) {
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(codigo).perform();
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error inesperado al presionar una tecla: ");
            if (controlAssert) {
                Assert.fail("Error inesperado al presionar una tecla: ");
            }
        }
    }

    /***
     * Presiona la tecla indicada en él condigo numerico indicado
     * @param driver Driver que manipula el navegador y realiza las acciones
     * @param codigo Codigo numerico de la tecla que queremos presionar
     */
    public static void keyPress(WebDriver driver, int codigo) {
        keyPress(driver, codigo, false);
    }

    /***
     * Presiona la tecla indicada en él condigo numerico indicado
     * @param driver Driver que manipula el navegador y realiza las acciones
     * @param codigo Codigo numerico de la tecla que queremos presionar
     * @param controlAssert Variable booleana para controlar si se ejecuta el Assert.fail o no
     */
    public static void keyPress(WebDriver driver, int codigo, boolean controlAssert) {
        try {
            char asciiValue = (char) codigo;
            Actions actions = new Actions(driver);
            actions.keyDown(String.valueOf(asciiValue)).keyUp(String.valueOf(asciiValue)).perform();
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error inesperado al presionar una tecla: ");
            if (controlAssert) {
                Assert.fail("Error inesperado al presionar una tecla: ");
            }
        }
    }

    /***
     * Obtiene una espera fluida, con el fin de mejorar los tiempos.
     * @param driver driver que está controlando el navegador.
     * @param timeduration tiempo máximo de la espera en mili segundos.
     * @param timerepetition tiempo de espera entre cada intento en mili segundos, tiene que ser menor al tiempo máximo.
     * @return Retorna una espera del tipo WebDriver, la cual es fluida, no causara una excepción si la operación realizada falla.
     */
    public static Wait<WebDriver> getFluentWait(WebDriver driver, int timeduration, int timerepetition) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofMillis(timeduration))
                .pollingEvery(Duration.ofMillis(timerepetition))
                .ignoring(UnhandledAlertException.class)
                .ignoring(Exception.class)
                .ignoring(TimeoutException.class)
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .ignoring(InvalidElementStateException.class)
                .ignoring(JavascriptException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(org.openqa.selenium.remote.UnreachableBrowserException.class)
                .ignoring(InvalidSelectorException.class)
                .ignoring(WebDriverException.class)
                .ignoring(ElementClickInterceptedException.class);
    }

    /***
     * Verifica si el elemento en cuestión está habilitado
     * @param element Elemento que se desea verificar si esta habilitado
     * @return Retorna True si el elemento esta habilitado, False si no esta habilitado o visible.
     */
    public static boolean ElementoDeshabilitado(WebElement element) {
        if (Objects.isNull(element)) {
            LogsJB.warning("El elemento es nulo. No se puede verificar la habilitación.");
            return true; // Se puede considerar deshabilitado si el elemento es nulo
        }
        // Verificar si el elemento está deshabilitado o no visible
        if (!element.isEnabled() || !element.isDisplayed()) {
            LogsJB.debug("El elemento está deshabilitado o no visible.");
            return true;
        }
        // Si se llega hasta aquí, el elemento está habilitado y visible
        return false;
    }

    /**
     * Función que convierte un array de objetos en un ArrayList de cadenas
     *
     * @param object El Array de objetos que se desea convertir en cadenas
     * @return Un ArrayList que contiene las representaciones en forma de cadena de los objetos
     */
    public static ArrayList<String> convertObjectToArrayString(Object[] object) {
        try {
            ArrayList<String> array = new ArrayList<>();
            for (Object o : object) {
                array.add((o.toString()));
            }
            return array;
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error inesperado al convertir un array de objetos en un ArrayList de cadenas: ");
        }
        return null;
    }

    /***
     * Hace una pausa sobre el hilo en ejecución por el tiempo especificado
     * @param milisegundos Tiempo en milisegundos que se detendrá la ejecución
     */
    public static void threadslepp(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error inesperado al hacer una pausa en el hilo de ejecución: ");
        }
    }

    /***
     * Limpia el elemento
     * @param driver Driver que manipula el navegador
     * @param element Elemento que se desea limpiar
     * @return True si logra limpiar el elemento, False si no logra limpiar el elemento o si sucede un error.
     */
    public static boolean cleanElement(WebDriver driver, WebElement element) {
        try {
            if (Objects.isNull(element)) {
                LogsJB.warning("El elemento es nulo. No se puede limpiar.");
                return false;
            }
            // Limpiar el elemento
            element.clear();
            LogsJB.debug("Limpió el elemento por medio del API estandar de selenium.");
            SeleniumUtils.posicionarmeEn(driver, element);
            return true;
        } catch (Exception e) {
            LogsJB.debug("No fue posible limpiar el elemento por medio del api estandar, se intenta por medio de JavaScript");
            return SeleniumUtils.limpiarTextUsingJavaScript(driver, element);
        }
    }

    /**
     * Limpia el elemento por medio de javascript
     *
     * @param driver  Driver que manipula el navegador
     * @param element Elemento que se desea limpiar
     * @return True si logra limpiar el elemento, False si no logra limpiar el elemento o si sucede un error.
     */
    public static boolean limpiarTextUsingJavaScript(WebDriver driver, WebElement element) {
        if (Objects.isNull(element)) {
            LogsJB.warning("El elemento es nulo. No se puede limpiar.");
            return false;
        }
        // Utilizar JavaScript para borrar el contenido del elemento
        String text = SeleniumUtils.limpiarTextJavaScript(driver, element);
        if (!cadenaNulaoVacia(text)) {
            LogsJB.debug("No fue posible limpiar el elemento por medio de JavaScript.");
            return false;
        }
        LogsJB.debug("Limpió el elemento por medio de JavaScript.");
        return true;
    }

    /***
     * Limpiar el texto de un elemento por medio de JavaScript
     * @param driver Driver que manipula el navegador
     * @param element Elemento que se desea limpiar
     * @return Retorna el texto limpio, si logra limpiarlo, de lo contrario retorna el texto original
     */
    public static String limpiarTextJavaScript(WebDriver driver, WebElement element) {
        String text = null;
        String[] propiedades = new String[]{"innerText", "value", "textContent"};
        for (String propiedad : propiedades) {
            text = SeleniumUtils.getTextOfWebElement(driver, element);
            if (!cadenaNulaoVacia(text)) {
                SeleniumUtils.ejecutarJsComando(driver, "arguments[0]." +
                        propiedad +
                        " = '';", element);
                text = SeleniumUtils.getTextOfWebElement(driver, element);
            }
            if (cadenaNulaoVacia(text)) {
                break;
            }
        }
        return text;
    }

    /**
     * Función que se utiliza para posicionarse en un elemento, utilizando un controlador WebDriver
     *
     * @param driver   WebDriver se utiliza para interactuar con el entorno
     * @param elemento WebElement el elemento al que se desea posicionarse
     */
    public static void posicionarmeEn(WebDriver driver, WebElement elemento) {
        if (Objects.isNull(elemento)) {
            return;
        }
        LogsJB.debug("Posicionandonos en el elemento con scrollIntoViewIfNeeded.");
        // Opción 3: Usar scrollIntoViewIfNeeded (obsoleto en algunos navegadores)
        SeleniumUtils.ejecutarJsComando(driver, "arguments[0].scrollIntoViewIfNeeded(true);", elemento);
    }

    /***
     * Verifica si un elemento existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param searchContext contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda
     * @return Retorna True si el elemento Existe, caso contrario retorna False
     */
    public static boolean ElementoExistente(WebDriver driver, SearchContext searchContext, String element) {
        //Para optimizar el tiempo de respuestá
        LogsJB.debug("* ");
        LogsJB.debug(" Buscara si existe el elemento indicado: " + element);
        LogsJB.debug("* ");
        CountDownLatch latchId = new CountDownLatch(1);
        CountDownLatch latchClassName = new CountDownLatch(1);
        CountDownLatch latchCss = new CountDownLatch(1);
        CountDownLatch latchTagName = new CountDownLatch(1);
        CountDownLatch latchLinkText = new CountDownLatch(1);
        CountDownLatch latchPartialLinkText = new CountDownLatch(1);
        CountDownLatch latchXpath = new CountDownLatch(1);
        CountDownLatch latchName = new CountDownLatch(1);
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        //Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<Boolean> futureId = SeleniumParallel.elementExist(wait, searchContext, "id", element, latchId);
        Future<Boolean> futureClassName = SeleniumParallel.elementExist(wait, searchContext, "class name", element, latchClassName);
        Future<Boolean> futureCss = SeleniumParallel.elementExist(wait, searchContext, "css selector", element, latchCss);
        Future<Boolean> futureTagName = SeleniumParallel.elementExist(wait, searchContext, "tag name", element, latchTagName);
        Future<Boolean> futureLinkText = SeleniumParallel.elementExist(wait, searchContext, "link text", element, latchLinkText);
        Future<Boolean> futurePartialLinkText = SeleniumParallel.elementExist(wait, searchContext, "partial link text", element, latchPartialLinkText);
        Future<Boolean> futureXpath = SeleniumParallel.elementExist(wait, searchContext, "xpath", element, latchXpath);
        Future<Boolean> futureName = SeleniumParallel.elementExist(wait, searchContext, "name", element, latchName);
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        long startTime = System.currentTimeMillis();
        long endTime = startTime + SeleniumUtils.getSearchTime();
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        boolean result = waitForElement(endTime, SeleniumUtils.getSearchRepetitionTime(),
                latchId, futureId, latchClassName, futureClassName, latchCss, futureCss, latchTagName, futureTagName, latchLinkText, futureLinkText, latchPartialLinkText, futurePartialLinkText, latchXpath, futureXpath, latchName, futureName);
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        LogsJB.debug("Fecha actual: " + new Date(System.currentTimeMillis()));
        if (!result && System.currentTimeMillis() >= endTime) {
            LogsJB.info(" No Existe el elemento especificado: " + element);
        } else {
            return result;
        }
        //Retorna Falso si el elemento no Existe
        return false;
    }

    /***
     * Limpia el elemento especificado, si existe en el contexto actual.
     * @param driver Driver que está manipulando el navegador
     * @param searchContext contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento, por medio del cual se realizara la busquedad
     * @return Retorna True si logra limpiar el elemento, False si no logra limpiar el elemento o sucede un error en la ejecución de esta
     * instrucción.
     */
    public static boolean LimpiarElementoExistente(WebDriver driver, SearchContext searchContext, String element) {
        //Para optimizar el tiempo de respuestá
        LogsJB.debug("* ");
        LogsJB.debug(" Si existe el elemento indicado, lo limpiara: " + element);
        LogsJB.debug("* ");
        CountDownLatch latchId = new CountDownLatch(1);
        CountDownLatch latchClassName = new CountDownLatch(1);
        CountDownLatch latchCss = new CountDownLatch(1);
        CountDownLatch latchTagName = new CountDownLatch(1);
        CountDownLatch latchLinkText = new CountDownLatch(1);
        CountDownLatch latchPartialLinkText = new CountDownLatch(1);
        CountDownLatch latchXpath = new CountDownLatch(1);
        CountDownLatch latchName = new CountDownLatch(1);
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        // Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<Boolean> futureId = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, "id", element, latchId);
        Future<Boolean> futureClassName = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, "class name", element, latchClassName);
        Future<Boolean> futureCss = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, "css selector", element, latchCss);
        Future<Boolean> futureTagName = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, "tag name", element, latchTagName);
        Future<Boolean> futureLinkText = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, "link text", element, latchLinkText);
        Future<Boolean> futurePartialLinkText = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, "partial link text", element, latchPartialLinkText);
        Future<Boolean> futureXpath = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, "xpath", element, latchXpath);
        Future<Boolean> futureName = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, "name", element, latchName);
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        long startTime = System.currentTimeMillis();
        long endTime = startTime + SeleniumUtils.getSearchTime();
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        boolean result = waitForElement(endTime, SeleniumUtils.getSearchRepetitionTime(),
                latchId, futureId, latchClassName, futureClassName, latchCss, futureCss, latchTagName, futureTagName, latchLinkText, futureLinkText, latchPartialLinkText, futurePartialLinkText, latchXpath, futureXpath, latchName, futureName);
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        LogsJB.debug("Fecha actual: " + new Date(System.currentTimeMillis()));
        if (!result && System.currentTimeMillis() >= endTime) {
            LogsJB.info(" No logro limpiar el elemento especificado: " + element);
        } else {
            return result;
        }
        //Retorna Falso si el elemento no Existe
        return false;
    }

    /**
     * Función que se utiliza para verificar si un valor es inválido o no cumple ciertos criterios predefinidos
     *
     * @param value Valor que se desea verificar para determinar si es inválido
     * @return Verdadero si el valor es considerado, o Falso si el valor cumple con los citerios válidos
     */
    public static boolean esValorValido(String value) {
        if (!SeleniumUtils.cadenaNulaoVacia(value)) {
            return !value.equalsIgnoreCase(SeleniumUtils.getInespecific());
        }
        return false;
    }

    /****
     * Verifica si una cadena está vacía o es nula
     * @param cadena Cadena a Validar
     * @return Retorna True si la cadena envíada está vacía o nula, de lo contrario retorna false
     */
    public static boolean cadenaNulaoVacia(String cadena) {
        return Objects.isNull(cadena) || cadena.trim().isEmpty();
    }

    /***
     * Toma la captura de pantalla sobre el elemento proporcionado como parametro
     * @param elementScreenshot Elemento sobre el cual se desea tomar la captura de pantalla
     * @return Retorna un objeto File que representa la captura de pantalla del elemento, si no logra tomar la captura de pantalla retorna null
     */
    public static File getImageScreeenshotWebElement(WebElement elementScreenshot) {
        if (!Objects.isNull(elementScreenshot)) {
            return elementScreenshot.getScreenshotAs(OutputType.FILE);
        }
        LogsJB.warning("No pudo tomar la captura de pantalla del elemento indicado, retorna null");
        return null;
    }

    /***
     * Obtiene el elemento web por medio de un locator y un elemento
     * @param driver Driver que manipula el navegador
     * @param locator Tipo de locator que se utilizará para buscar el elemento
     * @param element Criterio que se utilizará para buscar el elemento
     * @return Retorna el WebElement si logra encontrarlo, de lo contrario retorna null
     */
    public static WebElement getElementByLocator(WebDriver driver, String locator, String element) {
        switch (locator) {
            case "xpath":
                return driver.findElement(By.xpath(element));
            case "css selector":
                return driver.findElement(By.cssSelector(element));
            case "id":
                return driver.findElement(By.id(element));
            case "tag name":
                return driver.findElement(By.tagName(element));
            case "name":
                return driver.findElement(By.name(element));
            case "link text":
                return driver.findElement(By.linkText(element));
            case "partial link text":
                return driver.findElement(By.partialLinkText(element));
            case "class name":
                return driver.findElement(By.className(element));
        }
        return null;
    }

    /**
     * Actualiza la referencia al elemento si está disponible en el contexto actual de selenium
     *
     * @param driver   Driver que manipula el navegador
     * @param elemento Elemento a refrescar
     * @return null si no logra refrescar el elemento, caso contrario la referencia al elemento
     */
    public static WebElement RefreshReferenceToElement(WebDriver driver, WebElement elemento) {
        if (!Objects.isNull(elemento) || !Objects.isNull(driver)) {
            String locator = getTermOrLocator(elemento, true);
            String term = getTermOrLocator(elemento, false);
            return getElementByLocator(driver, locator, term);
        }
        LogsJB.info("No fue posible refrescar la referencia al elemento");
        return null;
    }

    /**
     * Obtiene el termino o el locator de un elemento web
     *
     * @param elemento      Elemento web del cual se desea obtener el termino o el locator
     * @param locatorOrTerm True si se desea obtener el locator, False si se desea obtener el termino
     * @return Retorna el locator o el termino del elemento web
     */
    public static String getTermOrLocator(WebElement elemento, boolean locatorOrTerm) {
        String tempelement = elemento.toString().split(" -> ")[1];
        String[] data = tempelement.split(": ");
        String locator = data[0];
        if (locatorOrTerm) {
            return locator;
        }
        String term = data[1];
        // Verificar si el string termina con "]},"
        if (term.endsWith("]}")) {
            // Eliminar los últimos dos carácteres
            term = term.substring(0, term.length() - 2);
        } else if (term.endsWith("]]") && (StringUtils.equalsIgnoreCase(locator, "xpath") || StringUtils.equalsIgnoreCase(locator, "css selector"))) {
            // Eliminar los últimos dos carácteres
            term = term.substring(0, term.length() - 1);
        }
        return term;
    }

    /**
     * @param driver     El controlador de WebDriver utilizado para interactuar con el navegador.
     * @param element    El elemento web al que se enviarán las teclas. Si el elemento es nulo, el método retornará `false`.
     * @param keysToSend Las secuencias de caracteres que se enviarán al elemento.
     * @return `true` si las teclas se ingresaron correctamente en el elemento; de lo contrario, `false`
     * .
     */
    public static boolean sendKeysToElement(WebDriver driver, WebElement element, CharSequence... keysToSend) {
        boolean result = false;
        try {
            if (Objects.isNull(element)) {
                LogsJB.warning("El elemento es nulo. No se puede enviar el texto.");
                result = false;
            }
            SeleniumUtils.posicionarmeEn(driver, element);
            element.sendKeys(keysToSend);
            String text = SeleniumUtils.getTextOfWebElement(driver, element);
            String temp = Arrays.toString(keysToSend).substring(1, Arrays.toString(keysToSend).length() - 1);
            if (!cadenaNulaoVacia(text) || cadenaNulaoVacia(temp)) {
                result = true;
            }
            return SeleniumUtils.sendKeyTextJavaScript(driver, element, temp, keysToSend);
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error inesperado al intentar enviar el texto: " + Arrays.toString(keysToSend) +
                    " al  elemento: " + element.toString());
            result = false;
        } finally {
            return result;
        }
    }

    /***
     * Envia el texto al elemento indicado, si este existe en el contexto actual.
     * @param driver Driver que está manipulando el navegador
     * @param element Elemento al que se enviará el texto
     * @param temp Texto que se enviará al elemento
     * @param keysToSend Caracteres que se enviarán al elemento
     * @return Retorna True si logra enviar el texto al elemento, False si no logra enviar el texto o si sucede un error en la ejecución de esta instrucción.
     */
    public static boolean sendKeyTextJavaScript(WebDriver driver, WebElement element, String temp, CharSequence... keysToSend) {
        boolean result = false;
        String[] propiedades = new String[]{"innerText", "value", "textContent"};
        String text = null;
        for (String propiedad : propiedades) {
            text = SeleniumUtils.getTextOfWebElement(driver, element);
            if (cadenaNulaoVacia(text) || cadenaNulaoVacia(temp)) {
                SeleniumUtils.ejecutarJsComando(driver, "arguments[0].setAttribute('" +
                        propiedad +
                        "', '" + keysToSend + "')", element);
                text = element.getAttribute("value");
                if (!cadenaNulaoVacia(text) || cadenaNulaoVacia(temp)) {
                    result = true;
                }
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Envía un texto al elemento indicado, si este existe en el contexto actual.
     *
     * @param driver        Driver que está manipulando el navegador
     * @param searchContext contexto en el que se desea buscar el elemento
     * @param element       Atributo del elemento, por medio del cual se realizara la busqueda
     * @param Texto         Texto a envíar al elemento indicado
     * @return Retorna True si encontro el elemento y pudo setear el texto.
     */
    public static boolean sendKeysIfElementExist(WebDriver driver, SearchContext searchContext, String element, CharSequence... Texto) {
        //Para optimizar el tiempo de respuestá
        LogsJB.debug("* ");
        LogsJB.debug(" Si existe el elemento " + element +
                ", enviara el texto: " + Arrays.toString(Texto).substring(1, Arrays.toString(Texto).length() - 1));
        LogsJB.debug("* ");
        CountDownLatch latchId = new CountDownLatch(1);
        CountDownLatch latchClassName = new CountDownLatch(1);
        CountDownLatch latchCss = new CountDownLatch(1);
        CountDownLatch latchTagName = new CountDownLatch(1);
        CountDownLatch latchLinkText = new CountDownLatch(1);
        CountDownLatch latchPartialLinkText = new CountDownLatch(1);
        CountDownLatch latchXpath = new CountDownLatch(1);
        CountDownLatch latchName = new CountDownLatch(1);
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        // Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<Boolean> futureId = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, "id", element, latchId, Texto);
        Future<Boolean> futureClassName = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, "class name", element, latchClassName, Texto);
        Future<Boolean> futureCss = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, "css selector", element, latchCss, Texto);
        Future<Boolean> futureTagName = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, "tag name", element, latchTagName, Texto);
        Future<Boolean> futureLinkText = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, "link text", element, latchLinkText, Texto);
        Future<Boolean> futurePartialLinkText = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, "partial link text", element, latchPartialLinkText, Texto);
        Future<Boolean> futureXpath = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, "xpath", element, latchXpath, Texto);
        Future<Boolean> futureName = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, "name", element, latchName, Texto);
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        long startTime = System.currentTimeMillis();
        long endTime = startTime + SeleniumUtils.getSearchTime();
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        boolean result = waitForElement(endTime, SeleniumUtils.getSearchRepetitionTime(),
                latchId, futureId, latchClassName, futureClassName, latchCss, futureCss, latchTagName, futureTagName, latchLinkText, futureLinkText, latchPartialLinkText, futurePartialLinkText, latchXpath, futureXpath, latchName, futureName);
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        LogsJB.debug("Fecha actual: " + new Date(System.currentTimeMillis()));
        if (!result && System.currentTimeMillis() >= endTime) {
            LogsJB.info(" No logro encontrar y setear el Texto: " + Arrays.toString(Texto) +
                    " en el elemento especificado: " + element);
        } else {
            return result;
        }
        //Retorna Falso si el elemento no Existe
        return false;
    }

    public static Boolean waitForElement(
            long endTime,
            int searchRepetitionTime,
            CountDownLatch latchId, Future<Boolean> futureId,
            CountDownLatch latchClassName, Future<Boolean> futureClassName,
            CountDownLatch latchCss, Future<Boolean> futureCss,
            CountDownLatch latchTagName, Future<Boolean> futureTagName,
            CountDownLatch latchLinkText, Future<Boolean> futureLinkText,
            CountDownLatch latchPartialLinkText, Future<Boolean> futurePartialLinkText,
            CountDownLatch latchXpath, Future<Boolean> futureXpath,
            CountDownLatch latchName, Future<Boolean> futureName) {
        Boolean result = waitForElementGeneric(endTime, searchRepetitionTime, latchId, futureId, latchClassName, futureClassName,
                latchCss, futureCss, latchTagName, futureTagName, latchLinkText, futureLinkText, latchPartialLinkText, futurePartialLinkText, latchXpath, futureXpath, latchName, futureName);
        if (Objects.isNull(result)) {
            result = false;
        }
        return result;
    }

    public static <T> T waitForElementGeneric(
            long endTime,
            int searchRepetitionTime,
            CountDownLatch latchId, Future<T> futureId,
            CountDownLatch latchClassName, Future<T> futureClassName,
            CountDownLatch latchCss, Future<T> futureCss,
            CountDownLatch latchTagName, Future<T> futureTagName,
            CountDownLatch latchLinkText, Future<T> futureLinkText,
            CountDownLatch latchPartialLinkText, Future<T> futurePartialLinkText,
            CountDownLatch latchXpath, Future<T> futureXpath,
            CountDownLatch latchName, Future<T> futureName) {
        while (System.currentTimeMillis() < endTime) {
            try {
                if (latchId.await(searchRepetitionTime, TimeUnit.MILLISECONDS) && futureId.isDone()) {
                    T result = futureId.get();
                    if (result != null && !result.toString().isEmpty()) {
                        return result;
                    }
                }
                if (latchClassName.await(searchRepetitionTime, TimeUnit.MILLISECONDS) && futureClassName.isDone()) {
                    T result = futureClassName.get();
                    if (result != null && !result.toString().isEmpty()) {
                        return result;
                    }
                }
                if (latchCss.await(searchRepetitionTime, TimeUnit.MILLISECONDS) && futureCss.isDone()) {
                    T result = futureCss.get();
                    if (result != null && !result.toString().isEmpty()) {
                        return result;
                    }
                }
                if (latchTagName.await(searchRepetitionTime, TimeUnit.MILLISECONDS) && futureTagName.isDone()) {
                    T result = futureTagName.get();
                    if (result != null && !result.toString().isEmpty()) {
                        return result;
                    }
                }
                if (latchLinkText.await(searchRepetitionTime, TimeUnit.MILLISECONDS) && futureLinkText.isDone()) {
                    T result = futureLinkText.get();
                    if (result != null && !result.toString().isEmpty()) {
                        return result;
                    }
                }
                if (latchPartialLinkText.await(searchRepetitionTime, TimeUnit.MILLISECONDS) && futurePartialLinkText.isDone()) {
                    T result = futurePartialLinkText.get();
                    if (result != null && !result.toString().isEmpty()) {
                        return result;
                    }
                }
                if (latchXpath.await(searchRepetitionTime, TimeUnit.MILLISECONDS) && futureXpath.isDone()) {
                    T result = futureXpath.get();
                    if (result != null && !result.toString().isEmpty()) {
                        return result;
                    }
                }
                if (latchName.await(searchRepetitionTime, TimeUnit.MILLISECONDS) && futureName.isDone()) {
                    T result = futureName.get();
                    if (result != null && !result.toString().isEmpty()) {
                        return result;
                    }
                }
            } catch (Exception e) {
                SeleniumParallel.printError(e, "Error al obtener el resultado del Future: ");
            }
        }
        return null;
    }

    /***
     * Obtiene el texto del elemento indicado, si este existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda
     * @return Retorna el texto del elemento, si lo logra encontrar, de lo contrario retorna null
     */
    public static String getTextIfElementExist(WebDriver driver, SearchContext searchContext, String element) {
        return getTextIfElementExist(driver, searchContext, element, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
    }

    /***
     * Obtiene el texto del elemento indicado, si este existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda y obtendrá el Texto
     * @param timeDuration Duración de la espera para la busqueda del elemento
     * @param timeRepetition Cada cuanto tiempo durante el tiempo de espera, intentar obtener nuevamente el elemento
     * @return Retorna el texto del elemento, si lo logra encontrar, de lo contrario retorna null
     */
    public static String getTextIfElementExist(WebDriver driver, SearchContext searchContext, String element, Integer timeDuration, Integer timeRepetition) {
        String texto = null;
        //Para optimizar el tiempo de respuestá
        LogsJB.debug("* ");
        LogsJB.debug(" Obtendrá el texto del elemento si este existe: " + element);
        LogsJB.debug("* ");
        CountDownLatch latchId = new CountDownLatch(1);
        CountDownLatch latchClassName = new CountDownLatch(1);
        CountDownLatch latchCss = new CountDownLatch(1);
        CountDownLatch latchTagName = new CountDownLatch(1);
        CountDownLatch latchLinkText = new CountDownLatch(1);
        CountDownLatch latchPartialLinkText = new CountDownLatch(1);
        CountDownLatch latchXpath = new CountDownLatch(1);
        CountDownLatch latchName = new CountDownLatch(1);
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, timeDuration, timeRepetition);
        //Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<String> futureId = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, "id", element, latchId);
        Future<String> futureClassName = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, "class name", element, latchClassName);
        Future<String> futureCss = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, "css selector", element, latchCss);
        Future<String> futureTagName = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, "tag name", element, latchTagName);
        Future<String> futureLinkText = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, "link text", element, latchLinkText);
        Future<String> futurePartialLinkText = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, "partial link text", element, latchPartialLinkText);
        Future<String> futureXpath = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, "xpath", element, latchXpath);
        Future<String> futureName = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, "name", element, latchName);
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        long startTime = System.currentTimeMillis();
        long endTime = startTime + SeleniumUtils.getSearchTime();
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        texto = waitForElementGeneric(endTime, timeRepetition,
                latchId, futureId, latchClassName, futureClassName, latchCss, futureCss, latchTagName, futureTagName,
                latchLinkText, futureLinkText, latchPartialLinkText, futurePartialLinkText, latchXpath, futureXpath, latchName, futureName);
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        LogsJB.debug("Fecha actual: " + new Date(System.currentTimeMillis()));
        if (Objects.isNull(texto) && System.currentTimeMillis() >= endTime) {
            LogsJB.info(" No pudo hacer obtener el texto del elemento especificado, ya que no existe: " + element);
        } else {
            LogsJB.info(" Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
            return texto;
        }
        //Retorna Falso si el elemento no Existe
        return null;
    }

    /***
     * Realiza 2 veces la busquedad del texto de un elemento
     * @param driver Driver que controla el navegador
     * @param searchContext Contexto de busquedad
     * @param element Atributo del elemento a buscar
     * @return Si logra obtener el texto del elemento especifícado, lo retorna, de lo contrario retorna NULL
     */
    public static String obtenerTextOfWebElementx2(WebDriver driver, SearchContext searchContext, String element) {
        int i = 0;
        String texto = null;
        while (Objects.isNull(texto) && i < 2) {
            texto = getTextIfElementExist(driver, searchContext, element);
            i++;
        }
        return texto;
    }

    /****
     * Realiza 2 veces la busquedad del texto de un elemento
     * @param driver Driver que controla el navegador
     * @param searchContext Contexto de busquedad
     * @param element Atributo del elemento a buscar
     * @param timeduration Duración de la busquedad del texto del elemento especificado
     * @param timerepetition Tiempo de repetición para realizar la busquedad del elemento y obtener el texto
     * @return Si logra obtener el texto del elemento especifícado, lo retorna, de lo contrario retorna NULL
     */
    public static String obtenerOfTextWebElementx2(WebDriver driver, SearchContext searchContext, String element, int timeduration, int timerepetition) {
        int i = 0;
        String texto = null;
        while (Objects.isNull(texto) && i < 2) {
            texto = getTextIfElementExist(driver, searchContext, element, timeduration, timerepetition);
            i++;
        }
        return texto;
    }

    /***
     * Obtiene el texto del elemento proporcionado del metodo convencional de selenium,
     * si no lo logra por ese medio, lo hace por medio de atributos, si no lo logra de esa forma lo intenta por JavaScript
     * @param driver Driver que está manipulando el navegador
     * @param element Elemento del cual se desea obtener el texto
     * @return Texto del elemento, si no logra obtenerlo, retorna una cadena vacía
     */
    public static String getTextOfWebElement(WebDriver driver, WebElement element) {
        if (Objects.isNull(element)) {
            return "";
        }
        String text = null;
        posicionarmeEn(driver, element);
        text = element.getText();
        if (cadenaNulaoVacia(text)) {
            text = element.getAttribute("innerText");
        }
        if (cadenaNulaoVacia(text)) {
            text = element.getAttribute("value");
        }
        if (cadenaNulaoVacia(text)) {
            // Intentar obtener el texto utilizando JavaScript en caso de que las formas estándar fallen
            text = getTextUsingJavaScript(driver, element);
        }
        if (cadenaNulaoVacia(text)) {
            LogsJB.warning(" No se pudo obtener el texto del elemento, comuníquese con los administradores ");
        }
        return cadenaNulaoVacia(text) ? "" : text;
    }

    /***
     * Obtiene el texto de un elemento web por medio de Java Script
     * @param driver Driver que manipula el navegador actualmente
     * @param element Elemento del cual se desea obtener el texto
     * @return Retorna el texto del elemento, si no se puede obtener el texto, retorna una cadena vacía
     */
    public static String getTextUsingJavaScript(WebDriver driver, WebElement element) {
        return (String) SeleniumUtils.ejecutarJsComando(driver, "return arguments[0].textContent", element);
    }

    /***
     * Hace clic en el elemento indicado, si este existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda
     * @return Retorna True si logra hacer clic en el elemento, de lo contrario false
     */
    public static boolean clickElementIfExist(WebDriver driver, SearchContext searchContext, String element) {
        //Para optimizar el tiempo de respuestá
        LogsJB.debug("* ");
        LogsJB.debug(" Si existe el elemento indicado, hará click en el elemento: " + element);
        LogsJB.debug("* ");
        CountDownLatch latchId = new CountDownLatch(1);
        CountDownLatch latchClassName = new CountDownLatch(1);
        CountDownLatch latchCss = new CountDownLatch(1);
        CountDownLatch latchTagName = new CountDownLatch(1);
        CountDownLatch latchLinkText = new CountDownLatch(1);
        CountDownLatch latchPartialLinkText = new CountDownLatch(1);
        CountDownLatch latchXpath = new CountDownLatch(1);
        CountDownLatch latchName = new CountDownLatch(1);
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        //Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<Boolean> futureId = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, "id", element, latchId);
        Future<Boolean> futureClassName = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, "class name", element, latchClassName);
        Future<Boolean> futureCss = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, "css selector", element, latchCss);
        Future<Boolean> futureTagName = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, "tag name", element, latchTagName);
        Future<Boolean> futureLinkText = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, "link text", element, latchLinkText);
        Future<Boolean> futurePartialLinkText = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, "partial link text", element, latchPartialLinkText);
        Future<Boolean> futureXpath = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, "xpath", element, latchXpath);
        Future<Boolean> futureName = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, "name", element, latchName);     // Esperará saber si existe el elemento en alguno de los tipos usando Future
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        long startTime = System.currentTimeMillis();
        long endTime = startTime + SeleniumUtils.getSearchTime();
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        boolean result = waitForElement(endTime, SeleniumUtils.getSearchRepetitionTime(),
                latchId, futureId, latchClassName, futureClassName, latchCss, futureCss, latchTagName, futureTagName, latchLinkText, futureLinkText, latchPartialLinkText, futurePartialLinkText, latchXpath, futureXpath, latchName, futureName);
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        LogsJB.debug("Fecha actual: " + new Date(System.currentTimeMillis()));
        if (!result && System.currentTimeMillis() >= endTime) {
            LogsJB.info(" No pudo hacer click en el elemento especificado, ya que no existe: " + element);
        } else {
            return result;
        }
        //Retorna Falso si el elemento no Existe
        return false;
    }

    /**
     * Trata de hacer clic en el elemento especificado 2 veces, si no logra hacerlo a la primera
     *
     * @param driver        Driver que está manipulando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element       Atributo del elemento, por medio del cual se realizara la busqueda
     * @return Retorna True si logra hacer clic en el elemento, de lo contrario false
     */
    public static boolean clicktoElementx2intents(WebDriver driver, SearchContext searchContext, String element) {
        int i = 0;
        while (i < 2) {
            if (SeleniumUtils.clickElementIfExist(driver, searchContext, element)) {
                return true;
            }
            i++;
        }
        return false;
    }

    /**
     * Obtiene el identificador de un elemento By para realizar la busqueda por medio
     * de las funciones del middleware
     *
     * @param by identificador del cual obtendrá el atributo del elemento a buscar
     * @return atributo por medio del cual se filtrara el elemento a buscar
     */
    public static String getIdentificadorBy(By by) {
        String tempelement = by.toString();
        String[] data = tempelement.split(": ");
        return data[1];
    }

    /**
     * Espera implícita de 30 segundos, luego de los 30 segundos lanzara excepción
     *
     * @param driver Driver que está manipulando el navegador
     * @param by     Identificador del tipo By
     * @return retorna verdadero si se da la espera de manera correcta
     */
    public static boolean waitImplicity(WebDriver driver, By by) {
        boolean bandera = false;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(getSearchTime()));
            wait.until(driver1 -> !ElementoDeshabilitado(driver1.findElement(by)));
            bandera = true;
        } catch (TimeoutException ignored) {
        } finally {
            return bandera;
        }
    }

    /***
     * Obtiene los elementos que cumplen con el criterio de busqueda especificado
     * @param driver Driver que está controlando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Atributo de los elementos a buscar
     * @return Retorna la lista de elementos que cumplen con los criterios de busqueda, si no encuentra ningun elemento retorna una lista
     * vacía
     */
    public static List<WebElement> getElementsIfExist(WebDriver driver, SearchContext searchContext, String element) {
        //Declaración de Variable a Retornar
        List<WebElement> elementos = new ArrayList<>();
        //Para optimizar el tiempo de respuestá
        LogsJB.debug("* ");
        LogsJB.debug(" Si existen los elementos que corresponden al identificador: " + element);
        LogsJB.debug("* ");
        CountDownLatch latchId = new CountDownLatch(1);
        CountDownLatch latchClassName = new CountDownLatch(1);
        CountDownLatch latchCss = new CountDownLatch(1);
        CountDownLatch latchTagName = new CountDownLatch(1);
        CountDownLatch latchLinkText = new CountDownLatch(1);
        CountDownLatch latchPartialLinkText = new CountDownLatch(1);
        CountDownLatch latchXpath = new CountDownLatch(1);
        CountDownLatch latchName = new CountDownLatch(1);
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        //Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<List<WebElement>> futureId = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, "id", element, latchId);
        Future<List<WebElement>> futureClassName = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, "class name", element, latchClassName);
        Future<List<WebElement>> futureCss = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, "css selector", element, latchCss);
        Future<List<WebElement>> futureTagName = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, "tag name", element, latchTagName);
        Future<List<WebElement>> futureLinkText = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, "link text", element, latchLinkText);
        Future<List<WebElement>> futurePartialLinkText = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, "partial link text", element, latchPartialLinkText);
        Future<List<WebElement>> futureXpath = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, "xpath", element, latchXpath);
        Future<List<WebElement>> futureName = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, "name", element, latchName);
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        long startTime = System.currentTimeMillis();
        long endTime = startTime + SeleniumUtils.getSearchTime();
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        elementos = waitForElementGeneric(endTime, SeleniumUtils.getSearchRepetitionTime(),
                latchId, futureId, latchClassName, futureClassName, latchCss, futureCss, latchTagName, futureTagName,
                latchLinkText, futureLinkText, latchPartialLinkText, futurePartialLinkText, latchXpath, futureXpath, latchName, futureName);
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        LogsJB.debug("Fecha actual: " + new Date(System.currentTimeMillis()));
        if ((Objects.isNull(elementos) && System.currentTimeMillis() >= endTime) || elementos.isEmpty()) {
            LogsJB.warning(" No pudo obtener los elementos especificados, ya que no existen: " + elementos);
            elementos = new ArrayList<>();
        }
        //Retorna null si el elemento no Existe
        return elementos;
    }

    /***
     * Funcion que mueve al frame parametrizado
     * @param driver Driver que maneja el explorador
     * @param frame frame activo para interacción
     * @return
     */
    public static boolean movetoframeforwebelement(WebDriver driver, WebElement frame) {
        if (!Objects.isNull(frame)) {
            LogsJB.info("El Iframe Obtenido no es nulo, es: " + frame);
            driver.switchTo().frame(frame);
            SeleniumUtils.threadslepp(200);
            return true;
        }
        return false;
    }

    /***
     * Obtiene el elemento si existe en el contexto actual
     * @param driver Driver que está controlando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento a buscar
     * @return Retorna el elemento, si no lo encuentra retorna Null
     */
    public static WebElement getElementIfExist(WebDriver driver, SearchContext searchContext, String element) {
        //Declaración de Variable a Retornar
        WebElement elemento = null;
        //Para optimizar el tiempo de respuestá
        LogsJB.debug("* ");
        LogsJB.debug(" Si existen los elementos que corresponden al identificador: " + element);
        LogsJB.debug("* ");
        CountDownLatch latchId = new CountDownLatch(1);
        CountDownLatch latchClassName = new CountDownLatch(1);
        CountDownLatch latchCss = new CountDownLatch(1);
        CountDownLatch latchTagName = new CountDownLatch(1);
        CountDownLatch latchLinkText = new CountDownLatch(1);
        CountDownLatch latchPartialLinkText = new CountDownLatch(1);
        CountDownLatch latchXpath = new CountDownLatch(1);
        CountDownLatch latchName = new CountDownLatch(1);
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        // Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<WebElement> futureId = SeleniumParallel.getElementIfExist(driver, wait, searchContext, "id", element, latchId);
        Future<WebElement> futureClassName = SeleniumParallel.getElementIfExist(driver, wait, searchContext, "class name", element, latchClassName);
        Future<WebElement> futureCss = SeleniumParallel.getElementIfExist(driver, wait, searchContext, "css selector", element, latchCss);
        Future<WebElement> futureTagName = SeleniumParallel.getElementIfExist(driver, wait, searchContext, "tag name", element, latchTagName);
        Future<WebElement> futureLinkText = SeleniumParallel.getElementIfExist(driver, wait, searchContext, "link text", element, latchLinkText);
        Future<WebElement> futurePartialLinkText = SeleniumParallel.getElementIfExist(driver, wait, searchContext, "partial link text", element, latchPartialLinkText);
        Future<WebElement> futureXpath = SeleniumParallel.getElementIfExist(driver, wait, searchContext, "xpath", element, latchXpath);
        Future<WebElement> futureName = SeleniumParallel.getElementIfExist(driver, wait, searchContext, "name", element, latchName);
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        long startTime = System.currentTimeMillis();
        long endTime = startTime + SeleniumUtils.getSearchTime();
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        elemento = waitForElementGeneric(endTime, SeleniumUtils.getSearchRepetitionTime(),
                latchId, futureId, latchClassName, futureClassName, latchCss, futureCss, latchTagName, futureTagName,
                latchLinkText, futureLinkText, latchPartialLinkText, futurePartialLinkText, latchXpath, futureXpath, latchName, futureName);
        LogsJB.debug("Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + new Date(endTime));
        LogsJB.debug("Fecha actual: " + new Date(System.currentTimeMillis()));
        if (System.currentTimeMillis() >= endTime && Objects.isNull(elemento)) {
            LogsJB.warning(" No pudo obtener el elemento especificado, ya que no existe: " + element);
        }
        //Retorna null si el elemento no Existe
        return elemento;
    }

    /***
     * Realiza 2 veces la busquedad de un elemento que cumpla con el criterio de busqueda especificado
     * @param driver Driver que está controlando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento a buscar
     * @return Retorna el elemento que cumple con los criterios de busqueda, si no encuentra ningun elemento retorna null
     */
    public static WebElement obtenerWebElementx2(WebDriver driver, SearchContext searchContext, String element) {
        int i = 0;
        WebElement temp = null;
        while (Objects.isNull(temp) && i < 2) {
            temp = SeleniumUtils.getElementIfExist(driver, searchContext, element);
            i++;
        }
        return temp;
    }

    /***
     * Realiza 2 veces la busquedad de los elementos que cumplen con el criterio de busqueda especificado
     * @param driver Driver que está controlando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Filtro de Atributo de los elementos a buscar
     * @return Retorna la lista de elementos que cumplen con los criterios de busqueda, si no encuentra ningun elemento retorna una lista
     * vacía
     */
    public static WebElement obtenerWebElementx2(WebDriver driver, SearchContext searchContext, By element) {
        int i = 0;
        WebElement temp = null;
        while (Objects.isNull(temp) && i < 2) {
            temp = getElementIfExist(driver, searchContext, getIdentificadorBy(element));
            i++;
        }
        return temp;
    }

    /***
     * Obtiene el elemento si existe en el contexto actual
     * @param driver Driver que está controlando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento a buscar
     * @return Retorna el elemento, si no lo encuentra retorna Null
     */
    public static WebElement getElementIfExist(WebDriver driver, SearchContext searchContext, By element) {
        WebElement temp;
        temp = getElementIfExist(driver, searchContext, getIdentificadorBy(element));
        return temp;
    }

    /***
     * Realiza 2 veces la busquedad de los elementos que cumplen con el criterio de busqueda especificado
     * @param driver Driver que está controlando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Filtro de Atributo de los elementos a buscar
     * @return Retorna la lista de elementos que cumplen con los criterios de busqueda, si no encuentra ningun elemento retorna una lista
     * vacía
     */
    public static List<WebElement> obtenerWebElementsx2(WebDriver driver, SearchContext searchContext, String element) {
        int i = 0;
        List<WebElement> temp = null;
        while (Objects.isNull(temp) && i < 2) {
            temp = getElementsIfExist(driver, searchContext, element);
            i++;
        }
        return temp;
    }

    /***
     * Realiza 2 veces la busquedad de los elementos que cumplen con el criterio de busqueda especificado
     * @param driver Driver que está controlando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Filtro de Atributo de los elementos a buscar
     * @return Retorna la lista de elementos que cumplen con los criterios de busqueda, si no encuentra ningun elemento retorna una lista
     * vacía
     */
    public static List<WebElement> obtenerWebElementsx2(WebDriver driver, SearchContext searchContext, By element) {
        int i = 0;
        List<WebElement> temp = new ArrayList<>();
        while (temp.isEmpty() && i < 2) {
            temp = SeleniumUtils.getElementsIfExist(driver, searchContext, SeleniumUtils.getIdentificadorBy(element));
            i++;
        }
        return temp;
    }

    /***
     * Realiza 2 veces la busquedad de los elementos que cumplen con el criterio de busqueda especificado
     * @param driver Driver que está controlando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Filtro de Atributo de los elementos a buscar
     * @return Retorna la lista de elementos que cumplen con los criterios de busqueda, si no encuentra ningun elemento retorna una lista
     * vacía
     */
    public static List<WebElement> getElementsIfExist(WebDriver driver, SearchContext searchContext, By element) {
        return SeleniumUtils.getElementsIfExist(driver, searchContext, SeleniumUtils.getIdentificadorBy(element));
    }

    /**
     * Deselecciona el elemento proporcionado
     *
     * @param driver        Driver que está manipulando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element       Elemento que se desea deseleccionar, tiene que ser tipo radio
     * @return True si el elemento está desseleccionado o si logra de seleccionarlo,
     * si el elemento proporcionado es null, retorna False
     */
    public static boolean deseleccionarElemento(WebDriver driver, SearchContext searchContext, WebElement element) {
        if (!Objects.isNull(element)) {
            LogsJB.info("La opcion está seleccionada: " + element.isSelected());
            LogsJB.info("Color: " + element.getCssValue("background-color"));
            String color = element.getCssValue("background-color");
            if (element.isSelected() || StringUtils.containsIgnoreCase(color, "46, 152, 9")) {
                LogsJB.info("Deselecciona el elemento: " + element);
                String term = getTermOrLocator(element, false);
                clicktoElementx2intents(driver, searchContext, term);
                return true;
            } else {
                return true;
            }
        } else {
            LogsJB.error("Elemento proporcionado es null");
            return false;
        }
    }

    /**
     * Selecciona el elemento si no está seleccionado
     *
     * @param driver        Driver que está manipulando el navegador
     * @param searchcontext Contexto en el que se desea buscar el elemento
     * @param element       Elemento que se desea seleccionar, tiene que ser tipo radio
     * @return True si el elemento está seleccionado o si logra seleccionarlo, si el elemento proporcionado
     * es null retorna False
     */
    public static boolean seleccionarElemento(WebDriver driver, SearchContext searchcontext, WebElement element) {
        if (!Objects.isNull(element)) {
            LogsJB.info("La opcion está seleccionada: " + element.isSelected());
            LogsJB.info("Color: " + element.getCssValue("background-color"));
            String color = element.getCssValue("background-color");
            if (!element.isSelected() && StringUtils.containsIgnoreCase(color, "164, 9, 32")) {
                LogsJB.info("Selecciona el elemento: " + element);
                String term = getTermOrLocator(element, false);
                clicktoElementx2intents(driver, searchcontext, term);
                return true;
            } else {
                return true;
            }
        } else {
            LogsJB.fatal("Elemento proporcionado es null");
            return false;
        }
    }

    /***
     * Obtiene el texto de la opción seleccionada de un elemento Select
     * @param driver  Driver que está manipulando el navegador
     * @param temp Elemento Select del cual queremos saber cuál es la primera Opcion Seleccionada
     * @return Retorna el texto de la opción seleccionada o una cadena vacía
     */
    public static String obtenerTextoSeleccionadoSelect(WebDriver driver, WebElement temp) {
        try {
            Select proceso = new Select(temp);
            String retorno;
            retorno = getTextOfWebElement(driver, proceso.getFirstSelectedOption());
            return retorno;
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error al intentar obtener el texto de la opción seleccionada: ");
            return "fatal";
        }
    }

    /**
     * Envia carácter por carácter al elemento especificado
     *
     * @param driver        Driver que está manipulando el navegador
     * @param searchcontext Contexto en el que se desea buscar el elemento
     * @param element       Elemento al que se desea envíar el texto
     * @param valor         String que se desea envíar al elemento
     */
    public static void enviarTxtforKeyPress(WebDriver driver, SearchContext searchcontext, String element, String valor) {
        //Pendiente eliminar el texto existente
        WebElement campo = SeleniumUtils.getElementIfExist(driver, searchcontext, element);
        assert campo != null;
        String texto = SeleniumUtils.getTextOfWebElement(driver, campo);
        LogsJB.info("Texto que tiene el elemento: " + texto);
        if (SeleniumUtils.clickElementIfExist(driver, searchcontext, element)) {
            //Elimina carácter por carácter
            for (char c : texto.toCharArray()) {
                keyPress(driver, KeyEvent.VK_BACK_SPACE);
            }
            //Escribe carácter por carácter
            for (char c : valor.toCharArray()) {
                keyPress(driver, c);
            }
        }
    }

    /**
     * Función donde obtiene y estáblece el marco actual (frame)
     *
     * @param driver WebDriver es el que se estáblecera el marco actual
     */
    public static boolean FrameActivo(WebDriver driver) {
        String currentFrame = (String) SeleniumUtils.ejecutarJsComando(driver, "return self.frameElement ? self.frameElement.id : ''");
        LogsJB.info("Frame: " + currentFrame);
        return !currentFrame.isEmpty();
    }

    /**
     * Función para guardar la captura de pantalla de la página web y la guarda en un archivo
     *
     * @param driver            WebDriver que representa la sesión del navegador
     * @param elementScreenshot Elemento web, el cual se enfocará para tomar la captura de pantalla.
     * @return Retorna a un objeto File que representa la captura de pantalla de la pagina web
     */
    public static File getImageScrennshot(WebDriver driver, WebElement elementScreenshot) {
        if (!Objects.isNull(elementScreenshot)) {
            // Desplazarse hasta el elemento
            SeleniumUtils.posicionarmeEn(driver, elementScreenshot);
            // Tomar la altura del elemento después de desplazarse
            int elementHeight = elementScreenshot.getSize().getHeight();
            // Calcular porcentaje de Zoom necesario
            double windowHeight = driver.manage().window().getSize().getHeight();
            double zoomPercentage = 1.0;
            if (windowHeight < elementHeight) {
                zoomPercentage = Math.min(1.0, windowHeight / elementHeight); // Limitar zoom a máximo 100%
            } else {
                zoomPercentage = Math.min(1.0, elementHeight / windowHeight); // Limitar zoom a máximo 100%
            }
            zoomPercentage = zoomPercentage < 0.5 ? 1 : zoomPercentage;
            String zoomActual = SeleniumUtils.ejecutarJsComando(driver, "return document.body.style.zoom").toString();
            // Aplicar el zoom usando JavaScript
            if (!cadenaNulaoVacia(zoomActual)) {
                try {
                    double zoomActualValue = Double.parseDouble(zoomActual.replace("%", ""));
                    double newZoomValue = (zoomActualValue < 100.0) ? (zoomActualValue - 15.0) : 100.0;
                    SeleniumUtils.ejecutarJsComando(driver, "document.body.style.zoom = '" + newZoomValue + "%'");
                } catch (NumberFormatException e) {
                    LogsJB.info("El valor de zoomActual no es numérico: " + zoomActual);
                }
            }
            File scrFile = SeleniumUtils.getImageScreeenshotWebElement(elementScreenshot);
            if (Objects.isNull(scrFile)) {
                TakesScreenshot scrShot = ((TakesScreenshot) driver);
                // Restáurar el Zoom a 100% si fue ajustado
                SeleniumUtils.ejecutarJsComando(driver, "document.body.style.zoom = '100%';");
                return scrShot.getScreenshotAs(OutputType.FILE);
            } else {
                // Restáurar el Zoom a 100% si fue ajustado
                SeleniumUtils.ejecutarJsComando(driver, "document.body.style.zoom = '100%';");
                return scrFile;
            }
        } else {
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            return scrShot.getScreenshotAs(OutputType.FILE);
        }
    }

    /**
     * Espera implícita de 5 segundos o menos si el elemento desaparece del DOM, luego de los 5 segundos lanzara excepción
     *
     * @param driver Variable que manipula el navegador
     * @param by     Identificador del tipo By
     */
    public static boolean waitImplicityForElementNotExist(WebDriver driver, By by) {
        return waitImplicityForElementNotExist(driver, by, false);
    }

    /**
     * Espera implícita de 5 segundos o menos si el elemento desaparece del DOM, luego de los 5 segundos lanzara excepción
     *
     * @param driver        Variable que manipula el navegador
     * @param by            Identificador del tipo By
     * @param banderaAssert Bandera para preguntar si se quiere el assertFail
     */
    public static boolean waitImplicityForElementNotExist(WebDriver driver, By by, boolean banderaAssert) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(getSearchTime()));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (TimeoutException ignored) {
            return false;
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error inesperado al esperar la aparicion del elemento: ");
            if (banderaAssert) {
                Assert.fail("Error inesperado al esperar la aparicion del elemento: " + by);
            }
            return false;
        }
    }

    /***
     * Permite Aceptar las Alertas emergentes por medio de la definición estándar de W3C de los navegadores.
     * @param driver Web Driver que manipula el navegador
     * @param aceptar Variable booleana que acepta o declina el cuadro de diálogo
     * Debido a la naturaleza del manejo interno de accept por parte de javascript, la funcion DEBE DE LLAMARSE JUSTO ANTES DE DAR CLIC PARA DISPARAR EL CUADRO DE DIALOGO
     */
    public static boolean acceptConfirm(WebDriver driver, boolean aceptar) {
        SeleniumUtils.ejecutarJsComando(driver, "window.confirm=function(){return " + aceptar + "}");
        return true;
    }

    /***
     * Permite Aceptar las Alertas emergentes por medio de la definición estándar de W3C de los navegadores.
     * @param driver Web Driver que manipula el navegador
     */
    public static boolean acceptAlert(WebDriver driver) {
        try {
            Wait wait = getFluentWait(driver, getSearchTime(), getSearchRepetitionTime());
            Alert alert = (Alert) wait.until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            LogsJB.info(text);
            alert.accept();
            return true;
        } catch (Exception e) {
            LogsJB.fatal("No se encontró ninguna alerta presente.");
            SeleniumUtils.ejecutarJsComando(driver, "window.alert = function() {};");
            return true;
        }
    }

    /***
     * Presiona la tecla indicada en él condigo numerico indicado
     * @param driver  Driver que está manipulando el navegador
     * @param repeticiones Cantidad de veces que deseamos se repita el cambio de Zoom
     * @param codigo Codigo numerico de la tecla que queremos presionar
     * @return
     */
    public static boolean cambiarZOOM(WebDriver driver, int repeticiones, Keys codigo) {
        return cambiarZOOM(driver, repeticiones, codigo, false);
    }

    /***
     * Presiona la tecla indicada en él condigo numerico indicado
     * @param driver  Driver que está manipulando el navegador
     * @param repeticiones Cantidad de veces que deseamos se repita el cambio de Zoom
     * @param codigo Codigo numerico de la tecla que queremos presionar
     * @return
     */
    public static boolean cambiarZOOM(WebDriver driver, int repeticiones, Keys codigo, boolean banderaAssert) {
        try {
            for (int i = 0; i < repeticiones; i++) {
                threadslepp(100);
                Actions actions = new Actions(driver);
                actions.keyDown(Keys.CONTROL).keyDown(codigo).keyUp(codigo).keyUp(Keys.CONTROL).perform();
                LogsJB.info("Presiona la tecla: " + codigo);
                LogsJB.info("Suelta la tecla: " + codigo);
                threadslepp(100);
            }
            return true;
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error inesperado al Cambiar Zoom: ");
            if (banderaAssert) {
                Assert.fail("Error inesperado al presionar una tecla: ");
            }
            return false;
        }
    }

    /***
     * Presiona la tecla indicada en él condigo numerico indicado
     * @param driver  Driver que está manipulando el navegador
     * @param repeticiones Cantidad de veces que deseamos se repita el cambio de Zoom
     * @param codigo Codigo numerico de la tecla que queremos presionar
     * @return
     */
    public static boolean cambiarZOOM(WebDriver driver, int repeticiones, int codigo) {
        return cambiarZOOM(driver, repeticiones, codigo, false);
    }

    /***
     * Presiona la tecla indicada en él condigo numerico indicado
     * @param driver  Driver que está manipulando el navegador
     * @param repeticiones Cantidad de veces que deseamos se repita el cambio de Zoom
     * @param codigo Codigo numerico de la tecla que queremos presionar
     * @return
     */
    public static boolean cambiarZOOM(WebDriver driver, int repeticiones, int codigo, boolean banderaAssert) {
        try {
            for (int i = 0; i < repeticiones; i++) {
                threadslepp(100);
                char asciiValue = (char) codigo;
                Actions actions = new Actions(driver);
                actions.keyDown(Keys.CONTROL).keyDown(String.valueOf(asciiValue)).keyUp(String.valueOf(asciiValue)).keyUp(Keys.CONTROL).perform();
                LogsJB.info("Presiona la tecla: " + codigo);
                LogsJB.info("Suelta la tecla: " + codigo);
                threadslepp(100);
            }
            return true;
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error inesperado al cambiar el Zoom: ");
            if (banderaAssert) {
                Assert.fail("Error inesperado al presionar una tecla: ");
            }
            return false;
        }
    }

    /***
     * Presiona la tecla indicada en él codigo numerico indicado
     * @param driver  Driver que está manipulando el navegador
     * @param repeticiones Cantidad de veces que deseamos se repita el cambio de Zoom
     *
     * @return
     */
    public static boolean cambiarZOOMMenos(WebDriver driver, int repeticiones) {
        return cambiarZOOM(driver, repeticiones, Keys.SUBTRACT);
    }

    /***
     * Presiona la tecla indicada en él codigo numerico indicado
     * @param driver  Driver que está manipulando el navegador
     * @param repeticiones Cantidad de veces que deseamos se repita el cambio de Zoom
     *
     * @return
     */
    public static boolean cambiarZOOMMenos(WebDriver driver, int repeticiones, boolean banderaAssert) {
        return cambiarZOOM(driver, repeticiones, Keys.SUBTRACT);
    }

    /***
     * Presiona la tecla indicada en él codigo numerico indicado
     * @param driver  Driver que está manipulando el navegador
     * @param repeticiones Cantidad de veces que deseamos se repita el cambio de Zoom
     *
     * @return
     */
    public static boolean cambiarZOOMMas(WebDriver driver, int repeticiones) {
        return cambiarZOOM(driver, repeticiones, Keys.ADD);
    }

    /***
     * Presiona la tecla indicada en él codigo numerico indicado
     * @param driver  Driver que está manipulando el navegador
     * @param repeticiones Cantidad de veces que deseamos se repita el cambio de Zoom
     *
     * @return
     */
    public static boolean cambiarZOOMMas(WebDriver driver, int repeticiones, boolean banderaAssert) {
        return cambiarZOOM(driver, repeticiones, Keys.ADD);
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param driver          Driver que está manipulando el navegador
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia abajo.
     */
    public static boolean scrollMouseDown(WebDriver driver, int cantidadScrolls) {
        return scrollMouseKey(driver, cantidadScrolls, Keys.PAGE_DOWN);
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param driver          Driver que está manipulando el navegador
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia abajo.
     */
    public static boolean scrollMouseDown(WebDriver driver, int cantidadScrolls, boolean banderaAssert) {
        return scrollMouseKey(driver, cantidadScrolls, Keys.PAGE_DOWN, banderaAssert);
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param driver          Driver que está manipulando el navegador
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia arriba.
     */
    public static boolean scrollMouseUp(WebDriver driver, int cantidadScrolls) {
        return scrollMouseKey(driver, cantidadScrolls, Keys.PAGE_UP);
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param driver          Driver que está manipulando el navegador
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia arriba.
     * @param key             Tecla que se desea presionar, aumentar o disminuir el zoom
     * @return Retorna True si logra realizar el scroll, de lo contrario retorna False
     */
    public static boolean scrollMouseKey(WebDriver driver, int cantidadScrolls, Keys key) {
        return scrollMouseKey(driver, cantidadScrolls, key, false);
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param driver          Driver que está manipulando el navegador
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia arriba.
     */
    public static boolean scrollMouseUp(WebDriver driver, int cantidadScrolls, boolean banderaAssert) {
        return scrollMouseKey(driver, cantidadScrolls, Keys.PAGE_UP, banderaAssert);
    }

    /***
     * Mueve el escrol del mouse
     * @param driver Driver que está manipulando el navegador
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia abajo o hacía arriba dependiendo de la tecla que se envía
     * @param key Tecla que se desea presionar, aumentar o disminuir el zoom
     * @param banderaAssert Bandera con la cual se va a controlar si se desea un assert o no
     * @return Retorna True si logra realizar el scroll, de lo contrario retorna False
     */
    public static boolean scrollMouseKey(WebDriver driver, int cantidadScrolls, Keys key, boolean banderaAssert) {
        try {
            Actions actions = new Actions(driver);
            for (int i = 0; i < cantidadScrolls; i++) {
                actions.sendKeys(key).build().perform();
            }
            return true;
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error inesperado al realizar el scroll: ");
            if (banderaAssert) {
                Assert.fail("Error inesperado al intentar realizar el scroll: ");
            }
            return false;
        }
    }

    /***
     *Selecciona la opcion indicada, si el elemento proporcionado existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param searchcontext Contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento, por medio del cual se realizara la busquedad
     * @param opcion Opcion del elemento que queremos seleccionar
     * @param banderaAssert Bandera con la cual se va a controlar si se desea un assert o no
     */
    public static boolean selectOption(WebDriver driver, SearchContext searchcontext, String element, String opcion, boolean banderaAssert) {
        WebElement elemento = SeleniumUtils.getElementIfExist(driver, searchcontext, element);
        try {
            if (!Objects.isNull(elemento)) {
                //Si encuentra el elemento ejecuta este codigo
                try {
                    LogsJB.info("Encontro el elemento Select: " + element + " " +
                            "Procedera a seleccionar la opcion por medio del Index numerico " + opcion);
                    int opcionint;
                    opcionint = Integer.parseInt(opcion);
                    LogsJB.info("Parcio la opcion a entero: " + opcionint);
                    new Select(elemento).selectByIndex(opcionint);
                    return true;
                } catch (NumberFormatException e) {
                    try {
                        LogsJB.info("Encontro el elemento Select: " + element + " " +
                                "Procedera a seleccionar la opcion por medio del Texto Visible: " + opcion);
                        new Select(elemento).selectByVisibleText(opcion);
                        return true;
                    } catch (Exception m) {
                        LogsJB.info("Encontro el elemento Select: " + element + " " +
                                "Procedera a seleccionar la opcion por medio del Value: " + opcion);
                        new Select(elemento).selectByValue(opcion);
                        return true;
                    }
                } catch (Exception ex) {
                    LogsJB.info("Encontro el elemento Select: " + element + " " +
                            "Procedera a seleccionar la opcion por medio del Value: " + opcion);
                    new Select(elemento).selectByValue(opcion);
                    return true;
                }
            } else {
                LogsJB.info("No pudo encontrar el elemento: " + element + " por lo que no se pudo seleccionar la opcion indicada");
            }
            return true;
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error inesperado al seleccionar el elemento: " + element);
            if (banderaAssert) {
                Assert.fail("Error inesperado al seleccionar el elemento: " + element);
            }
            return false;
        }
    }

    /***
     *Selecciona la opcion indicada, si el elemento proporcionado existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param searchcontext Contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento, por medio del cual se realizara la busquedad
     * @param opcion Opcion del elemento que queremos seleccionar
     */
    public static boolean selectOption(WebDriver driver, SearchContext searchcontext, String element, String opcion) {
        return SeleniumUtils.selectOption(driver, searchcontext, element, opcion, false);
    }

    /***
     * Obtener el valor booleano de un número
     * @param numero numero que se evaluara
     * @return si el número es mayor o igual a uno, retorna true, de lo contrario, retorna false.
     */
    public static boolean getBooleanfromInt(int numero) {
        return numero >= 1;
    }

    /***
     * Mueve el driver el frame con el ID indicado si este existe en el contexto actual
     * @param driver driver que está controlando el navegador
     * @param searchcontext Contexto en el que se desea buscar el elemento
     * @param frameIDorName Id del frame al que se desea mover el driver
     * @return Si el frame existe y se mueve al mismo, retorna true, de lo contrario retorna false
     */
    public static boolean movetoframeIDorName(WebDriver driver, SearchContext searchcontext, String frameIDorName) {
        //Se traslada al frame de la transaccion
        threadslepp(500);
        WebElement frame = null;
        int i = 0;
        while (Objects.isNull(frame) && i < 2) {
            frame = SeleniumUtils.getElementIfExist(driver, searchcontext, frameIDorName);
            i++;
        }
        return movetoframeforwebelement(driver, frame);
    }

    /***
     * Hace clic en el elemento proporcionado por el metodo estandar de selenium, si no puede hacer clic, intenta hacer clic por medio de JavaScript
     * @param driver Driver que está controlando el navegador
     * @param element Elemento al que se desea hacer clic
     * @return Retorna True si logra hacer clic en el elemento, de lo contrario retorna False
     */
    public static boolean clickToElement(WebDriver driver, WebElement element) {
        if (Objects.isNull(element)) {
            LogsJB.fatal("El elemento es nulo. No se puede hacer clic.");
            return false;
        }
        try {
            SeleniumUtils.posicionarmeEn(driver, element);
            element.click();
            LogsJB.info("Hizo clic en el elemento directamente.");
            return true;
        } catch (WebDriverException e) {
            LogsJB.error("Capturó ElementNotInteractableException. Intentará hacer clic mediante JavaScript.");
            SeleniumUtils.ejecutarJsComando(driver, "arguments[0].click();", element);
            LogsJB.info("Hizo clic en el elemento por medio de JavaScript.");
            return true;
        }
    }

    /**
     * Funcion que normaliza un texto dado aplicando operaciones para estandarizar su formato o contenido
     *
     * @param texto Se refiere al texto que se va normalizar
     * @return Retorna el texto normalizado
     */
    public static String Normalizar(String texto) {
        String convertedString = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        LogsJB.info("Se normaliza texto: " + texto);
        return convertedString.toUpperCase();
    }
    //SEGUNDO LOTE DE MÉTODOS

    /**
     * Valida si una cadena es nula o vacía y genera un log de error si es así.
     *
     * @param campo  El valor de la cadena a validar.
     * @param nombre El nombre del campo asociado con la cadena, utilizado en el mensaje de error.
     * @return true si la cadena es nula o vacía, false en caso contrario.
     */
    public static boolean validarNull(String campo, String nombre) {
        if (cadenaNulaoVacia(campo)) {
            LogsJB.error("Debe ingresar el valor del campo: " + nombre);
            return true;
        }
        return false;
    }

    /**
     * Sube un archivo a un elemento de tipo file en una página web utilizando WebDriver.
     *
     * @param driver       El WebDriver que controla el navegador.
     * @param elementoFile El selector del elemento de tipo file donde se subirá el archivo.
     * @param path         La ruta del archivo que se va a subir.
     * @return true si el archivo se sube correctamente, false en caso de error.
     */
    public static boolean subirArchivo(WebDriver driver, String elementoFile, String path) {
        try {
            WebElement subirArchivo = SeleniumUtils.getElementIfExist(driver, driver, elementoFile);
            sendKeysToElement(driver, subirArchivo, path);
            String nameFile = Path.of(path).getFileName().toString();
            LogsJB.info("Se subió archivo: " + nameFile);
            return true;
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error al subir archivo: ");
            return false;
        }
    }

    /**
     * Regresa al frame principal de la página actual en el navegador.
     *
     * @param driver El WebDriver que controla el navegador.
     * @return true si el cambio de frame fue exitoso, false en caso de error.
     */
    public static boolean regresarFramePrincipal(WebDriver driver) {
        try {
            driver.switchTo().defaultContent();
            LogsJB.info("Se regresa al frame inicial");
            return true;
        } catch (Exception ex) {
            SeleniumParallel.printError(ex, "Error al cambiar al frame principal: ");
            return false;
        }
    }

    /**
     * Convierte una dirección IP en formato decimal a su representación en formato hexadecimal.
     *
     * @param ipDecimal La dirección IP en formato decimal (ejemplo: "192.168.1.1").
     * @return La representación hexadecimal de la dirección IP.
     */
    public static String convertirIpDecimalAHexadecimal(String ipDecimal) {
        StringBuilder hex = new StringBuilder();
        for (String octeto : ipDecimal.split("\\.")) {
            String hexOcteto = Integer.toHexString(Integer.parseInt(octeto));
            hex.append(hexOcteto.length() == 1 ? "0" + hexOcteto : hexOcteto);
        }
        return hex.toString().toUpperCase();
    }

    /**
     * Ejecuta un comando de JavaScript en el contexto actual de la página utilizando WebDriver.
     *
     * @param driver    El WebDriver que controla el navegador.
     * @param comandoJs El comando JavaScript que se va a ejecutar.
     * @return true si el comando se ejecuta correctamente, false en caso de error.
     */
    public static Object ejecutarJsComando(WebDriver driver, String comandoJs, Object... args) {
        try {
            if (Objects.isNull(args)) {
                return ((JavascriptExecutor) driver).executeScript(comandoJs);
            } else {
                return ((JavascriptExecutor) driver).executeScript(comandoJs, args);
            }
        } catch (Exception e) {
            SeleniumParallel.printError(e, "Error al ejecutar comando JavaScript: " + comandoJs);
            return null;
        }
    }

    /**
     * Cambia el foco a una pestaña anterior en el navegador, según el identificador de la pestaña.
     *
     * @param driver      El WebDriver que controla el navegador.
     * @param previousTab El identificador de la pestaña anterior a la que se desea cambiar.
     */
    public static void moverATabAnterior(WebDriver driver, String previousTab) {
        if (esValorValido(previousTab)) {
            for (String windowHandle : driver.getWindowHandles()) {
                if (previousTab.equals(windowHandle)) {
                    LogsJB.info("Moviendo a la pestaña solicitada");
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
        }
    }

    /**
     * Envia el texto al elemento especificado 2 veces seguidas, confirmando con un enter
     *
     * @param driver  driver que manipula el navegador
     * @param element elemento al que se envÃ­ara el texto
     * @param value   Valor que se validara no sea null o vacio
     */
    public static void enviarTextoSiValidoX2(WebDriver driver, SearchContext searchContext, String element, String value) {
        if (!cadenaNulaoVacia(value) && !value.equalsIgnoreCase(inespecific)) {
            enviarTextoX2(driver, searchContext, element, value);
        }
    }

    /**
     * Envía un texto a un elemento si el valor proporcionado no es nulo, vacío o igual a un valor específico.
     *
     * @param driver        El WebDriver que controla el navegador.
     * @param searchContext El contexto de búsqueda para encontrar el elemento (puede ser una página o un marco).
     * @param element       El selector del elemento al que se enviará el texto.
     * @param value         El texto que se enviará al elemento, si es válido.
     */
    public static void enviarTextoSiValido(WebDriver driver, SearchContext searchContext, String element, String value) {
        if (!cadenaNulaoVacia(value) && !value.equalsIgnoreCase(inespecific)) {
            enviarTexto(driver, searchContext, element, value);
        }
    }

    /**
     * Trata de envíar el texto al elemento especificado en mas de una ocasión
     *
     * @param driver  Driver que está manipulando el navegador
     * @param element Atributo por medio del cual identificaremos el elemento a modificar
     * @param texto   Texto que deseamos envíar al elmento
     * @return True si logra envíar el texto, de lo contrario false
     */
    public static boolean enviarTextoX2Intentos(WebDriver driver, SearchContext searchContext, String element, CharSequence... texto) {
        for (int i = 0; i < 2; i++) {
            if (sendKeysIfElementExist(driver, searchContext, element, texto)) {
                return true;
            }
        }
        return false;
    }

    /***
     * Envía dos veces el texto indicado al elemento indicado
     * @param searchContext Driver que está manipulando el navegador
     * @param elemento Atributo por medio del cual identificaremos el elemento a modificar
     * @param texto Texto que deseamos envíar al elmento
     */
    public static void enviarTextoX2(WebDriver driver, SearchContext searchContext, String elemento, String texto) {
        enviarTexto(driver, searchContext, elemento, texto);
        keyPress(driver, Keys.ENTER);
        SeleniumUtils.threadslepp(10);
        enviarTexto(driver, searchContext, elemento, texto);
        keyPress(driver, Keys.ENTER);
        SeleniumUtils.threadslepp(100);
    }

    /***
     * Realiza 2 veces la busquedad de el texto de un elemento
     * @param driver Driver que controla el navegador
     * @param element Atributo del elemento a buscar
     * @return Si logra obtener el texto del elemento especifícado, lo retorna, de lo contrario retorna NULL
     */
    public static String obtenerTextWebElementx2(WebDriver driver, SearchContext searchContext, String element) {
        int i = 0;
        String texto = null;
        while (Objects.isNull(texto) && i < 2) {
            texto = SeleniumUtils.getTextIfElementExist(driver, searchContext, element);
            i++;
        }
        return texto;
    }

    /**
     * Intenta obtener el texto de un elemento en dos intentos. Si el texto es encontrado en el primer o segundo intento, lo retorna.
     *
     * @param driver        El WebDriver que controla el navegador.
     * @param searchContext El contexto de búsqueda para encontrar el elemento (puede ser una página o un marco).
     * @param element       El selector del elemento del cual se intentará obtener el texto.
     * @return El texto del elemento si es encontrado, o null si no se encuentra después de dos intentos.
     */
    public static String obtenerTextoElementoX2(WebDriver driver, SearchContext searchContext, String element) {
        for (int i = 0; i < 2; i++) {
            String texto = getTextIfElementExist(driver, searchContext, element);
            if (texto != null) {
                return texto;
            }
        }
        return null;
    }

    /****
     * Realiza 2 veces la busquedad de el texto de un elemento
     * @param driver Driver que controla el navegador
     * @param element Atributo del elemento a buscar
     * @param timeduration Duración de la busquedad del texto del elemento especificado
     * @param timerepetition Tiempo de repeticion para realizar la busquedad del elemento y obtener el texto
     * @return Si logra obtener el texto del elemento especifícado, lo retorna, de lo contrario retorna NULL
     */
    public static String obtenerTextoElementoX2(WebDriver driver, SearchContext searchContext, String element, int timeduration, int timerepetition) {
        for (int i = 0; i < 2; i++) {
            String texto = getTextIfElementExist(driver, searchContext, element, timeduration, timerepetition);
            if (texto != null) {
                return texto;
            }
        }
        return null;
    }

    /**
     * Envía un texto al elemento indicado, si este existe en el contexto actual.
     *
     * @param driver  Driver que está manipulando el navegador
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda
     * @param texto   Texto a envíar al elemento indicad
     */
    public static boolean enviarTexto(WebDriver driver, SearchContext searchContext, String element, String texto) {
        return SeleniumUtils.sendKeysIfElementExist(driver, searchContext, element, texto);
    }

    /**
     * Envía texto a un elemento web, intentando hasta dos veces, y maneja cualquier excepción que pueda ocurrir.
     * Si el envío de texto falla, puede opcionalmente generar un fallo en la prueba.
     *
     * @param driver        El WebDriver que controla el navegador.
     * @param searchContext El contexto de búsqueda para encontrar el elemento (puede ser una página o un marco).
     * @param element       El selector del elemento al que se enviará el texto.
     * @param texto         El texto o secuencia de caracteres que se enviará al elemento.
     * @return true si el texto fue enviado correctamente, false si falló después de los intentos.
     */
    public static boolean enviarTexto(WebDriver driver, SearchContext searchContext, String element, CharSequence... texto) {
        return sendKeysIfElementExist(driver, searchContext, element, texto);
    }
//Tercer lote de métodos

    /**
     * Envia el texto al elemento especificado 2 veces seguidas, confirmando con un enter
     *
     * @param driver        driver para interactuar con el navegador
     * @param searchContext driver que actúa como searchContext
     * @param element       elemento al que se envÃ­ara el texto
     * @param value         Valor que se validara no sea null o vacio
     */
    public static void sendKeystoElementvalidValueX2(WebDriver driver, SearchContext searchContext, String element, String value) {
        if (!SeleniumUtils.cadenaNulaoVacia(value)) {
            if (!value.equalsIgnoreCase(inespecific)) {
                enviarTextoX2Intentos(driver, searchContext, element, value);
            }
        }
    }

    /**
     * Envia el texto al elemento especificado
     *
     * @param driver        driver para interactuar con la página
     * @param searchContext driver que funciona como searchContext
     * @param element       elemento al que se envÃ­ara el texto
     * @param value         Valor que se validara no sea null o vacio
     */
    public static void sendKeystoElementvalidValueForMap(WebDriver driver, SearchContext searchContext, String element, String value) {
        if (!SeleniumUtils.cadenaNulaoVacia(value)) {
            if (!value.equalsIgnoreCase(inespecific)) {
                enviarTexto(driver, searchContext, element, value);
                SeleniumUtils.keyPress(driver, Keys.ENTER);
            }
        }
    }

    /**
     * Envia el texto al elemento especificado
     *
     * @param driver        driver que interactúa con el navegador
     * @param searchContext driver que funciona como searchContext
     * @param element       elemento al que se envÃ­ara el texto
     * @param value         Valor que se validara no sea null o vacio
     */
    public static void sendKeystoElementvalidValue(WebDriver driver, SearchContext searchContext, String element, String value) {
        if (!SeleniumUtils.cadenaNulaoVacia(value)) {
            if (!value.equalsIgnoreCase(inespecific)) {
                enviarTexto(driver, searchContext, element, value);
            }
        }
    }

    /**
     * Trata de envíar el texto al elemento especificado en mas de una ocasión
     *
     * @param driver        Driver que interactúa con el navegador
     * @param searchContext Driver que funciona como searchContext
     * @param element       Atributo por medio del cual identificaremos el elemento a modificar
     * @param texto         Texto que deseamos envíar al elmento
     * @return True si logra envíar el texto, de lo contrario false
     */
    public static boolean sendKeystoElementx2intents(WebDriver driver, SearchContext searchContext, String element, CharSequence... texto) {
        int i = 0;
        while (i < 2) {
            if (SeleniumUtils.sendKeysIfElementExist(driver, searchContext, element, texto)) {
                return true;
            }
            i++;
        }
        return false;
    }

    /****
     * Realiza 2 veces la busquedad de el texto de un elemento
     *
     * @param driver driver que interactúa con el navegador
     * @param searchContext Driver que funciona como searchContext
     * @param element Atributo del elemento a buscar
     * @param timeduration Duración de la busquedad del texto del elemento especificado
     * @param timerepetition Tiempo de repeticion para realizar la busquedad del elemento y obtener el texto
     * @return Si logra obtener el texto del elemento especifícado, lo retorna, de lo contrario retorna NULL
     */
    public static String obtenerTextWebElementx2(WebDriver driver, SearchContext searchContext, String element, int timeduration, int timerepetition) {
        int i = 0;
        String texto = null;
        while (Objects.isNull(texto) && i < 2) {
            texto = SeleniumUtils.getTextIfElementExist(driver, searchContext, element, timeduration, timerepetition);
            i++;
        }
        return texto;
    }
    //Cuarto lote de métodos
    /*
     *
     *
     *
     * */

    /**
     * Función para cambiar el contexto del WebDriver para interactuar con un marco (frame)
     *
     * @param driver WebDriver es el que cambiará el contexto al marco especificado
     * @param frame  Identificador del marco al que se desea cambiar
     */
    public static void switchFrame(WebDriver driver, SearchContext searchContext, String frame) {
        String cadena = "#" + frame;
        WebElement iframe = SeleniumUtils.getElementIfExist(driver, searchContext, cadena);
        driver.switchTo().frame(iframe);
    }

    /*
     * Captura y registra un error 500 (Internal Server Error) en la aplicación si el texto de error es encontrado.
     *
     * @param driver         El controlador de WebDriver utilizado para interactuar con la página web.
     * @param searchContext  El contexto de búsqueda (puede ser un WebDriver o WebElement) donde se buscará el elemento.
     * @param element        El selector o identificador del elemento donde se buscará el mensaje de error.
     * @param messageWait    El mensaje de error esperado que se debe buscar.
     * @param comment        El comentario personalizado que se incluirá en caso de que se capture el error.
     * @param timeduration   La duración máxima en segundos que se debe esperar al buscar el mensaje de error.
     * @param timerepetition El número de repeticiones para intentar buscar el mensaje de error en el elemento.


    public static void capturar500ServerError(WebDriver driver, SearchContext searchContext, String element, String messageWait, String comment, int timeduration, int timerepetition) {
        String mesajeerror = obtenerTextWebElementx2(driver, searchContext, element, timeduration, timerepetition);
        WebElement elemento = getElementIfExist(driver, searchContext, element);
        if (!Objects.isNull(mesajeerror)) {
            if (StringUtils.equalsIgnoreCase(mesajeerror, messageWait) || StringUtils.containsIgnoreCase(mesajeerror, "Internal server error")) {
                LogsJB.fatal("Se encontro un mensaje de Error");
                LogsJB.info("*");
                LogsJB.info("*");
                LogsJB.info("Mensaje de Error Capturado: " + mesajeerror);
                LogsJB.info("*");
                LogsJB.info("*");
                Assert.fail(comment + mesajeerror);
            }
        } else {
            LogsJB.info("*");
            LogsJB.error("El servicio si está habilitado");
            LogsJB.info("*");
        }
    }
    */

    /***
     * Permite Aceptar las Alertas emergentes por medio de la definición estándar de W3C de los navegadores.
     * @param driver Web Driver que manipula el navegador
     * @param texto Texto a ingresar en el prompt
     */
    public static boolean handlePrompt(WebDriver driver, String texto) {
        try {
            // Intentar interactuar con el prompt usando JavaScript en caso de falla con Selenium
            SeleniumUtils.ejecutarJsComando(driver, "window.promptResult = prompt(arguments[0]);", texto);
            String promptResult = (String) SeleniumUtils.ejecutarJsComando(driver, "return window.promptResult;");
            if (promptResult == null) {
                throw new Exception("No se pudo manejar el prompt con JavaScript.");
            }
            return true;
        } catch (Exception javascriptException) {
            // Intentar interactuar con el prompt usando Selenium
            Alert alert = driver.switchTo().alert();
            alert.sendKeys(texto);
            alert.accept();
        } finally {
            return false;
        }
    }
}
