package it.fscarponi.opensea

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.fscarponi.opensea.ui.theme.OpenSeaTheme
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.util.AndroidUtil
import org.mapsforge.map.android.view.MapView
import org.mapsforge.map.layer.renderer.TileRendererLayer
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.rendertheme.InternalRenderTheme
import java.io.FileInputStream


class MainActivity : ComponentActivity() {
    private val viewModel = OpenSeaMapViewModel()

    init {
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.mapUri = uri
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            OpenSeaTheme {
                // A surface container using the "background" color from the theme
                NavHost(navController = navController, startDestination = "HOME"){
                    composable("HOME"){ OpenSeaMap() }
                    composable("USER"){ }
                    composable("MAP_DOWNLOADER"){ }
                }
            }
        }

    }


}


@Composable
fun OpenSeaMap(viewModel: OpenSeaMapViewModel = OpenSeaMapViewModel()) {
    if (viewModel.mapUri != null) {
        MapView(LocalContext.current).apply {
            mapScaleBar.isVisible = true;
            setBuiltInZoomControls(true);

            /*
             * To avoid redrawing all the tiles all the time, we need to set up a tile cache with an
             * utility method.
             */
            val tileCache = AndroidUtil.createTileCache(
                LocalContext.current, "mapcache",
                model.displayModel.tileSize, 1f,
                model.frameBufferModel.overdrawFactor
            )

            /*
             * Now we need to set up the process of displaying a map. A map can have several layers,
             * stacked on top of each other. A layer can be a map or some visual elements, such as
             * markers. Here we only show a map based on a mapsforge map file. For this we need a
             * TileRendererLayer. A TileRendererLayer needs a TileCache to hold the generated map
             * tiles, a map file from which the tiles are generated and Rendertheme that defines the
             * appearance of the map.
             */
            val fis = LocalContext.current.contentResolver.openInputStream(viewModel.mapUri!!) as FileInputStream
            val mapDataStore = MapFile(fis);
            val tileRendererLayer = TileRendererLayer(
                tileCache, mapDataStore,
                model.mapViewPosition, AndroidGraphicFactory.INSTANCE
            );
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);

            /*
             * On its own a tileRendererLayer does not know where to display the map, so we need to
             * associate it with our mapView.
             */
            layerManager.layers.add(tileRendererLayer);

            /*
             * The map also needs to know which area to display and at what zoom level.
             * Note: this map position is specific to Berlin area.
             */
            setCenter(LatLong(42.566667, 14.1));
            setZoomLevel(12);
        }
    } else {
        Text("no map found")
        val navController = rememberNavController()
    }
}

//
//    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//        OpenSeaMap(viewModel)
//    }
//    Button(onClick = {
//
//        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//        { result: ActivityResult ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                //  you will get result here in result.data
//            }
//
//        }
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//
//
//// Always use string resources for UI text.
//// This says something like "Share this photo with"
//// Create intent to show chooser
//        val chooser = Intent(Intent.ACTION_GET_CONTENT).apply {
//            type = "*/*"
//        }
//// Try to invoke the intent.
//
//        try {
//            startActivity(chooser)
//        } catch (e: ActivityNotFoundException) {
//            e.printStackTrace()
//        }
//
//    }) {
//        Text(text = "Select Map")
//    }
//}