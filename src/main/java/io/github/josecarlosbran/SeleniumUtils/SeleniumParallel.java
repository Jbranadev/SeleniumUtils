package io.github.josecarlosbran.SeleniumUtils;

import com.josebran.LogsJB.LogsJB;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Wait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.function.Function;

public class SeleniumParallel {
    /***
     * Verifica si el Elemento Existe
     * @param wait Espera fluida que aplicara la función lambda
     * @param searchContext Contexto en el que se buscara el elemento en cuestión
     * @param identificador Identificador del elemento a buscar
     * @return Retorna un Future<Boolean> con el resultado de la búsqueda, true si encuentra el elemento, false
     * si no lo encuentra o si sucede un error dentro de la busqueda
     */
    static Future<Boolean> elementExist(Wait<WebDriver> wait, SearchContext searchContext, By identificador) {
        Callable<Boolean> run = () -> {
            Boolean exist = false;
            try {
                //Verifica si el Elemento Existe
                exist = wait.until(new Function<>() {
                    public Boolean apply(WebDriver driver) {
                        List<WebElement> elements = searchContext.findElements(identificador);
                        return !elements.isEmpty();
                    }
                });
                if (exist) {
                    LogsJB.info(" Encontro el elemento por medio de " + identificador.toString());
                }
            } catch (WebDriverException ignored) {
            } catch (Exception e) {
                LogsJB.fatal(" Exepcion Capturada - Busquedad por medio de " + identificador.toString());
                LogsJB.fatal("*");
                LogsJB.fatal(" " + ExceptionUtils.getStackTrace(e));
                LogsJB.fatal("*");
            } finally {
                return exist;
            }
        };
        return SeleniumUtils.getSeleniumEjecutor().submit(run);
    }

    /**
     * Limpia el elemento si este existe en el contexto actual
     *
     * @param driver        Driver que manipula el navegador
     * @param wait          Espera fluida que aplicara la función lambda
     * @param searchContext Contexto en el que se buscara el elemento en cuestión
     * @param identificador Identificador del elemento a limpiar
     * @return Retorna un Future<Boolean> con el resultado de la limpieza, true si limpia el elemento, false si
     * no logra limpiar el elemento o sucede un error durante la limpieza
     */
    static Future<Boolean> clearElementIfExist(WebDriver driver, Wait<WebDriver> wait, SearchContext searchContext, By identificador, CountDownLatch latch) {
        Callable<Boolean> run = () -> {
            Boolean exist = false;
            try {
                //Limpia el elemento
                exist = wait.until(new Function<>() {
                    public Boolean apply(WebDriver driver) {
                        if (SeleniumUtils.ElementoDeshabilitado(searchContext.findElement(identificador))) {
                            LogsJB.warning(" El elemento no se encuentra habilitado para su limpieza " + identificador);
                            return true;
                        }
                        LogsJB.info(" Limpiando el elemento por medio de " + identificador);
                        latch.countDown(); // Marcar la tarea como completada
                        Boolean result = SeleniumUtils.cleanElement(driver, searchContext.findElement(identificador));
                        if (!result) {
                            LogsJB.warning(" No pudo limpiar el elemento, " + identificador +
                                    " comuniquese con los administradores ");
                        }
                        return true;
                    }
                });
            } catch (WebDriverException ignored) {
            } catch (Exception e) {
                LogsJB.fatal(" Exepcion Capturada - Busquedad por medio de " + identificador.toString());
                LogsJB.fatal("*");
                LogsJB.fatal(" " + ExceptionUtils.getStackTrace(e));
                LogsJB.fatal("*");
            } finally {
                return exist;
            }
        };
        return SeleniumUtils.getSeleniumEjecutor().submit(run);
    }

    /***
     * Envia el texto proporcionado como parametro al elemento si este existe en el contexto actual
     * @param driver Driver que manipula el navegador
     * @param wait Espera fluida que aplicara la función lambda
     * @param searchContext Contexto en el que se buscara el elemento en cuestión
     * @param identificador Identificador del elemento al que se le enviara el texto
     * @param Texto Texto a enviar al elemento
     * @return Retorna un Future<Boolean> con el resultado del envio de texto, true si envia el texto, false si
     * no logra enviar el texto o sucede un error durante el envio
     */
    static Future<Boolean> sendKeysIfElementExist(WebDriver driver, Wait<WebDriver> wait, SearchContext searchContext, By identificador, CountDownLatch latch, CharSequence... Texto) {
        Callable<Boolean> run = () -> {
            Boolean exist = false;
            try {
                //Envia el texto al elemento
                exist = wait.until(new Function<>() {
                    public Boolean apply(WebDriver driver) {
                        if (!searchContext.findElement(identificador).isEnabled()) {
                            LogsJB.warning(" El elemento " + identificador +
                                    " no se encuentra habilitado");
                            return true;
                        }
                        LogsJB.info(" Enviando Texto al elemento por medio de " + identificador +
                                " : " + Arrays.toString(Texto).substring(1, Arrays.toString(Texto).length() - 1));
                        latch.countDown(); // Marcar la tarea como completada
                        Boolean result = SeleniumUtils.sendKeysToElement(driver, searchContext.findElement(identificador), Texto);
                        if (!result) {
                            LogsJB.warning(" No pudo enviar el texto a el elemento, " + identificador +
                                    " comuniquese con los administradores ");
                        }
                        return true;
                    }
                });
            } catch (WebDriverException ignored) {
            } catch (Exception e) {
                LogsJB.fatal(" Exepcion Capturada - Busquedad por medio de " + identificador.toString());
                LogsJB.fatal("*");
                LogsJB.fatal(" " + ExceptionUtils.getStackTrace(e));
                LogsJB.fatal("*");
            } finally {
                return exist;
            }
        };
        return SeleniumUtils.getSeleniumEjecutor().submit(run);
    }

    /***
     * Obtiene el texto del elemento si este existe en el contexto actual
     * @param driver Driver que manipula el navegador
     * @param wait Espera fluida que aplicara la función lambda
     * @param searchContext Contexto en el que se buscara el elemento en cuestión
     * @param identificador Identificador del elemento al que se le obtendra el texto
     * @return Retorna un Future<String> con el texto del elemento, si el elemento no existe o sucede un error
     * durante la obtención del texto, se retornara un String vacio
     */
    static Future<String> getTextIfElementExist(WebDriver driver, Wait<WebDriver> wait, SearchContext searchContext, By identificador) {
        Callable<String> run = () -> {
            String result = "";
            try {
                //Obtiene el Texto del Elemento
                result = wait.until(new Function<>() {
                    public String apply(WebDriver driver) {
                        if (!searchContext.findElement(identificador).isEnabled()) {
                            LogsJB.warning(" El elemento no se encuentra habilitado para obtener su texto " + identificador);
                            return "";
                        }
                        LogsJB.info(" Obteniendo el Texto del elemento por medio de " + identificador);
                        return SeleniumUtils.getTextOfWebElement(driver, searchContext.findElement(identificador));
                    }
                });
            } catch (WebDriverException ignored) {
            } catch (Exception e) {
                LogsJB.fatal(" Exepcion Capturada - Busquedad por medio de " + identificador.toString());
                LogsJB.fatal("*");
                LogsJB.fatal(" " + ExceptionUtils.getStackTrace(e));
                LogsJB.fatal("*");
            } finally {
                return result;
            }
        };
        return SeleniumUtils.getSeleniumEjecutor().submit(run);
    }

    /***
     * Hace click en el elemento si este existe en el contexto actual
     * @param driver Driver que manipula el navegador
     * @param wait Espera fluida que aplicara la función lambda
     * @param searchContext Contexto en el que se buscara el elemento en cuestión
     * @param identificador Identificador del elemento al que se le hara click
     * @return Retorna un Future<Boolean> con el resultado del click, true si hace click en el elemento, false si
     * no logra hacer click en el elemento o sucede un error durante el click
     */
    static Future<Boolean> clickElementIfExist(WebDriver driver, Wait<WebDriver> wait, SearchContext searchContext, By identificador, CountDownLatch latch) {
        Callable<Boolean> run = () -> {
            Boolean exist = false;
            try {
                //Hace Click sobre el elemento
                exist = wait.until(new Function<>() {
                    public Boolean apply(WebDriver driver) {
                        if (SeleniumUtils.ElementoDeshabilitado(searchContext.findElement(identificador))) {
                            LogsJB.warning(" El elemento no se encuentra habilitado para hacer click en el " + identificador);
                            return true;
                        }
                        LogsJB.info(" Hace click en el elemento por medio de " + identificador);
                        latch.countDown(); // Marcar la tarea como completada
                        Boolean result = SeleniumUtils.clickToElement(driver, searchContext.findElement(identificador));
                        if (!result) {
                            LogsJB.warning(" No pudo hacer click en el elemento, comuniquese con los administradores ");
                        }
                        return true;
                    }
                });
            } catch (WebDriverException ignored) {
            } catch (Exception e) {
                LogsJB.fatal(" Exepcion Capturada - Busquedad por medio de " + identificador.toString());
                LogsJB.fatal("*");
                LogsJB.fatal(" " + ExceptionUtils.getStackTrace(e));
                LogsJB.fatal("*");
            } finally {
                return exist;
            }
        };
        return SeleniumUtils.getSeleniumEjecutor().submit(run);
    }

    /***
     * Obtiene los elementos si estos existen en el contexto actual
     * @param driver Driver que manipula el navegador
     * @param wait Espera fluida que aplicara la función lambda
     * @param searchContext Contexto en el que se buscara el elemento en cuestión
     * @param identificador Identificador de los elementos a buscar
     * @return Retorna un Future<List<WebElement>> con los elementos encontrados, si no se encuentran elementos o sucede un error
     * durante la busqueda, se retornara una lista vacia
     */
    static Future<List<WebElement>> getElementsIfExist(WebDriver driver, Wait<WebDriver> wait, SearchContext searchContext, By identificador, CharSequence... Texto) {
        Callable<List<WebElement>> run = () -> {
            List<WebElement> elementos = new ArrayList<>();
            try {
                elementos = wait.until(new Function<>() {
                    public List<WebElement> apply(WebDriver driver) {
                        LogsJB.trace(" Obtiene los elementos por medio de " + identificador.toString());
                        return searchContext.findElements(identificador);
                    }
                });
            } catch (WebDriverException ignored) {
            } catch (Exception e) {
                LogsJB.fatal(" Exepcion Capturada - Busquedad por medio de " + identificador.toString());
                LogsJB.fatal("*");
                LogsJB.fatal(" " + ExceptionUtils.getStackTrace(e));
                LogsJB.fatal("*");
            } finally {
                return elementos;
            }
        };
        return SeleniumUtils.getSeleniumEjecutor().submit(run);
    }

    /***
     * Obtiene el elemento si este existe en el contexto actual
     * @param driver Driver que manipula el navegador
     * @param wait Espera fluida que aplicara la función lambda
     * @param searchContext Contexto en el que se buscara el elemento en cuestión
     * @param identificador Identificador del elemento a buscar
     * @return Retorna un Future<WebElement> con el elemento encontrado, si no se encuentra el elemento o sucede un error
     * durante la busqueda, se retornara un elemento nulo
     */
    static Future<WebElement> getElementIfExist(WebDriver driver, Wait<WebDriver> wait, SearchContext searchContext, By identificador, CharSequence... Texto) {
        Callable<WebElement> run = () -> {
            final WebElement[] elemento = {null};
            try {
                wait.until(new Function<WebDriver, Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        LogsJB.trace(" Obtiene el elemento por medio de " + identificador.toString());
                        elemento[0] = searchContext.findElement(identificador);
                        return true;
                    }
                });
            } catch (WebDriverException ignored) {
            } catch (Exception e) {
                LogsJB.fatal(" Exepcion Capturada - Busquedad por medio de " + identificador.toString());
                LogsJB.fatal("*");
                LogsJB.fatal(" " + ExceptionUtils.getStackTrace(e));
                LogsJB.fatal("*");
            } finally {
                return elemento[0];
            }
        };
        return SeleniumUtils.getSeleniumEjecutor().submit(run);
    }
}
