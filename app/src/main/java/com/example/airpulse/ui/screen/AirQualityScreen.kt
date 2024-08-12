package com.example.airpulse.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.airpulse.viewmodel.AirQualityViewModel

@Composable
fun AirQualityScreen(viewModel: AirQualityViewModel) {
    val airQualityData by viewModel.airQualityData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        airQualityData?.let { data ->
            Text(text = "City: ${data.city.name}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "AQI: ${data.aqi}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Dominant Pollutant: ${data.dominentpol}")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Temperature: ${data.iaqi.t.v} °C")
            Text(text = "Humidity: ${data.iaqi.h.v} %")
            Text(text = "PM10: ${data.iaqi.pm10.v}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.fetchAirQualityData("here") }) {
                Text("Refresh Data")
            }
        } ?: run {
            Text(text = "Loading data...")
        }
    }
}