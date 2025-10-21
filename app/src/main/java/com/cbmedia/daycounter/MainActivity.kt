package com.cbmedia.daycounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cbmedia.daycounter.di.AppContainer
import com.cbmedia.daycounter.presentation.GameScreen
import com.cbmedia.daycounter.presentation.GameViewModel
import com.cbmedia.daycounter.ui.theme.DayCounterTheme

class MainActivity : ComponentActivity() {
    private lateinit var container: AppContainer
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        container = AppContainer(this)
        
        setContent {
            DayCounterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(
                        viewModel = container.gameViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    DayCounterTheme {
        // Preview would need a mock ViewModel
    }
}
