package com.izanaminightz.mochi.data

import com.izanaminightz.mochi.domain.models.*
import com.izanaminightz.mochi.domain.repository.MochiRepository
import com.izanaminightz.mochi.remote.MangadexApi
import com.izanaminightz.mochi.utils.Endpoints
import com.izanaminightz.mochi.utils.MochiHelper
import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

@Serializable
private data class ClientModel(
    var username: String? = null,
    var email: String? = null,
    val password: String,
)

@Serializable
private data class RefreshModel(
    val token: String
)

class MochiRepositoryImpl(
    private val token: UserTokens? = null
) : MochiRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private val client = HttpClient {
        expectSuccess = false

        install(JsonFeature) {
            val json = Json {
                ignoreUnknownKeys = true;
                coerceInputValues = true
            }
            serializer = KotlinxSerializer(json)

        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

    }.apply {
        requestPipeline.intercept(HttpRequestPipeline.State) {
            if (context.headers.contains("Authorization")) {
                //Refresh function
               if (needsRefreshing()) {
                   println("Refreshing token!!!!")
                    refreshToken(token!!)?.let {
                        context.headers["Authorization"] = "Bearer ${it.token!!.session}"
                        MochiHelper().storeTokens(it)
                    }

               }
            }
        }
    }



    private fun needsRefreshing() : Boolean {
        val time = MochiHelper().getTokens().expiresAt
        val currentClock = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        return (currentClock > time)
    }


    override suspend fun fetchMangadexRecentList(): Flow<MangadexResultsModel> {
        val prefLang = MochiHelper().preferredLanguage().toCode()

        val data =
            client.get<String>("${Endpoints.baseURL}/manga?order[updatedAt]=desc&limit=9&contentRating[]=suggestive&includes[]=cover_art&availableTranslatedLanguage[]=$prefLang")

        val jsonData = json.decodeFromString<MangadexResultsModel>(data)
        return flow { emit(jsonData) }
    }

    override suspend fun fetchDetail(id: String): Flow<MangadexResultsSingleModel> = flow {
        val data = client.get<String>("${Endpoints.baseURL}/manga/$id?includes[]=cover_art&includes[]=author")
        emit(json.decodeFromString(data))
    }

    override suspend fun fetchFeed(id: String): Flow<MangadexMangaFeed> = flow {
        val prefLang = MochiHelper().preferredLanguage().toCode()
    val data =
        client.get<String>("${Endpoints.baseURL}/manga/$id/feed?limit=100&order[chapter]=asc&translatedLanguage[]=$prefLang&includes[]=scanlation_group")
    emit ( json.decodeFromString(data ) )
    }

    override suspend fun searchResultsFor(query: String) : Flow<MangadexResultsModel> = flow {
        val data = client.get<String>("${Endpoints.baseURL}/manga?title=$query&order[relevance]=desc&includes[]=cover_art&limit=25")
        emit (json.decodeFromString(data))
    }

    override suspend fun fetchMangaChapterHomeURL(chapterID: String): Flow<String> = flow {
        val data = client.get<MangadexAtHome>("${Endpoints.baseURL}/at-home/server/$chapterID")
        emit ( data.baseUrl )
    }

    override suspend fun fetchUserStatusList(): Flow<List<Datum>> = flow {
        val token = MochiHelper().getTokens()
        val response = client.get<String>("${Endpoints.baseURL}/manga/status") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.session}")
            }
        }

        val json = Json.decodeFromString<UserListModel>(response)
        emit(convertIDToMangaData(json.statuses.keys.toList()))

    }


    private suspend fun convertIDToMangaData(ids: List<String>) : List<Datum>  {

        val response = client.get<JsonObject>("${Endpoints.baseURL}/manga?includes[]=cover_art&includes[]=artist&includes[]=author${ids.joinToString(separator = "") { "&ids[]=$it" }}&limit=${ids.size}")
        val localJson = JsonObject(response)
        val array =  localJson.jsonObject["data"]!!.jsonArray
        return json.decodeFromJsonElement(array)
    }

    //Login

    override suspend fun login(userPreferred: String, password: String): AuthModel {
        var user = ClientModel(
            password = password
        )

        if (userPreferred.contains(Regex("(@)|(.com)")))
            user.email = userPreferred
        else user.username = userPreferred

        return client.post<AuthModel>(
            urlString = "${Endpoints.baseURL}/auth/login",
        ) {
            body = user
            contentType(ContentType.Application.Json)
        }

    }


    override suspend fun fetchUser(token: String): UserModel {
        val response = client.get<String>("${Endpoints.baseURL}/user/me") {
            headers.append("Authorization", token)
        }

        val jsonData = Json.parseToJsonElement(response)
        return UserModel(
            username = jsonData.jsonObject["data"]!!.jsonObject["attributes"]!!.jsonObject["username"]!!.jsonPrimitive.content,
            id = jsonData.jsonObject["data"]!!.jsonObject["id"]!!.jsonPrimitive.content
        )
    }

    private suspend fun refreshToken(tokens: UserTokens): AuthModel {
        val dial =  client.post<AuthModel>("${Endpoints.baseURL}/auth/refresh") {
            body = RefreshModel(tokens.refresh)
            contentType(ContentType.Application.Json)
        }

        println(dial)

        return dial
    }



}