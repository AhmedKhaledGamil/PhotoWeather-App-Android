package com.ahmedkhaled.photoweatherapp.data

import android.content.Context
import android.util.Log
import com.ahmedkhaled.photoweatherapp.api.APIsInterface
import com.ahmedkhaled.photoweatherapp.api.APIsListener
import com.google.gson.JsonObject
import io.reactivex.disposables.CompositeDisposable
import org.json.JSONObject

class CameraRepository(private val dataSource: CameraDataSource) : APIsListener {

    /**
     * Singleton Pattern
     * */
    companion object {
        @Volatile
        private var instance: CameraRepository? = null

        @Synchronized
        fun getInstance(dataSource: CameraDataSource): CameraRepository =
            instance ?: CameraRepository(dataSource = dataSource).also { instance = it }
    }

    private lateinit var anInterface: RepositoryInterface
    private lateinit var context: Context

    fun initRepository(context: Context, anInterface: RepositoryInterface) {
        this.context = context
        this.anInterface = anInterface
    }


    fun getWeather(
        lat: Double,
        lon: Double,
        apiService: APIsInterface,
        compositeDisposable: CompositeDisposable
    ) {
        dataSource.getWeather(lat, lon, apiService, compositeDisposable, this)
    }

    override fun onResponseListener(response: Any?, code: Int?) {
        anInterface.onSuccess(response, code)
    }

    override fun onFailureListener(code: Int?) {
        // Not Used
    }

    override fun onFailureListener(errorMessage: String, code: Int?) {
        anInterface.onError(errorMessage, code)
    }
}