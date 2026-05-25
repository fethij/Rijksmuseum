package com.tewelde.rijksmuseum.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Subset of the Rijksmuseum Linked Art JSON-LD vocabulary that we actually consume.
 *
 * Image resolution chain:
 *   HumanMadeObject → shows → VisualItem → digitally_shown_by → DigitalObject → access_point → IIIF URL
 */

@Serializable
data class LinkedArtIdentifier(
    @SerialName("type") val type: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("classified_as") val classifiedAs: List<LinkedArtRef> = emptyList(),
    @SerialName("language") val language: List<LinkedArtRef> = emptyList(),
)

@Serializable
data class LinkedArtReferredTo(
    @SerialName("type") val type: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("classified_as") val classifiedAs: List<LinkedArtRef> = emptyList(),
    @SerialName("language") val language: List<LinkedArtRef> = emptyList(),
)

@Serializable
data class LinkedArtProductionPart(
    @SerialName("type") val type: String? = null,
    @SerialName("referred_to_by") val referredToBy: List<LinkedArtReferredTo> = emptyList(),
)

@Serializable
data class LinkedArtProduction(
    @SerialName("type") val type: String? = null,
    @SerialName("part") val part: List<LinkedArtProductionPart> = emptyList(),
    @SerialName("referred_to_by") val referredToBy: List<LinkedArtReferredTo> = emptyList(),
)

@Serializable
data class LinkedArtSubjectOfPart(
    @SerialName("type") val type: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("classified_as") val classifiedAs: List<LinkedArtRef> = emptyList(),
    @SerialName("language") val language: List<LinkedArtRef> = emptyList(),
)

@Serializable
data class LinkedArtSubjectOf(
    @SerialName("type") val type: String? = null,
    @SerialName("part") val part: List<LinkedArtSubjectOfPart> = emptyList(),
    @SerialName("classified_as") val classifiedAs: List<LinkedArtRef> = emptyList(),
    @SerialName("language") val language: List<LinkedArtRef> = emptyList(),
)

@Serializable
data class LinkedArtHumanMadeObject(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String,
    @SerialName("identified_by") val identifiedBy: List<LinkedArtIdentifier> = emptyList(),
    @SerialName("produced_by") val producedBy: LinkedArtProduction? = null,
    @SerialName("shows") val shows: List<LinkedArtRef> = emptyList(),
    @SerialName("subject_of") val subjectOf: List<LinkedArtSubjectOf> = emptyList(),
)

@Serializable
data class LinkedArtVisualItem(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String,
    @SerialName("digitally_shown_by") val digitallyShownBy: List<LinkedArtRef> = emptyList(),
)

@Serializable
data class LinkedArtDigitalObject(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String,
    @SerialName("access_point") val accessPoint: List<LinkedArtRef> = emptyList(),
)

/** Well-known Getty AAT identifiers used to classify metadata. */
object LinkedArtConstants {
    const val AAT_OBJECT_NUMBER = "http://vocab.getty.edu/aat/300312355"
    const val AAT_PRIMARY_NAME = "http://vocab.getty.edu/aat/300417200"
    /** "creator description" — verbose maker statement like "painter: Rembrandt van Rijn, Amsterdam". */
    const val AAT_CREATOR_STATEMENT = "http://vocab.getty.edu/aat/300435416"
    /** "attribution" — short maker name like "Rembrandt van Rijn". Preferred over the statement. */
    const val AAT_CREATOR_NAME = "http://vocab.getty.edu/aat/300435417"
    const val AAT_DESCRIPTION = "http://vocab.getty.edu/aat/300048722"
    const val AAT_ENGLISH = "http://vocab.getty.edu/aat/300388277"
    const val AAT_DUTCH = "http://vocab.getty.edu/aat/300388256"
}
