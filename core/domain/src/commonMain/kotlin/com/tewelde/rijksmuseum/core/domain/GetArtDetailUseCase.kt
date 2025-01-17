package com.tewelde.rijksmuseum.core.domain

import com.tewelde.rijksmuseum.core.common.ApiResponse
import com.tewelde.rijksmuseum.core.common.Either
import com.tewelde.rijksmuseum.core.data.ArtRepository
import com.tewelde.rijksmuseum.core.model.ArtObject
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

/**
 * Use case to get art detail.
 */
@Inject
@ContributesBinding(AppScope::class)
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