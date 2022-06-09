package it.fscarponi.opensea.di

import it.fscarponi.opensea.OpenSeaMapViewModel
import it.fscarponi.opensea.OpenSeaRepository
import it.fscarponi.opensea.OpenSeaTileStreamProvider
import org.kodein.di.*

object DIModules {

    val viewModels get() = DI.Module("main"){
        bind<OpenSeaRepository>() with singleton { OpenSeaRepository() }
        bind<OpenSeaMapViewModel>() with singleton { OpenSeaMapViewModel() }
    }
}