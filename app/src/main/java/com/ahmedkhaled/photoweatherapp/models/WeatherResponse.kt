package com.ahmedkhaled.photoweatherapp.models


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("cod")
    val cod: Double,
    @SerializedName("coord")
    val coord: Coord,
    @SerializedName("dt")
    val dt: Double,
    @SerializedName("id")
    val id: Double,
    @SerializedName("main")
    val main: Main,
    @SerializedName("name")
    val name: String,
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("timezone")
    val timezone: Double,
    @SerializedName("visibility")
    val visibility: Double,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    val wind: Wind
)