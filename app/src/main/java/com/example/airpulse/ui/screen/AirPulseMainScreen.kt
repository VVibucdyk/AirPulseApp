package com.example.airpulse.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.airpulse.ui.components.AirPulseBottomNavigation
import com.example.airpulse.ui.components.AirPulseTopAppBar
import com.example.airpulse.ui.components.AirQualityDetailsSection
import com.example.airpulse.ui.components.CurrentAqiSection
import com.example.airpulse.ui.components.TipsSection
import com.example.airpulse.viewmodel.AirQualityViewModel

@Composable
fun AirPulseMainScreen(viewModel: AirQualityViewModel) {
    val airQualityData by viewModel.airQualityData.collectAsState()

    Scaffold(
        topBar = { AirPulseTopAppBar(onRefresh = { viewModel.fetchAirQualityData("here") }) },
        bottomBar = { AirPulseBottomNavigation(selectedTab = "Home", onTabSelected = {}) },
        content = { paddingValues ->
            airQualityData?.let { data ->
                Log.d("AirPulseMainScreen", "Data: $data")
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Display current AQI
                    CurrentAqiSection(aqi = data.aqi, city = data.city.name)

                    // Display detailed air quality data
                    AirQualityDetailsSection(
                        temperature = data.iaqi.t.v,
                        humidity = data.iaqi.h.v,
                        pm10 = data.iaqi.pm10.v,
                        dominantPollutant = data.dominentpol
                    )

                    Button(onClick = { viewModel.fetchAirQualityData("here") }) {
                        Text("Refresh")
                    }

                    // Display tips on reducing air pollution
                    TipsSection(tips = listOf("Kurangi menggunakan kendaraan bermotor", "Matikan mesin mobil", "Jangan membakar sampah", "Menanam dan merawat pohon", "Kurangi penggunaan energi di rumah"))
                }
            } ?: run {
                Text(text = "Loading data...")
            }
        }
    )
}