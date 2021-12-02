package com.izanaminightz.mochi.android

import android.app.Application
import android.content.Context
import com.izanaminightz.mochi.domain.models.MangadexLanguages
import com.izanaminightz.mochi.domain.models.UserModel
import com.izanaminightz.mochi.utils.MochiHelper

class MainApplication : Application() {


    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

        var user: UserModel? = null

        fun userPreferredLanguage() : MangadexLanguages {
            return MochiHelper().preferredLanguage()
        }

    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        user = MochiHelper().getUser()
//        MochiHelper().clearSettings()
    }
}