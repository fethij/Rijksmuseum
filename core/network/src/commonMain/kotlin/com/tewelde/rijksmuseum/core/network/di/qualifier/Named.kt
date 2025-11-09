package com.tewelde.rijksmuseum.core.network.di.qualifier

import me.tatarka.inject.annotations.Qualifier

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