package io.github.josecarlosbran.SeleniumUtils;

import com.josebran.LogsJB.LogsJB;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Path;
import java.text.Normalizer;
import java.time.Duration;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SeleniumUtils {
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    static ExecutorService seleniumEjecutor = Executors.newVirtualThreadPerTaskExecutor();
    @Getter
    @Setter
    private static String inespecific = "N/E";
    @Getter
    @Setter
    private static Integer searchTime = 3000;
    @Getter
    @Setter
    private static Integer searchRepetitionTime = 50;

    /***
     * Método que presiona una tecla del teclado simulado por selenium
     * @param driver Driver que manipula el navegador y realiza las acciones
     * @param codigo Clave de la tecla que se desea presionar
     */
    public static void keyPress(WebDriver driver, Keys codigo) {
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(codigo).perform();
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
        }
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
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
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
        try {
            char asciiValue = (char) codigo;
            Actions actions = new Actions(driver);
            actions.keyDown(String.valueOf(asciiValue)).keyUp(String.valueOf(asciiValue)).perform();
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
        }
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
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
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
        try {
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
        } catch (Exception e) {
            LogsJB.fatal("Excepción al intentar verificar si el elemento está deshabilitado: " + ExceptionUtils.getStackTrace(e));
            return true; // En caso de excepción, consideramos el elemento como deshabilitado
        }
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
            LogsJB.fatal("error al parsear el Objeto object a strings");
            LogsJB.fatal(e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
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
            LogsJB.fatal("Se ha capturado un error en thread sleep");
            LogsJB.fatal("Stacktrace de la exception capturada: " + ExceptionUtils.getStackTrace(e));
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
            LogsJB.debug("Limpió el elemento.");
            SeleniumUtils.posicionarmeEn(driver, element);
            return true;
        } catch (ElementNotInteractableException e) {
            LogsJB.warning("Capturó ElementNotInteractableException. Intentará limpiar mediante JavaScript.");
            try {
                // Utilizar JavaScript para borrar el contenido del elemento
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].value = '';", element);
                LogsJB.debug("Limpió el elemento por medio de JavaScript.");
                return true;
            } catch (Exception jsException) {
                LogsJB.fatal("Excepción capturada al intentar limpiar mediante JavaScript: " + ExceptionUtils.getStackTrace(jsException));
                return false;
            }
        } catch (Exception e) {
            LogsJB.fatal("Excepción capturada al intentar limpiar el elemento: " + ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * Función que se utiliza para posicionarse en un elemento, utilizando un controlador WebDriver
     *
     * @param driver   WebDriver se utiliza para interactuar con el entorno
     * @param elemento WebElement el elemento al que se desea posicionarse
     */
    public static void posicionarmeEn(WebDriver driver, WebElement elemento) {
        try {
            if (Objects.isNull(elemento)) {
                return;
            }
            try {
                // Crear un objeto Actions
                Actions actions = new Actions(driver);
                // Desplazar el scroll hasta el elemento
                actions.moveToElement(elemento);
                actions.perform();
            } catch (WebDriverException e) {
                // Desplazar el scroll hasta el elemento
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elemento);
            }
        } catch (Exception e) {
            LogsJB.fatal("Excepción capturada al intentar hacer scroll en el elemento: " + elemento.toString());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
        }
    }

    /***
     * Verifica si un elemento existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param searchContext contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda
     * @return Retorna True si el elemento Existe, caso contrario retorna False
     */
    public static Boolean ElementoExistente(WebDriver driver, SearchContext searchContext, String element) {
        //Para optimizar el tiempo de respuestá
        LogsJB.debug("* ");
        LogsJB.debug(" Buscara si existe el elemento indicado: " + element);
        LogsJB.debug("* ");
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        addseconds.add(Calendar.MILLISECOND, SeleniumUtils.getSearchTime());
        fecha = addseconds.getTime();
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        //Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<Boolean> futureId = SeleniumParallel.elementExist(wait, searchContext, By.id(element));
        Future<Boolean> futureClassName = SeleniumParallel.elementExist(wait, searchContext, By.className(element));
        Future<Boolean> futureCss = SeleniumParallel.elementExist(wait, searchContext, By.cssSelector(element));
        Future<Boolean> futureTagName = SeleniumParallel.elementExist(wait, searchContext, By.tagName(element));
        Future<Boolean> futureLinkText = SeleniumParallel.elementExist(wait, searchContext, By.linkText(element));
        Future<Boolean> futurePartialLinkText = SeleniumParallel.elementExist(wait, searchContext, By.partialLinkText(element));
        Future<Boolean> futureXpath = SeleniumParallel.elementExist(wait, searchContext, By.xpath(element));
        Future<Boolean> futureName = SeleniumParallel.elementExist(wait, searchContext, By.name(element));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        while (!(futureId.isDone() && futureClassName.isDone() && futureCss.isDone() && futureTagName.isDone() && futureLinkText.isDone() && futurePartialLinkText.isDone() && futureXpath.isDone() && futureName.isDone()) && !fecha2.after(fecha)) {
            fecha2 = Calendar.getInstance().getTime();
            try {
                if (futureId.isDone() && futureId.get()) {
                    return true;
                }
                if (futureClassName.isDone() && futureClassName.get()) {
                    return true;
                }
                if (futureCss.isDone() && futureCss.get()) {
                    return true;
                }
                if (futureTagName.isDone() && futureTagName.get()) {
                    return true;
                }
                if (futureLinkText.isDone() && futureLinkText.get()) {
                    return true;
                }
                if (futurePartialLinkText.isDone() && futurePartialLinkText.get()) {
                    return true;
                }
                if (futureXpath.isDone() && futureXpath.get()) {
                    return true;
                }
                if (futureName.isDone() && futureName.get()) {
                    return true;
                }
            } catch (Exception e) {
                LogsJB.fatal("Error al obtener el resultado del Future: " + e.getMessage());
                LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            }
        }
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha);
        LogsJB.debug(" Fecha contra la que se comparo si transcurrieron los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha2);
        if (fecha2.after(fecha)) {
            LogsJB.info(" No Existe el elemento especificado: " + element);
        } else {
            LogsJB.info(" Logro encontrar el elemento especificado: " + element);
            return true;
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
    public static Boolean LimpiarElementoExistente(WebDriver driver, SearchContext searchContext, String element) {
        //Para optimizar el tiempo de respuestá
        LogsJB.debug("* ");
        LogsJB.debug(" Si existe el elemento indicado, lo limpiara: " + element);
        LogsJB.debug("* ");
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        addseconds.add(Calendar.MILLISECOND, SeleniumUtils.getSearchTime());
        fecha = addseconds.getTime();
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        //Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<Boolean> futureId = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, By.id(element));
        Future<Boolean> futureClassName = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, By.className(element));
        Future<Boolean> futureCss = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, By.cssSelector(element));
        Future<Boolean> futureTagName = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, By.tagName(element));
        Future<Boolean> futureLinkText = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, By.linkText(element));
        Future<Boolean> futurePartialLinkText = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, By.partialLinkText(element));
        Future<Boolean> futureXpath = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, By.xpath(element));
        Future<Boolean> futureName = SeleniumParallel.clearElementIfExist(driver, wait, searchContext, By.name(element));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        while (!(futureId.isDone() && futureClassName.isDone() && futureCss.isDone() && futureTagName.isDone() && futureLinkText.isDone() && futurePartialLinkText.isDone() && futureXpath.isDone() && futureName.isDone()) && !fecha2.after(fecha)) {
            fecha2 = Calendar.getInstance().getTime();
            try {
                if (futureId.isDone() && futureId.get()) {
                    return true;
                }
                if (futureClassName.isDone() && futureClassName.get()) {
                    return true;
                }
                if (futureCss.isDone() && futureCss.get()) {
                    return true;
                }
                if (futureTagName.isDone() && futureTagName.get()) {
                    return true;
                }
                if (futureLinkText.isDone() && futureLinkText.get()) {
                    return true;
                }
                if (futurePartialLinkText.isDone() && futurePartialLinkText.get()) {
                    return true;
                }
                if (futureXpath.isDone() && futureXpath.get()) {
                    return true;
                }
                if (futureName.isDone() && futureName.get()) {
                    return true;
                }
            } catch (Exception e) {
                LogsJB.fatal("Error al obtener el resultado del Future: " + e.getMessage());
                LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            }
        }
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha);
        LogsJB.debug(" Fecha contra la que se comparo si transcurrieron los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha2);
        if (fecha2.after(fecha)) {
            LogsJB.info(" No logro limpiar el elemento especificado: " + element);
        } else {
            LogsJB.info(" Logro limpiar el elemento especificado: " + element);
            return true;
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
     * @param driver Driver que manipula el navegador
     * @param elementScreenshot Elemento sobre el cual se desea tomar la captura de pantalla
     * @return Retorna un objeto File que representa la captura de pantalla del elemento, si no logra tomar la captura de pantalla retorna null
     */
    public static File getImageScreeenshotWebElement(WebDriver driver, WebElement elementScreenshot) {
        try {
            if (!Objects.isNull(elementScreenshot)) {
                String tempelement = SeleniumUtils.RefreshReferenceToElement(driver, elementScreenshot).toString().split(" -> ")[1];
                String[] data = tempelement.split(": ");
                String locator = data[0];
                String term = data[1];
                // Verificar si el string termina con "]},"
                if (term.endsWith("]}")) {
                    // Eliminar los últimos dos carácteres
                    term = term.substring(0, term.length() - 2);
                }
                switch (locator) {
                    case "xpath":
                        return driver.findElement(By.xpath(term)).getScreenshotAs(OutputType.FILE);
                    case "css selector":
                        return driver.findElement(By.cssSelector(term)).getScreenshotAs(OutputType.FILE);
                    case "id":
                        return driver.findElement(By.id(term)).getScreenshotAs(OutputType.FILE);
                    case "tag name":
                        return driver.findElement(By.tagName(term)).getScreenshotAs(OutputType.FILE);
                    case "name":
                        return driver.findElement(By.name(term)).getScreenshotAs(OutputType.FILE);
                    case "link text":
                        return driver.findElement(By.linkText(term)).getScreenshotAs(OutputType.FILE);
                    case "partial link text":
                        return driver.findElement(By.partialLinkText(term)).getScreenshotAs(OutputType.FILE);
                    case "class name":
                        return driver.findElement(By.className(term)).getScreenshotAs(OutputType.FILE);
                }
            }
            LogsJB.warning("No pudo tomar la captura de pantalla del elemento indicado, retorna null");
        } catch (org.openqa.selenium.InvalidSelectorException | org.openqa.selenium.NoSuchElementException ex) {
            return null;
        } catch (Exception e) {
            LogsJB.fatal("Excepción capturada al tomar la captura de pantalla");
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            return null;
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
        try {
            if (!Objects.isNull(elemento)) {
                String tempelement = elemento.toString().split(" -> ")[1];
                String[] data = tempelement.split(": ");
                String locator = data[0];
                String term = data[1];
                // Verificar si el string termina con "]},"
                if (term.endsWith("]}")) {
                    // Eliminar los últimos dos carácteres
                    term = term.substring(0, term.length() - 2);
                }
                switch (locator) {
                    case "xpath":
                        return driver.findElement(By.xpath(term));
                    case "css selector":
                        return driver.findElement(By.cssSelector(term));
                    case "id":
                        return driver.findElement(By.id(term));
                    case "tag name":
                        return driver.findElement(By.tagName(term));
                    case "name":
                        return driver.findElement(By.name(term));
                    case "link text":
                        return driver.findElement(By.linkText(term));
                    case "partial link text":
                        return driver.findElement(By.partialLinkText(term));
                    case "class name":
                        return driver.findElement(By.className(term));
                }
            }
            LogsJB.info("No fue posible refrescar la referencia al elemento");
        } catch (org.openqa.selenium.InvalidSelectorException | org.openqa.selenium.NoSuchElementException ex) {
            return null;
        } catch (Exception e) {
            LogsJB.fatal("Excepción al refrescar el elemento");
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            return null;
        }
        return null;
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
            if (cadenaNulaoVacia(text) || cadenaNulaoVacia(temp)) {
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].setAttribute('value', '" + keysToSend + "')", element);
                text = element.getAttribute("value");
                if (!cadenaNulaoVacia(text) || cadenaNulaoVacia(temp)) {
                    result = true;
                }
            }
            text = SeleniumUtils.getTextOfWebElement(driver, element);
            if (cadenaNulaoVacia(text) || cadenaNulaoVacia(temp)) {
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].setAttribute('innerText', '" + keysToSend + "')", element);
                text = element.getAttribute("innerText");
                if (!cadenaNulaoVacia(text) || cadenaNulaoVacia(temp)) {
                    result = true;
                }
            }
            text = SeleniumUtils.getTextOfWebElement(driver, element);
            if (cadenaNulaoVacia(text)) {
                result = false;
            }
        } catch (Exception e) {
            LogsJB.fatal("Excepción capturada al intentar enviar el texto" + Arrays.toString(keysToSend) +
                    " al  elemento: " + element.toString());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            result = false;
        } finally {
            return result;
        }
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
    public static Boolean sendKeysIfElementExist(WebDriver driver, SearchContext searchContext, String element, CharSequence... Texto) {
        //Para optimizar el tiempo de respuestá
        LogsJB.debug("* ");
        LogsJB.debug(" Si existe el elemento " + element +
                ", enviara el texto: " + Arrays.toString(Texto).substring(1, Arrays.toString(Texto).length() - 1));
        LogsJB.debug("* ");
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        addseconds.add(Calendar.MILLISECOND, SeleniumUtils.getSearchTime());
        fecha = addseconds.getTime();
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        //Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<Boolean> futureId = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, By.id(element), Texto);
        Future<Boolean> futureClassName = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, By.className(element), Texto);
        Future<Boolean> futureCss = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, By.cssSelector(element), Texto);
        Future<Boolean> futureTagName = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, By.tagName(element), Texto);
        Future<Boolean> futureLinkText = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, By.linkText(element), Texto);
        Future<Boolean> futurePartialLinkText = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, By.partialLinkText(element), Texto);
        Future<Boolean> futureXpath = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, By.xpath(element), Texto);
        Future<Boolean> futureName = SeleniumParallel.sendKeysIfElementExist(driver, wait, searchContext, By.name(element), Texto);
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        while (!(futureId.isDone() && futureClassName.isDone() && futureCss.isDone() && futureTagName.isDone() && futureLinkText.isDone() && futurePartialLinkText.isDone() && futureXpath.isDone() && futureName.isDone()) && !fecha2.after(fecha)) {
            fecha2 = Calendar.getInstance().getTime();
            try {
                if (futureId.isDone() && futureId.get()) {
                    return true;
                }
                if (futureClassName.isDone() && futureClassName.get()) {
                    return true;
                }
                if (futureCss.isDone() && futureCss.get()) {
                    return true;
                }
                if (futureTagName.isDone() && futureTagName.get()) {
                    return true;
                }
                if (futureLinkText.isDone() && futureLinkText.get()) {
                    return true;
                }
                if (futurePartialLinkText.isDone() && futurePartialLinkText.get()) {
                    return true;
                }
                if (futureXpath.isDone() && futureXpath.get()) {
                    return true;
                }
                if (futureName.isDone() && futureName.get()) {
                    return true;
                }
            } catch (Exception e) {
                LogsJB.fatal("Error al obtener el resultado del Future: " + e.getMessage());
                LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            }
        }
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha);
        LogsJB.debug(" Fecha contra la que se comparo si transcurrieron los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha2);
        if (fecha2.after(fecha)) {
            LogsJB.info(" No logro encontrar y setear el Texto: " + Arrays.toString(Texto) +
                    " en el elemento especificado: " + element);
        } else {
            LogsJB.info(" Logro encontrar y setear el Texto: " + Arrays.toString(Texto) +
                    " en el elemento especificado: " + element);
            return true;
        }
        //Retorna Falso si el elemento no Existe
        return false;
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
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        addseconds.add(Calendar.MILLISECOND, SeleniumUtils.getSearchTime());
        fecha = addseconds.getTime();
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " + timeDuration + " mili segundos: " + timeRepetition);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, timeDuration, timeRepetition);
        //Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<String> futureId = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, By.id(element));
        Future<String> futureClassName = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, By.className(element));
        Future<String> futureCss = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, By.cssSelector(element));
        Future<String> futureTagName = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, By.tagName(element));
        Future<String> futureLinkText = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, By.linkText(element));
        Future<String> futurePartialLinkText = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, By.partialLinkText(element));
        Future<String> futureXpath = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, By.xpath(element));
        Future<String> futureName = SeleniumParallel.getTextIfElementExist(driver, wait, searchContext, By.name(element));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        while (!(futureId.isDone() && futureClassName.isDone() && futureCss.isDone() && futureTagName.isDone() && futureLinkText.isDone() && futurePartialLinkText.isDone() && futureXpath.isDone() && futureName.isDone()) && !fecha2.after(fecha)) {
            fecha2 = Calendar.getInstance().getTime();
            try {
                if (futureId.isDone() && !futureId.get().isEmpty()) {
                    texto = futureId.get();
                    break;
                }
                if (futureClassName.isDone() && !futureClassName.get().isEmpty()) {
                    texto = futureClassName.get();
                    break;
                }
                if (futureCss.isDone() && !futureCss.get().isEmpty()) {
                    texto = futureCss.get();
                    break;
                }
                if (futureTagName.isDone() && !futureTagName.get().isEmpty()) {
                    texto = futureTagName.get();
                    break;
                }
                if (futureLinkText.isDone() && !futureLinkText.get().isEmpty()) {
                    texto = futureLinkText.get();
                    break;
                }
                if (futurePartialLinkText.isDone() && !futurePartialLinkText.get().isEmpty()) {
                    texto = futurePartialLinkText.get();
                    break;
                }
                if (futureXpath.isDone() && !futureXpath.get().isEmpty()) {
                    texto = futureXpath.get();
                    break;
                }
                if (futureName.isDone() && !futureName.get().isEmpty()) {
                    texto = futureName.get();
                    break;
                }
            } catch (Exception e) {
                LogsJB.fatal("Error al obtener el resultado del Future: " + e.getMessage());
                LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            }
        }
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha);
        LogsJB.debug(" Fecha contra la que se comparo si transcurrieron los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha2);
        if (fecha2.after(fecha)) {
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
        try {
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
        } catch (WebDriverException e) {
            LogsJB.fatal("El elemento ya no existe en el contexto actual ");
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
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
        try {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver; // Asegúrate de tener una instancia válida de WebDriver
            return (String) jsExecutor.executeScript("return arguments[0].textContent", element);
        } catch (Exception e) {
            LogsJB.fatal("Error al intentar obtener el texto mediante JavaScript: " + ExceptionUtils.getStackTrace(e));
            return "";
        }
    }

    /***
     * Hace clic en el elemento indicado, si este existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param searchContext Contexto en el que se desea buscar el elemento
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda
     * @return Retorna True si logra hacer clic en el elemento, de lo contrario false
     */
    public static Boolean clickElementIfExist(WebDriver driver, SearchContext searchContext, String element) {
        //Para optimizar el tiempo de respuestá
        LogsJB.debug("* ");
        LogsJB.debug(" Si existe el elemento indicado, hará click en el elemento: " + element);
        LogsJB.debug("* ");
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        addseconds.add(Calendar.MILLISECOND, SeleniumUtils.getSearchTime());
        fecha = addseconds.getTime();
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        //Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<Boolean> futureId = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, By.id(element));
        Future<Boolean> futureClassName = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, By.className(element));
        Future<Boolean> futureCss = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, By.cssSelector(element));
        Future<Boolean> futureTagName = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, By.tagName(element));
        Future<Boolean> futureLinkText = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, By.linkText(element));
        Future<Boolean> futurePartialLinkText = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, By.partialLinkText(element));
        Future<Boolean> futureXpath = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, By.xpath(element));
        Future<Boolean> futureName = SeleniumParallel.clickElementIfExist(driver, wait, searchContext, By.name(element));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        while (!(futureId.isDone() && futureClassName.isDone() && futureCss.isDone() && futureTagName.isDone() && futureLinkText.isDone() && futurePartialLinkText.isDone() && futureXpath.isDone() && futureName.isDone()) && !fecha2.after(fecha)) {
            fecha2 = Calendar.getInstance().getTime();
            try {
                if (futureId.isDone() && futureId.get()) {
                    return true;
                }
                if (futureClassName.isDone() && futureClassName.get()) {
                    return true;
                }
                if (futureCss.isDone() && futureCss.get()) {
                    return true;
                }
                if (futureTagName.isDone() && futureTagName.get()) {
                    return true;
                }
                if (futureLinkText.isDone() && futureLinkText.get()) {
                    return true;
                }
                if (futurePartialLinkText.isDone() && futurePartialLinkText.get()) {
                    return true;
                }
                if (futureXpath.isDone() && futureXpath.get()) {
                    return true;
                }
                if (futureName.isDone() && futureName.get()) {
                    return true;
                }
            } catch (Exception e) {
                LogsJB.fatal("Error al obtener el resultado del Future: " + e.getMessage());
                LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            }
        }
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha);
        LogsJB.debug(" Fecha contra la que se comparo si transcurrieron los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha2);
        if (fecha2.after(fecha)) {
            LogsJB.info(" No pudo hacer click en el elemento especificado, ya que no existe: " + element);
        } else {
            LogsJB.info(" Logro encontrar y hacer click en el elemento especificado: " + element);
            return true;
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
    public static Boolean clicktoElementx2intents(WebDriver driver, SearchContext searchContext, String element) {
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
        String locator = data[0];
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
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(driver1 -> !ElementoDeshabilitado(driver1.findElement(by)));
            bandera = true;
        } catch (TimeoutException ignored) {
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al esperar la aparicion del elemento: " + by);
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
        } finally {
            return bandera;
        }
    }


    /**
     * Espera implícita de 30 segundos, luego de los 30 segundos lanzara excepción
     *
     * @param driver Driver que está manipulando el navegador
     * @param by     Identificador del tipo By
     * @param banderaAssert bandera para decidir si se quiere manejar o no, el assertFail
     * @return retorna verdadero si se da la espera de manera correcta
     */
    public static boolean waitImplicity(WebDriver driver, By by,boolean banderaAssert) {
        boolean bandera = false;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(driver1 -> !ElementoDeshabilitado(driver1.findElement(by)));
            bandera = true;
        } catch (TimeoutException ignored) {
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al esperar la aparicion del elemento: " + by);
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
                Assert.fail("Error inesperado al esperar la aparicion del elemento: " + by);

            }
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
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        addseconds.add(Calendar.MILLISECOND, SeleniumUtils.getSearchTime());
        fecha = addseconds.getTime();
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        //Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<List<WebElement>> futureId = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, By.id(element));
        Future<List<WebElement>> futureClassName = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, By.className(element));
        Future<List<WebElement>> futureCss = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, By.cssSelector(element));
        Future<List<WebElement>> futureTagName = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, By.tagName(element));
        Future<List<WebElement>> futureLinkText = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, By.linkText(element));
        Future<List<WebElement>> futurePartialLinkText = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, By.partialLinkText(element));
        Future<List<WebElement>> futureXpath = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, By.xpath(element));
        Future<List<WebElement>> futureName = SeleniumParallel.getElementsIfExist(driver, wait, searchContext, By.name(element));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        while (!fecha2.after(fecha)) {
            fecha2 = Calendar.getInstance().getTime();
            try {
                if (futureId.isDone() && !futureId.get().isEmpty()) {
                    LogsJB.info("Elementos encontrados por ID: " + futureId.get());
                    return futureId.get();
                }
                if (futureClassName.isDone() && !futureClassName.get().isEmpty()) {
                    LogsJB.info("Elementos encontrados por ClassName: " + futureClassName.get());
                    return futureClassName.get();
                }
                if (futureCss.isDone() && !futureCss.get().isEmpty()) {
                    LogsJB.info("Elementos encontrados por CSS: " + futureCss.get());
                    return futureCss.get();
                }
                if (futureTagName.isDone() && !futureTagName.get().isEmpty()) {
                    LogsJB.info("Elementos encontrados por TagName: " + futureTagName.get());
                    return futureTagName.get();
                }
                if (futureLinkText.isDone() && !futureLinkText.get().isEmpty()) {
                    LogsJB.info("Elementos encontrados por LinkText: " + futureLinkText.get());
                    return futureLinkText.get();
                }
                if (futurePartialLinkText.isDone() && !futurePartialLinkText.get().isEmpty()) {
                    LogsJB.info("Elementos encontrados por PartialLinkText: " + futurePartialLinkText.get());
                    return futurePartialLinkText.get();
                }
                if (futureXpath.isDone() && !futureXpath.get().isEmpty()) {
                    LogsJB.info("Elementos encontrados por XPath: " + futureXpath.get());
                    return futureXpath.get();
                }
                if (futureName.isDone() && !futureName.get().isEmpty()) {
                    LogsJB.info("Elementos encontrados por Name: " + futureName.get());
                    return futureName.get();
                }
            } catch (Exception e) {
                LogsJB.fatal("Error al obtener el resultado del Future: " + e.getMessage());
                LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            }
        }
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha);
        LogsJB.debug(" Fecha contra la que se comparo si transcurrieron los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha2);
        if ((fecha2.after(fecha)) || elementos.isEmpty()) {
            LogsJB.warning(" No pudo obtener los elementos especificados, ya que no existen: " + elementos);
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
            driver.switchTo().frame(frame);
            SeleniumUtils.threadslepp(200);
            LogsJB.info("El Iframe Obtenido no es nulo, es: " + frame);
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
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        addseconds.add(Calendar.MILLISECOND, SeleniumUtils.getSearchTime());
        fecha = addseconds.getTime();
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " + SeleniumUtils.getSearchTime() + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = SeleniumUtils.getFluentWait(driver, SeleniumUtils.getSearchTime(), SeleniumUtils.getSearchRepetitionTime());
        // Declaración de features para obtener el resultado de buscar los elementos en cuestión
        Future<WebElement> futureId = SeleniumParallel.getElementIfExist(driver, wait, searchContext, By.id(element));
        Future<WebElement> futureClassName = SeleniumParallel.getElementIfExist(driver, wait, searchContext, By.className(element));
        Future<WebElement> futureCss = SeleniumParallel.getElementIfExist(driver, wait, searchContext, By.cssSelector(element));
        Future<WebElement> futureTagName = SeleniumParallel.getElementIfExist(driver, wait, searchContext, By.tagName(element));
        Future<WebElement> futureLinkText = SeleniumParallel.getElementIfExist(driver, wait, searchContext, By.linkText(element));
        Future<WebElement> futurePartialLinkText = SeleniumParallel.getElementIfExist(driver, wait, searchContext, By.partialLinkText(element));
        Future<WebElement> futureXpath = SeleniumParallel.getElementIfExist(driver, wait, searchContext, By.xpath(element));
        Future<WebElement> futureName = SeleniumParallel.getElementIfExist(driver, wait, searchContext, By.name(element));
        // Esperará saber si existe el elemento en alguno de los tipos usando Future
        while (!fecha2.after(fecha)) {
            fecha2 = Calendar.getInstance().getTime();
            try {
                if (futureId.isDone()) {
                    elemento = futureId.get();
                    if (elemento != null) {
                        LogsJB.info("Elemento encontrado por ID: " + elemento);
                        return elemento;
                    }
                }
                if (futureClassName.isDone()) {
                    elemento = futureClassName.get();
                    if (elemento != null) {
                        LogsJB.info("Elemento encontrado por ClassName: " + elemento);
                        return elemento;
                    }
                }
                if (futureCss.isDone()) {
                    elemento = futureCss.get();
                    if (elemento != null) {
                        LogsJB.info("Elemento encontrado por CSS: " + elemento);
                        return elemento;
                    }
                }
                if (futureTagName.isDone()) {
                    elemento = futureTagName.get();
                    if (elemento != null) {
                        LogsJB.info("Elemento encontrado por TagName: " + elemento);
                        return elemento;
                    }
                }
                if (futureLinkText.isDone()) {
                    elemento = futureLinkText.get();
                    if (elemento != null) {
                        LogsJB.info("Elemento encontrado por LinkText: " + elemento);
                        return elemento;
                    }
                }
                if (futurePartialLinkText.isDone()) {
                    elemento = futurePartialLinkText.get();
                    if (elemento != null) {
                        LogsJB.info("Elemento encontrado por PartialLinkText: " + elemento);
                        return elemento;
                    }
                }
                if (futureXpath.isDone()) {
                    elemento = futureXpath.get();
                    if (elemento != null) {
                        LogsJB.info("Elemento encontrado por XPath: " + elemento);
                        return elemento;
                    }
                }
                if (futureName.isDone()) {
                    elemento = futureName.get();
                    if (elemento != null) {
                        LogsJB.info("Elemento encontrado por Name: " + elemento);
                        return elemento;
                    }
                }
            } catch (Exception e) {
                LogsJB.fatal("Error al obtener el resultado del Future: " + e.getMessage());
                LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            }
        }
        LogsJB.debug(" Fecha contra la que se comparara si transcurren los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha);
        LogsJB.debug(" Fecha contra la que se comparo si transcurrieron los " +
                SeleniumUtils.getSearchTime() +
                " mili segundos: " + fecha2);
        if ((fecha2.after(fecha)) || Objects.isNull(elemento)) {
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
        List<WebElement> temp = new ArrayList<>();
        temp = SeleniumUtils.getElementsIfExist(driver, searchContext, SeleniumUtils.getIdentificadorBy(element));
        return temp;
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
                String tempelement = element.toString().split(" -> ")[1];
                String[] data = tempelement.substring(0, tempelement.length() - 1).split(": ");
                String locator = data[0];
                String term = data[1];
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
                String tempelement = element.toString().split(" -> ")[1];
                String[] data = tempelement.substring(0, tempelement.length() - 1).split(": ");
                String locator = data[0];
                String term = data[1];
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
            LogsJB.fatal(e.getMessage());
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
        WebElement campo = SeleniumUtils.obtenerWebElementx2(driver, searchcontext, element);
        assert campo != null;
        String texto = SeleniumUtils.getTextOfWebElement(driver, campo);
        LogsJB.info("Texto que tiene el elemento: " + texto);
        if (SeleniumUtils.clicktoElementx2intents(driver, searchcontext, element)) {
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
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String currentFrame = (String) jsExecutor.executeScript("return self.frameElement ? self.frameElement.id : ''");
        LogsJB.info("Frame: " + currentFrame);
        return !currentFrame.isEmpty();
    }

    /**
     * Función que permite implementar y modificar un tiempo de espera.
     *
     * @param driver maneja los tiempos de espera para la carga de elementos
     * @param segs   indica los segundos del tiempo de espera.
     */
    public static boolean waitCall(WebDriver driver, int segs) {
        try {
            driver.manage().timeouts().implicitlyWait(segs, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            LogsJB.info("----------------------");
            LogsJB.fatal(e.getMessage());
            LogsJB.info("----------------------");
            return false;
        }
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
            WebElement element = RefreshReferenceToElement(driver, elementScreenshot);
            // Desplazarse hasta el elemento
            Actions actions = new Actions(driver);
            actions.moveToElement(element);
            actions.perform();
            // Tomar la altura del elemento después de desplazarse
            int elementHeight = element.getSize().getHeight();
            // Calcular porcentaje de Zoom necesario
            double windowHeight = driver.manage().window().getSize().getHeight();
            double zoomPercentage = 1.0;
            if (windowHeight < elementHeight) {
                zoomPercentage = Math.min(1.0, windowHeight / elementHeight); // Limitar zoom a máximo 100%
            } else {
                zoomPercentage = Math.min(1.0, elementHeight / windowHeight); // Limitar zoom a máximo 100%
            }
            zoomPercentage = zoomPercentage < 0.5 ? 1 : zoomPercentage;
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String zoomActual = js.executeScript("return document.body.style.zoom").toString();
            // Aplicar el zoom usando JavaScript
            if (!cadenaNulaoVacia(zoomActual)) {
                try {
                    double zoomActualValue = Double.parseDouble(zoomActual.replace("%", ""));
                    double newZoomValue = (zoomActualValue < 100.0) ? (zoomActualValue - 15.0) : 100.0;
                    js.executeScript("document.body.style.zoom = '" + newZoomValue + "%'");
                } catch (NumberFormatException e) {
                    LogsJB.info("El valor de zoomActual no es numérico: "+zoomActual);
                }
            }
            File scrFile = SeleniumUtils.getImageScreeenshotWebElement(driver, elementScreenshot);
            if (Objects.isNull(scrFile)) {
                TakesScreenshot scrShot = ((TakesScreenshot) driver);
                // Restáurar el Zoom a 100% si fue ajustado
                js.executeScript("document.body.style.zoom = '100%';");
                return scrShot.getScreenshotAs(OutputType.FILE);
            } else {
                // Restáurar el Zoom a 100% si fue ajustado
                js.executeScript("document.body.style.zoom = '100%';");
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
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (TimeoutException ignored) {
            return false;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al esperar la aparicion del elemento: " + by);
            LogsJB.fatal("*");
            LogsJB.fatal(" " + e);
            LogsJB.fatal(" Tipo de Excepción : " + e.getClass());
            LogsJB.fatal(" Causa de la Excepción : " + e.getCause());
            LogsJB.fatal(" Mensaje de la Excepción : " + e.getMessage());
            LogsJB.fatal("*");
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * Espera implícita de 5 segundos o menos si el elemento desaparece del DOM, luego de los 5 segundos lanzara excepción
     *
     * @param driver Variable que manipula el navegador
     * @param by     Identificador del tipo By
     * @param banderaAssert Bandera para preguntar si se quiere el assertFail
     */
    public static boolean waitImplicityForElementNotExist(WebDriver driver, By by,boolean banderaAssert) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (TimeoutException ignored) {
            return false;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al esperar la aparicion del elemento: " + by);
            LogsJB.fatal("*");
            LogsJB.fatal(" " + e);
            LogsJB.fatal(" Tipo de Excepción : " + e.getClass());
            LogsJB.fatal(" Causa de la Excepción : " + e.getCause());
            LogsJB.fatal(" Mensaje de la Excepción : " + e.getMessage());
            LogsJB.fatal("*");
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
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
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.confirm=function(){return " + aceptar + "}");
            return true;
        } catch (WebDriverException e) {
            LogsJB.fatal("Error WebDriver al interactuar con la alerta: " + e.getMessage());
            return false;
            // Puedes agregar más manejo de excepciones específicas según sea necesario
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al esperar la aparición del elemento: " + e.getMessage());
            return false;
        }
    }

    /***
     * Permite Aceptar las Alertas emergentes por medio de la definición estándar de W3C de los navegadores.
     * @param driver Web Driver que manipula el navegador
     */
    public static boolean acceptAlert(WebDriver driver) {
        try {
            Wait wait = getFluentWait(driver, 5000, 100);
            Alert alert = (Alert) wait.until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            LogsJB.info(text);
            alert.accept();
            return true;
        } catch (java.util.NoSuchElementException e) {
            // Manejar la falta de alerta específica si es necesario
            LogsJB.fatal("No se encontró ninguna alerta presente.");
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("window.alert = function() {};");
            return false;
        } catch (WebDriverException e) {
            LogsJB.fatal("Error WebDriver al interactuar con la alerta: " + e.getMessage());
            return false;
            // Puedes agregar más manejo de excepciones específicas según sea necesario
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al esperar la aparición del elemento: " + e.getMessage());
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
    public static boolean cambiarZOOM(WebDriver driver, int repeticiones, Keys codigo) {
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
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
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
    public static boolean cambiarZOOM(WebDriver driver, int repeticiones, Keys codigo,boolean banderaAssert) {
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
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
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
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
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
    public static boolean cambiarZOOM(WebDriver driver, int repeticiones, int codigo,boolean banderaAssert) {
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
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
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
        try {
            cambiarZOOM(driver, repeticiones, Keys.SUBTRACT);
            return true;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
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
    public static boolean cambiarZOOMMenos(WebDriver driver, int repeticiones,boolean banderaAssert) {
        try {
            cambiarZOOM(driver, repeticiones, Keys.SUBTRACT);
            return true;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
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
    public static boolean cambiarZOOMMas(WebDriver driver, int repeticiones) {
        try {
            cambiarZOOM(driver, repeticiones, Keys.ADD);
            return true;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
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
    public static boolean cambiarZOOMMas(WebDriver driver, int repeticiones,boolean banderaAssert) {
        try {
            cambiarZOOM(driver, repeticiones, Keys.ADD);
            return true;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
                Assert.fail("Error inesperado al presionar una tecla: ");

            }
            return false;
        }
    }

    /**
     * Mueve el scroll del mouse
     *
     * @param cantidad Si el número es positivo, el desplazamiento es hacia abajo en la pantalla, si el número es negativo
     *                 el desplazamiento es hacia arriba.
     */
    public static boolean scrollMouse(int cantidad) {
        try {
            Robot robot = new Robot();
            int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width / 2;
            int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height / 2;
            LogsJB.info("Altura de la pantalla " + alto * 2 + " ancho de la pantalla " + ancho * 2);
            robot.mouseMove(ancho, alto);
            robot.mouseWheel(cantidad);
            LogsJB.info("Se realizo el movimiento del scroll: ");
            threadslepp(100);
            return true;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al realizar un el scroll: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * Mueve el scroll del mouse
     *
     * @param cantidad Si el número es positivo, el desplazamiento es hacia abajo en la pantalla, si el número es negativo
     *                 el desplazamiento es hacia arriba.
     */
    public static boolean scrollMouse(int cantidad,boolean banderaAssert) {
        try {
            Robot robot = new Robot();
            int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width / 2;
            int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height / 2;
            LogsJB.info("Altura de la pantalla " + alto * 2 + " ancho de la pantalla " + ancho * 2);
            robot.mouseMove(ancho, alto);
            robot.mouseWheel(cantidad);
            LogsJB.info("Se realizo el movimiento del scroll: ");
            threadslepp(100);
            return true;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al realizar un el scroll: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
                Assert.fail("Error inesperado al intentar realizar el scroll: ");

            }
            return false;
        }
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param driver          Driver que está manipulando el navegador
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia abajo.
     */
    public static boolean scrollMouseDown(WebDriver driver, int cantidadScrolls) {
        try {
            Actions actions = new Actions(driver);
            for (int i = 0; i < cantidadScrolls; i++) {
                actions.sendKeys(Keys.PAGE_DOWN).build().perform();
            }
            return true;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al realizar el scroll: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param driver          Driver que está manipulando el navegador
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia abajo.
     */
    public static boolean scrollMouseDown(WebDriver driver, int cantidadScrolls,boolean banderaAssert) {
        try {
            Actions actions = new Actions(driver);
            for (int i = 0; i < cantidadScrolls; i++) {
                actions.sendKeys(Keys.PAGE_DOWN).build().perform();
            }
            return true;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al realizar el scroll: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
                Assert.fail("Error inesperado al intentar realizar el scroll: ");

            }
            return false;
        }
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param driver          Driver que está manipulando el navegador
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia arriba.
     */
    public static boolean scrollMouseUp(WebDriver driver, int cantidadScrolls) {
        try {
            Actions actions = new Actions(driver);
            for (int i = 0; i < cantidadScrolls; i++) {
                actions.sendKeys(Keys.PAGE_UP).build().perform();
            }
            return true;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al realizar el scroll: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param driver          Driver que está manipulando el navegador
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia arriba.
     */
    public static boolean scrollMouseUp(WebDriver driver, int cantidadScrolls,boolean banderaAssert) {
        try {
            Actions actions = new Actions(driver);
            for (int i = 0; i < cantidadScrolls; i++) {
                actions.sendKeys(Keys.PAGE_UP).build().perform();
            }
            return true;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al realizar el scroll: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
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
     * @param comment Comentario que será colocado sobre la imagen capturada si el Elemento indicado existe
     */
    public static boolean selectOption(WebDriver driver, SearchContext searchcontext, String element, String opcion, String comment,boolean banderaAssert) {
        try {
            WebElement elemento = obtenerWebElementx2(driver, searchcontext, element);
            if (!Objects.isNull(elemento)) {
                //Si encuentra el elemento ejecuta este codigo
                try {
                    int opcionint;
                    opcionint = Integer.parseInt(opcion);
                    LogsJB.info("Parcio la opcion a entero: " + opcionint);
                    new Select(elemento).selectByIndex(opcionint - 1);
                    LogsJB.info("Encontro el elemento Select: " + element + " " +
                            "Procedera a seleccionar la opcion por medio del Index numerico");
                } catch (NumberFormatException e) {
                    new Select(elemento).selectByVisibleText(opcion);
                    LogsJB.info("Encontro el elemento Select: " + element + " " +
                            "Procedera a seleccionar la opcion por medio del Texto Visible");
                }
            } else {
                LogsJB.info("No pudo encontrar el elemento: " + element + " por lo que no se pudo seleccionar la opcion indicada");
            }
            return true;
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al seleccionar el elemento: " + element + " " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
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
        WebElement elemento = obtenerWebElementx2(driver, searchcontext, element);
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
            LogsJB.fatal("Error inesperado al seleccionar el elemento: " + element + " " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
        }
        return false;
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
            frame = obtenerWebElementx2(driver, searchcontext, frameIDorName);
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
        try {
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
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", element);
                LogsJB.info("Hizo clic en el elemento por medio de JavaScript.");
                return true;
            }
        } catch (Exception e) {
            LogsJB.fatal("Excepción capturada al intentar hacer clic en el elemento: " + element.toString());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            return false;
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
     * @param Campo  Texto incluido en provider para validar si existe contenido.
     * @param Nombre Nombre del campo que se valida.
     */
    public static boolean ValidarNull(String Campo, String Nombre) {
        if (cadenaNulaoVacia(Campo)) {
            LogsJB.error("Debe de ingresar el valor del campo: " + Nombre);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Función para subir archivos
     *
     * @param driver WebDriver se utiliza para interactuar con los elementos de la BERediseño
     * @param path   Identificador de la ruta donde se encuntra hubicado el archivo
     */
    public static boolean subirArchivo(WebDriver driver, String elementoFile, String path) {
        try {
            WebElement subirArchivo = SeleniumUtils.obtenerWebElementx2(driver, driver, elementoFile);
            SeleniumUtils.sendKeysToElement(driver, subirArchivo, path);
            StringUtils.equalsIgnoreCase("", "");
            Path ruta = Path.of(path);
            String nameFile = ruta.getFileName().toString();
            LogsJB.info("Se subio archivo: " + nameFile);
            return true;
        } catch (Exception e) {
            LogsJB.error("No se pudo subir el archivo: " + ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * método para regresar el driver al contenido principal, el que aparece cuando se inicia la página.
     *
     * @param driver es el manejador de la página, el cual será regresado al contenido principal
     */
    public static boolean regresarFramePrincipal(WebDriver driver) {
        try {
            driver.switchTo().defaultContent();
            LogsJB.info("Se regresa al frame inicial ");
            return true;
        } catch (Exception ex) {
            LogsJB.error("Se ha capturado un error al cambiar al frame principal: ");
            LogsJB.fatal("Error: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Este método convierte una direccion ip a terminal(Hexadecimal):
     * Por ejemplo: 127.0.0.1-->7F000001
     *
     * @param ipDecimal
     * @return
     */
    public static String convertirIpDecimalAHexadecimal(String ipDecimal) {
        // Dividimos la dirección IP en sus componentes (octetos)
        String[] octetos = ipDecimal.split("\\.");
        // Convertimos cada octeto a su representación hexadecimal
        StringBuilder hex = new StringBuilder();
        for (String octeto : octetos) {
            // Parseamos el octeto a un entero y lo convertimos a hexadecimal
            String hexOcteto = Integer.toHexString(Integer.parseInt(octeto));
            // Si el octeto hexadecimal tiene solo un carácter, añadimos un cero al principio
            if (hexOcteto.length() == 1) {
                hex.append("0");
            }
            hex.append(hexOcteto);
        }
        // Convertimos la cadena a mayúsculas para el formato deseado
        return hex.toString().toUpperCase();
    }

    public static boolean JsComando(WebDriver driver, String comandoJs) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            js.executeScript(comandoJs);
            LogsJB.info("Ejecuta el siguiente comando de Javasript " + comandoJs);
            return true;
        } catch (Exception ex) {
            LogsJB.error("Error capturado en la función que ejecuta comandos JS: " + comandoJs);
            LogsJB.error("StackTrace de la excepción: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    /***
     * Mueve el navegador a la tab que está recibiendo como parametro
     * @param driver Driver que está manipulando el navegador
     * @param previousTab Previous Tab al que nos queremos mover
     */
    public void movetoPreviousTab(WebDriver driver, String previousTab) {
        if (SeleniumUtils.esValorValido(previousTab)) {
            //Loop through until we find a new window handle
            for (String windowHandle : driver.getWindowHandles()) {
                LogsJB.debug("Previus Tab: " + previousTab);
                LogsJB.debug("Windows Handle: " + windowHandle);
                if (previousTab.contentEquals(windowHandle)) {
                    LogsJB.info("Se movera el driver a la pestaña solicitada");
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
    public void sendKeystoElementvalidValueX2(WebDriver driver,SearchContext searchContext, String element, String value) {
        if (!SeleniumUtils.cadenaNulaoVacia(value)) {
            if (!value.equalsIgnoreCase(inespecific)) {
                sendkeystoelementX2(driver,searchContext, element, value);
            }
        }
    }

    /**
     * Envia el texto al elemento especificado
     *
     * @param driver  driver que manipula el navegador
     * @param element elemento al que se envÃ­ara el texto
     * @param value   Valor que se validara no sea null o vacio
     */
    public void sendKeystoElementvalidValue(WebDriver driver,SearchContext searchContext, String element, String value) {
        if (!SeleniumUtils.cadenaNulaoVacia(value)) {
            if (!value.equalsIgnoreCase(inespecific)) {
                sendKeystoElement(driver, searchContext,element, false);
            }
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
    private Boolean sendKeystoElementx2intents(WebDriver driver,SearchContext searchContext, String element, CharSequence... texto) {
        int i = 0;
        while (i < 2) {
            if (SeleniumUtils.sendKeysIfElementExist(driver,searchContext, element, texto)) {
                return true;
            }
            i++;
        }
        return false;
    }

    /***
     * Envía dos veces el texto indicado al elemento indicado
     * @param searchContext Driver que está manipulando el navegador
     * @param elemento Atributo por medio del cual identificaremos el elemento a modificar
     * @param texto Texto que deseamos envíar al elmento
     */
    public void sendkeystoelementX2(WebDriver driver,SearchContext searchContext, String elemento, String texto) {
        sendKeystoElement(driver,searchContext,elemento,false,texto);
        SeleniumUtils.keyPress(driver, Keys.ENTER);
        sendKeystoElement(driver,searchContext, elemento,false, texto);
        SeleniumUtils.keyPress(driver, Keys.ENTER);
    }

    /***
     * Realiza 2 veces la busquedad de el texto de un elemento
     * @param driver Driver que controla el navegador
     * @param element Atributo del elemento a buscar
     * @return Si logra obtener el texto del elemento especifícado, lo retorna, de lo contrario retorna NULL
     */
    public String obtenerTextWebElementx2(WebDriver driver,SearchContext searchContext, String element) {
        int i = 0;
        String texto = null;
        while (Objects.isNull(texto) && i < 2) {
            texto = SeleniumUtils.getTextIfElementExist(driver, searchContext, element);
            i++;
        }
        return texto;
    }

    /****
     * Realiza 2 veces la busquedad de el texto de un elemento
     * @param driver Driver que controla el navegador
     * @param element Atributo del elemento a buscar
     * @param timeduration Duración de la busquedad del texto del elemento especificado
     * @param timerepetition Tiempo de repeticion para realizar la busquedad del elemento y obtener el texto
     * @return Si logra obtener el texto del elemento especifícado, lo retorna, de lo contrario retorna NULL
     */
    public String obtenerTextWebElementx2(WebDriver driver,SearchContext searchContext, String element, int timeduration, int timerepetition) {
        int i = 0;
        String texto = null;
        while (Objects.isNull(texto) && i < 2) {
            texto = SeleniumUtils.getTextIfElementExist(driver, searchContext, element, timeduration, timerepetition);
            i++;
        }
        return texto;
    }

    /**
     * Envía un texto al elemento indicado, si este existe en el contexto actual.
     *
     * @param driver  Driver que está manipulando el navegador
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda
     * @param Texto   Texto a envíar al elemento indicado
     * @param banderaAssert Bandera para controlar si se quiere controlar el Assert.fail
     */
    public Boolean sendKeystoElement(WebDriver driver,SearchContext searchContext, String element, String Texto, boolean banderaAssert) {
        try {
            if (sendKeystoElementx2intents(driver, searchContext,element, Texto)) {
                WebElement elemento=obtenerWebElementx2(driver,searchContext,element);
                getImageScreeenshotWebElement(driver,elemento);
                return true;
            } else {
                LogsJB.info("No pudo encontrar el elemento: " + element + " por lo que no se envío el Texto y no se tomo la captura");
            }
        } catch (Exception e) {
            LogsJB.error("Error inesperado al envíar el texto y tomar la captura del elemento: " + element);
            LogsJB.error("Error inesperado al envíar el texto y tomar la captura del elemento: " + element + " " + e.getMessage());
            LogsJB.error("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
                Assert.fail("Error inesperado al envíar el texto y tomar la captura del elemento: " + element);
            }
        }
        return false;
    }

    /**
     * Envía un texto al elemento indicado, si este existe en el contexto actual.
     *
     * @param driver  Driver que está manipulando el navegador
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda
     * @param Texto   Texto a envíar al elemento indicado
     */
    public Boolean sendKeystoElement(WebDriver driver, SearchContext searchContext, String element, boolean banderaAssert, CharSequence... Texto) {
        try {
            if (sendKeystoElementx2intents(driver, searchContext,element, Texto)) {
                return true;
            } else {
                LogsJB.info("No pudo encontrar el elemento: " + element + " por lo que no se envío el Texto");
            }
        } catch (Exception e) {
            LogsJB.error("Error inesperado al envíar el texto y tomar la captura del elemento: " + element);
            LogsJB.error("Error inesperado al envíar el texto y tomar la captura del elemento: " + element + " " + e.getMessage());
            LogsJB.error("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            if(banderaAssert){
                Assert.fail("Error inesperado al envíar el texto y tomar la captura del elemento: " + element + " " + e.getMessage());

            }
        }
        return false;
    }


}
