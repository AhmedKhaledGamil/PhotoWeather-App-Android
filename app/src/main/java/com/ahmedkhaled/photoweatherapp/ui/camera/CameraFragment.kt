package com.ahmedkhaled.photoweatherapp.ui.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ahmedkhaled.photoweatherapp.R
import com.ahmedkhaled.photoweatherapp.api.APIsClient
import com.ahmedkhaled.photoweatherapp.models.WeatherResponse
import com.ahmedkhaled.photoweatherapp.utils.ImageHelper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment(private val activity: Activity) : Fragment() {

    companion object {
        private const val TAG = "CameraFragment"
        private const val CAMERA_RESULT = 0
    }

    private lateinit var viewModel: CameraViewModel
    private lateinit var currentPhotoPath: String
    private var locationString = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(this, CameraViewModelFactory()).get(CameraViewModel::class.java)

        viewModel.weatherResult.observe(viewLifecycleOwner, {
            val result = it ?: return@observe

            when {
                result.errorMessage != null -> {
                    Toast.makeText(activity, result.errorMessage, Toast.LENGTH_SHORT).show()
                }
                result.success != null -> {
                    val response = result.success as WeatherResponse
                    //val finalString = locationString + "\nWeather: ${weather.weather[0].main}" + "\nTemperature: ${weather.main.temp} Celsius"
                    location.text = locationString
                    weather.text = response.weather[0].main
                    val tempString = "${response.main.temp} Celsius"
                    temp.text = tempString
                    loader.visibility = View.GONE
                    cameraLayout.visibility = View.VISIBLE
                }
            }
        })

        fetchLocation()
        newPhotoBtn.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initRepository(activity)
    }

    /**
     * Permission Already Given
     * */
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun fetchLocation() {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        println("Lat: ${location.latitude}, Long: ${location.longitude}")
                        val geoCoder = Geocoder(activity, Locale.US)
                        val addresses =
                            geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses.isNotEmpty()) {
                            val address = addresses[0]
                            locationString =
                                "${address.adminArea}, ${address.countryName}"
                            viewModel.getWeather(
                                location.latitude,
                                location.longitude,
                                APIsClient.getClient()
                            )
                        }
                    }
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            null
        )
        LocationServices.getFusedLocationProviderClient(activity).lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                println("Lat: ${location.latitude}, Long: ${location.longitude}")
                val geoCoder = Geocoder(activity, Locale.US)
                val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    locationString = "${address.adminArea}, ${address.countryName}"
                    viewModel.getWeather(
                        location.latitude,
                        location.longitude,
                        APIsClient.getClient()
                    )
                }
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                Log.e(
                    TAG,
                    "dispatchTakePictureIntent: Error creating file"
                )
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    activity,
                    "com.ahmedkhaled.photoweatherapp.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                    takePictureIntent,
                    CAMERA_RESULT
                )
            }
        }
    }

    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = timeStamp + "_"
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                val imageFile = File(currentPhotoPath)
                val foregroundBitmap = ImageHelper.getBitmapFromView(dataLayout)
                if (foregroundBitmap != null) {
                    val backgroundBitmap =
                        ImageHelper.handleSamplingAndRotationBitmap(activity, imageFile.toUri())!!
                    val combined = ImageHelper.combineImages(backgroundBitmap, foregroundBitmap)
                    if (combined != null) {
                        try {
                            FileOutputStream(imageFile).use { out ->
                                combined.compress(
                                    Bitmap.CompressFormat.PNG,
                                    100,
                                    out
                                ) // bmp is your Bitmap instance
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}