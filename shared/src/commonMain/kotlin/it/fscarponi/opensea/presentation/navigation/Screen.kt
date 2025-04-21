package it.fscarponi.opensea.presentation.navigation

/**
 * Represents a screen in the application.
 * Used for navigation between screens.
 */
sealed class Screen(val route: String) {
    /**
     * The map screen.
     */
    object Map : Screen("map")
    
    /**
     * The settings screen.
     */
    object Settings : Screen("settings")
    
    companion object {
        /**
         * Gets a screen by its route.
         *
         * @param route The route of the screen.
         * @return The screen with the given route, or null if no such screen exists.
         */
        fun fromRoute(route: String): Screen? {
            return when (route) {
                Map.route -> Map
                Settings.route -> Settings
                else -> null
            }
        }
    }
}