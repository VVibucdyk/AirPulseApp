package com.example.airpulse.viewmodel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpulse.data.api.RetrofitInstance
import com.example.airpulse.data.model.AirQualityData
import com.example.airpulse.data.repository.AirQualityRepository
import com.example.airpulse.notification.FirebaseService
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
                val data = response.data
                _airQualityData.value = data

                // Check if AQI is high and trigger notification
                if (data.aqi > 100) { // You can adjust the threshold as needed
                    sendHighAqiNotification(data.city.name, data.aqi)
                }
            } catch (e: Exception) {
                _airQualityData.value = null
                Log.e("AirQualityViewModel", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun sendHighAqiNotification(city: String, aqi: Int) {
        val notificationTitle = "High AQI Alert"
        val notificationMessage = "The AQI in $city is currently $aqi. Take precautions."
        FirebaseService().sendNotification(notificationTitle, notificationMessage)
    }
}