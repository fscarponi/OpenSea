package it.fscarponi.opensea.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.fscarponi.opensea.presentation.ui.components.NavigationControls
import it.fscarponi.opensea.presentation.ui.components.SpeedIndicator
import it.fscarponi.opensea.presentation.viewmodel.NavigationViewModel

/**
 * The main navigation screen of the application.
 * Displays the map, speed indicator, and navigation controls.
 *
 * @param viewModel The view model for the navigation screen.
 * @param onMapReady Callback for when the map is ready.
 */
@Composable
fun NavigationScreen(
    viewModel: NavigationViewModel,
    onMapReady: () -> Unit = {}
) {
    val uiState by viewModel.state.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Map component would be implemented here
        // This is a placeholder for the actual map implementation
        // which would be platform-specific
        
        // Speed indicator
        SpeedIndicator(
            speed = uiState.speed,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
        
        // Control buttons
        NavigationControls(
            isFollowingLocation = uiState.isFollowingLocation,
            onFollowLocationToggle = { viewModel.toggleFollowLocation() },
            onDropMarker = { viewModel.dropMarker() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}