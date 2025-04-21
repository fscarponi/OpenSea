package it.fscarponi.opensea.presentation.navigation

/**
 * Interface for navigation between screens.
 * This will be implemented by platform-specific navigation handlers.
 */
interface Navigator {
    /**
     * Navigates to the given screen.
     *
     * @param screen The screen to navigate to.
     */
    fun navigateTo(screen: Screen)
    
    /**
     * Navigates back to the previous screen.
     *
     * @return True if navigation was successful, false otherwise (e.g., if there is no previous screen).
     */
    fun navigateBack(): Boolean
    
    /**
     * Navigates to the home screen (Map).
     */
    fun navigateToHome() {
        navigateTo(Screen.Map)
    }
}