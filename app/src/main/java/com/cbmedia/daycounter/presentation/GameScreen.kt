package com.cbmedia.daycounter.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.cbmedia.daycounter.presentation.components.DiceDisplay
import com.cbmedia.daycounter.presentation.components.GameStats
import com.cbmedia.daycounter.presentation.components.RoundHistory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var isRolling by remember { mutableStateOf(false) }
    
    // Start rolling animation when rolling dice
    val rollDice = {
        isRolling = true
        viewModel.rollDice()
        // Stop animation after a delay
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000) // Animation duration
            isRolling = false
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Day Counter Challenge",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        // Dice Display
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Roll the Dice!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                DiceDisplay(
                    diceRoll = uiState.lastDiceRoll,
                    baseFirstDice = uiState.gameState.baseFirstDice,
                    isRolling = isRolling,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { rollDice() },
                    enabled = uiState.gameState.isGameActive && !isRolling,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when {
                            !uiState.gameState.isGameActive -> "Challenge Complete!"
                            isRolling -> "Rolling..."
                            else -> "Roll Dice"
                        },
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        
        // Game Statistics
        GameStats(
            gameState = uiState.gameState,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Round History
        RoundHistory(
            rounds = uiState.gameState.rounds,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Reset Button
        OutlinedButton(
            onClick = { viewModel.resetGame() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Reset Game",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        // Loading indicator
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
