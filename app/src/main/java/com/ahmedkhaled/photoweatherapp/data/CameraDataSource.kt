package com.ahmedkhaled.photoweatherapp.data

import com.ahmedkhaled.photoweatherapp.api.APIsInterface
import com.ahmedkhaled.photoweatherapp.api.APIsListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CameraDataSource {

    fun getWeather(
        lat: Double,
        lon: Double,
        apiService: APIsInterface,
        compositeDisposable: CompositeDisposable,
        listener: APIsListener
    ) {
        compositeDisposable.add(
            apiService.getWeather(lat, lon)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        println("Response $it")
                        if (it.cod.toInt() == 200) {
                            listener.onResponseListener(it)
                        } else if (it.cod.toInt() == 429) { // Blocked due to overuse!
                            listener.onFailureListener("Please wait and Try again!")
                        }
                    },
                    {
                        println("Throwable $it")
                        listener.onFailureListener(it.message.toString())
                    }
                )
        )
    }
}