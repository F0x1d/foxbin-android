package com.f0x1d.foxbin.store.datastore

import android.content.Context
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
    }

    private val Context.dataStore by preferencesDataStore("user_data")

    val accessToken = context.dataStore.data.map {
        it[ACCESS_TOKEN_KEY]
    }

    suspend fun saveAccessToken(accessToken: String?) {
        context.dataStore.edit {
            if (accessToken != null)
                it[ACCESS_TOKEN_KEY] = accessToken
            else
                it.remove(ACCESS_TOKEN_KEY)
        }
    }
}