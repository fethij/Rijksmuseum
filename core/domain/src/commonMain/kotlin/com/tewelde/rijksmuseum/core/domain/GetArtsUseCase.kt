package com.tewelde.rijksmuseum.core.domain

import com.tewelde.rijksmuseum.core.common.Either
import com.tewelde.rijksmuseum.core.data.ArtRepository
import com.tewelde.rijksmuseum.core.data.CollectionPage
import me.tatarka.inject.annotations.Inject

/**
 * Use case which gets a page of arts from the collection.
 */
@Inject
class GetArtsUseCase(
    private val artRepository: ArtRepository
) {
    suspend operator fun invoke(
        pageToken: String? = null
    ): Either<String, CollectionPage> =
        when (val result = artRepository.getCollection(pageToken)) {
            is Either.Left -> when (result.value) {
                is com.tewelde.rijksmuseum.core.common.ApiResponse.IOException ->
                    Either.Left("Network unavailable")
                is com.tewelde.rijksmuseum.core.common.ApiResponse.HttpError ->
                    Either.Left("Error getting arts, try again later")
            }
            is Either.Right -> Either.Right(result.value)
        }
}