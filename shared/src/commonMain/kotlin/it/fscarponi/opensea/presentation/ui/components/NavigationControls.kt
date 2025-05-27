package it.fscarponi.opensea.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Navigation control buttons for the map screen.
 *
 * @param isFollowingLocation Whether the map is currently following the user's location.
 * @param onFollowLocationToggle Callback for when the follow location button is clicked.
 * @param onDropMarker Callback for when the drop marker button is clicked.
 * @param modifier The modifier to be applied to the component.
 */
@Composable
fun NavigationControls(
    isFollowingLocation: Boolean,
    onFollowLocationToggle: () -> Unit,
    onDropMarker: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Follow location toggle button
        FloatingActionButton(
            onClick = onFollowLocationToggle,
            backgroundColor = if (isFollowingLocation) MaterialTheme.colors.primary else MaterialTheme.colors.surface
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Follow Location",
                tint = if (isFollowingLocation) Color.White else MaterialTheme.colors.onSurface
            )
        }
        
        // Drop marker button
        FloatingActionButton(
            onClick = onDropMarker,
            backgroundColor = MaterialTheme.colors.secondary
        ) {
            Icon(
                imageVector = Icons.Default.AddLocation,
                contentDescription = "Drop Marker",
                tint = Color.White
            )
        }
    }
}