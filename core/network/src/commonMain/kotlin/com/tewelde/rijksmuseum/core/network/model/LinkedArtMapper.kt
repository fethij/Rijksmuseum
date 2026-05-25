package com.tewelde.rijksmuseum.core.network.model

import com.tewelde.rijksmuseum.core.model.Art
import com.tewelde.rijksmuseum.core.model.ArtObject
import com.tewelde.rijksmuseum.core.network.model.LinkedArtConstants.AAT_CREATOR_NAME
import com.tewelde.rijksmuseum.core.network.model.LinkedArtConstants.AAT_CREATOR_STATEMENT
import com.tewelde.rijksmuseum.core.network.model.LinkedArtConstants.AAT_DESCRIPTION
import com.tewelde.rijksmuseum.core.network.model.LinkedArtConstants.AAT_ENGLISH
import com.tewelde.rijksmuseum.core.network.model.LinkedArtConstants.AAT_OBJECT_NUMBER
import com.tewelde.rijksmuseum.core.network.model.LinkedArtConstants.AAT_PRIMARY_NAME

/** Maps Linked Art JSON-LD payloads to domain models. */

private fun List<LinkedArtRef>.hasId(id: String): Boolean = any { it.id == id }

private fun List<LinkedArtRef>.isEnglish(): Boolean = hasId(AAT_ENGLISH)

fun LinkedArtHumanMadeObject.extractObjectNumber(): String =
    identifiedBy
        .firstOrNull { it.type == "Identifier" && it.classifiedAs.hasId(AAT_OBJECT_NUMBER) }
        ?.content
        ?: id.substringAfterLast("/")

fun LinkedArtHumanMadeObject.extractTitle(): String {
    val primaryNames = identifiedBy.filter {
        it.type == "Name" && it.classifiedAs.hasId(AAT_PRIMARY_NAME)
    }
    return primaryNames.firstOrNull { it.language.isEnglish() }?.content
        ?: primaryNames.firstOrNull()?.content
        ?: identifiedBy.firstOrNull { it.type == "Name" }?.content
        ?: "Untitled"
}

fun LinkedArtHumanMadeObject.extractMaker(): String {
    val production = producedBy ?: return "Unknown"
    val refs = production.part.flatMap { it.referredToBy } + production.referredToBy

    fun pick(classification: String, englishOnly: Boolean): String? =
        refs.firstOrNull { ref ->
            ref.classifiedAs.hasId(classification) &&
                (!englishOnly || ref.language.isEnglish())
        }?.content

    // Prefer the short "attribution" form (e.g. "Rembrandt van Rijn"),
    // English first; fall back to any language, then to the verbose creator statement.
    return pick(AAT_CREATOR_NAME, englishOnly = true)
        ?: pick(AAT_CREATOR_NAME, englishOnly = false)
        ?: pick(AAT_CREATOR_STATEMENT, englishOnly = true)
        ?: pick(AAT_CREATOR_STATEMENT, englishOnly = false)
        ?: "Unknown"
}

fun LinkedArtHumanMadeObject.extractDescription(): String? {
    val descriptions = subjectOf.flatMap { subject ->
        subject.part.map { part -> subject to part }
    }.filter { (_, part) ->
        part.content != null && part.classifiedAs.hasId(AAT_DESCRIPTION)
    }
    return descriptions.firstOrNull { (subject, part) ->
        subject.language.isEnglish() || part.language.isEnglish()
    }?.second?.content
        ?: descriptions.firstOrNull()?.second?.content
}

fun LinkedArtHumanMadeObject.extractVisualItemId(): String? =
    shows.firstOrNull { it.type == "VisualItem" }?.id

fun LinkedArtHumanMadeObject.asArt(imageUrl: String): Art =
    Art(
        objectNumber = extractObjectNumber(),
        title = extractTitle(),
        imageUrl = imageUrl,
        maker = extractMaker(),
    )

fun LinkedArtHumanMadeObject.asArtObject(imageUrl: String): ArtObject =
    ArtObject(
        objectNumber = extractObjectNumber(),
        title = extractTitle(),
        description = extractDescription(),
        imageUrl = imageUrl,
        principalMaker = extractMaker(),
    )
