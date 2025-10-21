package com.cbmedia.daycounter.data.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class DiceRoll(
    val dice1: Int,
    val dice2: Int,
    val dice3: Int
) {
    init {
        require(dice1 in 1..6) { "Dice 1 must be between 1 and 6, got $dice1" }
        require(dice2 in 1..6) { "Dice 2 must be between 1 and 6, got $dice2" }
        require(dice3 in 1..6) { "Dice 3 must be between 1 and 6, got $dice3" }
    }
    
    val allSame: Boolean
        get() = dice1 == dice2 && dice2 == dice3
    
    val twoSame: Boolean
        get() = dice1 == dice2 || dice1 == dice3 || dice2 == dice3
    
    val allSixes: Boolean
        get() = dice1 == 6 && dice2 == 6 && dice3 == 6
    
    companion object {
        fun random(): DiceRoll {
            return DiceRoll(
                dice1 = (1..6).random(),
                dice2 = (1..6).random(),
                dice3 = (1..6).random()
            )
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class DiceTask(
    val description: String,
    val isComplete: Boolean,
    val skipThirdDiceMultiplier: Boolean = false,
    val additionalThirdDiceRolls: Int = 0,
    val subtractFromMultiplier: Boolean = false
) {
    companion object {
        fun forSecondDice(value: Int): DiceTask {
            return when (value) {
                1 -> DiceTask(
                    description = "Do 5 pressups",
                    isComplete = true,
                    skipThirdDiceMultiplier = false,
                    additionalThirdDiceRolls = 0
                )
                2 -> DiceTask(
                    description = "Do 5 pressups",
                    isComplete = false,
                    skipThirdDiceMultiplier = true,
                    additionalThirdDiceRolls = 0
                )
                3 -> DiceTask(
                    description = "Do 5 pressups",
                    isComplete = false,
                    skipThirdDiceMultiplier = false,
                    additionalThirdDiceRolls = 1,
                    subtractFromMultiplier = true
                )
                4 -> DiceTask(
                    description = "Do 10 pressups",
                    isComplete = false,
                    skipThirdDiceMultiplier = false,
                    additionalThirdDiceRolls = 0
                )
                5 -> DiceTask(
                    description = "Do 10 pressups",
                    isComplete = false,
                    skipThirdDiceMultiplier = false,
                    additionalThirdDiceRolls = 3,
                    subtractFromMultiplier = true
                )
                6 -> DiceTask(
                    description = "Do 20 pressups",
                    isComplete = false,
                    skipThirdDiceMultiplier = false,
                    additionalThirdDiceRolls = 1
                )
                else -> throw IllegalArgumentException("Invalid dice value: $value")
            }
        }
    }
}
