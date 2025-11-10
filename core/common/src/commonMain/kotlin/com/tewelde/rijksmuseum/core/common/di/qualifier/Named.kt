package com.tewelde.rijksmuseum.core.common.di.qualifier

import com.tewelde.rijksmuseum.core.common.di.RijksmuseumDispatchers
import dev.zacsweers.metro.Qualifier

@Qualifier
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE
)
annotation class Named(val value: RijksmuseumDispatchers)
