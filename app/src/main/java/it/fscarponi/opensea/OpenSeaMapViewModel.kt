package it.fscarponi.opensea

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.enableRotation
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.state.MapState

class OpenSeaMapViewModel {

    var mapUri: Uri? by mutableStateOf(null)


    val state: MapState by mutableStateOf(
        MapState(4, 4096, 4096) {
            scroll(0.5, 0.5)
        }.apply {
            enableRotation()
        }
    )

    fun setTileStreamProvider(tileStreamProvider: TileStreamProvider){
        state.addLayer(tileStreamProvider)
    }
}
