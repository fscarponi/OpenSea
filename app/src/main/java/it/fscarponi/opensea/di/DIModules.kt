package it.fscarponi.opensea.di

import it.fscarponi.opensea.OpenSeaMapViewModel
import org.kodein.di.*

object DIModules {

    val viewModels get() = DI.Module("viemodels"){
        bind<OpenSeaMapViewModel>() with singleton { OpenSeaMapViewModel() }
    }
}