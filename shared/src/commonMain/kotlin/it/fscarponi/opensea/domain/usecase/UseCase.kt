package it.fscarponi.opensea.domain.usecase

import it.fscarponi.opensea.domain.model.Result

/**
 * Base class for all use cases in the application.
 * Use cases represent the business logic of the application.
 *
 * @param P The type of parameters the use case accepts.
 * @param R The type of result the use case produces.
 */
abstract class UseCase<in P, R> {
    /**
     * Executes the use case with the given parameters.
     *
     * @param params The parameters for the use case.
     * @return A Result containing the result of the use case or an error.
     */
    abstract suspend operator fun invoke(params: P): Result<R>
}

/**
 * Base class for use cases that don't require parameters.
 *
 * @param R The type of result the use case produces.
 */
abstract class NoParamUseCase<R> : UseCase<Unit, R>() {
    /**
     * Executes the use case without parameters.
     *
     * @return A Result containing the result of the use case or an error.
     */
    abstract suspend operator fun invoke(): Result<R>

    /**
     * Implementation of the parent invoke method.
     * Delegates to the parameterless invoke method.
     */
    final override suspend operator fun invoke(params: Unit): Result<R> = invoke()
}