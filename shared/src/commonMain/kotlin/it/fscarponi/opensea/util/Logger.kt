package it.fscarponi.opensea.util

/**
 * Interface for logging messages.
 * This will be implemented by platform-specific logging handlers.
 */
interface Logger {
    /**
     * Logs a debug message.
     *
     * @param tag The tag for the message.
     * @param message The message to log.
     */
    fun debug(tag: String, message: String)
    
    /**
     * Logs an info message.
     *
     * @param tag The tag for the message.
     * @param message The message to log.
     */
    fun info(tag: String, message: String)
    
    /**
     * Logs a warning message.
     *
     * @param tag The tag for the message.
     * @param message The message to log.
     */
    fun warn(tag: String, message: String)
    
    /**
     * Logs an error message.
     *
     * @param tag The tag for the message.
     * @param message The message to log.
     * @param throwable The throwable associated with the error, if any.
     */
    fun error(tag: String, message: String, throwable: Throwable? = null)
    
    companion object {
        /**
         * The default logger instance.
         * This will be set by platform-specific code.
         */
        lateinit var instance: Logger
            private set
        
        /**
         * Sets the logger instance.
         * This should be called by platform-specific code during initialization.
         *
         * @param logger The logger instance to use.
         */
        fun setInstance(logger: Logger) {
            instance = logger
        }
    }
}

/**
 * Extension function for logging debug messages.
 *
 * @param message The message to log.
 */
inline fun Any.logDebug(message: String) {
    Logger.instance.debug(this::class.simpleName ?: "Unknown", message)
}

/**
 * Extension function for logging info messages.
 *
 * @param message The message to log.
 */
inline fun Any.logInfo(message: String) {
    Logger.instance.info(this::class.simpleName ?: "Unknown", message)
}

/**
 * Extension function for logging warning messages.
 *
 * @param message The message to log.
 */
inline fun Any.logWarn(message: String) {
    Logger.instance.warn(this::class.simpleName ?: "Unknown", message)
}

/**
 * Extension function for logging error messages.
 *
 * @param message The message to log.
 * @param throwable The throwable associated with the error, if any.
 */
inline fun Any.logError(message: String, throwable: Throwable? = null) {
    Logger.instance.error(this::class.simpleName ?: "Unknown", message, throwable)
}