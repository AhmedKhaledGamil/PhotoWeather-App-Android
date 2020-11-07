package com.ahmedkhaled.photoweatherapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.ahmedkhaled.photoweatherapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA = 0
        private const val LOCATION = 1
        private const val WRITE = 2
        private const val READ = 3
    }

    private var cameraNeverAskAgain = false
    private var locationNeverAskAgain = false
    private var writeNeverAskAgain = false
    private var readNeverAskAgain = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionBtn.setOnClickListener {
            when {
                cameraNeverAskAgain -> {
                    openSettings(CAMERA)
                }
                isPermissionNotGranted(Manifest.permission.CAMERA) -> {
                    requestPermission(Manifest.permission.CAMERA, CAMERA)
                }
                locationNeverAskAgain -> {
                    openSettings(LOCATION)
                }
                isPermissionNotGranted(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION)
                }
                writeNeverAskAgain -> {
                    openSettings(WRITE)
                }
                isPermissionNotGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE)
                }
                readNeverAskAgain -> {
                    openSettings(READ)
                }
                isPermissionNotGranted(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ)
                }
                !checkIfLocationIsEnabled() -> {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                !isOnline() -> {
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkAllPermissions()
    }

    private fun checkIfLocationIsEnabled(): Boolean {
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        val gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        return gpsEnabled || networkEnabled
    }

    private fun checkAllPermissions() {
        when {
            isPermissionNotGranted(Manifest.permission.CAMERA) -> {
                permissionTV.text = getString(R.string.enable_camera)
            }
            isPermissionNotGranted(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                permissionTV.text = getString(R.string.enable_location)
            }
            isPermissionNotGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                permissionTV.text = getString(R.string.enable_write)
            }
            isPermissionNotGranted(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                permissionTV.text = getString(R.string.enable_read)
            }
            !checkIfLocationIsEnabled() -> {
                permissionTV.text = getString(R.string.open_location)
            }
            !isOnline() -> {
                permissionTV.text = getString(R.string.open_wifi)
            }
            else -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun openSettings(code: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts(
            "package",
            packageName, null
        )
        intent.data = uri
        startActivityForResult(
            intent,
            code
        )
    }

    private fun isPermissionNotGranted(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, permission
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(permission: String, code: Int) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission),
            code
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraNeverAskAgain = false
                } else {
                    // user rejected the permission
                    var showRationale = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permissions[0])
                    }
                    if (!showRationale) {
                        cameraNeverAskAgain = true
                    }
                }
            }
            LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationNeverAskAgain = false
                } else {
                    // user rejected the permission
                    var showRationale = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permissions[0])
                    }
                    if (!showRationale) {
                        locationNeverAskAgain = true
                    }
                }
            }
            WRITE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeNeverAskAgain = false
                } else {
                    // user rejected the permission
                    var showRationale = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permissions[0])
                    }
                    if (!showRationale) {
                        writeNeverAskAgain = true
                    }
                }
            }
            READ -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readNeverAskAgain = false
                } else {
                    // user rejected the permission
                    var showRationale = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permissions[0])
                    }
                    if (!showRationale) {
                        readNeverAskAgain = true
                    }
                }
            }
        }
        checkAllPermissions()
    }

    @Suppress("DEPRECATION")
    private fun isOnline(): Boolean {
        var result = false
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        result = true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        result = true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                        result = true
                    }
                }
            }
        } else {
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) {
                // connected to the internet
                when (activeNetwork.type) {
                    ConnectivityManager.TYPE_WIFI -> {
                        result = true
                    }
                    ConnectivityManager.TYPE_MOBILE -> {
                        result = true
                    }
                    ConnectivityManager.TYPE_VPN -> {
                        result = true
                    }
                }
            }
        }
        return result
    }
}