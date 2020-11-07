package com.ahmedkhaled.photoweatherapp.data

interface RepositoryInterface {

    fun onSuccess(response: Any?, code: Int? = 0)

    fun onError(code: Int? = 0)

    fun onError(errorMessage: String, code: Int? = 0)

}