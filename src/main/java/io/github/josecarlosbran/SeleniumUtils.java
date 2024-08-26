package io.github.josecarlosbran;

import com.josebran.LogsJB.LogsJB;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

public class SeleniumUtils {


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




}
