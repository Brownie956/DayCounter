package com.cbmedia.daycounter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmedia.daycounter.data.model.GameState
import com.cbmedia.daycounter.data.repository.GameRepository
import com.cbmedia.daycounter.domain.service.GameLogicService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameRepository: GameRepository,
    private val gameLogicService: GameLogicService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    
    init {
        loadGameState()
    }
    
    private fun loadGameState() {
        viewModelScope.launch {
            gameRepository.getGameState().collect { gameState ->
                _uiState.value = _uiState.value.copy(
                    gameState = gameState ?: GameState(),
                    isLoading = false
                )
            }
        }
    }
    
    fun rollDice() {
        val currentGameState = _uiState.value.gameState
        if (!currentGameState.isGameActive) return
        
        val diceRoll = gameLogicService.rollDice()
        val newRound = gameLogicService.processRound(currentGameState, diceRoll)
        val updatedGameState = gameLogicService.updateGameState(currentGameState, newRound)
        
        _uiState.value = _uiState.value.copy(
            gameState = updatedGameState,
            lastDiceRoll = diceRoll,
            lastRound = newRound
        )
        
        viewModelScope.launch {
            gameRepository.saveGameState(updatedGameState)
        }
    }
    
    fun resetGame() {
        val newGameState = GameState()
        _uiState.value = _uiState.value.copy(
            gameState = newGameState,
            lastDiceRoll = null,
            lastRound = null
        )
        
        viewModelScope.launch {
            gameRepository.clearGameState()
        }
    }
}

data class GameUiState(
    val gameState: GameState = GameState(),
    val isLoading: Boolean = true,
    val lastDiceRoll: com.cbmedia.daycounter.data.model.DiceRoll? = null,
    val lastRound: com.cbmedia.daycounter.data.model.GameRound? = null
)
