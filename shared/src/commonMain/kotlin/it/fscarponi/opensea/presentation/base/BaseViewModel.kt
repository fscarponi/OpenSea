package it.fscarponi.opensea.presentation.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Base ViewModel class for the MVVM architecture.
 * Provides common functionality for all ViewModels in the application.
 *
 * @param S The type of UI state managed by this ViewModel
 */
abstract class BaseViewModel<S : Any>(initialState: S) {
    
    // Coroutine scope for launching coroutines from the ViewModel
    protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    // Internal mutable state flow that holds the current UI state
    private val _state = MutableStateFlow(initialState)
    
    // Public immutable state flow exposed to the UI
    val state: StateFlow<S> = _state.asStateFlow()
    
    /**
     * Updates the current state using the provided transform function.
     *
     * @param transform A function that takes the current state and returns a new state
     */
    protected fun updateState(transform: (S) -> S) {
        _state.update(transform)
    }
    
    /**
     * Cleans up resources when the ViewModel is no longer used.
     * Should be called when the associated UI component is destroyed.
     */
    fun clear() {
        viewModelScope.cancel()
        onCleared()
    }
    
    /**
     * Called when the ViewModel is being cleared.
     * Override this to perform any cleanup operations.
     */
    protected open fun onCleared() {
        // Override in subclasses if needed
    }
}