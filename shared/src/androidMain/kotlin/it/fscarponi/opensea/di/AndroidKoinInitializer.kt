package it.fscarponi.opensea.di

import android.content.Context
import it.fscarponi.opensea.domain.location.AndroidLocationService
import it.fscarponi.opensea.domain.location.LocationService
import it.fscarponi.opensea.presentation.navigation.AndroidNavigator
import it.fscarponi.opensea.util.AndroidLogger
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Initializes Koin for Android.
 */
object AndroidKoinInitializer {
    /**
     * Android-specific module.
     */
    fun androidModule(context: Context): Module = module {
        single { context }
        single { AndroidNavigator.create() }

        // Location service
        single<LocationService> { AndroidLocationService(get()) }
    }

    /**
     * Initializes Koin for Android.
     *
     * @param context The application context.
     */
    fun init(context: Context) {
        // Initialize the logger
        AndroidLogger.init()

        // Initialize Koin with all modules
        KoinModule.initKoin(listOf(androidModule(context)))
    }
}
