package com.tewelde.rijksmuseum.core.network.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * A reference-like JSON-LD value that the Rijksmuseum API serializes inconsistently as
 * either a bare URL string or a full object with `id`, `type`, `_label`. We accept either.
 */
@Serializable(with = LinkedArtRefSerializer::class)
data class LinkedArtRef(
    val id: String? = null,
    val type: String? = null,
    val label: String? = null,
)

object LinkedArtRefSerializer : KSerializer<LinkedArtRef> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("LinkedArtRef") {
        element<String?>("id")
        element<String?>("type")
        element<String?>("_label")
    }

    override fun deserialize(decoder: Decoder): LinkedArtRef {
        val input = decoder as? JsonDecoder
            ?: throw SerializationException("LinkedArtRef requires JSON")
        return when (val element = input.decodeJsonElement()) {
            is JsonPrimitive -> LinkedArtRef(id = element.content)
            is JsonObject -> LinkedArtRef(
                id = element["id"]?.jsonPrimitive?.content,
                type = element["type"]?.jsonPrimitive?.content,
                label = element["_label"]?.jsonPrimitive?.content,
            )
            else -> throw SerializationException("Unexpected JSON element for LinkedArtRef: $element")
        }
    }

    override fun serialize(encoder: Encoder, value: LinkedArtRef) {
        val output = encoder as? JsonEncoder
            ?: throw SerializationException("LinkedArtRef requires JSON")
        val element = if (value.type == null && value.label == null && value.id != null) {
            JsonPrimitive(value.id)
        } else {
            buildJsonObject {
                value.id?.let { put("id", it) }
                value.type?.let { put("type", it) }
                value.label?.let { put("_label", it) }
            }
        }
        output.encodeJsonElement(element)
    }
}
