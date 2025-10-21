package com.cbmedia.daycounter.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cbmedia.daycounter.data.model.GameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameRepository(private val context: Context) {
    
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "game_data")
    private val gameStateKey = stringPreferencesKey("game_state")
    private val json = Json { ignoreUnknownKeys = true }
    
    fun getGameState(): Flow<GameState?> {
        return context.dataStore.data.map { preferences ->
            val gameStateJson = preferences[gameStateKey]
            if (gameStateJson != null) {
                try {
                    json.decodeFromString<GameState>(gameStateJson)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }
    
    suspend fun saveGameState(gameState: GameState) {
        context.dataStore.edit { preferences ->
            val gameStateJson = json.encodeToString(gameState)
            preferences[gameStateKey] = gameStateJson
        }
    }
    
    suspend fun clearGameState() {
        context.dataStore.edit { preferences ->
            preferences.remove(gameStateKey)
        }
    }
}
