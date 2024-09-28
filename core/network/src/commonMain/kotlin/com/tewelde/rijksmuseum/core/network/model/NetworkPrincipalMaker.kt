package com.tewelde.rijksmuseum.core.network.model

import com.tewelde.rijksmuseum.core.model.Maker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPrincipalMaker(
    @SerialName("name") val name: String?,
    @SerialName("unFixedName") val unFixedName: String?,
    @SerialName("placeOfBirth") val placeOfBirth: String?,
    @SerialName("dateOfBirth") val dateOfBirth: String?,
    @SerialName("dateOfBirthPrecision") val dateOfBirthPrecision: String?,
    @SerialName("dateOfDeath") val dateOfDeath: String?,
    @SerialName("dateOfDeathPrecision") val dateOfDeathPrecision: String?,
    @SerialName("placeOfDeath") val placeOfDeath: String?,
    @SerialName("occupation") val occupation: List<String>?,
    @SerialName("roles") val roles: List<String>?,
    @SerialName("nationality") val nationality: String?,
    @SerialName("biography") val biography: String?,
    @SerialName("productionPlaces") val productionPlaces: List<String>?,
    @SerialName("qualification") val qualification: String?,
    @SerialName("labelDesc") val labelDesc: String?
)

fun NetworkPrincipalMaker.asMaker(): Maker = Maker(
    name = name,
    placeOfBirth = placeOfBirth,
    dateOfBirth = dateOfBirth,
    dateOfDeath = dateOfDeath,
    placeOfDeath = placeOfDeath
)