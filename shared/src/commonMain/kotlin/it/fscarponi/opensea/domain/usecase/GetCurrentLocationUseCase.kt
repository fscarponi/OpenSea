package it.fscarponi.opensea.domain.usecase

import it.fscarponi.opensea.domain.location.model.Location
import it.fscarponi.opensea.domain.model.Result
import it.fscarponi.opensea.domain.repository.LocationRepository

/**
 * Use case for getting the current location.
 */
class GetCurrentLocationUseCase(
    private val locationRepository: LocationRepository
) : NoParamUseCase<Location>() {

    /**
     * Gets the current location.
     *
     * @return A Result containing the current location or an error.
     */
    override suspend fun invoke(): Result<Location> {
        return locationRepository.getCurrentLocation()
    }
}
