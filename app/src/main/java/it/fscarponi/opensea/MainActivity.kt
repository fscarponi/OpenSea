package it.fscarponi.opensea

import android.content.Context
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
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.MapUI
import java.io.InputStream


class MainActivity : ComponentActivity(), DIAware {

    override val di: DI = DI.lazy {
        import(DIModules.viewModels)
    }

    private val viewModel: OpenSeaMapViewModel by instance()
    private val repo: OpenSeaRepository by instance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            OpenSeaTheme {
                // A surface container using the "background" color from the theme
                NavHost(navController = navController, startDestination = "HOME") {
                    composable("HOME") {
                        OpenSeaMap(viewModel = viewModel, navController = navController, repo)
                    }
                    composable("USER") { }
                    composable("mapDownload") { withDI(di = di) { MapDownloader(navController) } }
                }
            }
        }

    }
}

class OpenSeaTileStreamProvider(private val context: Context, private val mapUri: Uri, private val repo: OpenSeaRepository) : TileStreamProvider {

    override suspend fun getTileStream(row: Int, col: Int, zoomLvl: Int): InputStream? {
        return repo.getTileStream(row, col, zoomLvl)
    }

}


@Composable
fun OpenSeaMap(viewModel: OpenSeaMapViewModel = OpenSeaMapViewModel(), navController: NavHostController, repository: OpenSeaRepository) {

    if (viewModel.mapUri != null) {
        viewModel.setTileStreamProvider(
            OpenSeaTileStreamProvider(LocalContext.current, viewModel.mapUri!!, repository)
        )
        MapUI(state = viewModel.state)
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
