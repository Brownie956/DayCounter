package com.cbmedia.daycounter.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cbmedia.daycounter.data.model.GameRound

@Composable
fun RoundHistory(
    rounds: List<GameRound>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Round History",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (rounds.isEmpty()) {
                Text(
                    text = "No rounds played yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items(rounds.reversed()) { round ->
                        RoundItem(round = round)
                    }
                }
            }
        }
    }
}

@Composable
private fun RoundItem(
    round: GameRound,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (round.isComplete) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Round ${round.roundNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${round.daysForThisRound} days",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Dice: ${round.diceRoll.dice1}, ${round.diceRoll.dice2}, ${round.diceRoll.dice3}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (round.isComplete) "Complete" else "Continue",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (round.isComplete) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                text = "Task: ${round.task.description}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = if (round.task.isComplete) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (round.exceededMaxDays) {
                Text(
                    text = "⚠️ Days capped at 30! Multiplier reset",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            if (round.task.skipThirdDiceMultiplier) {
                Text(
                    text = "⚠️ Third dice multiplier skipped",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            if (round.task.additionalThirdDiceRolls > 0) {
                Text(
                    text = "🎲 Extra third dice roll(s): ${round.task.additionalThirdDiceRolls}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Text(
                text = "Third dice: ${round.diceRoll.dice3} → ${when(round.diceRoll.dice3) { 1 -> "0"; 2 -> "0.25"; 3 -> "0.5"; 4 -> "0.75"; 5 -> "1"; 6 -> "2"; else -> "?" }}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (round.additionalRolls.isNotEmpty()) {
                Text(
                    text = "Additional: ${round.additionalRolls.joinToString(", ") { String.format("%.2f", it) }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
