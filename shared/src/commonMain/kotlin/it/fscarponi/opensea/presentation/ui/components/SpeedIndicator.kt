package it.fscarponi.opensea.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A reusable speed indicator component that displays the current speed in knots.
 *
 * @param speed The current speed in knots.
 * @param modifier The modifier to be applied to the component.
 */
@Composable
fun SpeedIndicator(
    speed: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${speed.roundToOneDecimal()}",
                style = MaterialTheme.typography.h4
            )
            Text(
                text = "knots",
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}

/**
 * Rounds a double to one decimal place and returns it as a string.
 */
fun Double.roundToOneDecimal(): String {
    return (kotlin.math.round(this * 10) / 10).toString()
}
