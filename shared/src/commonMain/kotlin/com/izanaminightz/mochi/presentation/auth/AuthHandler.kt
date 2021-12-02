package com.izanaminightz.mochi.presentation.auth

import com.izanaminightz.mochi.data.MochiRepositoryImpl
import com.izanaminightz.mochi.domain.models.AuthModel
import com.izanaminightz.mochi.domain.models.ResultModel
import com.izanaminightz.mochi.domain.models.UserModel
import com.izanaminightz.mochi.utils.MochiHelper
import com.russhwolf.settings.Settings
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

sealed class AuthState {
    data class AuthError(val error: String) : AuthState()
    data class AuthSuccess(val tokens: AuthModel) : AuthState()
}

class AuthHandler {
    private val repo = MochiRepositoryImpl()


    @OptIn(DelicateCoroutinesApi::class)
    suspend fun login(
        username: String,
        password: String,
        user: ((UserModel?) -> Unit)
    ) : AuthState {


        val client = repo.login(
            username,
            password
        )

        if (client.result == ResultModel.Error)  return AuthState.AuthError(client.errors!!.first().detail)

        saveTokens(client.token!!.session, client.token.refresh) {
            GlobalScope.launch(Dispatchers.Main) {
                val loggedUser = repo.fetchUser(it)
                user(loggedUser)
                MochiHelper().storeUserModel(loggedUser)
            }
        }
        return AuthState.AuthSuccess(client)
    }


    private fun saveTokens(
        token: String,
        refresh: String,
        tokenCB: ((String) -> Unit)
    ) {
        val prefs = Settings()
        prefs.putString("session", token)
        prefs.putString("refresh", refresh)
        tokenCB(token)
    }


}