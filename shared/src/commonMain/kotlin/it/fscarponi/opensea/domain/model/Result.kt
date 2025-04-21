package it.fscarponi.opensea.domain.model

/**
 * A generic class that holds a value or an error.
 * Used to represent the outcome of domain operations.
 */
sealed class Result<out T> {
    /**
     * Represents a successful operation with the resulting data.
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents a failed operation with an error message and optional exception.
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : Result<Nothing>()

    /**
     * Represents an operation in progress.
     */
    object Loading : Result<Nothing>()

    /**
     * Checks if this result is a success.
     */
    val isSuccess: Boolean get() = this is Success

    /**
     * Checks if this result is an error.
     */
    val isError: Boolean get() = this is Error

    /**
     * Checks if this result is loading.
     */
    val isLoading: Boolean get() = this is Loading

    /**
     * Gets the data if this is a success, or null otherwise.
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    /**
     * Gets the error message if this is an error, or null otherwise.
     */
    fun errorOrNull(): String? = when (this) {
        is Error -> message
        else -> null
    }
}