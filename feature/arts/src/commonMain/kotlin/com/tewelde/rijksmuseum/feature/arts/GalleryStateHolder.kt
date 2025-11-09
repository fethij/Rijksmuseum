package com.tewelde.rijksmuseum.feature.arts

import com.tewelde.rijksmuseum.core.model.Art
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class GalleryStateHolder(var arts: List<Art> = emptyList())