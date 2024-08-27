package io.github.josecarlosbran.SeleniumUtils;

import com.josebran.LogsJB.LogsJB;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Wait;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Function;

public class SeleniumParallel {

    /*

    public void temp(Wait<WebDriver> wait, SearchContext searchContext, By identificador) {
        Callable<Boolean> run = () -> {
            try {
                //Verifica si el Elemento Existe
                boolean exist = wait.until(new Function<>() {
                    public Boolean apply(WebDriver driver) {
                        List<WebElement> elements = searchContext.findElements(identificador);
                        return !elements.isEmpty();
                    }
                });
                if (exist) {
                    LogsJB.debug( " Encontro el elemento por medio de "+identificador.toString());
                }

                //Si el elemento existe lo asígna y lo retornara
                if (getElementExist()) {
                    wait.until(new Function<WebDriver, Boolean>() {
                        public Boolean apply(WebDriver driver) {
                            utilities.writeLog( " Obtiene el elemento por medio de "+identificador.toString());
                            setElemento(searchContext.findElement(identificador));
                            return true;
                        }
                    });
                    setElementos(wait.until(new Function<>() {
                        public List<WebElement> apply(WebDriver driver) {
                            utilities.writeLog( " Obtiene los elementos por medio de "+identificador.toString());
                            return searchContext.findElements(identificador);
                        }
                    }));
                    //Hace Click sobre el elemento
                    if (!isClick()) {
                        setClick(wait.until(new Function<>() {
                            public Boolean apply(WebDriver driver) {
                                if (utilities.elementIsDisabled(searchContext.findElement(identificador))) {
                                    utilities.writeLog( " El elemento no se encuentra habilitado");
                                    return false;
                                }
                                utilities.writeLog( " Hace click en el elemento por medio de "+identificador.toString());
                                boolean result = utilities.clickToElement(searchContext.findElement(identificador));
                                if (!result) {
                                    utilities.writeLog( " No pudo hacer click en el elemento, comuniquese con los administradores ");
                                }
                                return true;
                            }
                        }));
                    }
                    //Limpia el elemento
                    if (!isClearElement()) {
                        setClearElement(wait.until(new Function<>() {
                            public Boolean apply(WebDriver driver) {
                                if (utilities.elementIsDisabled(searchContext.findElement(identificador))) {
                                    utilities.writeLog( " El elemento no se encuentra habilitado");
                                    return false;
                                }
                                utilities.writeLog( " Limpiando el elemento por medio de "+identificador.toString());
                                boolean result = utilities.cleanElement(searchContext.findElement(identificador));
                                if (!result) {
                                    utilities.writeLog( " No pudo limpiar el elemento, comuniquese con los administradores ");
                                }
                                return true;
                            }
                        }));
                    }
                    //Envia el texto al elemento
                    if (!isSendKeys()) {
                        //
                        setSendKeys(
                                wait.until(new Function<>() {
                                    public Boolean apply(WebDriver driver) {
                                        if (!searchContext.findElement(identificador).isEnabled()) {
                                            utilities.writeLog( " El elemento no se encuentra habilitado");
                                            return true;
                                        }
                                        utilities.writeLog( " Enviando Texto al elemento por medio de Class Name: " + Arrays.toString(getTextoaenviar()).substring(1, Arrays.toString(getTextoaenviar()).length() - 1));
                                        boolean result = utilities.sendKeysToElement(searchContext.findElement(identificador), getTextoaenviar());
                                        if (!result) {
                                            utilities.writeLog( " No pudo enviar el texto a el elemento, comuniquese con los administradores ");
                                        }
                                        return true;
                                    }
                                })
                        );
                    }
                    //Obtiene el Texto del Elemento
                    if (!isObtenerText()) {
                        setObtenerText(
                                wait.until(new Function<>() {
                                    public Boolean apply(WebDriver driver) {
                                        if (!searchContext.findElement(identificador).isEnabled()) {
                                            utilities.writeLog( " El elemento no se encuentra habilitado");
                                            return true;
                                        }
                                        setTexto(utilities.getTextOfWebElement(searchContext.findElement(identificador)));
                                        utilities.writeLog( " Obteniendo el Texto del elemento por medio de Class Name: " + getTexto());
                                        return true;
                                    }
                                })
                        );
                    }
                }
            } catch (WebDriverException ignored) {
            } catch (Exception e) {
                LogsJB.fatal( " Exepcion Capturada - Busquedad por Class Name ");
                LogsJB.fatal( "*");
                LogsJB.fatal( " " + ExceptionUtils.getStackTrace(e));
                LogsJB.fatal( "*");
            }
            return true;
        };
        SeleniumUtils.getSeleniumEjecutor().submit(run);
    }
*/

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
            boolean exist = false;
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
    static Future<Boolean> clearElementIfExist(WebDriver driver, Wait<WebDriver> wait, SearchContext searchContext, By identificador) {
        Callable<Boolean> run = () -> {
            boolean exist = false;
            try {
                //Limpia el elemento
                exist = wait.until(new Function<>() {
                    public Boolean apply(WebDriver driver) {
                        if (SeleniumUtils.elementIsDisabled(searchContext.findElement(identificador))) {
                            LogsJB.warning(" El elemento no se encuentra habilitado para su limpieza " + identificador.toString());
                            return false;
                        }
                        LogsJB.info(" Limpiando el elemento por medio de " + identificador.toString());
                        boolean result = SeleniumUtils.cleanElement(driver, searchContext.findElement(identificador));
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
    static Future<Boolean> sendKeysIfElementExist(WebDriver driver, Wait<WebDriver> wait, SearchContext searchContext, By identificador, CharSequence... Texto) {
        Callable<Boolean> run = () -> {
            boolean exist = false;
            try {
                //Envia el texto al elemento
                exist = wait.until(new Function<>() {
                    public Boolean apply(WebDriver driver) {
                        if (!searchContext.findElement(identificador).isEnabled()) {
                            LogsJB.warning(" El elemento " + identificador.toString() +
                                    " no se encuentra habilitado");
                            return true;
                        }
                        LogsJB.info(" Enviando Texto al elemento por medio de " + identificador.toString() +
                                " : " + Arrays.toString(Texto).substring(1, Arrays.toString(Texto).length() - 1));
                        boolean result = SeleniumUtils.sendKeysToElement(driver, searchContext.findElement(identificador), Texto);
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
                            LogsJB.warning(" El elemento no se encuentra habilitado para obtener su texto " + identificador.toString());
                            return "";
                        }
                        LogsJB.info(" Obteniendo el Texto del elemento por medio de " + identificador.toString());
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
}
