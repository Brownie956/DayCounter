package com.cbmedia.daycounter.data.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class GameRound(
    val roundNumber: Int,
    val diceRoll: DiceRoll,
    val baseDays: Int,
    val multiplierBefore: Double,
    val multiplierAfter: Double,
    val daysForThisRound: Int,
    val isComplete: Boolean,
    val additionalRolls: List<Double> = emptyList(),
    val task: DiceTask,
    val exceededMaxDays: Boolean = false
) {
    val totalDays: Int
        get() = (baseDays * multiplierAfter).toInt()
}
