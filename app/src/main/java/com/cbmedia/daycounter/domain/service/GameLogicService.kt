package com.cbmedia.daycounter.domain.service

import com.cbmedia.daycounter.data.model.DiceRoll
import com.cbmedia.daycounter.data.model.DiceTask
import com.cbmedia.daycounter.data.model.GameRound
import com.cbmedia.daycounter.data.model.GameState
import kotlin.random.Random

class GameLogicService {
    
    fun rollDice(): DiceRoll {
        return DiceRoll.random()
    }
    
    private fun scaleThirdDiceValue(diceValue: Int): Double {
        return when (diceValue) {
            1 -> 0.0
            2 -> 0.25
            3 -> 0.5
            4 -> 0.75
            5 -> 1.0
            6 -> 2.0
            else -> throw IllegalArgumentException("Invalid dice value: $diceValue")
        }
    }
    
    fun processRound(gameState: GameState, diceRoll: DiceRoll): GameRound {
        val roundNumber = gameState.currentRoundNumber
        val currentMultiplier = gameState.currentMultiplier
        
        // Determine the first dice value - use existing if available, otherwise use rolled value
        val firstDiceValue = gameState.baseFirstDice ?: diceRoll.dice1
        val actualDiceRoll = DiceRoll(
            dice1 = firstDiceValue,
            dice2 = diceRoll.dice2,
            dice3 = diceRoll.dice3
        )
        
        // Handle special case: all dice are 6
        val (finalDiceRoll, baseDays, multiplierIncrease) = if (actualDiceRoll.allSixes) {
            Triple(actualDiceRoll, 10, 5.0)
        } else {
            Triple(actualDiceRoll, actualDiceRoll.dice1, 0.0)
        }
        
        // Handle special case: all three dice are the same (but not all sixes)
        val (processedDiceRoll, additionalRolls, multiplierIncreaseBefore) = if (finalDiceRoll.allSame && !finalDiceRoll.allSixes) {
            val additionalRoll1 = (1..6).random()
            val additionalRoll2 = (1..6).random()
            val modifiedDiceRoll = DiceRoll(dice1 = finalDiceRoll.dice1, dice2 = 6, dice3 = finalDiceRoll.dice3)
            Triple(modifiedDiceRoll, listOf(additionalRoll1.toDouble(), additionalRoll2.toDouble()), (additionalRoll1 + additionalRoll2).toDouble())
        } else {
            Triple(finalDiceRoll, emptyList(), 0.0)
        }
        
        // Calculate multiplier before round
        var multiplierBefore = currentMultiplier + multiplierIncrease + multiplierIncreaseBefore
        
        // Handle two of the same dice case
        if (processedDiceRoll.twoSame && !processedDiceRoll.allSame) {
            multiplierBefore += 1.0
        }
        
        // Get task from second dice
        val task = DiceTask.forSecondDice(processedDiceRoll.dice2)
        
        // Calculate multiplier after round
        var multiplierAfter = multiplierBefore
        
        // Add scaled dice 3 value to multiplier for next round (unless skipped by task)
        if (!task.skipThirdDiceMultiplier) {
            multiplierAfter += scaleThirdDiceValue(processedDiceRoll.dice3)
        }
        
        // Add additional rolls if all three dice were the same
        multiplierAfter += additionalRolls.sum()
        
        // Add additional third dice rolls if specified by task
        val additionalThirdDiceRolls = mutableListOf<Double>()
        repeat(task.additionalThirdDiceRolls) {
            val additionalRoll = (1..6).random()
            val scaledRoll = scaleThirdDiceValue(additionalRoll)
            if(task.subtractFromMultiplier) {
                additionalThirdDiceRolls.add(-scaledRoll)
                multiplierAfter -= scaledRoll
            } else {
                additionalThirdDiceRolls.add(scaledRoll)
                multiplierAfter += scaledRoll
            }
        }
        
        // Handle two of the same dice case (add again after round)
        if (processedDiceRoll.twoSame && !processedDiceRoll.allSame) {
            multiplierAfter += 1.0
        }
        
        // Calculate days for this round (round up)
        var daysForThisRound = kotlin.math.ceil(baseDays * multiplierBefore).toInt()
        
        // Check for maximum days cap (30)
        val exceededMaxDays = daysForThisRound > 30
        if (exceededMaxDays) {
            daysForThisRound = 30
            multiplierAfter = 1.0 // Reset multiplier to 1 for next round
        }
        
        // Combine all additional rolls
        val allAdditionalRolls = additionalRolls + additionalThirdDiceRolls
        
        if (multiplierAfter < 0) {
            multiplierAfter = 0.0
        }
        return GameRound(
            roundNumber = roundNumber,
            diceRoll = processedDiceRoll,
            baseDays = baseDays,
            multiplierBefore = multiplierBefore,
            multiplierAfter = multiplierAfter,
            daysForThisRound = daysForThisRound,
            isComplete = task.isComplete,
            additionalRolls = allAdditionalRolls,
            task = task,
            exceededMaxDays = exceededMaxDays
        )
    }
    
    fun updateGameState(gameState: GameState, newRound: GameRound): GameState {
        val updatedRounds = gameState.rounds + newRound
        val newTotalDays = gameState.totalDays + newRound.daysForThisRound
        val newMultiplier = newRound.multiplierAfter
        
        // Set baseFirstDice on the first round
        val newBaseFirstDice = gameState.baseFirstDice ?: newRound.diceRoll.dice1
        
        return gameState.copy(
            rounds = updatedRounds,
            currentMultiplier = newMultiplier,
            totalDays = newTotalDays,
            isGameActive = !newRound.isComplete,
            baseFirstDice = newBaseFirstDice
        )
    }
}
