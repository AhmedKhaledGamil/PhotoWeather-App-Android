package com.ahmedkhaled.photoweatherapp.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedkhaled.photoweatherapp.data.CameraDataSource
import com.ahmedkhaled.photoweatherapp.data.CameraRepository

class CameraViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel(
                repository = CameraRepository.getInstance(
                    dataSource = CameraDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}