package com.ahmedkhaled.photoweatherapp.models


import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("country")
    val country: String,
    @SerializedName("id")
    val id: Double,
    @SerializedName("sunrise")
    val sunrise: Double,
    @SerializedName("sunset")
    val sunset: Double,
    @SerializedName("type")
    val type: Double
)