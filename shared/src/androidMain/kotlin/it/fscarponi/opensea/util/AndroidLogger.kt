package it.fscarponi.opensea.util

import android.util.Log

/**
 * Android implementation of the Logger interface.
 */
class AndroidLogger : Logger {
    override fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun info(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun warn(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }

    companion object {
        /**
         * Initializes the Android logger.
         * This should be called from the Application class.
         */
        fun init() {
            Logger.setInstance(AndroidLogger())
        }
    }
}