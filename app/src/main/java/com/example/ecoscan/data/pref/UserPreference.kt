package com.example.ecoscan.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ecoscan.remote.response.UserDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserDetail) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.fullname
            preferences[USER_ID] = user.userId
            preferences[USER_EMAIL] = user.email
            preferences[TOKEN_KEY] = user.token
        }
    }

    fun getSession(): Flow<UserDetail> {
        return dataStore.data.map { preferences ->
            UserDetail(
                preferences[NAME_KEY] ?: "",
                preferences[USER_ID] ?: 0,
                preferences[USER_EMAIL] ?: "",
                preferences[TOKEN_KEY] ?:""
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("fullname")
        private val USER_ID = intPreferencesKey("userId")
        private val USER_EMAIL = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}