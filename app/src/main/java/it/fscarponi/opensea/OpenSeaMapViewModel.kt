package it.fscarponi.opensea

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class OpenSeaMapViewModel {

    var mapUri: Uri? by mutableStateOf(null)
}
