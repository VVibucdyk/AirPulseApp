package com.example.airpulse.viewmodel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpulse.data.api.RetrofitInstance
import com.example.airpulse.data.model.AirQualityData
import com.example.airpulse.data.repository.AirQualityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class AirQualityViewModel : ViewModel() {
    private val apiKey = "d576c9ce1b626caf8b20c3ea5eaf31206daad48d"
    private val repository = AirQualityRepository(RetrofitInstance.api, apiKey)

    private val _airQualityData = MutableStateFlow<AirQualityData?>(null)
    val airQualityData: StateFlow<AirQualityData?> = _airQualityData

    init {
        fetchAirQualityData("here") // Default city
    }

    fun fetchAirQualityData(city: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAirQualityData(city)
                _airQualityData.value = response.data
            } catch (e: Exception) {
                _airQualityData.value = null
                Log.e("AirQualityViewModel", "Error fetching data: ${e.message}")
            }
        }
    }
}