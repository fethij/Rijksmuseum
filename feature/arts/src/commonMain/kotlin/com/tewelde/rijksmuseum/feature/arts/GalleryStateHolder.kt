package com.tewelde.rijksmuseum.feature.arts

import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.model.Art
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(UiScope::class)
class GalleryStateHolder {
    var arts: List<Art> = emptyList()
        private set

    var nextPageToken: String? = null
        private set

    var isLoadingMore: Boolean = false

    val hasMore: Boolean get() = nextPageToken != null

    fun setInitialPage(arts: List<Art>, nextPageToken: String?) {
        this.arts = arts
        this.nextPageToken = nextPageToken
    }

    fun appendPage(newArts: List<Art>, nextPageToken: String?) {
        this.arts += newArts
        this.nextPageToken = nextPageToken
    }
}