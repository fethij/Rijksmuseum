package com.tewelde.rijksmuseum.core.network.di.qualifier

import dev.zacsweers.metro.Qualifier

@Qualifier
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE
)
annotation class Named(val value: RijksmuseumClients)

enum class RijksmuseumClients {
    AUTHORIZED,
    UNAUTHORIZED
}