package com.echoself.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.echoself.app.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {
    
    companion object {
        val NAME_KEY = stringPreferencesKey("user_name")
        val AGE_KEY = stringPreferencesKey("user_age")
        val GOAL_KEY = stringPreferencesKey("user_goal")
        val STRUGGLE_KEY = stringPreferencesKey("user_struggle")
    }

    val userProfileFlow: Flow<UserProfile> = context.dataStore.data.map { preferences ->
        UserProfile(
            name = preferences[NAME_KEY] ?: "",
            age = preferences[AGE_KEY] ?: "",
            biggestGoal = preferences[GOAL_KEY] ?: "",
            currentStruggle = preferences[STRUGGLE_KEY] ?: ""
        )
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        context.dataStore.edit { preferences ->
            preferences[NAME_KEY] = profile.name
            preferences[AGE_KEY] = profile.age
            preferences[GOAL_KEY] = profile.biggestGoal
            preferences[STRUGGLE_KEY] = profile.currentStruggle
        }
    }
}
