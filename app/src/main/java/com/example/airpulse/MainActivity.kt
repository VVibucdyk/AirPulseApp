package com.example.airpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.airpulse.ui.screen.AirPulseMainScreen
import com.example.airpulse.ui.theme.AirPulseTheme
import com.example.airpulse.viewmodel.AirQualityViewModel

class MainActivity : ComponentActivity() {
    private val airQualityViewModel: AirQualityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize repository and viewModel
//        val repository = AirQualityRepository()
//        val viewModel = AirQualityViewModel(repository)

        setContent {
            AirPulseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AirPulseMainScreen(viewModel = airQualityViewModel)
//                    AirQualityScreen(viewModel = airQualityViewModel)
                }
            }
        }
    }
}