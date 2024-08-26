package io.github.josecarlosbran;

import com.josebran.LogsJB.LogsJB;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import java.time.Duration;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SeleniumUtils {
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    static ExecutorService seleniumEjecutor = Executors.newVirtualThreadPerTaskExecutor();
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
     * @param object El Array de objetos que se desea convertit en cadenas
     * @param acierto Variable booleana que decide si será un acierto(True) o un fallo (False)
     * @return Un ArrayList que contiene las representaciones en forma de cadena de los objetos
     */
    public static ArrayList<String> convertObjectToArrayString(Object[] object, Boolean acierto) {
        if(acierto) {


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
        }else{
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
}
