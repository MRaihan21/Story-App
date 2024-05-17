package com.rad21.storyapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_preferences")

class PreferenceManager private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveSession(user: User) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = user.name
            preferences[USER_TOKEN] = user.token
            preferences[STATE_KEY] = user.isLogin
            preferences[IS_LOGGED_IN] = true
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[USERNAME] ?: "",
                preferences[USER_TOKEN] ?: "",
                preferences[IS_LOGGED_IN] ?: false
            )
        }
    }

    val getUsername: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USERNAME]
    }

    companion object {
        @Volatile
        private var INSTANCE: PreferenceManager? = null
        private val USERNAME = stringPreferencesKey("username")
        private val USER_TOKEN = stringPreferencesKey("user_token")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): PreferenceManager {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferenceManager(dataStore)
                INSTANCE = instance
                instance
            }
        }

    }
}