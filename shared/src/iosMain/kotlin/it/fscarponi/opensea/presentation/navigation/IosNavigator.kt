package it.fscarponi.opensea.presentation.navigation

import it.fscarponi.opensea.util.logDebug

/**
 * iOS implementation of the Navigator interface.
 * This class will be used to navigate between screens in the iOS app.
 * 
 * Note: This is a placeholder implementation. In a real app, this would use SwiftUI navigation
 * or another navigation library.
 */
class IosNavigator : Navigator {
    private val navigationStack = mutableListOf<Screen>()
    
    init {
        // Start with the Map screen
        navigationStack.add(Screen.Map)
    }
    
    override fun navigateTo(screen: Screen) {
        logDebug("Navigating to ${screen.route}")
        navigationStack.add(screen)
        // In a real app, this would use SwiftUI navigation
    }
    
    override fun navigateBack(): Boolean {
        if (navigationStack.size <= 1) {
            logDebug("Cannot navigate back, already at the root screen")
            return false
        }
        
        navigationStack.removeAt(navigationStack.size - 1)
        logDebug("Navigated back to ${navigationStack.last().route}")
        // In a real app, this would use SwiftUI navigation
        return true
    }
    
    companion object {
        /**
         * Initializes the iOS navigator.
         * This should be called from the SwiftUI App struct.
         */
        fun create(): IosNavigator {
            return IosNavigator()
        }
    }
}