package io.github.josecarlosbran;

import com.josebran.LogsJB.LogsJB;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class SeleniumUtils {
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



}
