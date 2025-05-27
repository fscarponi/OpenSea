package it.fscarponi.opensea.di

import it.fscarponi.opensea.data.repository.LocationRepositoryImpl
import it.fscarponi.opensea.data.repository.MapRepositoryImpl
import it.fscarponi.opensea.domain.location.LocationService
import it.fscarponi.opensea.domain.repository.LocationRepository
import it.fscarponi.opensea.domain.repository.MapRepository
import it.fscarponi.opensea.domain.usecase.GetCurrentLocationUseCase
import it.fscarponi.opensea.presentation.viewmodel.LocationViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module definitions for the application.
 */
object KoinModule {
    /**
     * Domain module containing use cases.
     */
    val domainModule = module {
        factory { GetCurrentLocationUseCase(get()) }
    }

    /**
     * Data module containing repositories and data sources.
     */
    val dataModule = module {
        // Map repository
        single<MapRepository> { MapRepositoryImpl(get()) }

        // Location repository
        single<LocationRepository> { LocationRepositoryImpl(get()) }
    }

    /**
     * Presentation module containing ViewModels.
     */
    val presentationModule = module {
        factory { LocationViewModel(get(), get()) }
    }

    /**
     * All modules combined.
     */
    val allModules = listOf(domainModule, dataModule, presentationModule)

    /**
     * Initializes Koin with all modules.
     *
     * @param additionalModules Additional modules to include in the Koin context.
     */
    fun initKoin(additionalModules: List<Module> = emptyList()) {
        startKoin {
            modules(allModules + additionalModules)
        }
    }
}
