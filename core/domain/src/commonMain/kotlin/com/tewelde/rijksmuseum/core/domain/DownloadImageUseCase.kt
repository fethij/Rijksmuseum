package com.tewelde.rijksmuseum.core.domain

import com.tewelde.rijksmuseum.core.data.ArtRepository
import dev.zacsweers.metro.Inject
import io.ktor.utils.io.ByteReadChannel

/**
 * Use case to download an image.
 */
@Inject
class DownloadImageUseCase(
    private val artRepository: ArtRepository
) {
    suspend operator fun invoke(
        url: String,
        onDownload: (Long, Long?) -> Unit
    ): ByteReadChannel = artRepository.downloadImage(url, onDownload)
}