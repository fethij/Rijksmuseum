package com.tewelde.rijksmuseum.core.domain

import arrow.core.Either
import com.tewelde.rijksmuseum.core.common.ApiResponse
import com.tewelde.rijksmuseum.core.data.ArtRepository
import com.tewelde.rijksmuseum.core.model.ArtObject

/**
 * Use case to get art detail.
 */
class GetArtDetailUseCase(
    private val artRepository: ArtRepository
) {
    suspend operator fun invoke(objectId: String): Either<String, ArtObject> =
        when (val art = artRepository.getArt(objectId)) {
            is Either.Left -> {
                when (art.value) {
                    is ApiResponse.IOException -> Either.Left("Network unavailable")
                    is ApiResponse.HttpError -> Either.Left("Error getting art, try again later")
                }
            }

            is Either.Right -> Either.Right(art.value)
        }
}