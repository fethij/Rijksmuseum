package com.tewelde.rijksmuseum.core.domain

import com.tewelde.rijksmuseum.core.common.Result
import com.tewelde.rijksmuseum.core.data.ArtRepository
import com.tewelde.rijksmuseum.core.model.Art

/**
 * Use case which gets [Art]s
 */
class GetArtsUseCase(
    private val artRepository: ArtRepository
) {
    suspend operator fun invoke(page: Int = 1): List<Art> =
        when (val arts = artRepository.getCollection(page)) {
            is Result.Success -> arts.data
            is Result.Error -> emptyList() /* Could be handled in viewModel as well */
        }
}