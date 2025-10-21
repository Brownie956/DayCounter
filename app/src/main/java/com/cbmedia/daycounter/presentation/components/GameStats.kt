package com.cbmedia.daycounter.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cbmedia.daycounter.data.model.GameState

@Composable
fun GameStats(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Game Statistics",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "Total Days",
                    value = gameState.totalDays.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Current Round",
                    value = gameState.currentRoundNumber.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Multiplier",
                    value = String.format("%.2f", gameState.currentMultiplier),
                    modifier = Modifier.weight(1f)
                )
            }
            
            if (gameState.baseFirstDice != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatItem(
                        label = "Base Days Dice",
                        value = gameState.baseFirstDice.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            gameState.lastRound?.let { lastRound ->
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                
                Column {
                    Text(
                        text = "Last Round Results",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Task display
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (lastRound.task.isComplete) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "Task: ${lastRound.task.description}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (lastRound.task.isComplete) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.onSurface
                            )
                            if (lastRound.task.skipThirdDiceMultiplier) {
                                Text(
                                    text = "⚠️ Third dice multiplier skipped",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            if (lastRound.task.additionalThirdDiceRolls > 0) {
                                Text(
                                    text = "🎲 Extra third dice roll(s): ${lastRound.task.additionalThirdDiceRolls}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Days this round: ${lastRound.daysForThisRound}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Base days: ${lastRound.baseDays}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Multiplier: ${String.format("%.2f", lastRound.multiplierBefore)} → ${String.format("%.2f", lastRound.multiplierAfter)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (lastRound.exceededMaxDays) {
                        Text(
                            text = "⚠️ Days capped at 30! Multiplier reset to 1",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    if (lastRound.additionalRolls.isNotEmpty()) {
                        Text(
                            text = "Additional rolls: ${lastRound.additionalRolls.joinToString(", ") { String.format("%.2f", it) }}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "Third dice scaled: ${lastRound.diceRoll.dice3} → ${when(lastRound.diceRoll.dice3) { 1 -> "0"; 2 -> "0.25"; 3 -> "0.5"; 4 -> "0.75"; 5 -> "1"; 6 -> "2"; else -> "?" }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (lastRound.isComplete) "Challenge Complete!" else "Challenge continues...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (lastRound.isComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
