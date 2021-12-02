package com.izanaminightz.mochi.di
//
//import com.izanaminightz.mochi.models.DescriptionClass
//import com.izanaminightz.mochi.models.DescriptionUnion
//import com.izanaminightz.mochi.remote.MangadexApi
//import com.izanaminightz.mochi.repository.DetailRepository
//import com.izanaminightz.mochi.repository.MangadexHomeRepository
//import io.ktor.client.*
//import io.ktor.client.features.json.*
//import io.ktor.client.features.json.serializer.*
//import kotlinx.serialization.json.Json
//import org.koin.core.context.startKoin
//import org.koin.dsl.KoinAppDeclaration
//import io.ktor.client.features.logging.*
//import kotlinx.serialization.modules.SerializersModule
//import org.koin.dsl.module
//import kotlinx.serialization.modules.polymorphic
//

import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

fun initKoin(appModule: Module): KoinApplication {
    val koinApplication = startKoin {
        modules(
            appModule,
            platformModule,
            coreModule
        )
    }

    val koin = koinApplication.koin
    val doOnStartup = koin.get<() -> Unit>() // doOnStartup is a lambda which is implemented in Swift on iOS side
    doOnStartup.invoke()

    return koinApplication
}

private val coreModule = module {

    // TODO: add database helper, repository, etc

//    single {
//        DatabaseHelper(
//            get(),
//            getWith("DatabaseHelper"),
//            Dispatchers.Default
//        )
//    }
//    single<KtorApi> {
//        DogApiImpl(
//            getWith("DogApiImpl")
//        )
//    }
}

internal inline fun <reified T> Scope.getWith(vararg params: Any?): T {
    return get(parameters = { parametersOf(*params) })
}

expect val platformModule: Module

//
//fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
//    startKoin {
//        appDeclaration()
//        modules(commonModule())
//    }
//
//fun initKoin() = initKoin {}
//
//fun commonModule() = module {
//    single { createJson() }
//    single { createHttpClient(get()) }
//
//    single { MangadexHomeRepository() }
//    single { DetailRepository() }
//    single { MangadexApi(get()) }
//}
//
//
//fun createJson() = Json {
//    isLenient = true;
//    ignoreUnknownKeys = true;
//    useAlternativeNames = false;
//    coerceInputValues = true
//}
//
//fun createHttpClient(json: Json,) = HttpClient {
//    install(JsonFeature) {
//        serializer = KotlinxSerializer(json)
//
//    }
//    install(Logging) {
//        logger = Logger.DEFAULT
//        level = LogLevel.ALL
//    }
//}