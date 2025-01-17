package com.tewelde.rijksmuseum.core.domain

import com.tewelde.rijksmuseum.core.common.ApiResponse
import com.tewelde.rijksmuseum.core.common.Either
import com.tewelde.rijksmuseum.core.data.ArtRepository
import com.tewelde.rijksmuseum.core.model.Art
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

/**
 * Use case which gets [Art]s
 */
@Inject
@ContributesBinding(AppScope::class)
class GetArtsUseCase(
    private val artRepository: ArtRepository
) {
    suspend operator fun invoke(page: Int = 1): Either<String, List<Art>> =
        when (val arts = artRepository.getCollection(page)) {
            is Either.Left -> {
                when (arts.value) {
                    is ApiResponse.IOException -> Either.Left("Network unavailable")
                    is ApiResponse.HttpError -> Either.Left("Error getting arts, try again later")
                }
            }

            is Either.Right -> Either.Right(arts.value)
        }
}