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
 * Extension function to convert a Flow<T> to a Flow<Result<T>>
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }.catch { emit(Result.Error(it)) }
}
