package io.github.josecarlosbran;

import com.josebran.LogsJB.LogsJB;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

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





}
