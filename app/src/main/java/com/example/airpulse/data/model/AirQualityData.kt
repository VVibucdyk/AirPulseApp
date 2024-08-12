package com.example.airpulse.data.model

data class AirQualityResponse(
    val status: String,
    val data: AirQualityData
)

data class AirQualityData(
    val aqi: Int,
    val idx: Int,
    val attributions: List<Attribution>,
    val city: City,
    val dominentpol: String,
    val iaqi: Iaqi,
    val time: Time,
    val forecast: Forecast
)

data class Attribution(
    val url: String,
    val name: String,
    val logo: String? = null
)

data class City(
    val geo: List<Double>,
    val name: String,
    val url: String,
    val location: String
)

data class Iaqi(
    val dew: Measurement,
    val h: Measurement,
    val p: Measurement,
    val pm10: Measurement,
    val t: Measurement,
    val w: Measurement
)

data class Measurement(
    val v: Double
)

data class Time(
    val s: String,
    val tz: String,
    val v: Int,
    val iso: String
)

data class Forecast(
    val daily: DailyForecast
)

data class DailyForecast(
    val o3: List<ForecastItem>,
    val pm10: List<ForecastItem>,
    val pm25: List<ForecastItem>,
    val uvi: List<ForecastItem>
)

data class ForecastItem(
    val avg: Int,
    val day: String,
    val max: Int,
    val min: Int
)