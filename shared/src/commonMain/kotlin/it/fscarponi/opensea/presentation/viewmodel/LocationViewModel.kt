package it.fscarponi.opensea.presentation.viewmodel

import it.fscarponi.opensea.domain.model.Result
import it.fscarponi.opensea.domain.usecase.GetCurrentLocationUseCase
import it.fscarponi.opensea.presentation.base.BaseViewModel
import it.fscarponi.opensea.presentation.model.DataUiState
import kotlinx.coroutines.launch

/**
 * ViewModel for location-related functionality.
 */
class LocationViewModel(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : BaseViewModel<DataUiState<Pair<Double, Double>>>(DataUiState.loading()) {

    /**
     * Gets the current location.
     */
    fun getCurrentLocation() {
        updateState { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                when (val result = getCurrentLocationUseCase()) {
                    is Result.Success -> {
                        updateState { DataUiState.success(result.data) }
                    }
                    is Result.Error -> {
                        updateState { DataUiState.error(result.message) }
                    }
                    is Result.Loading -> {
                        // Already handled by initial state update
                    }
                }
            } catch (e: Exception) {
                updateState { DataUiState.error(e.message ?: "Unknown error") }
            }
        }
    }
}
