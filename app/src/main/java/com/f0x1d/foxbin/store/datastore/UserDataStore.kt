package com.f0x1d.foxbin.store.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val USERNAME_KEY = stringPreferencesKey("username")
    }

    private val Context.dataStore by preferencesDataStore("user_data")

    val accessToken = getAsFlow(ACCESS_TOKEN_KEY)
    val username = getAsFlow(USERNAME_KEY)

    suspend fun saveAccessToken(accessToken: String?) = save(ACCESS_TOKEN_KEY, accessToken)
    suspend fun saveUsername(username: String?) = save(USERNAME_KEY, username)

    private fun <T> getAsFlow(key: Preferences.Key<T>) = context.dataStore.data.map {
        it[key]
    }

    private suspend fun <T> save(key: Preferences.Key<T>, value: T?) = context.dataStore.edit {
        if (value != null)
            it[key] = value
        else
            it.remove(key)
    }
}