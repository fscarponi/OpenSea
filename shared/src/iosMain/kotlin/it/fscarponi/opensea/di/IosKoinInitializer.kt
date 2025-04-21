package it.fscarponi.opensea.di

import it.fscarponi.opensea.presentation.navigation.IosNavigator
import it.fscarponi.opensea.util.IosLogger
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Initializes Koin for iOS.
 */
object IosKoinInitializer {
    /**
     * iOS-specific module.
     */
    fun iosModule(): Module = module {
        single { IosNavigator.create() }
    }

    /**
     * Initializes Koin for iOS.
     */
    fun init() {
        // Initialize the logger
        IosLogger.init()
        
        // Initialize Koin with all modules
        KoinModule.initKoin(listOf(iosModule()))
    }
}