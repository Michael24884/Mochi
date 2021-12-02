package com.izanaminightz.mochi.utils

import com.izanaminightz.mochi.domain.models.MangadexLanguages
import com.izanaminightz.mochi.domain.models.UserModel
import com.izanaminightz.mochi.domain.models.UserTokens
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MangadexHelper {
    inline fun coverCreator(id: String, image: String?) : String? = if (image != null) "https://uploads.mangadex.org/covers/$id/$image.512.jpg" else null


    fun timeDifference(time: String) : String {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
        val formatter = DateTimeFormatter.ofPattern("M/d/yyyy")
        val time = LocalDate.parse(time, format)
        return time.format(formatter)
    }


    fun fetchReaderImage() {

    }


}

class MochiHelper {
    fun i() {}
    fun storeLanguage(language: MangadexLanguages) {
        Settings()[LANGUAGE_STORAGE_KEY] = language.name
    }

    fun storeCompletedOnboarding() {
        Settings()[ONBOARDING_STORAGE_KEY] = true
    }

    fun storeUserModel(userModel: UserModel?) {
        if (userModel != null) {
            Settings()[USERNAME_STORAGE_KEY] = userModel.username
            Settings()[USER_ID_STORAGE_KEY] = userModel.id
        }
    }


    fun completedOnboarding() : Boolean {
        return Settings().getBooleanOrNull(ONBOARDING_STORAGE_KEY) ?: false
    }

    fun preferredLanguage() : MangadexLanguages =
        MangadexLanguages.valueOf(Settings().get(LANGUAGE_STORAGE_KEY, MangadexLanguages.en.name))

    fun getUser() : UserModel? {
        val name =  Settings().get<String>(USERNAME_STORAGE_KEY)
        val id = Settings().get<String>(USER_ID_STORAGE_KEY)
       if (name != null)
           return UserModel(name, id!!)
        return null
    }

    fun getTokens() : UserTokens {
        val session = Settings().get<String>("session")
        val refresh = Settings().get<String>("refresh")
        return UserTokens(
            session!!,
            refresh!!,
        )
    }

    fun clearSettings() {
        Settings().clear()
    }

}