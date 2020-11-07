package com.ahmedkhaled.photoweatherapp.ui.camera

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmedkhaled.photoweatherapp.api.APIsInterface
import com.ahmedkhaled.photoweatherapp.data.CameraRepository
import com.ahmedkhaled.photoweatherapp.data.ListenerResult
import com.ahmedkhaled.photoweatherapp.data.RepositoryInterface
import io.reactivex.disposables.CompositeDisposable

class CameraViewModel(private val repository: CameraRepository) : ViewModel(),
    RepositoryInterface {

    private var compositeDisposable = CompositeDisposable()

    private val _weatherResult = MutableLiveData<ListenerResult>()
    val weatherResult: LiveData<ListenerResult> = _weatherResult

    fun initRepository(context: Context) {
        compositeDisposable = CompositeDisposable()
        repository.initRepository(context, this)
    }

    fun getWeather(
        lat: Double,
        lon: Double,
        apiService: APIsInterface
    ) {
        repository.getWeather(lat, lon, apiService, compositeDisposable)
    }

    override fun onSuccess(response: Any?, code: Int?) {
        _weatherResult.postValue(ListenerResult(success = response))
    }

    override fun onError(code: Int?) {
        // Not Used
    }

    override fun onError(errorMessage: String, code: Int?) {
        _weatherResult.postValue(ListenerResult(errorMessage = errorMessage))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}