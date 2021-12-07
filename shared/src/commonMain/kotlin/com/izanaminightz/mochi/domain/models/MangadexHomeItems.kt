// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json                 = Json(JsonConfiguration.Stable)
// val mangadexResultsModel = json.parse(MangadexResultsModel.serializer(), jsonString)

package com.izanaminightz.mochi.domain.models

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class MangadexResultsSingleModel (
    val data: Datum
)

@Serializable
data class MangadexResultsModel (
    val result: String? = null,
    val response: String? = null,
    val data: List<Datum>? = null,
    val limit: Long? = null,
    val offset: Long? = null,
    val total: Long? = null
)


@Serializable
data class Datum (
    val id: String? = null,
    val type: RelationshipType? = null,
    val attributes: DatumAttributes? = null,
    val relationships: List<Relationship>? = null
)

@Serializable
data class DatumAttributes (
    val title: Title? = null,
    val altTitles: List<AltTitle>? = null,
    val description: Description? = null,
//    val links: LinksUnion? = null,
//    val originalLanguage: OriginalLanguage? = null,
//    val lastVolume: String? = null,
//    val lastChapter: String? = null,
//    val publicationDemographic: String? = null,
//    val status: Status? = null,
//    val year: Long? = null,
//    val contentRating: ContentRating? = null,
    val tags: Tags? = null,
//    val state: State? = null,
//    val createdAt: String? = null,
//    val updatedAt: String? = null,
//    val version: Long? = null
) {
}

enum class MangadexLanguages(var language: String) {
    zh("Chinese"),
    ja("Japanese"),
    en("English"),
    esLa("Spanish"),
    ptBr("Brazilian Portuguese");

    fun toCode() : String = when(this) {
            en -> "en"
            ja -> "ja"
            zh -> "zh"
            esLa -> "es-la"
            ptBr -> "pt-br"
        }
}

@Serializable
data class AltTitle (
    val en: String? = null,
    val ja: String? = null,
    val zh: String? = null,
    val ko: String? = null,
    val bn: String? = null,

    @SerialName("zh-hk")
    val zhHk: String? = null
)

@Serializable
enum class ContentRating(val value: String) {
    Erotica("erotica"),
    Safe("safe"),
    Suggestive("suggestive");

    companion object : KSerializer<ContentRating> {
        override val descriptor: SerialDescriptor get() {
            return PrimitiveSerialDescriptor("com.izanaminightz.mochi.models.ContentRating", PrimitiveKind.STRING)
        }
        override fun deserialize(decoder: Decoder): ContentRating = when (val value = decoder.decodeString()) {
            "erotica"    -> Erotica
            "safe"       -> Safe
            "suggestive" -> Suggestive
            else         -> throw IllegalArgumentException("ContentRating could not parse: $value")
        }
        override fun serialize(encoder: Encoder, value: ContentRating) {
            return encoder.encodeString(value.value)
        }
    }
}



@Serializable(with = DescriptionSerializer::class)
sealed class Description



@Serializable(with = ArraySerializer::class)
class ArrayValue(val value: JsonArray) : Description()

//sealed class DescriptionUnion {
//    @Serializable
//     object ArrayValue: DescriptionUnion()
//    @Serializable
//    data class DescriptionClassValue(val value: DescriptionClass) : DescriptionUnion()
//}

@Serializable
data class DescriptionClass (
    val en: String? = null,
    val ja: String? = null,
    val fr: String? = null,
    val bn: String? = null
) : Description()

object DescriptionSerializer: JsonContentPolymorphicSerializer<Description>(Description::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Description> = when {
        element is JsonArray -> ArrayValue.serializer()
        element is JsonPrimitive -> ArrayValue.serializer()
        else -> DescriptionClass.serializer()
    }
}

object ArraySerializer: KSerializer<ArrayValue> {
    override fun deserialize(decoder: Decoder): ArrayValue {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        return ArrayValue(element.jsonArray)
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ArrayValue")

    override fun serialize(encoder: Encoder, value: ArrayValue) {
        TODO("Not yet implemented")
    }
}


@Serializable
sealed class LinksUnion {
    class AnythingArrayValue(val value: JsonArray) : LinksUnion()
    class LinksClassValue(val value: LinksClass)   : LinksUnion()
}

@Serializable
data class LinksClass (
    val bw: String? = null,
    val mu: String? = null,
    val amz: String? = null,
    val al: String? = null,
    val ap: String? = null,
    val mal: String? = null,
    val raw: String? = null,
    val engtl: String? = null,
    val kt: String? = null
)

@Serializable
enum class OriginalLanguage(val value: String) {
    Bn("bn"),
    Ja("ja"),
    Ko("ko");

    companion object : KSerializer<OriginalLanguage> {
        override val descriptor: SerialDescriptor get() {
            return PrimitiveSerialDescriptor("com.izanaminightz.mochi.models.OriginalLanguage", PrimitiveKind.STRING)
        }
        override fun deserialize(decoder: Decoder): OriginalLanguage = when (val value = decoder.decodeString()) {
            "bn" -> Bn
            "ja" -> Ja
            "ko" -> Ko
            else -> throw IllegalArgumentException("OriginalLanguage could not parse: $value")
        }
        override fun serialize(encoder: Encoder, value: OriginalLanguage) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
enum class State(val value: String) {
    Published("published");

    companion object : KSerializer<State> {
        override val descriptor: SerialDescriptor get() {
            return PrimitiveSerialDescriptor("com.izanaminightz.mochi.models.State", PrimitiveKind.STRING)
        }
        override fun deserialize(decoder: Decoder): State = when (val value = decoder.decodeString()) {
            "published" -> Published
            else        -> throw IllegalArgumentException("State could not parse: $value")
        }
        override fun serialize(encoder: Encoder, value: State) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
enum class Status(val value: String) {
    Completed("completed"),
    Ongoing("ongoing");

    companion object : KSerializer<Status> {
        override val descriptor: SerialDescriptor get() {
            return PrimitiveSerialDescriptor("com.izanaminightz.mochi.models.Status", PrimitiveKind.STRING)
        }
        override fun deserialize(decoder: Decoder): Status = when (val value = decoder.decodeString()) {
            "completed" -> Completed
            "ongoing"   -> Ongoing
            else        -> throw IllegalArgumentException("Status could not parse: $value")
        }
        override fun serialize(encoder: Encoder, value: Status) {
            return encoder.encodeString(value.value)
        }
    }
}

object TagsSerializer: JsonContentPolymorphicSerializer<Tags>(Tags::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Tags> = when {
        element is Map<*, *> -> TagMapValue.serializer()

        else -> {

            val j = Json {ignoreUnknownKeys = true} .decodeFromJsonElement<List<Tag>>(element)
             TagArrayValue.serializer()
        }

    }
}

//
//object TagsSerializerObject: KSerializer<Tag> {
//    override fun deserialize(decoder: Decoder): Tag {
//        require(decoder is JsonDecoder)
//        val element = decoder.decodeJsonElement()
//       return Json.decodeFromJsonElement(element)
//    }
//
//    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("TagsValue")
//
//    override fun serialize(encoder: Encoder, value: Tag) {
//        TODO("Not yet implemented")
//    }
//}

private val json: Json
    get() = Json { ignoreUnknownKeys = true }

object TagArraySerializerObject: KSerializer<TagArrayValue> {
    override fun deserialize(decoder: Decoder): TagArrayValue {
        require(decoder is JsonDecoder)

        val d =  json.decodeFromJsonElement<List<Tag>>(decoder.decodeJsonElement())

        return TagArrayValue(d)
    }


    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("TagsArray")

    override fun serialize(encoder: Encoder, value: TagArrayValue) {
        TODO("Not yet implemented")
    }

}





    @Serializable(with = TagsSerializer::class)
    sealed class Tags

    @Serializable
    data class TagMapValue(val value: Map<String, Tag>) : Tags()

    @Serializable(with = TagArraySerializerObject::class)
    data class TagArrayValue(val value: List<Tag>)      : Tags()





@Serializable
data class Tag (
    val id: String? = null,
    val type: TagType? = null,
    val attributes: TagAttributes? = null,
    val relationships: JsonArray? = null
) : Tags()

@Serializable
data class TagAttributes (
    val name: Name? = null,
//    val description: JsonArray? = null,
//    val group: Group? = null,
//    val version: Long? = null
)

@Serializable
enum class Group(val value: String) {
    Format("format"),
    Genre("genre"),
    Theme("theme");

    companion object : KSerializer<Group> {
        override val descriptor: SerialDescriptor get() {
            return PrimitiveSerialDescriptor("com.izanaminightz.mochi.models.Group", PrimitiveKind.STRING)
        }
        override fun deserialize(decoder: Decoder): Group = when (val value = decoder.decodeString()) {
            "format" -> Format
            "genre"  -> Genre
            "theme"  -> Theme
            else     -> throw IllegalArgumentException("Group could not parse: $value")
        }
        override fun serialize(encoder: Encoder, value: Group) {
            return encoder.encodeString(value.value)
        }
    }
}

@Serializable
data class Name (
    val en: String? = null
)

@Serializable
enum class TagType {
    @SerialName("tag")
    Tag
//    Tag("tag");
//
//    companion object : KSerializer<TagType> {
//        override val descriptor: SerialDescriptor get() {
//            return PrimitiveSerialDescriptor("com.izanaminightz.mochi.models.TagType", PrimitiveKind.STRING)
//        }
//        override fun deserialize(decoder: Decoder): TagType = when (val value = decoder.decodeString()) {
//            "tag" -> Tag
//            else  -> throw IllegalArgumentException("TagType could not parse: $value")
//        }
//        override fun serialize(encoder: Encoder, value: TagType) {
//            return encoder.encodeString(value.value)
//        }
//    }
}

@Serializable
data class Title (
    val en: String? = null,
    val ja: String? = null
)

@Serializable
data class Relationship (
    val id: String? = null,
    val type: RelationshipType? = null,
    val attributes: RelationshipAttributes? = null,
    val related: String? = null
)

@Serializable
data class RelationshipAttributes (
    val description: String? = null,
    val volume: String? = null,
    val fileName: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val version: Long? = null,
    val name: String? = null,
)

@Serializable
enum class RelationshipType {
    @SerialName("cover_art")
    Cover_Art,

    @SerialName("artist")
    Artist,

    @SerialName("author")
    Author,

    @SerialName("manga")
    Manga,

    @SerialName("user")
    User,

    @SerialName("scanlation_group")
    Scanlation
}

//@Serializable
//enum class RelationshipType(val value: String) {
//    Artist("artist"),
//    Author("author"),
//    CoverArt("cover_art"),
//    Manga("manga"),
//    User("user");
//
//    companion object : KSerializer<RelationshipType> {
//        override val descriptor: SerialDescriptor get() {
//            return buildClassSerialDescriptor("RelationshipType")
//        }
//        override fun deserialize(decoder: Decoder): RelationshipType = when (val value = decoder.decodeString()) {
//            "artist"    -> Artist
//            "author"    -> Author
//            "cover_art" -> CoverArt
//            "manga"     -> Manga
//            "user"      -> User
//            else        -> throw IllegalArgumentException("RelationshipType could not parse: $value")
//        }
//        override fun serialize(encoder: Encoder, value: RelationshipType) {
//            return encoder.encodeString(value.value)
//        }
//    }
//}
