package com.ahmedkhaled.photoweatherapp.api

import com.ahmedkhaled.photoweatherapp.models.WeatherResponse
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

interface APIsInterface {

    @POST("data/2.5/weather?appid=9ced440166f871310cbd5492dc51539c&units=metric")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Single<WeatherResponse>
}