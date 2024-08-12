package com.example.airpulse.data.repository
import android.util.Log
import com.example.airpulse.data.api.AirQualityApi
import com.example.airpulse.data.model.AirQualityResponse

//class AirQualityRepository(private val service: AirQualityService, private val apiKey: String) {
//    suspend fun getAirQualityData(city: String): AirQualityData {
//        return service.getAirQualityData(city, apiKey)
//    }
//}

class AirQualityRepository(private val api: AirQualityApi, private val apiKey: String) {
    suspend fun getAirQualityData(city: String): AirQualityResponse {
        val response = api.getAirQualityData(city, apiKey)
        Log.d("AirQualityRepository", "API Response: $response")
        return response
    }
}