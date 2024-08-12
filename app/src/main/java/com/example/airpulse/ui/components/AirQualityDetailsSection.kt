package com.example.airpulse.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AirQualityDetailsSection(
    temperature: Double,
    humidity: Double,
    pm10: Double,
    dominantPollutant: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Air Quality Details", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Temperature: $temperature°C")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Humidity: $humidity%")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "PM10: $pm10")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Dominant Pollutant: $dominantPollutant")
        Spacer(modifier = Modifier.height(16.dp))
    }

}