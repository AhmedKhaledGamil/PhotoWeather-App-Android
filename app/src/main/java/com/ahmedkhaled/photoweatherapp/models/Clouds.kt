package com.ahmedkhaled.photoweatherapp.models


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Double
)