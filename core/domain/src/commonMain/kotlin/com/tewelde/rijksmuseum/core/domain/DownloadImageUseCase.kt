package com.tewelde.rijksmuseum.core.domain

import com.tewelde.rijksmuseum.core.data.ArtRepository
import io.ktor.utils.io.ByteReadChannel
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

/**
 * Use case to download an image.
 */
@Inject
@ContributesBinding(AppScope::class)
class DownloadImageUseCase(
    private val artRepository: ArtRepository
) {
    suspend operator fun invoke(
        url: String,
        onDownload: (Long, Long?) -> Unit
    ): ByteReadChannel = artRepository.downloadImage(url, onDownload)
}