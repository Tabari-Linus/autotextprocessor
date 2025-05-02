package lii.autotexttprocessor.util;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName());

    static {
        try {
            // Create a FileHandler to log to a file
            FileHandler fileHandler = new FileHandler("applicationActivities.log", true); // Append mode
            fileHandler.setFormatter(new SimpleFormatter()); // Use a simple text format
            logger.addHandler(fileHandler);

            // Set the logging level
            logger.setLevel(Level.ALL);

            // Optional: Remove default console handler
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                if (handler instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handler);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger", e);
        }

    }

    public static void logError(String message, Throwable throwable) {
        Logger.getLogger(LoggerUtil.class.getName()).log(Level.SEVERE, message, throwable);
    }
    public static void logError(String message) {
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

    public static void logError(String errorStartingApplication, String message) {
        logger.log(Level.SEVERE, errorStartingApplication + ": " + message);
    }
}