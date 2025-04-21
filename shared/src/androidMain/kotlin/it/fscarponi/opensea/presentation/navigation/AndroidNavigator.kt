package it.fscarponi.opensea.presentation.navigation

import it.fscarponi.opensea.util.logDebug

/**
 * Android implementation of the Navigator interface.
 * This class will be used to navigate between screens in the Android app.
 * 
 * Note: This is a placeholder implementation. In a real app, this would use the Android Navigation component
 * or another navigation library.
 */
class AndroidNavigator : Navigator {
    private val navigationStack = mutableListOf<Screen>()
    
    init {
        // Start with the Map screen
        navigationStack.add(Screen.Map)
    }
    
    override fun navigateTo(screen: Screen) {
        logDebug("Navigating to ${screen.route}")
        navigationStack.add(screen)
        // In a real app, this would use the Android Navigation component
    }
    
    override fun navigateBack(): Boolean {
        if (navigationStack.size <= 1) {
            logDebug("Cannot navigate back, already at the root screen")
            return false
        }
        
        navigationStack.removeAt(navigationStack.size - 1)
        logDebug("Navigated back to ${navigationStack.last().route}")
        // In a real app, this would use the Android Navigation component
        return true
    }
    
    companion object {
        /**
         * Initializes the Android navigator.
         * This should be called from the Application class.
         */
        fun create(): AndroidNavigator {
            return AndroidNavigator()
        }
    }
}