package com.tewelde.rijksmuseum.feature.arts

import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.model.Art
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@Inject
@SingleIn(UiScope::class)
class GalleryStateHolder(var arts: List<Art> = emptyList())