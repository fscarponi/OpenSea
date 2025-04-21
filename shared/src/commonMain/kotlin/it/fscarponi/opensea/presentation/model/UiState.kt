package it.fscarponi.opensea.presentation.model

/**
 * Base class for UI states in the MVVM architecture.
 * UI states represent the current state of the UI and are managed by ViewModels.
 */
interface UiState

/**
 * Represents a UI state with loading, error, and data states.
 *
 * @param T The type of data in the UI state.
 */
data class DataUiState<T>(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: T? = null
) : UiState {
    /**
     * Checks if this state has data.
     */
    val hasData: Boolean get() = data != null

    /**
     * Checks if this state has an error.
     */
    val hasError: Boolean get() = error != null

    companion object {
        /**
         * Creates a loading state.
         */
        fun <T> loading(): DataUiState<T> = DataUiState(isLoading = true)

        /**
         * Creates a success state with the given data.
         */
        fun <T> success(data: T): DataUiState<T> = DataUiState(data = data)

        /**
         * Creates an error state with the given error message.
         */
        fun <T> error(message: String): DataUiState<T> = DataUiState(error = message)
    }
}