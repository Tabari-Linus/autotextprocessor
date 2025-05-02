package lii.autotexttprocessor.util;

import java.util.logging.*;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName());

    static {
        try {
            // Set up the logger
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL);
            ConsoleHandler consoleHandler = new ConsoleHandler();;
            consoleHandler.setLevel(Level.ALL);
            logger.addHandler(consoleHandler);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    public static void logError(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message);
    }


    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logDebug(String message) {
        logger.log(Level.FINE, message);
    }

    public static void logWarning(String message) {
        logger.log(Level.WARNING, message);
    }
}