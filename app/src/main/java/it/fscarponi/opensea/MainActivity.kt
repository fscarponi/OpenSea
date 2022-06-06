package it.fscarponi.opensea

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.fscarponi.opensea.di.DIModules
import it.fscarponi.opensea.ui.theme.OpenSeaTheme
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.util.AndroidUtil
import org.mapsforge.map.android.view.MapView
import org.mapsforge.map.layer.renderer.TileRendererLayer
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.rendertheme.InternalRenderTheme
import java.io.FileInputStream


class MainActivity : ComponentActivity(), DIAware {

    override val di: DI = DI.lazy {
        import(DIModules.viewModels)
    }

    private val viewModel: OpenSeaMapViewModel by instance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            OpenSeaTheme {
                // A surface container using the "background" color from the theme
                NavHost(navController = navController, startDestination = "HOME") {
                    composable("HOME") {
                        OpenSeaMap(viewModel = viewModel, navController = navController)
                    }
                    composable("USER") { }
                    composable("mapDownload") { withDI(di = di) { MapDownloader(navController) } }
                }
            }
        }
    }


}


@Composable
fun OpenSeaMap(viewModel: OpenSeaMapViewModel = OpenSeaMapViewModel(), navController: NavHostController) {
    if (viewModel.mapUri != null) {
        MapView(LocalContext.current).also {
            it.mapScaleBar.isVisible = true
            it.setBuiltInZoomControls(true)
            /*
             * To avoid redrawing all the tiles all the time, we need to set up a tile cache with an
             * utility method.
             */
            val tileCache = AndroidUtil.createTileCache(
                LocalContext.current, "mapcache",
                it.model.displayModel.tileSize, 1f,
                it.model.frameBufferModel.overdrawFactor
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
            val mapDataStore = MapFile(fis)
            val tileRendererLayer = TileRendererLayer(
                tileCache, mapDataStore,
                it.model.mapViewPosition, AndroidGraphicFactory.INSTANCE
            )
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT)

            /*
             * On its own a tileRendererLayer does not know where to display the map, so we need to
             * associate it with our mapView.
             */
            it.layerManager.layers.add(tileRendererLayer)

            /*
             * The map also needs to know which area to display and at what zoom level.
             * Note: this map position is specific to Berlin area.
             */
            it.setCenter(LatLong(42.566667, 14.1))
            it.setZoomLevel(12)
        }
    } else {
        Column() {
            Text("no map found")
            Button(onClick = { navController.navigate("mapDownload") }) {
                Text("Download MAP")
            }
        }
    }
}

@Composable
fun MapDownloader(navController: NavHostController) {
    val openSeaMapViewModel: OpenSeaMapViewModel by rememberInstance()

    var uri by remember {
        mutableStateOf<Uri?>(null)
    }
    val chooser = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) {
        openSeaMapViewModel.mapUri = it
        uri = it
    }
    Column {
        Text("Map download not yet implemented, load map from devices")
        TextButton(onClick = {
            chooser.launch("*/*")
        }) {
            Text("CHOOSE")
        }
        if (uri != null) {
            Text(uri.toString())
            TextButton(onClick = {
                println(openSeaMapViewModel.mapUri)
                navController.navigate("HOME")
            }) {
                Text("BACK TO MAP")
            }
        }
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