package com.tewelde.rijksmuseum.core.domain

import com.tewelde.rijksmuseum.core.common.Result.Error
import com.tewelde.rijksmuseum.core.common.Result.Success
import com.tewelde.rijksmuseum.core.data.ArtRepository
import com.tewelde.rijksmuseum.core.model.ArtObject

/**
 * Use case to get art detail.
 */
class GetArtDetailUseCase(
    private val artRepository: ArtRepository
) {
    suspend operator fun invoke(objectId: String): ArtObject? =
        when (val art = artRepository.getArt(objectId)) {
            is Success -> art.data
            is Error -> null /* Could be handled in viewModel as well */
        }
}