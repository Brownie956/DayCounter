package com.cbmedia.daycounter.di

import android.content.Context
import com.cbmedia.daycounter.data.repository.GameRepository
import com.cbmedia.daycounter.domain.service.GameLogicService
import com.cbmedia.daycounter.presentation.GameViewModel

class AppContainer(private val context: Context) {
    
    private val gameRepository: GameRepository by lazy {
        GameRepository(context)
    }
    
    private val gameLogicService: GameLogicService by lazy {
        GameLogicService()
    }
    
    val gameViewModel: GameViewModel by lazy {
        GameViewModel(gameRepository, gameLogicService)
    }
}
