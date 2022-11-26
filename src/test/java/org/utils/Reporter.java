package org.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reporte class, acts as Logger
 */
public class Reporter {

    public Reporter(){}

    private static Logger getLogger(){
        return LoggerFactory.getLogger(Reporter.class);
    }

    /**
     * Logs an informative message to console
     * @param text
     */
    public static void info(String text){
        getLogger().info(text);
    }

    /**
     * Logs an error message to console
     * @param text
     */
    public static void error(String text){
        getLogger().error(text);
    }
}
