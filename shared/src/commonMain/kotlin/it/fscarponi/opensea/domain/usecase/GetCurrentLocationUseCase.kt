package it.fscarponi.opensea.domain.usecase

import it.fscarponi.opensea.domain.model.Result
import it.fscarponi.opensea.domain.repository.LocationRepository

/**
 * Use case for getting the current location.
 */
class GetCurrentLocationUseCase(
    private val locationRepository: LocationRepository
) : NoParamUseCase<Pair<Double, Double>>() {
    
    /**
     * Gets the current location.
     *
     * @return A Result containing the current location coordinates (latitude, longitude) or an error.
     */
    override suspend fun invoke(): Result<Pair<Double, Double>> {
        return locationRepository.getCurrentLocation()
    }
}