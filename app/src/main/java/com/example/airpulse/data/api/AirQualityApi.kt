package com.example.airpulse.data.api

import com.example.airpulse.data.model.AirQualityResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AirQualityApi {
    @GET("feed/{city}/")
    suspend fun getAirQualityData(
        @Path("city") city: String,
        @Query("token") token: String
    ): AirQualityResponse
}