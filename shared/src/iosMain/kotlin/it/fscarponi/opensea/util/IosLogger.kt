package it.fscarponi.opensea.util

import platform.Foundation.NSLog

/**
 * iOS implementation of the Logger interface.
 */
class IosLogger : Logger {
    override fun debug(tag: String, message: String) {
        NSLog("[$tag] DEBUG: $message")
    }

    override fun info(tag: String, message: String) {
        NSLog("[$tag] INFO: $message")
    }

    override fun warn(tag: String, message: String) {
        NSLog("[$tag] WARN: $message")
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        val errorMessage = if (throwable != null) {
            "$message\nException: ${throwable.message}"
        } else {
            message
        }
        NSLog("[$tag] ERROR: $errorMessage")
    }

    companion object {
        /**
         * Initializes the iOS logger.
         * This should be called from the SwiftUI App struct.
         */
        fun init() {
            Logger.setInstance(IosLogger())
        }
    }
}