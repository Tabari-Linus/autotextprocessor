public class LoggingUtil {
    private static final Logger logger = LogManager.getLogger(LoggingUtil.class);

    public static void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logDebug(String message) {
        logger.debug(message);
    }
}