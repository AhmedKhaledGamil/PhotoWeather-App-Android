package com.ahmedkhaled.photoweatherapp.api

interface APIsListener {
    fun onResponseListener(response: Any?, code: Int? = 0)

    fun onFailureListener(code: Int? = 0)

    fun onFailureListener(errorMessage: String, code: Int? = 0)
}