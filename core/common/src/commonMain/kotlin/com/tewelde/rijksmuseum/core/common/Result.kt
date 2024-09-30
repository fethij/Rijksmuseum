package com.tewelde.rijksmuseum.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * A generic class that cal hold a value
 * @param <T>
 */
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
}

/**
 * A generic class to represent different types of data.
 * [Either.Left] Represents the left side of [Either] class which by convention is a "Failure".
 * [Either.Right] Represents the right side of [Either] class which by convention is a "Success".
 * @param A The type of the left side.
 * @param B The type of the right side.
 */
sealed class Either<out A, out B>{
    data class Left<out A>(val value: A): Either<A, Nothing>()
    data class Right<out B>(val value: B): Either<Nothing, B>()
}

/**
 * Sealed class to represent the different types of API responses.
 * [ApiResponse.IOException] Represents an IO exception.
 * [ApiResponse.HttpError] Represents an HTTP error.
 */
sealed interface ApiResponse {
    data object HttpError : ApiResponse
    data object IOException : ApiResponse
}

/**
 * Extension function to convert a Flow<T> to a Flow<Result<T>>
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }.catch { emit(Result.Error(it)) }
}