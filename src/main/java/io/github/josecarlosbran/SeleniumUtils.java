package io.github.josecarlosbran;

import com.josebran.LogsJB.LogsJB;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
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
    private static Integer searchTime = 2500;
    @Getter
    @Setter
    private static Integer searchRepetitionTime = 100;

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
            Assert.fail("Error inesperado al presionar una tecla: ");
        }
    }

    /***
     * Presiona la tecla indicada en el condigo numerico indicado
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
            Assert.fail("Error inesperado al presionar una tecla: ");
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
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofMillis(timeduration))
                .pollingEvery(Duration.ofMillis(timerepetition))
                .ignoring(UnhandledAlertException.class)
                .ignoring(java.lang.Exception.class)
                .ignoring(TimeoutException.class)
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .ignoring(InvalidElementStateException.class)
                .ignoring(JavascriptException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(org.openqa.selenium.remote.UnreachableBrowserException.class)
                .ignoring(org.openqa.selenium.InvalidSelectorException.class)
                .ignoring(org.openqa.selenium.WebDriverException.class)
                .ignoring(ElementClickInterceptedException.class);
        return wait;
    }

    /***
     * Verifica si el elemento en cuestión esta habilitado
     * @param element Elemento que se desea verificar si esta habilitado
     * @return Retorna True si el elemento esta habilitado, False si no esta habilitado o visible.
     */
    public static boolean elementIsDisabled(WebElement element) {
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
     * @param object  El Array de objetos que se desea convertit en cadenas
     * @param acierto Variable booleana que decide si será un acierto(True) o un fallo (False)
     * @return Un ArrayList que contiene las representaciones en forma de cadena de los objetos
     */
    public static ArrayList<String> convertObjectToArrayString(Object[] object, Boolean acierto) {
        if (acierto) {
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
        } else {
            LogsJB.fatal("error al parsear el Objeto object a strings");
        }
        return null;
    }

    /***
     * Hace una pausa sobre el hilo en ejecución por el tiempo especificado
     * @param milisegundos Tiempo en milisegundos que se detendra la ejecucion
     */
    public static void threadslepp(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (Exception e) {
            LogsJB.fatal("Se ha capturado un error en threadsleep");
            LogsJB.fatal("Stacktrace de la excepcion capturada: " + ExceptionUtils.getStackTrace(e));
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
     * Función que se utiliza para posicionarsee en un elemento, utilizando un controlador WebDriver
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
     * @return Retorna True si el elemento Existe
     */
    public static Boolean elementExist(WebDriver driver, SearchContext searchContext, String element) {
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
     * @return Retorna True si logra limpiar el elemento
     */
    public static Boolean clearElementIfExist(WebDriver driver, SearchContext searchContext, String element) {
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
     * Función que se utiliza para verificar si un valor es invalido o no cumple ciertos criterios prefefinidos
     *
     * @param value Valor que se desea verificar para determianr si es inválido
     * @return Verdadero si el valor es considerado, o Falso si el valor cumple con los citerios validos
     */
    public static boolean isanvalidValue(String value) {
        if (!SeleniumUtils.stringIsNullOrEmpty(value)) {
            return !value.equalsIgnoreCase(SeleniumUtils.getInespecific());
        }
        return false;
    }
    /**
     * Función para pasar a la pestáña anterior
     *
     * @param driver WebDriver representa el controlador e interactua con el navegador
     */

    /****
     * Verifica si una cadena está vacía o es nula
     * @param cadena Cadena a Validar
     * @return Retorna True si la cadena envíada está vacía o nula, de lo contrario retorna false
     */
    public static boolean stringIsNullOrEmpty(String cadena) {
        return Objects.isNull(cadena) || cadena.trim().isEmpty();
    }

    /***
     * Mueve el navegador a la tab que esta recibiendo como parametro
     * @param driver Driver que esta manipulando el navegador
     * @param previousTab Previous Tab al que nos queremos mover
     */
    public void movetoPreviousTab(WebDriver driver, String previousTab) {
        if (SeleniumUtils.isanvalidValue(previousTab)) {
            //Loop through until we find a new window handle
            for (String windowHandle : driver.getWindowHandles()) {
                writeLog("Previus Tab: " + previousTab);
                writeLog("Windows Handle: " + windowHandle);
                if (previousTab.contentEquals(windowHandle)) {
                    writeLog("Se movera a la primera Tab");
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
        }
    }

    /**
     * Función para guardar la captura de pantalla de la página web y la guarda en un archivo
     *
     * @param driver WebDriver que representa la sesión del navegador
     * @return Retorna a un objeto File que representa la captura de pantalla de la pagina web
     */
    private File getImageScrennshot(WebDriver driver, WebElement elementScreenshot) {
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
            if (!stringIsNullOrEmpty(zoomActual)) {
                try {
                    double zoomActualValue = Double.parseDouble(zoomActual.replace("%", ""));
                    double newZoomValue = (zoomActualValue < 100.0) ? (zoomActualValue - 15.0) : 100.0;
                    js.executeScript("document.body.style.zoom = '" + newZoomValue + "%'");
                } catch (NumberFormatException e) {
                    System.out.println("El valor de zoomActual no es numérico: " + zoomActual);
                }
            }
            File scrFile = getImageScrennshotIE(driver, elementScreenshot);
            if (Objects.isNull(scrFile)) {
                TakesScreenshot scrShot = ((TakesScreenshot) driver);
                // Restáurar el zoom a 100% si fue ajustado
                js.executeScript("document.body.style.zoom = '100%';");
                return scrShot.getScreenshotAs(OutputType.FILE);
            } else {
                // Restáurar el zoom a 100% si fue ajustado
                js.executeScript("document.body.style.zoom = '100%';");
                return scrFile;
            }
        } else {
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            return scrShot.getScreenshotAs(OutputType.FILE);
        }
    }

    /**
     * Función para la captura de pantalla actual utilizando Internet Explorer y la guarda un archivo
     *
     * @param driver WebDriver representa la sesión de internet Explorer
     * @return Retorna a un objeto File que representa la captura de pantalla de la pagina web
     */
    private File getImageScrennshotIE(WebDriver driver, WebElement elementScreenshot) {
        try {
            if (!Objects.isNull(elementScreenshot)) {
                String tempelement = RefreshReferenceToElement(driver, elementScreenshot).toString().split(" -> ")[1];
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
            writeLog("No pudo tomar la captura de IE retorna null");
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
     * @param elemento Elemento a refrescar
     * @return null si no logra refrescar el elemento, caso contrario la referencia al elemento
     */
    private WebElement RefreshReferenceToElement(WebDriver driver, WebElement elemento) {
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
            writeLog("No fue posible refrescar la referencia al elemento");
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
     * Obtiene el identificador de un elemento By para realizar la busqueda por medio
     * de las funciones del middleware
     *
     * @param by identificador del cual obtendrá el atributo del elemento a buscar
     * @return atributo por medio del cual se filtrara el elemento a buscar
     */
    private String getIdentificadorBy(By by) {
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
     */
    public void waitImplicity(WebDriver driver, By by) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(driver1 -> {
                return !elementIsDisabled(driver1.findElement(by));
            });
        } catch (TimeoutException ignored) {
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al esperar la aparicion del elemento: " + by);
            LogsJB.fatal(convertir_fecha() + "*");
            LogsJB.fatal(convertir_fecha() + " " + e);
            LogsJB.fatal(convertir_fecha() + " Tipo de Excepción : " + e.getClass());
            LogsJB.fatal(convertir_fecha() + " Causa de la Excepción : " + e.getCause());
            LogsJB.fatal(convertir_fecha() + " Mensaje de la Excepción : " + e.getMessage());
            LogsJB.fatal(convertir_fecha() + "*");
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al esperar la aparicion del elemento: " + by);
        }
    }

    /**
     * Espera implícita de 5 segundos o menos si el elemento desaparece del DOM, luego de los 5 segundos lanzara excepción
     *
     * @param driver Variable que manipula el navegador
     * @param by     Identificador del tipo By
     */
    public void waitImplicityForElementNotExist(WebDriver driver, By by) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (TimeoutException ignored) {
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al esperar la aparicion del elemento: " + by);
            LogsJB.fatal(convertir_fecha() + "*");
            LogsJB.fatal(convertir_fecha() + " " + e);
            LogsJB.fatal(convertir_fecha() + " Tipo de Excepción : " + e.getClass());
            LogsJB.fatal(convertir_fecha() + " Causa de la Excepción : " + e.getCause());
            LogsJB.fatal(convertir_fecha() + " Mensaje de la Excepción : " + e.getMessage());
            LogsJB.fatal(convertir_fecha() + "*");
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al esperar la aparicion del elemento: " + by);
        }
    }

    /**
     * Espera implícita de 30 segundos, luego de los 30 segundos lanzara excepción
     *
     * @param wait Wait espera predeterminada
     * @param by   Identificador del tipo By
     */
    public void waitImplicity(WebDriverWait wait, By by) {
        try {
            wait.until(driver -> {
                return driver.findElement(by).isDisplayed() || driver.findElement(by).isEnabled();
            });
        } catch (TimeoutException ignored) {
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al esperar la aparicion del elemento: " + by);
            LogsJB.fatal(convertir_fecha() + "*");
            LogsJB.fatal(convertir_fecha() + " " + e);
            LogsJB.fatal(convertir_fecha() + " Tipo de Excepción : " + e.getClass());
            LogsJB.fatal(convertir_fecha() + " Causa de la Excepción : " + e.getCause());
            LogsJB.fatal(convertir_fecha() + " Mensaje de la Excepción : " + e.getMessage());
            LogsJB.fatal(convertir_fecha() + "*");
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al esperar la aparicion del elemento: " + by);
        }
    }

    /***
     * Permite Aceptar las Alertas emergentes por medio de la definición estándar de W3C de los navegadores.
     * @param driver Web Driver que manipula el navegador
     * @param aceptar Variable booleana que acepta o declina el cuadro de dialogo
     * Debido a la naturaleza del manejo interno de accept por parte de javascript, la funcion DEBE DE LLAMARSE JUSTO ANTES DE DAR CLICK PARA DISPARAR EL CUADRO DE DIALOGO
     */
    public void acceptConfirm(WebDriver driver, boolean aceptar) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.confirm=function(){return " + aceptar + "}");
        } catch (WebDriverException e) {
            LogsJB.fatal("Error WebDriver al interactuar con la alerta: " + e.getMessage());
            // Puedes agregar más manejo de excepciones específicas según sea necesario
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al esperar la aparición del elemento: " + e.getMessage());
        }
    }

    /***
     * Permite Aceptar las Alertas emergentes por medio de la definición estándar de W3C de los navegadores.
     * @param driver Web Driver que manipula el navegador
     */
    public void acceptAlert(WebDriver driver) {
        //WebElement currentFrame = driver.switchTo().activeElement();
        // Obtener el WebElement del frame actual usando JavaScript
        /*WebElement currentFrame = (WebElement) ((JavascriptExecutor) driver).executeScript(
                "return window.frameElement;"
        );*/
        try {
            //Wait for the alert to be displayed and store it in a variable
            Wait wait = getFluentWait(driver, 5000, 100);
            Alert alert = (Alert) wait.until(ExpectedConditions.alertIsPresent());
            //Alert alert = driver.switchTo().alert();
            String text = alert.getText();
//         String text=driver.switchTo().alert().getText();
//         writeLog(text);
//            driver.switchTo().alert().accept();
            writeLog(text);
            alert.accept();
        } catch (java.util.NoSuchElementException e) {
            // Manejar la falta de alerta específica si es necesario
            LogsJB.fatal("No se encontró ninguna alerta presente.");
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("window.alert = function() {};");
        } catch (WebDriverException e) {
            LogsJB.fatal("Error WebDriver al interactuar con la alerta: " + e.getMessage());
            // Puedes agregar más manejo de excepciones específicas según sea necesario
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al esperar la aparición del elemento: " + e.getMessage());
        }
    }

    /***
     * Verifica si un elemento existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda
     * @return Retorna True si el elemento Existe
     */
    public Boolean elementExist(SearchContext driver, String element) {
        //Para optimizar el tiempo de respuestá
        writeLog(convertir_fecha() + "* ");
        writeLog(convertir_fecha() + " Buscara si existe el elemento indicado: " + element);
        writeLog(convertir_fecha() + "* ");
        int time = 3500;
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        if (this.testContext.getNavegador().equalsIgnoreCase("IE")) {
            time = 10000;
            addseconds.add(Calendar.MILLISECOND, time);
        } else {
            if (StringUtils.containsIgnoreCase(testContext.getCanal(), "Banca") || StringUtils.containsIgnoreCase(testContext.getCanal(), "App")) {
                time = 3000;
                addseconds.add(Calendar.MILLISECOND, time);
            } else {
                time = 500;
                addseconds.add(Calendar.MILLISECOND, time);
            }
        }
        fecha = addseconds.getTime();
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " + time + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = getFluentWait(testContext.getDriver(), time, 100);
        //Declaración de Variables auxiliares para que no se bloqueen los hilos
        String elementid = element;
        String elementclasname = element;
        String elementcss = element;
        String elementtagname = element;
        String elementlinkt = element;
        String elementpartial = element;
        String elementxpath = element;
        String elementname = element;
        Wait<WebDriver> waitid = wait;
        Wait<WebDriver> waitclassname = wait;
        Wait<WebDriver> waitcss = wait;
        Wait<WebDriver> waittagname = wait;
        Wait<WebDriver> waitlinkt = wait;
        Wait<WebDriver> waitpartial = wait;
        Wait<WebDriver> waitxpath = wait;
        Wait<WebDriver> waitname = wait;
        //Declaración de Hilos
        searchId buscarId = new searchId(this.testContext, waitid, elementid);
        searchClassName buscarClassName = new searchClassName(this.testContext, waitclassname, elementclasname);
        searchCSSSelector buscarCSSSelector = new searchCSSSelector(this.testContext, waitcss, elementcss);
        searchTagName buscarTagName = new searchTagName(this.testContext, waittagname, elementtagname);
        searchLinkText buscarLinkText = new searchLinkText(this.testContext, waitlinkt, elementlinkt);
        searchPartialLinkText buscarPartialLinkText = new searchPartialLinkText(this.testContext, waitpartial, elementpartial);
        searchXPath buscarXpath = new searchXPath(this.testContext, waitxpath, elementxpath);
        searchName buscarName = new searchName(this.testContext, waitname, elementname);
        //Declaración de Variables de control
        Boolean existclassname = false;
        Boolean existcssselector = false;
        Boolean existtagname = false;
        Boolean existlinktext = false;
        Boolean existpartiallinktext = false;
        Boolean existxpath = false;
        Boolean existid = false;
        Boolean existname = false;
        //Ajusta los procesos para que estos no limpien el elemento indicado
        buscarId.setClearElement(true);
        buscarId.setSendKeys(true);
        buscarId.setObtenerText(true);
        buscarId.setClick(true);
        buscarXpath.setClearElement(true);
        buscarXpath.setSendKeys(true);
        buscarXpath.setObtenerText(true);
        buscarXpath.setClick(true);
        buscarClassName.setClearElement(true);
        buscarClassName.setSendKeys(true);
        buscarClassName.setObtenerText(true);
        buscarClassName.setClick(true);
        buscarCSSSelector.setClearElement(true);
        buscarCSSSelector.setSendKeys(true);
        buscarCSSSelector.setObtenerText(true);
        buscarCSSSelector.setClick(true);
        buscarLinkText.setClearElement(true);
        buscarLinkText.setSendKeys(true);
        buscarLinkText.setObtenerText(true);
        buscarLinkText.setClick(true);
        buscarPartialLinkText.setClearElement(true);
        buscarPartialLinkText.setSendKeys(true);
        buscarPartialLinkText.setObtenerText(true);
        buscarPartialLinkText.setClick(true);
        buscarName.setClearElement(true);
        buscarName.setSendKeys(true);
        buscarName.setObtenerText(true);
        buscarName.setClick(true);
        buscarId.setSearchContext(driver);
        buscarXpath.setSearchContext(driver);
        buscarClassName.setSearchContext(driver);
        buscarCSSSelector.setSearchContext(driver);
        buscarLinkText.setSearchContext(driver);
        buscarPartialLinkText.setSearchContext(driver);
        buscarName.setSearchContext(driver);
        buscarTagName.setSearchContext(driver);
        //Comienza a correr los procesos paralelos
        //Comienzan los subprocesos a funcionar
        buscarId.execute();
        buscarXpath.execute();
        buscarClassName.execute();
        buscarCSSSelector.execute();
        buscarTagName.execute();
        buscarLinkText.execute();
        buscarPartialLinkText.execute();
        buscarName.execute();
        //Esperará saber si existe el elemento en alguno de los tipos
        while ((!existid) && (!existname) && (!existclassname) && (!existcssselector) && (!existtagname) && (!existlinktext) && (!existpartiallinktext) && (!existxpath) && !(fecha2.after(fecha))) {
            fecha2 = Calendar.getInstance().getTime();
            //Actualiza la existencia
            existid = buscarId.getElementExist();
            existclassname = buscarClassName.getElementExist();
            existxpath = buscarXpath.getElementExist();
            existcssselector = buscarCSSSelector.getElementExist();
            existtagname = buscarTagName.getElementExist();
            existlinktext = buscarLinkText.getElementExist();
            existpartiallinktext = buscarPartialLinkText.getElementExist();
            existname = buscarName.getElementExist();
        }
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " +
                time +
                " mili segundos: " + fecha);
        writeLog(convertir_fecha() + " Fecha contra la que se comparo si transcurrieron los " +
                time +
                " mili segundos: " + fecha2);
        if (fecha2.after(fecha)) {
            writeLog(convertir_fecha() + " No Existe el elemento especificado: " + element);
        } else {
            writeLog(convertir_fecha() + " Logro encontrar el elemento especificado: " + element);
            return true;
        }
        //Retorna Falso si el elemento no Existe
        return false;
    }

    /***
     * Limpia el elemento especificado, si existe en el contexto actual.
     * @param driver Driver que está manipulando el navegador
     * @param element Atributo del elemento, por medio del cual se realizara la busquedad
     * @return Retorna True si logra limpiar el elemento
     */
    public Boolean clearElementIfExist(SearchContext driver, String element) {
        //Para optimizar el tiempo de respuestá
        writeLog(convertir_fecha() + "* ");
        writeLog(convertir_fecha() + " Buscara si existe el elemento indicado: " + element);
        writeLog(convertir_fecha() + "* ");
        int time = 3500;
        //Obtiene la espera fluida
        //Crea las variables de control que no permite que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        if (this.testContext.getNavegador().equalsIgnoreCase("IE")) {
            time = 10000;
            addseconds.add(Calendar.MILLISECOND, time);
        } else {
            if (StringUtils.containsIgnoreCase(testContext.getCanal(), "Banca") || StringUtils.containsIgnoreCase(testContext.getCanal(), "App")) {
                time = 3000;
                addseconds.add(Calendar.MILLISECOND, time);
            } else {
                time = 500;
                addseconds.add(Calendar.MILLISECOND, time);
            }
        }
        fecha = addseconds.getTime();
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " + time + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = getFluentWait(testContext.getDriver(), time, 100);
        //Declaración de Variables auxiliares para que no se bloqueen los hilos
        String elementid = element;
        String elementclasname = element;
        String elementcss = element;
        String elementtagname = element;
        String elementlinkt = element;
        String elementpartial = element;
        String elementxpath = element;
        String elementname = element;
        Wait<WebDriver> waitname = wait;
        Wait<WebDriver> waitid = wait;
        Wait<WebDriver> waitclassname = wait;
        Wait<WebDriver> waitcss = wait;
        Wait<WebDriver> waittagname = wait;
        Wait<WebDriver> waitlinkt = wait;
        Wait<WebDriver> waitpartial = wait;
        Wait<WebDriver> waitxpath = wait;
        //Declaración de Hilos
        searchId buscarId = new searchId(this.testContext, waitid, elementid);
        searchClassName buscarClassName = new searchClassName(this.testContext, waitclassname, elementclasname);
        searchCSSSelector buscarCSSSelector = new searchCSSSelector(this.testContext, waitcss, elementcss);
        searchTagName buscarTagName = new searchTagName(this.testContext, waittagname, elementtagname);
        searchLinkText buscarLinkText = new searchLinkText(this.testContext, waitlinkt, elementlinkt);
        searchPartialLinkText buscarPartialLinkText = new searchPartialLinkText(this.testContext, waitpartial, elementpartial);
        searchXPath buscarXpath = new searchXPath(this.testContext, waitxpath, elementxpath);
        searchName buscarName = new searchName(this.testContext, waitname, elementname);
        //Ajusta los procesos para que estos no limpien el elemento indicado
        buscarId.setSendKeys(true);
        buscarId.setObtenerText(true);
        buscarXpath.setSendKeys(true);
        buscarXpath.setObtenerText(true);
        buscarClassName.setSendKeys(true);
        buscarClassName.setObtenerText(true);
        buscarCSSSelector.setSendKeys(true);
        buscarCSSSelector.setObtenerText(true);
        buscarLinkText.setSendKeys(true);
        buscarLinkText.setObtenerText(true);
        buscarPartialLinkText.setSendKeys(true);
        buscarPartialLinkText.setObtenerText(true);
        buscarName.setSendKeys(true);
        buscarName.setObtenerText(true);
        //Declaración de Variables de control
        Boolean existclassname = false;
        Boolean existcssselector = false;
        Boolean existtagname = false;
        Boolean existlinktext = false;
        Boolean existpartiallinktext = false;
        Boolean existxpath = false;
        Boolean existid = false;
        Boolean existname = false;
        buscarId.setSearchContext(driver);
        buscarXpath.setSearchContext(driver);
        buscarClassName.setSearchContext(driver);
        buscarCSSSelector.setSearchContext(driver);
        buscarLinkText.setSearchContext(driver);
        buscarPartialLinkText.setSearchContext(driver);
        buscarName.setSearchContext(driver);
        buscarTagName.setSearchContext(driver);
        //Comienza a correr los procesos paralelos
        if (this.testContext.getNavegador().equalsIgnoreCase("IE")) {
            buscarCSSSelector.execute();
            buscarTagName.execute();
            buscarXpath.execute();
            buscarId.execute();
            buscarLinkText.execute();
            buscarName.execute();
        } else {
            buscarCSSSelector.execute();
            buscarTagName.execute();
            buscarXpath.execute();
            buscarId.execute();
            buscarName.execute();
            buscarClassName.execute();
            buscarLinkText.execute();
            buscarPartialLinkText.execute();
        }
        //Esperará saber si existe el elemento en alguno de los tipos
        while ((!existid) && (!existname) && (!existclassname) && (!existcssselector) && (!existtagname) && (!existlinktext) && (!existpartiallinktext) && (!existxpath) && !(fecha2.after(fecha))) {
            fecha2 = Calendar.getInstance().getTime();
            //Actualiza la existencia
            existid = buscarId.getElementExist();
            if (existid) {
                //Mata los subprocesos a funcionar
                while (!buscarId.isClearElement()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y limpiar el elemento especificado: " + element);
                return true;
            }
            existclassname = buscarClassName.getElementExist();
            if (existclassname) {
                //Mata los subprocesos a funcionar
                while (!buscarClassName.isClearElement()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y limpiar el elemento especificado: " + element);
                return true;
            }
            existxpath = buscarXpath.getElementExist();
            if (existxpath) {
                //Mata los subprocesos a funcionar
                while (!buscarXpath.isClearElement()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y limpiar el elemento especificado: " + element);
                return true;
            }
            existcssselector = buscarCSSSelector.getElementExist();
            if (existcssselector) {
                //Mata los subprocesos a funcionar
                while (!buscarCSSSelector.isClearElement()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y limpiar el elemento especificado: " + element);
                return true;
            }
            existtagname = buscarTagName.getElementExist();
            if (existtagname) {
                //Mata los subprocesos a funcionar
                while (!buscarTagName.isClearElement()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y limpiar el elemento especificado: " + element);
                return true;
            }
            existlinktext = buscarLinkText.getElementExist();
            if (existlinktext) {
                //Mata los subprocesos a funcionar
                while (!buscarLinkText.isClearElement()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y limpiar el elemento especificado: " + element);
                return true;
            }
            existpartiallinktext = buscarPartialLinkText.getElementExist();
            if (existpartiallinktext) {
                //Mata los subprocesos a funcionar
                while (!buscarPartialLinkText.isClearElement()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y limpiar el elemento especificado: " + element);
                return true;
            }
            existname = buscarName.getElementExist();
            if (existname) {
                //Mata los subprocesos a funcionar
                while (!buscarName.isClearElement()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y limpiar el elemento especificado: " + element);
                return true;
            }
        }
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " +
                time +
                " mili segundos: " + fecha);
        writeLog(convertir_fecha() + " Fecha contra la que se comparo si transcurrieron los " +
                time +
                " mili segundos: " + fecha2);
        if ((fecha2.after(fecha))) {
            writeLog(convertir_fecha() + " No pudo limpiar el elemento especificado, ya que no existe: " + element);
        } else {
            writeLog(convertir_fecha() + " Logro encontrar y limpiar el elemento especificado: " + element);
            return true;
        }
        //Retorna Falso si el elemento no Existe
        return false;
    }

    /**
     * Envía un texto al elemento indicado, si este existe en el contexto actual.
     *
     * @param driver  Driver que está manipulando el navegador
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda
     * @param Texto   Texto a envíar al elemento indicado
     * @return Retorna True si encontro el elemento y pudo setear el texto.
     */
    private Boolean sendKeysIfElementExist(SearchContext driver, String element, CharSequence... Texto) {
        //Para optimizar el tiempo de respuestá
        writeLog(convertir_fecha() + "* ");
        writeLog(convertir_fecha() + " Buscara si existe el elemento indicado: " + element);
        writeLog(convertir_fecha() + "* ");
        int time = 3500;
        //Obtiene la espera fluida
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        if (this.testContext.getNavegador().equalsIgnoreCase("IE")) {
            time = 10000;
            addseconds.add(Calendar.MILLISECOND, time);
        } else {
            if (StringUtils.containsIgnoreCase(testContext.getCanal(), "Banca") || StringUtils.containsIgnoreCase(testContext.getCanal(), "App")) {
                time = 3000;
                addseconds.add(Calendar.MILLISECOND, time);
            } else {
                time = 500;
                addseconds.add(Calendar.MILLISECOND, time);
            }
        }
        fecha = addseconds.getTime();
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " + time + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = getFluentWait(testContext.getDriver(), time, 100);
        //Declaración de Variables auxiliares para que no se bloqueen los hilos
        String elementid = element;
        String elementclasname = element;
        String elementcss = element;
        String elementtagname = element;
        String elementlinkt = element;
        String elementpartial = element;
        String elementxpath = element;
        String elementname = element;
        Wait<WebDriver> waitname = wait;
        Wait<WebDriver> waitid = wait;
        Wait<WebDriver> waitclassname = wait;
        Wait<WebDriver> waitcss = wait;
        Wait<WebDriver> waittagname = wait;
        Wait<WebDriver> waitlinkt = wait;
        Wait<WebDriver> waitpartial = wait;
        Wait<WebDriver> waitxpath = wait;
        CharSequence[] textoid = Texto;
        CharSequence[] textoname = Texto;
        CharSequence[] textoclassname = Texto;
        CharSequence[] textocss = Texto;
        CharSequence[] textotagname = Texto;
        CharSequence[] textolinkt = Texto;
        CharSequence[] textopartial = Texto;
        //Declaración de Hilos
        searchId buscarId = new searchId(this.testContext, waitid, elementid, textoid);
        searchClassName buscarClassName = new searchClassName(this.testContext, waitclassname, elementclasname, textoclassname);
        searchCSSSelector buscarCSSSelector = new searchCSSSelector(this.testContext, waitcss, elementcss, textocss);
        searchTagName buscarTagName = new searchTagName(this.testContext, waittagname, elementtagname, textotagname);
        searchLinkText buscarLinkText = new searchLinkText(this.testContext, waitlinkt, elementlinkt, textolinkt);
        searchPartialLinkText buscarPartialLinkText = new searchPartialLinkText(this.testContext, waitpartial, elementpartial, textopartial);
        searchXPath buscarXpath = new searchXPath(this.testContext, waitxpath, elementxpath, textopartial);
        searchName buscarName = new searchName(this.testContext, waitname, elementname, textoname);
        //Declaración de Variables de control
        Boolean existclassname = false;
        Boolean existcssselector = false;
        Boolean existtagname = false;
        Boolean existlinktext = false;
        Boolean existpartiallinktext = false;
        Boolean existxpath = false;
        Boolean existid = false;
        Boolean existname = false;
        //Ajusta los procesos para que estos no obtenga el texto del elemento
        buscarId.setObtenerText(true);
        buscarXpath.setObtenerText(true);
        buscarClassName.setObtenerText(true);
        buscarCSSSelector.setObtenerText(true);
        buscarLinkText.setObtenerText(true);
        buscarPartialLinkText.setObtenerText(true);
        buscarName.setObtenerText(true);
        buscarTagName.setObtenerText(true);
        buscarId.setClick(true);
        buscarXpath.setClick(true);
        buscarClassName.setClick(true);
        buscarCSSSelector.setClick(true);
        buscarLinkText.setClick(true);
        buscarPartialLinkText.setClick(true);
        buscarName.setClick(true);
        buscarTagName.setClick(true);
        buscarId.setSearchContext(driver);
        buscarXpath.setSearchContext(driver);
        buscarClassName.setSearchContext(driver);
        buscarCSSSelector.setSearchContext(driver);
        buscarLinkText.setSearchContext(driver);
        buscarPartialLinkText.setSearchContext(driver);
        buscarName.setSearchContext(driver);
        buscarTagName.setSearchContext(driver);
        //Comienza a correr los procesos paralelos
        if (this.testContext.getNavegador().equalsIgnoreCase("IE")) {
            buscarCSSSelector.execute();
            buscarTagName.execute();
            buscarXpath.execute();
            buscarId.execute();
            buscarLinkText.execute();
            buscarName.execute();
        } else {
            buscarCSSSelector.execute();
            buscarTagName.execute();
            buscarXpath.execute();
            buscarId.execute();
            buscarName.execute();
            buscarClassName.execute();
            buscarLinkText.execute();
            buscarPartialLinkText.execute();
        }
        //Esperará saber si existe el elemento en alguno de los tipos
        while ((!existid) && (!existname) && (!existclassname) && (!existcssselector) && (!existtagname) && (!existlinktext) && (!existpartiallinktext) && (!existxpath) && !(fecha2.after(fecha))) {
            fecha2 = Calendar.getInstance().getTime();
            //Actualiza la existencia
            existid = buscarId.getElementExist();
            if (buscarId.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarId.isSendKeys()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y setear el Texto: " + Texto.toString() + " en el elemento especificado: " + element);
                return true;
            }
            existclassname = buscarClassName.getElementExist();
            if (buscarClassName.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarClassName.isSendKeys()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y setear el Texto: " + Texto.toString() + " en el elemento especificado: " + element);
                return true;
            }
            existxpath = buscarXpath.getElementExist();
            if (buscarXpath.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarXpath.isSendKeys()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y setear el Texto: " + Texto.toString() + " en el elemento especificado: " + element);
                return true;
            }
            existcssselector = buscarCSSSelector.getElementExist();
            if (buscarCSSSelector.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarCSSSelector.isSendKeys()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y setear el Texto: " + Texto.toString() + " en el elemento especificado: " + element);
                return true;
            }
            existtagname = buscarTagName.getElementExist();
            if (buscarTagName.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarTagName.isSendKeys()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y setear el Texto: " + Texto.toString() + " en el elemento especificado: " + element);
                return true;
            }
            existlinktext = buscarLinkText.getElementExist();
            if (buscarLinkText.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarLinkText.isSendKeys()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y setear el Texto: " + Texto.toString() + " en el elemento especificado: " + element);
                return true;
            }
            existpartiallinktext = buscarPartialLinkText.getElementExist();
            if (buscarPartialLinkText.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarPartialLinkText.isSendKeys()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y setear el Texto: " + Texto.toString() + " en el elemento especificado: " + element);
                return true;
            }
            existname = buscarName.getElementExist();
            if (buscarName.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarName.isSendKeys()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y setear el Texto: " + Texto.toString() + " en el elemento especificado: " + element);
                return true;
            }
        }
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " +
                time +
                " mili segundos: " + fecha);
        writeLog(convertir_fecha() + " Fecha contra la que se comparo si transcurrieron los " +
                time +
                " mili segundos: " + fecha2);
        if ((fecha2.after(fecha))) {
            writeLog(convertir_fecha() + " No logro encontrar y setear el Texto: " + Texto.toString() +
                    " en el elemento especificado: " + element);
        } else {
            writeLog(convertir_fecha() + " Logro encontrar y setear el Texto: " + Texto.toString() +
                    " en el elemento especificado: " + element);
            return true;
        }
        //Retorna Falso si el elemento no Existe
        return false;
    }

    /***
     * Obtiene el texto del elemento indicado, si este existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param element Atributo del elemento, por medio del cual se realizara la busquedad
     * @return Retorna el texto del elemento, si lo logra encontrar, de lo contrario retorna null
     */
    public String getTextIfElementExist(SearchContext driver, String element) {
        //Para optimizar el tiempo de respuestá
        writeLog(convertir_fecha() + "* ");
        writeLog(convertir_fecha() + " Buscara si existe el elemento indicado para obtener el texto: " + element);
        writeLog(convertir_fecha() + "* ");
        //Declaración de Variable a Retornar
        String texto = null;
        int time = 3500;
        //Obtiene la espera fluida
        //Crea las variables de control que no permitiran que sobre pase los 7,000 mili segundos la busquedad del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        if (this.testContext.getNavegador().equalsIgnoreCase("IE")) {
            time = 10000;
            addseconds.add(Calendar.MILLISECOND, time);
        } else {
            if (StringUtils.containsIgnoreCase(testContext.getCanal(), "Banca") || StringUtils.containsIgnoreCase(testContext.getCanal(), "App")) {
                time = 3000;
                addseconds.add(Calendar.MILLISECOND, time);
            } else {
                time = 500;
                addseconds.add(Calendar.MILLISECOND, time);
            }
        }
        fecha = addseconds.getTime();
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " + time + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = getFluentWait(testContext.getDriver(), time, 100);
        //Declaracion de Variables auxiliares para que no se bloqueen los hilos
        String elementid = element;
        String elementclasname = element;
        String elementcss = element;
        String elementtagname = element;
        String elementlinkt = element;
        String elementpartial = element;
        String elementxpath = element;
        String elementname = element;
        Wait<WebDriver> waitname = wait;
        Wait<WebDriver> waitid = wait;
        Wait<WebDriver> waitclassname = wait;
        Wait<WebDriver> waitcss = wait;
        Wait<WebDriver> waittagname = wait;
        Wait<WebDriver> waitlinkt = wait;
        Wait<WebDriver> waitpartial = wait;
        Wait<WebDriver> waitxpath = wait;
        //Declaracion de Hilos
        searchId buscarId = new searchId(this.testContext, waitid, elementid);
        searchClassName buscarClassName = new searchClassName(this.testContext, waitclassname, elementclasname);
        searchCSSSelector buscarCSSSelector = new searchCSSSelector(this.testContext, waitcss, elementcss);
        searchTagName buscarTagName = new searchTagName(this.testContext, waittagname, elementtagname);
        searchLinkText buscarLinkText = new searchLinkText(this.testContext, waitlinkt, elementlinkt);
        searchPartialLinkText buscarPartialLinkText = new searchPartialLinkText(this.testContext, waitpartial, elementpartial);
        searchXPath buscarXpath = new searchXPath(this.testContext, waitxpath, elementxpath);
        searchName buscarName = new searchName(this.testContext, waitname, elementname);
        //Declaración de Variables de control
        Boolean existclassname = false;
        Boolean existcssselector = false;
        Boolean existtagname = false;
        Boolean existlinktext = false;
        Boolean existpartiallinktext = false;
        Boolean existxpath = false;
        Boolean existid = false;
        Boolean existname = false;
        //Ajusta los procesos para que estos no limpien el elemento indicado
        buscarId.setClearElement(true);
        buscarId.setSendKeys(true);
        buscarId.setClick(true);
        buscarXpath.setClearElement(true);
        buscarXpath.setSendKeys(true);
        buscarXpath.setClick(true);
        buscarClassName.setClearElement(true);
        buscarClassName.setSendKeys(true);
        buscarClassName.setClick(true);
        buscarCSSSelector.setClearElement(true);
        buscarCSSSelector.setSendKeys(true);
        buscarCSSSelector.setClick(true);
        buscarLinkText.setClearElement(true);
        buscarLinkText.setSendKeys(true);
        buscarLinkText.setClick(true);
        buscarPartialLinkText.setClearElement(true);
        buscarPartialLinkText.setSendKeys(true);
        buscarPartialLinkText.setClick(true);
        buscarName.setClearElement(true);
        buscarName.setSendKeys(true);
        buscarName.setClick(true);
        buscarId.setSearchContext(driver);
        buscarXpath.setSearchContext(driver);
        buscarClassName.setSearchContext(driver);
        buscarCSSSelector.setSearchContext(driver);
        buscarLinkText.setSearchContext(driver);
        buscarPartialLinkText.setSearchContext(driver);
        buscarName.setSearchContext(driver);
        buscarTagName.setSearchContext(driver);
        //Comienza a correr los procesos paralelos
        if (this.testContext.getNavegador().equalsIgnoreCase("IE")) {
            buscarCSSSelector.execute();
            buscarTagName.execute();
            buscarXpath.execute();
            buscarId.execute();
            buscarLinkText.execute();
            buscarName.execute();
        } else {
            buscarCSSSelector.execute();
            buscarTagName.execute();
            buscarXpath.execute();
            buscarId.execute();
            buscarName.execute();
            buscarClassName.execute();
            buscarLinkText.execute();
            buscarPartialLinkText.execute();
        }
        //Esperara saber si existe el elemento en alguno de los tipos
        while ((!existid) && (!existname) && (!existclassname) && (!existcssselector) && (!existtagname) && (!existlinktext) && (!existpartiallinktext) && (!existxpath) && !(fecha2.after(fecha))) {
            fecha2 = Calendar.getInstance().getTime();
            //Actualiza la existencia
            existid = buscarId.getElementExist();
            if (buscarId.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarId.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarId.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existclassname = buscarClassName.getElementExist();
            if (buscarClassName.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarClassName.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarClassName.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existxpath = buscarXpath.getElementExist();
            if (buscarXpath.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarXpath.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarXpath.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existcssselector = buscarCSSSelector.getElementExist();
            if (buscarCSSSelector.getElementExist()) {
                //Mata los subprocesos a funcionar
                writeLog(convertir_fecha() + " Encontro el elemento, esperara a que cambie el valor de obtener texto a true");
                while (!buscarCSSSelector.isObtenerText()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Cambio el valor a true, pasamos el while");
                texto = buscarCSSSelector.getTexto();
                writeLog(convertir_fecha() + " Texto Obtenido: " + texto);
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existtagname = buscarTagName.getElementExist();
            if (buscarTagName.getElementExist()) {
                //Mata los subprocesos a funcionar
                writeLog(convertir_fecha() + " Encontro el elemento, esperara a que cambie el valor de obtener texto a true");
                while (!buscarTagName.isObtenerText()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Cambio el valor a true, pasamos el while");
                texto = buscarTagName.getTexto();
                writeLog(convertir_fecha() + " Texto Obtenido: " + texto);
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existlinktext = buscarLinkText.getElementExist();
            if (buscarLinkText.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarLinkText.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarLinkText.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existpartiallinktext = buscarPartialLinkText.getElementExist();
            if (buscarPartialLinkText.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarPartialLinkText.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarPartialLinkText.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existname = buscarName.getElementExist();
            if (buscarName.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarName.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarName.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
        }
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " +
                time +
                " mili segundos: " + fecha);
        writeLog(convertir_fecha() + " Fecha contra la que se comparo si transcurrieron los " +
                time +
                " mili segundos: " + fecha2);
        if ((fecha2.after(fecha))) {
            writeLog(convertir_fecha() + " No pudo hacer obtener el texto del elemento especificado, ya que no existe: " + element);
        } else {
            writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
            return texto;
        }
        //Retorna Falso si el elemento no Existe
        return null;
    }

    /***
     * Obtiene el texto del elemento indicado, si este existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param element Atributo del elemento, por medio del cual se realizara la busqueda
     * @param timeduration Duración de la espera para la busqueda del elemento
     * @param timerepetition Cada cuanto tiempo durante el tiempo de espera, intentar obtener nuevamente el elemento
     * @return Retorna el texto del elemento, si lo logra encontrar, de lo contrario retorna null
     */
    public String getTextIfElementExist(SearchContext driver, String element, int timeduration, int timerepetition) {
        //Para optimizar el tiempo de respuestá
        writeLog(convertir_fecha() + "* ");
        writeLog(convertir_fecha() + " Buscara si existe el elemento indicado: " + element);
        writeLog(convertir_fecha() + "* ");
        //Declaración de Variable a Retornar
        String texto = null;
        int time = 3500;
        //Obtiene la espera fluida
        Wait<WebDriver> wait = getFluentWait(testContext.getDriver(), timeduration, timerepetition);
        //Declaración de Variables auxiliares para que no se bloqueen los hilos
        String elementid = element;
        String elementclasname = element;
        String elementcss = element;
        String elementtagname = element;
        String elementlinkt = element;
        String elementpartial = element;
        String elementxpath = element;
        String elementname = element;
        Wait<WebDriver> waitname = wait;
        Wait<WebDriver> waitid = wait;
        Wait<WebDriver> waitclassname = wait;
        Wait<WebDriver> waitcss = wait;
        Wait<WebDriver> waittagname = wait;
        Wait<WebDriver> waitlinkt = wait;
        Wait<WebDriver> waitpartial = wait;
        Wait<WebDriver> waitxpath = wait;
        //Declaración de Hilos
        searchId buscarId = new searchId(this.testContext, waitid, elementid);
        searchClassName buscarClassName = new searchClassName(this.testContext, waitclassname, elementclasname);
        searchCSSSelector buscarCSSSelector = new searchCSSSelector(this.testContext, waitcss, elementcss);
        searchTagName buscarTagName = new searchTagName(this.testContext, waittagname, elementtagname);
        searchLinkText buscarLinkText = new searchLinkText(this.testContext, waitlinkt, elementlinkt);
        searchPartialLinkText buscarPartialLinkText = new searchPartialLinkText(this.testContext, waitpartial, elementpartial);
        searchXPath buscarXpath = new searchXPath(this.testContext, waitxpath, elementxpath);
        searchName buscarName = new searchName(this.testContext, waitname, elementname);
        //Declaración de Variables de control
        Boolean existclassname = false;
        Boolean existcssselector = false;
        Boolean existtagname = false;
        Boolean existlinktext = false;
        Boolean existpartiallinktext = false;
        Boolean existxpath = false;
        Boolean existid = false;
        Boolean existname = false;
        //Ajusta los procesos para que estos no limpien el elemento indicado
        buscarId.setClearElement(true);
        buscarId.setSendKeys(true);
        buscarId.setClick(true);
        buscarXpath.setClearElement(true);
        buscarXpath.setSendKeys(true);
        buscarXpath.setClick(true);
        buscarClassName.setClearElement(true);
        buscarClassName.setSendKeys(true);
        buscarClassName.setClick(true);
        buscarCSSSelector.setClearElement(true);
        buscarCSSSelector.setSendKeys(true);
        buscarCSSSelector.setClick(true);
        buscarLinkText.setClearElement(true);
        buscarLinkText.setSendKeys(true);
        buscarLinkText.setClick(true);
        buscarPartialLinkText.setClearElement(true);
        buscarPartialLinkText.setSendKeys(true);
        buscarPartialLinkText.setClick(true);
        buscarName.setClearElement(true);
        buscarName.setSendKeys(true);
        buscarName.setClick(true);
        buscarId.setSearchContext(driver);
        buscarXpath.setSearchContext(driver);
        buscarClassName.setSearchContext(driver);
        buscarCSSSelector.setSearchContext(driver);
        buscarLinkText.setSearchContext(driver);
        buscarPartialLinkText.setSearchContext(driver);
        buscarName.setSearchContext(driver);
        buscarTagName.setSearchContext(driver);
        //Comienza a correr los procesos paralelos
        if (this.testContext.getNavegador().equals("IE")) {
            buscarCSSSelector.execute();
            buscarTagName.execute();
            buscarXpath.execute();
            buscarId.execute();
            buscarLinkText.execute();
            buscarName.execute();
        } else {
            buscarCSSSelector.execute();
            buscarTagName.execute();
            buscarXpath.execute();
            buscarId.execute();
            buscarName.execute();
            buscarClassName.execute();
            buscarLinkText.execute();
            buscarPartialLinkText.execute();
        }
        //Crea las variables de control que no permiten que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        addseconds.add(Calendar.MILLISECOND, timeduration);
        fecha = addseconds.getTime();
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " + timeduration + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        //Esperara saber si existe el elemento en alguno de los tipos
        while ((!existid) && (!existname) && (!existclassname) && (!existcssselector) && (!existtagname) && (!existlinktext) && (!existpartiallinktext) && (!existxpath) && !(fecha2.after(fecha))) {
            fecha2 = Calendar.getInstance().getTime();
            //Actualiza la existencia
            existid = buscarId.getElementExist();
            if (buscarId.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarId.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarId.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existclassname = buscarClassName.getElementExist();
            if (buscarClassName.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarClassName.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarClassName.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existxpath = buscarXpath.getElementExist();
            if (buscarXpath.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarXpath.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarXpath.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existcssselector = buscarCSSSelector.getElementExist();
            if (buscarCSSSelector.getElementExist()) {
                //Mata los subprocesos a funcionar
                writeLog(convertir_fecha() + " Encontro el elemento, esperara a que cambie el valor de obtener texto a true");
                while (!buscarCSSSelector.isObtenerText()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Cambio el valor a true, pasamos el while");
                texto = buscarCSSSelector.getTexto();
                writeLog(convertir_fecha() + " Texto Obtenido: " + texto);
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existtagname = buscarTagName.getElementExist();
            if (buscarTagName.getElementExist()) {
                //Mata los subprocesos a funcionar
                writeLog(convertir_fecha() + " Encontro el elemento, esperara a que cambie el valor de obtener texto a true");
                while (!buscarTagName.isObtenerText()) {
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Cambio el valor a true, pasamos el while");
                texto = buscarTagName.getTexto();
                writeLog(convertir_fecha() + " Texto Obtenido: " + texto);
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existlinktext = buscarLinkText.getElementExist();
            if (buscarLinkText.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarLinkText.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarLinkText.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existpartiallinktext = buscarPartialLinkText.getElementExist();
            if (buscarPartialLinkText.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarPartialLinkText.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarPartialLinkText.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
            existname = buscarName.getElementExist();
            if (buscarName.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (!buscarName.isObtenerText()) {
                    threadslepp(50);
                }
                texto = buscarName.getTexto();
                writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
                return texto;
            }
        }
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " +
                timeduration + " mili segundos: " + fecha);
        writeLog(convertir_fecha() + " Fecha contra la que se comparo si transcurrieron los " +
                timeduration + " mili segundos: " + fecha2);
        if ((fecha2.after(fecha))) {
            writeLog(convertir_fecha() + " No pudo hacer obtener el texto del elemento especificado, ya que no existe: " + element);
        } else {
            writeLog(convertir_fecha() + " Logro encontrar y obtener el texto: " + texto + " del elemento especificado: " + element);
            return texto;
        }
        //Retorna Falso si el elemento no Existe
        return null;
    }

    /***
     * Obtiene los elementos que cumplen con el criterio de busqueda especificado
     * @param driver Driver que está controlando el navegador
     * @param element Atributo de los elementos a buscar
     * @return Retorna la lista de elementos que cumplen con los criterios de busqueda, si no encuentra ningun elemento retorna una lista
     * vacía
     */
    public List<WebElement> getElementsIfExist(SearchContext driver, String element) {
        //Para optimizar el tiempo de respuestá
        writeLog(convertir_fecha() + "* ");
        writeLog(convertir_fecha() + " Buscara si existen los elementos indicados para obtenerlos: " + element);
        writeLog(convertir_fecha() + "* ");
        //Declaración de Variable a Retornar
        List<WebElement> elementos = new ArrayList<>();
        int time = 3500;
        //Obtiene la espera fluida
        //Crea las variables de control que no permite que sobre pase los 7,000 milisegundos la busqueda del elemento
        java.util.Date fecha = Calendar.getInstance().getTime();
        Calendar addseconds = Calendar.getInstance();
        addseconds.setTime(fecha);
        if (this.testContext.getNavegador().equalsIgnoreCase("IE")) {
            time = 10000;
            addseconds.add(Calendar.MILLISECOND, time);
        } else {
            if (StringUtils.containsIgnoreCase(testContext.getCanal(), "Banca") || StringUtils.containsIgnoreCase(testContext.getCanal(), "App")) {
                time = 3000;
                addseconds.add(Calendar.MILLISECOND, time);
            } else {
                time = 500;
                addseconds.add(Calendar.MILLISECOND, time);
            }
        }
        fecha = addseconds.getTime();
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " + time + " mili segundos: " + fecha);
        java.util.Date fecha2 = Calendar.getInstance().getTime();
        Wait<WebDriver> wait = getFluentWait(testContext.getDriver(), time, 100);
        //Declaración de Variables auxiliares para que no se bloqueen los hilos
        String elementid = element;
        String elementclasname = element;
        String elementcss = element;
        String elementtagname = element;
        String elementlinkt = element;
        String elementpartial = element;
        String elementxpath = element;
        String elementname = element;
        Wait<WebDriver> waitname = wait;
        Wait<WebDriver> waitid = wait;
        Wait<WebDriver> waitclassname = wait;
        Wait<WebDriver> waitcss = wait;
        Wait<WebDriver> waittagname = wait;
        Wait<WebDriver> waitlinkt = wait;
        Wait<WebDriver> waitpartial = wait;
        Wait<WebDriver> waitxpath = wait;
        //Declaración de Hilos
        searchId buscarId = new searchId(this.testContext, waitid, elementid);
        searchClassName buscarClassName = new searchClassName(this.testContext, waitclassname, elementclasname);
        searchCSSSelector buscarCSSSelector = new searchCSSSelector(this.testContext, waitcss, elementcss);
        searchTagName buscarTagName = new searchTagName(this.testContext, waittagname, elementtagname);
        searchLinkText buscarLinkText = new searchLinkText(this.testContext, waitlinkt, elementlinkt);
        searchPartialLinkText buscarPartialLinkText = new searchPartialLinkText(this.testContext, waitpartial, elementpartial);
        searchXPath buscarXpath = new searchXPath(this.testContext, waitxpath, elementxpath);
        searchName buscarName = new searchName(this.testContext, waitname, elementname);
        //Declaración de Variables de control
        Boolean existclassname = false;
        Boolean existcssselector = false;
        Boolean existtagname = false;
        Boolean existlinktext = false;
        Boolean existpartiallinktext = false;
        Boolean existxpath = false;
        Boolean existid = false;
        Boolean existname = false;
        //Ajusta los procesos para que estos no limpien el elemento indicado
        buscarId.setClearElement(true);
        buscarId.setSendKeys(true);
        buscarId.setClick(true);
        buscarId.setObtenerText(true);
        buscarXpath.setClearElement(true);
        buscarXpath.setSendKeys(true);
        buscarXpath.setClick(true);
        buscarXpath.setObtenerText(true);
        buscarClassName.setClearElement(true);
        buscarClassName.setSendKeys(true);
        buscarClassName.setClick(true);
        buscarClassName.setObtenerText(true);
        buscarCSSSelector.setClearElement(true);
        buscarCSSSelector.setSendKeys(true);
        buscarCSSSelector.setClick(true);
        buscarCSSSelector.setObtenerText(true);
        buscarTagName.setClearElement(true);
        buscarTagName.setSendKeys(true);
        buscarTagName.setClick(true);
        buscarTagName.setObtenerText(true);
        buscarLinkText.setClearElement(true);
        buscarLinkText.setSendKeys(true);
        buscarLinkText.setClick(true);
        buscarLinkText.setObtenerText(true);
        buscarPartialLinkText.setClearElement(true);
        buscarPartialLinkText.setSendKeys(true);
        buscarPartialLinkText.setClick(true);
        buscarPartialLinkText.setObtenerText(true);
        buscarName.setClearElement(true);
        buscarName.setSendKeys(true);
        buscarName.setClick(true);
        buscarName.setObtenerText(true);
        buscarId.setSearchContext(driver);
        buscarXpath.setSearchContext(driver);
        buscarClassName.setSearchContext(driver);
        buscarCSSSelector.setSearchContext(driver);
        buscarLinkText.setSearchContext(driver);
        buscarPartialLinkText.setSearchContext(driver);
        buscarName.setSearchContext(driver);
        buscarTagName.setSearchContext(driver);
        //Comienza a correr los procesos paralelos
        if (this.testContext.getNavegador().equals("IE")) {
            buscarCSSSelector.execute();
            buscarTagName.execute();
            buscarXpath.execute();
            buscarId.execute();
            buscarLinkText.execute();
            buscarName.execute();
        } else {
            buscarCSSSelector.execute();
            buscarTagName.execute();
            buscarXpath.execute();
            buscarId.execute();
            buscarName.execute();
            buscarClassName.execute();
            buscarLinkText.execute();
            buscarPartialLinkText.execute();
        }
        //Esperará saber si existe el elemento en alguno de los tipos
        while ((!existid) && (!existname) && (!existclassname) && (!existcssselector) && (!existtagname) && (!existlinktext) && (!existpartiallinktext) && (!existxpath) && !(fecha2.after(fecha))) {
            fecha2 = Calendar.getInstance().getTime();
            //Actualiza la existencia
            existid = buscarId.getElementExist();
            if (buscarId.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (elementos.isEmpty()) {
                    elementos = buscarId.getElementos();
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y obtener los Elementos: " + elementos);
                return elementos;
            }
            existclassname = buscarClassName.getElementExist();
            if (buscarClassName.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (elementos.isEmpty()) {
                    elementos = buscarClassName.getElementos();
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y obtener los Elementos: " + elementos);
                return elementos;
            }
            existxpath = buscarXpath.getElementExist();
            if (buscarXpath.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (elementos.isEmpty()) {
                    elementos = buscarXpath.getElementos();
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y obtener los Elementos: " + elementos);
                return elementos;
            }
            existcssselector = buscarCSSSelector.getElementExist();
            if (buscarCSSSelector.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (elementos.isEmpty()) {
                    elementos = buscarCSSSelector.getElementos();
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y obtener los Elementos: " + elementos);
                return elementos;
            }
            existtagname = buscarTagName.getElementExist();
            if (buscarTagName.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (elementos.isEmpty()) {
                    elementos = buscarTagName.getElementos();
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y obtener los Elementos: " + elementos);
                return elementos;
            }
            existlinktext = buscarLinkText.getElementExist();
            if (buscarLinkText.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (elementos.isEmpty()) {
                    elementos = buscarLinkText.getElementos();
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y obtener los Elementos: " + elementos);
                return elementos;
            }
            existpartiallinktext = buscarPartialLinkText.getElementExist();
            if (buscarPartialLinkText.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (elementos.isEmpty()) {
                    elementos = buscarPartialLinkText.getElementos();
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y obtener los Elementos: " + elementos);
                return elementos;
            }
            existname = buscarName.getElementExist();
            if (buscarName.getElementExist()) {
                //Mata los subprocesos a funcionar
                while (elementos.isEmpty()) {
                    elementos = buscarName.getElementos();
                    threadslepp(50);
                }
                writeLog(convertir_fecha() + " Logro encontrar y obtener los Elementos: " + elementos);
                return elementos;
            }
        }
        writeLog(convertir_fecha() + " Fecha contra la que se comparara si transcurren los " +
                time +
                " mili segundos: " + fecha);
        writeLog(convertir_fecha() + " Fecha contra la que se comparo si transcurrieron los " +
                time +
                " mili segundos: " + fecha2);
        if ((fecha2.after(fecha)) || elementos.isEmpty()) {
            writeLog(convertir_fecha() + " No pudo obtener los elementos especificados, ya que no existen: " + elementos);
        }
        //Retorna null si el elemento no Existe
        return elementos;
    }

    /***
     * Obtiene la fecha actual en formato dd/MM/YYYY HH:MM:SS
     * @return Retorna una cadena de texto con la fecha obtenida
     */
    public String convertir_fecha() {
        String temp;
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        //convertir_fecha()
        temp = formater.format(LocalDateTime.now());
        return temp;
    }

    /***
     * Presiona la tecla indicada en el condigo numerico indicado
     * @param codigo Codigo numerico de la tecla que queremos presionar
     */
    public void keyPress(int codigo) {
        try {
            char asciiValue = (char) codigo;
            Actions actions = new Actions(testContext.driver);
            actions.keyDown(String.valueOf(asciiValue)).keyUp(String.valueOf(asciiValue)).perform();
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al presionar una tecla: ");
        }
    }

    /***
     * Presiona la tecla indicada en el condigo numerico indicado
     * @param codigo Codigo numerico de la tecla que queremos presionar
     */
    public void keyPress(Keys codigo) {
        try {
            Actions actions = new Actions(testContext.driver);
            actions.sendKeys(codigo).perform();
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al presionar una tecla: ");
        }
    }

    /***
     * Presiona la tecla indicada en el condigo numerico indicado
     * @param repeticiones Cantidad de veces que deseamos se repita el cambio de zoom
     * @param codigo Codigo numerico de la tecla que queremos presionar
     */
    public void cambiarZOOM(int repeticiones, Keys codigo) {
        try {
            for (int i = 0; i < repeticiones; i++) {
                threadslepp(100);
                Actions actions = new Actions(testContext.driver);
                actions.keyDown(Keys.CONTROL).keyDown(codigo).keyUp(codigo).keyUp(Keys.CONTROL).perform();
                writeLog("Presiona la tecla: " + codigo);
                writeLog("Suelta la tecla: " + codigo);
                threadslepp(100);
            }
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al presionar una tecla: ");
        }
    }

    /***
     * Presiona la tecla indicada en el condigo numerico indicado
     * @param repeticiones Cantidad de veces que deseamos se repita el cambio de zoom
     * @param codigo Codigo numerico de la tecla que queremos presionar
     */
    public void cambiarZOOM(int repeticiones, int codigo) {
        try {
            for (int i = 0; i < repeticiones; i++) {
                threadslepp(100);
                char asciiValue = (char) codigo;
                Actions actions = new Actions(testContext.driver);
                actions.keyDown(Keys.CONTROL).keyDown(String.valueOf(asciiValue)).keyUp(String.valueOf(asciiValue)).keyUp(Keys.CONTROL).perform();
                writeLog("Presiona la tecla: " + codigo);
                writeLog("Suelta la tecla: " + codigo);
                threadslepp(100);
            }
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al presionar una tecla: ");
        }
    }

    /***


     /***
     * Presiona la tecla indicada en el condigo numerico indicado
     * @param repeticiones Cantidad de veces que deseamos se repita el cambio de zoom
     *
     */
    public void cambiarZOOMMenos(int repeticiones) {
        try {
            cambiarZOOM(repeticiones, Keys.SUBTRACT);
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al presionar una tecla: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al presionar una tecla: ");
        }
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param cantidad Si el numero es positivo, el desplazamiento es hacía abajo en la pantalla, si el numero es negativo
     *                 el desplazamiento es hacía arriba.
     */
    public void scrollMouse(int cantidad) {
        try {
            Robot robot = new Robot();
            int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width / 2;
            int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height / 2;
            writeLog("Altura de la pantalla " + alto * 2 + " ancho de la pantalla " + ancho * 2);
            robot.mouseMove(ancho, alto);
            robot.mouseWheel(cantidad);
            writeLog("Se realizo el movimiento del scroll: ");
            threadslepp(100);
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al realizar un el scroll: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al intentar realizar el scroll: ");
        }
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia abajo.
     */
    public void scrollMouseDown(int cantidadScrolls) {
        try {
            Actions actions = new Actions(testContext.driver);
            for (int i = 0; i < cantidadScrolls; i++) {
                actions.sendKeys(Keys.PAGE_DOWN).build().perform();
            }
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al realizar el scroll: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al intentar realizar el scroll: ");
        }
    }

    /**
     * Mueve el escrol del mouse
     *
     * @param cantidadScrolls Cantidad de scrolls deseados, el scroll se hace hacia arriba.
     */
    public void scrollMouseUp(int cantidadScrolls) {
        try {
            Actions actions = new Actions(testContext.driver);
            for (int i = 0; i < cantidadScrolls; i++) {
                actions.sendKeys(Keys.PAGE_UP).build().perform();
            }
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al realizar el scroll: " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al intentar realizar el scroll: ");
        }
    }

    /***
     *Selecciona la opcion indicada, si el elemento proporcionado existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param element Atributo del elemento, por medio del cual se realizara la busquedad
     * @param opcion Opcion del elemento que queremos seleccionar
     * @param comment Comentario que sera colocago sobre la imagen capturada si el Elemento indicado existe
     */
    public void selectOption(SearchContext driver, String element, String opcion, String comment) {
        try {
            WebElement elemento = obtenerWebElementx2(driver, element);
            if (!Objects.isNull(elemento)) {
                //Si encuentra el elemento ejecuta este codigo
                try {
                    int opcionint;
                    opcionint = Integer.parseInt(opcion);
                    writeLog("Parcio la opcion a entero: " + opcionint);
                    new Select(elemento).selectByIndex(opcionint - 1);
                    String tiposociedad = obtenerTextoSeleccionadoSelect(elemento);
                    this.takeScreenShotJB(testContext.getDriver(), comment + tiposociedad);
                    writeLog("Encontro el elemento Select: " + element + " " +
                            "Procedera a seleccionar la opcion por medio del Index numerico");
                } catch (NumberFormatException e) {
                    new Select(elemento).selectByVisibleText(opcion);
                    String tiposociedad = obtenerTextoSeleccionadoSelect(elemento);
                    this.takeScreenShotJB(testContext.getDriver(), comment + tiposociedad);
                    writeLog("Encontro el elemento Select: " + element + " " +
                            "Procedera a seleccionar la opcion por medio del Texto Visible");
                }
            } else {
                writeLog("No pudo encontrar el elemento: " + element + " por lo que no se pudo seleccionar la opcion indicada");
            }
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al seleccionar el elemento: " + element + " " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al seleccionar el elemento: " + element);
        }
    }

    /***
     *Selecciona la opcion indicada, si el elemento proporcionado existe en el contexto actual
     * @param driver Driver que está manipulando el navegador
     * @param element Atributo del elemento, por medio del cual se realizara la busquedad
     * @param opcion Opcion del elemento que queremos seleccionar
     */
    public boolean selectOption(SearchContext driver, String element, String opcion) {
        WebElement elemento = obtenerWebElementx2(driver, element);
        try {
            if (!Objects.isNull(elemento)) {
                //Si encuentra el elemento ejecuta este codigo
                try {
                    writeLog("Encontro el elemento Select: " + element + " " +
                            "Procedera a seleccionar la opcion por medio del Index numerico " + opcion);
                    int opcionint;
                    opcionint = Integer.parseInt(opcion);
                    writeLog("Parcio la opcion a entero: " + opcionint);
                    new Select(elemento).selectByIndex(opcionint);
                    return true;
                } catch (NumberFormatException e) {
                    try {
                        writeLog("Encontro el elemento Select: " + element + " " +
                                "Procedera a seleccionar la opcion por medio del Texto Visible: " + opcion);
                        new Select(elemento).selectByVisibleText(opcion);
                        return true;
                    } catch (Exception m) {
                        writeLog("Encontro el elemento Select: " + element + " " +
                                "Procedera a seleccionar la opcion por medio del Value: " + opcion);
                        new Select(elemento).selectByValue(opcion);
                        return true;
                    }
                } catch (Exception ex) {
                    writeLog("Encontro el elemento Select: " + element + " " +
                            "Procedera a seleccionar la opcion por medio del Value: " + opcion);
                    new Select(elemento).selectByValue(opcion);
                    return true;
                }
            } else {
                writeLog("No pudo encontrar el elemento: " + element + " por lo que no se pudo seleccionar la opcion indicada");
            }
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al seleccionar el elemento: " + element + " " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    /***
     * Captura un error y lanza la exepcion con la descripcion del mismo si este existe.
     * @param driver Driver que está manipulando el navegador
     * @param element Elemento el cual tiene la descripcion del error
     * @param salidaerror Si hay que presionar un boton para cerrar la advertencia, null si no queremos cerrarla
     * @param comment Comentario que queremos que coloque antes de la descripcion del error capturado en el documento de evidencia de pruebas
     */
    public void capturarError(WebDriver driver, String element, String salidaerror, String comment) {
        try {
            String mesajeerror = obtenerTextWebElementx2(driver, element);
            if (!Objects.isNull(mesajeerror)) {
                writeLog("Se encontro un mensaje de Error");
                writeLog("*");
                writeLog("*");
                writeLog("Mensaje de Error Capturado: " + mesajeerror);
                takeScreenShotError(driver, comment + mesajeerror);
                writeLog("*");
                writeLog("*");
                if (!Objects.isNull(salidaerror)) {
                    writeLog("si se especifico un elemento para presionar y salir del msj de error");
                    if (clicktoElementx2intents(driver, salidaerror)) {
                        writeLog("hace click en el elemento");
                        Assert.fail(comment + mesajeerror);
                    } else {
                        Assert.fail(comment + mesajeerror);
                    }
                } else {
                    Assert.fail(comment + mesajeerror);
                }
            } else {
                writeLog("No Existe Mensaje de Error!!!");
            }
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al buscar la existencia de un error: " + element + " " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al buscar la existencia de un error: " + element);
        }
    }

    /***
     * Verifica si el test fue exitoso por medio de la respuestá dada al procesar la transaccion
     * @param driver Driver que está manipulando el navegador
     * @param element Elemento que deseamos buscar para saber si el test fue exitoso
     * @param comment Comentario que queremos sea colocado al tomar la captura del test al ser procesado
     */
    public String testIsSuccessful(WebDriver driver, String element, String comment) {
        String response = null;
        try {
            String mesajeerror = getTextIfElementExist(driver, element);
            if (!Objects.isNull(mesajeerror)) {
                writeLog("Mensaje obtenido del elemento para verificar si se ejecuto la transacción correctamente");
                writeLog(mesajeerror);
                if (mesajeerror.contains("E000") || mesajeerror.contains("000") || mesajeerror.contains("Transacción procesada")) {
                    writeLog("Transaccion procesada correctamente");
                    writeLog("*");
                    writeLog("*");
                    writeLog(mesajeerror);
                    response = "E000";
                    takeScreenShotSuccessful(driver, comment + mesajeerror);
                    writeLog("*");
                    writeLog("*");
                    return response;
                } else if (mesajeerror.contains("WRG:")) {
                    //Capturo un mensaje de advertencia
                    writeLog("Advertencia");
                    writeLog("*");
                    writeLog("*");
                    writeLog(mesajeerror);
                    response = "WRG";
                    takeScreenShotWarning(driver, comment + mesajeerror);
                    writeLog("*");
                    writeLog("*");
                    return response;
                } else if (mesajeerror.contains("Err") || mesajeerror.contains("ERR:") || mesajeerror.contains("999")) {
                    //Capturo un mensaje de error
                    writeLog("Se ha capturado el siguiente error");
                    writeLog("*");
                    writeLog("*");
                    writeLog(mesajeerror);
                    response = "ERR";
                    takeScreenShotError(driver, comment + mesajeerror);
                    writeLog("*");
                    writeLog("*");
                    return response;
                }
            } else {
                writeLog("No Encontro el texto del elemento indicado, por lo que no se pudo verificar si la transaccion fue exitosa!!!");
                response = "ERR";
            }
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al verificar si el test fue exitoso: " + element);
            LogsJB.fatal("Error inesperado al verificar si el test fue exitoso: " + element + " " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al verificar si el test fue exitoso: " + element);
        }
        return response;
    }

    /***
     * Verifica si el test fue exitoso por medio de la respuestá dada al procesar la transaccion
     * @param driver Driver que está manipulando el navegador
     * @param element Elemento que deseamos buscar para saber si el test fue exitoso
     */
    public String testIsSuccessful(WebDriver driver, String element) {
        String response = null;
        try {
            String mesajeerror = obtenerTextWebElementx2(driver, element);
            if (!Objects.isNull(mesajeerror)) {
                writeLog("Mensaje obtenido del elemento para verificar si se ejecuto la transacción correctamente");
                writeLog(mesajeerror);
                if (mesajeerror.contains("E000") || mesajeerror.contains("000") || mesajeerror.contains("Transacción procesada")) {
                    writeLog("Transaccion procesada correctamente");
                    writeLog("*");
                    writeLog("*");
                    writeLog(mesajeerror);
                    response = "E000";
                    writeLog("*");
                    writeLog("*");
                    return response;
                } else if (mesajeerror.contains("WRG:")) {
                    //Capturo un mensaje de advertencia
                    writeLog("Advertencia");
                    writeLog("*");
                    writeLog("*");
                    writeLog(mesajeerror);
                    response = "WRG";
                    writeLog("*");
                    writeLog("*");
                    return response;
                } else if (mesajeerror.contains("Err") || mesajeerror.contains("ERR:") || mesajeerror.contains("999")) {
                    //Capturo un mensaje de error
                    writeLog("Se ha capturado el siguiente error");
                    writeLog("*");
                    writeLog("*");
                    writeLog(mesajeerror);
                    response = "ERR";
                    writeLog("*");
                    writeLog("*");
                    return response;
                }
            } else {
                writeLog("No Encontro el texto del elemento indicado, por lo que no se pudo verificar si la transaccion fue exitosa!!!");
                response = null;
                return response;
            }
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al verificar si el test fue exitoso: " + element);
            LogsJB.fatal("Error inesperado al verificar si el test fue exitoso: " + element + " " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al verificar si el test fue exitoso: " + element);
        }
        return response;
    }

    /**
     * Función que realiza una prueba para verificar si la captura de pantalla de un elemento web es exitosa
     *
     * @param driver  El Webdriver utilizado para la interacción con el navegador
     * @param element La expresión que identifica el elemento web para el cual se realizará la prueba. puede ser un
     *                identificador de elemento, como un ID, nombre, clase, etc.
     */
    public void testIsSuccessfulScrenshot(WebDriver driver, String element) {
        try {
            String mesajeerror = obtenerTextWebElementx2(driver, element);
            if (!Objects.isNull(mesajeerror)) {
                writeLog("Mensaje obtenido del elemento para verificar si se ejecuto la transacción correctamente");
                writeLog(mesajeerror);
                if (mesajeerror.contains("E000") || mesajeerror.contains("000") || mesajeerror.contains("Transacción procesada")) {
                    writeLog("Transaccion procesada correctamente");
                    writeLog("*");
                    writeLog("*");
                    writeLog(mesajeerror);
                    setElementscreenshott(driver, "div#FormAlign div");
                    this.takeScreenShotJB(driver, "Transacción Procesada Correctamente: " + mesajeerror);
                    writeLog("*");
                    writeLog("*");
                } else if (mesajeerror.contains("WRG:")) {
                    //Capturo un mensaje de advertencia
                    writeLog("Advertencia");
                    writeLog("*");
                    writeLog("*");
                    setElementscreenshott(driver, "div#FormAlign div");
                    //Tomamos la captura de pantalla para finalizar el test
                    takeScreenShotWarning(driver, "Se a capturado la siguiente advertencia: " + mesajeerror);
                    writeLog("*");
                    writeLog("*");
                } else if (mesajeerror.contains("Err") || mesajeerror.contains("ERR:") || mesajeerror.contains("999")) {
                    //Capturo un mensaje de error
                    writeLog("Se ha capturado el siguiente error");
                    writeLog("*");
                    writeLog("*");
                    setElementscreenshott(driver, "div#FormAlign div");
                    //Tomamos la captura de pantalla para finalizar el test
                    takeScreenShotError(driver, "Se a capturado el siguiente error: " + mesajeerror);
                    writeLog("*");
                    writeLog("*");
                    Assert.fail(mesajeerror);
                }
            } else {
                writeLog("No Encontro el texto del elemento indicado, por lo que no se pudo verificar si la transaccion fue exitosa!!!");
            }
        } catch (Exception e) {
            LogsJB.fatal("Error inesperado al verificar si el test fue exitoso: " + element);
            LogsJB.fatal("Error inesperado al verificar si el test fue exitoso: " + element + " " + e.getMessage());
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
            Assert.fail("Error inesperado al verificar si el test fue exitoso: " + element);
        }
    }

    /***
     * Verifica si el sitio web está disponible
     * @param driver Driver que está manipulando el navegador
     * @param timeduration Tiempo en el cual se estára validando si el servicio está disponible
     * @param timerepetition Tiempo de repeticion de la solicitud
     */
    public void serviceAvalaible(WebDriver driver, int timeduration, int timerepetition) {
        writeLog("Este es un comentario antes del test");
        String element = "/html/body/p";
        String comment = "El servicio está caído, comunicarse con el equipo de integración: ";
        String mesajeerror = obtenerTextWebElementx2(driver, element, timeduration, timerepetition);
        if (!Objects.isNull(mesajeerror)) {
            if (StringUtils.equalsIgnoreCase(mesajeerror, "HTTP Error 503. The service is unavailable.") || StringUtils.containsIgnoreCase(mesajeerror, "HTTP Error 503")) {
                writeLog("Se encontro un mensaje de Error");
                writeLog("*");
                writeLog("*");
                writeLog("Mensaje de Error Capturado: " + mesajeerror);
                takeScreenShotError(driver, comment + mesajeerror);
                writeLog("*");
                writeLog("*");
                Assert.fail(comment + mesajeerror);
            }
        } else {
            writeLog("*");
            writeLog("El servicio si está habilitado");
            writeLog("*");
        }
    }

    public void capturar500ServerError(WebDriver driver, int timeduration, int timerepetition) {
        String element = "/html/body/div[2]/div/fieldset/h2";
        String comment = "Ha sucedido un error en el servidor, comunicarse con el equipo de integración ";
        String mesajeerror = obtenerTextWebElementx2(driver, element, timeduration, timerepetition);
        if (!Objects.isNull(mesajeerror)) {
            if (StringUtils.equalsIgnoreCase(mesajeerror, "500 - Internal server error.") || StringUtils.containsIgnoreCase(mesajeerror, "Internal server error")) {
                writeLog("Se encontro un mensaje de Error");
                writeLog("*");
                writeLog("*");
                writeLog("Mensaje de Error Capturado: " + mesajeerror);
                takeScreenShotError(driver, comment + mesajeerror);
                writeLog("*");
                writeLog("*");
                Assert.fail(comment + mesajeerror);
            }
        } else {
            writeLog("*");
            writeLog("El servicio si está habilitado");
            writeLog("*");
        }
    }

    /***
     * Obtener el valor booleano de un numero
     * @param numero numero que se evaluara
     * @return si el numero es mayor o igual a uno, retorna true, de lo contrario, retorna false.
     */
    public boolean getBooleanfromInt(int numero) {
        return numero >= 1;
    }

    /***
     * Mueve el driver el frame con el ID indicado si este existe en el contexto actual
     * @param driver driver que está controlando el navegador
     * @param frameIDorName Id del frame al que se desea mover el driver
     * @return Si el frame existe y se mueve al mismo, retorna true, de lo contrario retorna false
     */
    public boolean movetoframeIDorName(WebDriver driver, String frameIDorName) {
        //Se traslada al frame de la transaccion
        threadslepp(500);
        WebElement frame = null;
        int i = 0;
        while (Objects.isNull(frame) && i < 2) {
            frame = obtenerWebElementx2(driver, frameIDorName);
            i++;
        }
        return movetoframeforwebelement(driver, frame);
    }

    public boolean movetoframeforwebelement(WebDriver driver, WebElement frame) {
        if (!Objects.isNull(frame)) {
            driver.switchTo().frame(frame);
            threadslepp(200);
            writeLog("El Iframe Obtenido no es nulo, es: " + frame);
            return true;
        }
        return false;
    }

    /***
     * Realiza 2 veces la busquedad de el texto de un elemento
     * @param driver Driver que controla el navegador
     * @param element Atributo del elemento a buscar
     * @return Si logra obtener el texto del elemento especifícado, lo retorna, de lo contrario retorna NULL
     */
    public String obtenerTextWebElementx2(SearchContext driver, String element) {
        int i = 0;
        String texto = null;
        while (Objects.isNull(texto) && i < 2) {
            texto = getTextIfElementExist(driver, element);
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
    public String obtenerTextWebElementx2(SearchContext driver, String element, int timeduration, int timerepetition) {
        int i = 0;
        String texto = null;
        while (Objects.isNull(texto) && i < 2) {
            texto = getTextIfElementExist(driver, element, timeduration, timerepetition);
            i++;
        }
        return texto;
    }

    /***
     * Realiza 2 veces la busquedad del elemento especifícado
     * @param driver Dirver que está controlando el navegador
     * @param element Atributo del elemento a buscar
     * @return Retorna el elemento, si no lo encuentra retorna Null
     */
    public WebElement obtenerWebElementx2(SearchContext driver, String element) {
        int i = 0;
        WebElement temp = null;
        while (Objects.isNull(temp) && i < 2) {
            temp = getElementIfExist(driver, element);
            i++;
        }
        return temp;
    }

    /***
     * Realiza 2 veces la busquedad del elemento especifícado
     * @param driver Dirver que está controlando el navegador
     * @param element Filtro de Atributo del elemento a buscar
     * @return Retorna el elemento, si no lo encuentra retorna Null
     */
    public WebElement obtenerWebElementx2(SearchContext driver, By element) {
        int i = 0;
        WebElement temp = null;
        while (Objects.isNull(temp) && i < 2) {
            temp = getElementIfExist(driver, getIdentificadorBy(element));
            i++;
        }
        return temp;
    }

    /***
     * Realiza 2 veces la busquedad del elemento especifícado
     * @param driver Dirver que está controlando el navegador
     * @param element Filtro de Atributo del elemento a buscar
     * @return Retorna el elemento, si no lo encuentra retorna Null
     */
    public WebElement getElementIfExist(SearchContext driver, By element) {
        int i = 0;
        WebElement temp = null;
        temp = getElementIfExist(driver, getIdentificadorBy(element));
        return temp;
    }

    /***
     * Realiza 2 veces la busquedad de los elementos que cumplen con el criterio de busqueda especificado
     * @param driver Dirver que está controlando el navegador
     * @param element Atributo de los elementos a buscar
     * @return Retorna la lista de elementos que cumplen con los criterios de busqueda, si no encuentra ningun elemento retorna una lista
     * vacía
     */
    public List<WebElement> obtenerWebElementsx2(SearchContext driver, String element) {
        int i = 0;
        List<WebElement> temp = null;
        while (Objects.isNull(temp) && i < 2) {
            temp = getElementsIfExist(driver, element);
            i++;
        }
        return temp;
    }

    /***
     * Realiza 2 veces la busquedad de los elementos que cumplen con el criterio de busqueda especificado
     * @param driver Dirver que está controlando el navegador
     * @param element Filtro de Atributo de los elementos a buscar
     * @return Retorna la lista de elementos que cumplen con los criterios de busqueda, si no encuentra ningun elemento retorna una lista
     * vacía
     */
    public List<WebElement> obtenerWebElementsx2(SearchContext driver, By element) {
        int i = 0;
        List<WebElement> temp = new ArrayList<>();
        while (temp.isEmpty() && i < 2) {
            temp = getElementsIfExist(driver, getIdentificadorBy(element));
            i++;
        }
        return temp;
    }

    /***
     * Obtiene los elementos que cumplen con el criterio de busqueda especificado
     * @param driver Dirver que está controlando el navegador
     * @param element Filtro de Atributo de los elementos a buscar
     * @return Retorna la lista de elementos que cumplen con los criterios de busqueda, si no encuentra ningun elemento retorna una lista
     * vacía
     */
    public List<WebElement> getElementsIfExist(SearchContext driver, By element) {
        int i = 0;
        List<WebElement> temp = new ArrayList<>();
        temp = getElementsIfExist(driver, getIdentificadorBy(element));
        return temp;
    }

    /**
     * Deselecciona el elemento proporcionado
     *
     * @param driver  Driver que está manipulando el navegador
     * @param element Elemento que se desea deseleccionar, tiene que ser tipo radio
     * @return True si el elemento está desseleccionado o si logra desseleccionarlo,
     * si el elemento proporcionado es null, retorna False
     */
    public boolean deseleccionarElemento(WebDriver driver, WebElement element) {
        if (!Objects.isNull(element)) {
            writeLog("La opcion está seleccionada: " + element.isSelected());
            writeLog("Color: " + element.getCssValue("background-color"));
            String color = element.getCssValue("background-color");
            if (element.isSelected() || StringUtils.containsIgnoreCase(color, "46, 152, 9")) {
                writeLog("Deselecciona el elemento: " + element);
                String tempelement = element.toString().split(" -> ")[1];
                String[] data = tempelement.substring(0, tempelement.length() - 1).split(": ");
                String locator = data[0];
                String term = data[1];
                clicktoElementx2intents(driver, term);
                return true;
            } else {
                return true;
            }
        } else {
            writeLog("Elemento proporcionado es nullo");
            return false;
        }
    }

    /**
     * Selecciona el elemento si no está seleccionado
     *
     * @param driver  Driver que está manippulando el navegador
     * @param element Elemento que se desea seleccionar, tiene que ser tipo radio
     * @return True si el elemento está seleccionado o si logra seleccionarlo, si el elemento proporcionado
     * es null retorna False
     */
    public boolean seleccionarElemento(WebDriver driver, WebElement element) {
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
                clicktoElementx2intents(driver, term);
                return true;
            } else {
                return true;
            }
        } else {
            LogsJB.fatal("Elemento proporcionado es nullo");
            return false;
        }
    }

    /***
     * Obtiene el texto de la opción seleccionada de un elemento Select
     * @param temp Elemento Select del cual queremos saber cual es la primera Opcion Seleccionada
     * @return Retorna el texto de la opción seleccionada o una cadena vacía
     */
    public String obtenerTextoSeleccionadoSelect(WebElement temp) {
        Select proceso = new Select(temp);
        String retorno;
        retorno = getTextOfWebElement(proceso.getFirstSelectedOption());
        return retorno;
    }

    /**
     * Envia carácter por carácter al elemento especificado
     *
     * @param driver  Driver que está manipulando el navegador
     * @param element Elemento al que se desea envíar el texto
     * @param valor   String que se desea envíar al elemento
     */
    public void enviarTxtforKeyPress(SearchContext driver, String element, String valor) {
        //Pendiente eliminar el texto existente
        WebElement campo = obtenerWebElementx2(driver, element);
        assert campo != null;
        String texto = getTextOfWebElement(campo);
        writeLog("Texto que tiene el elemento: " + texto);
        if (clicktoElementx2intents(driver, element)) {
            //Elimina carácter por carácter
            for (char c : texto.toCharArray()) {
                keyPress(KeyEvent.VK_BACK_SPACE);
            }
            //Escribe carácter por carácter
            for (char c : valor.toCharArray()) {
                keyPress(c);
            }
        }
    }

    public boolean clickToElement(WebDriver driver,WebElement element) {
        try {
            if (Objects.isNull(element)) {
                LogsJB.fatal("El elemento es nulo. No se puede hacer clic.");
                return false;
            }
            try {
                posicionarmeEn(driver, element);
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

    public String getTextOfWebElement(WebDriver driver, WebElement element) {
        if (Objects.isNull(element)) {
            return "";
        }
        String text = null;
        try {
            posicionarmeEn(driver, element);
            text = element.getText();
            if (stringIsNullOrEmpty(text)) {
                text = element.getAttribute("innerText");
            }
            if (stringIsNullOrEmpty(text)) {
                text = element.getAttribute("value");
            }
            if (stringIsNullOrEmpty(text)) {
                // Intentar obtener el texto utilizando JavaScript en caso de que las formas estándar fallen
                text = getTextUsingJavaScript(driver,element);
            }
        } catch (WebDriverException e) {
            LogsJB.fatal("El elemento ya no existe en el contexto actual ");
            LogsJB.fatal("Stacktrace de la excepción: " + ExceptionUtils.getStackTrace(e));
        }
        if (stringIsNullOrEmpty(text)) {
            LogsJB.fatal(convertir_fecha() + " No se pudo obtener el texto del elemento, comuniquese con los administradores ");
        }
        return stringIsNullOrEmpty(text) ? "" : text;
    }

    private String getTextUsingJavaScript(WebDriver driver, WebElement element) {
        try {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver; // Asegúrate de tener una instancia válida de WebDriver
            return (String) jsExecutor.executeScript("return arguments[0].textContent", element);
        } catch (Exception e) {
            LogsJB.fatal("Error al intentar obtener el texto mediante JavaScript: " + ExceptionUtils.getStackTrace(e));
            return "";
        }
    }

    /**
     * Función donde obtiene y estáblece el marco actual (frame)
     *
     * @param driver WebDriver es el que se estáblecera el marco actual
     */
    public void currentFrame(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String currentFrame = (String) jsExecutor.executeScript("return self.name");
        LogsJB.info("Frame: " + currentFrame);
    }

    /**
     * Función para cambiar el contexto del WebDriver para interactuar con un marco (frame) especifico en la BERediseño
     *
     * @param driver WebDriver es el que cambiará el contexto al marco especificado
     * @param frame  Identificador del marco al que se desea cambiar
     */
    public void switchFrame(WebDriver driver, String frame) {
        WebElement iframe = obtenerWebElementx2(driver, "#" + frame);
        driver.switchTo().frame(iframe);
    }

    /**
     * Función para la carga de inicio de la BERediseño utilizando el WebDriver proporcionado.
     * Esto podria incluir la navegación a la URL de la pagina de inicio de la aplicación
     *
     * @param driver WebDriver se utiliza para navegar a la pagina inicio
     */
    public void loadHome(WebDriver driver) {
        By byBandera = By.id("blockingDiv");
        waitImplicity(driver, byBandera);
    }

    /**
     * Función que permite implementar y modificar un tiempo de espera.
     *
     * @param driver maneja los tiempos de espera para la carga de elementos
     * @param segs   indica los segundos del tiempo de espera.
     */
    public void waitCall(WebDriver driver, int segs) {
        driver.manage().timeouts().implicitlyWait(segs, TimeUnit.SECONDS);
    }

    public ArrayList<String> SepararCadena(String cadena) {
        ArrayList<String> nuevaLista = new ArrayList<String>();
        for (int i = 0; i < cadena.length(); i++) {
            nuevaLista.add(String.valueOf(cadena.charAt(i)));
        }
        return nuevaLista;
    }
}
