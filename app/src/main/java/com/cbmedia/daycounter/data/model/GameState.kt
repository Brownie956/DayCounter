package com.cbmedia.daycounter.data.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class GameState(
    val rounds: List<GameRound> = emptyList(),
    val currentMultiplier: Double = 1.0,
    val totalDays: Int = 0,
    val isGameActive: Boolean = true,
    val baseFirstDice: Int? = null
) {
    val currentRoundNumber: Int
        get() = rounds.size + 1
    
    val lastRound: GameRound?
        get() = rounds.lastOrNull()
    
    val hasFirstDice: Boolean
        get() = baseFirstDice != null
}
