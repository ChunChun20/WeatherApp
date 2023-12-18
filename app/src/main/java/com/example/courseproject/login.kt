package com.example.courseproject

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class login : AppCompatActivity() {

    private lateinit var loginbtn: Button
    private lateinit var edituser: EditText
    private lateinit var editpword: EditText
    private lateinit var db: DBHelper
    private var city = "sofia"

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginbtn = findViewById(R.id.button4)
        edituser = findViewById(R.id.editTextText2)
        editpword = findViewById(R.id.editTextTextPassword3)
        db = DBHelper(this)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        getCurrentLocation()


        loginbtn.setOnClickListener {
            val useredtx = edituser.text.toString()
            val pwordedtx = editpword.text.toString()

            if (TextUtils.isEmpty(useredtx) || TextUtils.isEmpty(pwordedtx)){
                Toast.makeText(this,"Enter Username and Password",Toast.LENGTH_SHORT).show()
            }
            else {
                val checkuser = db.checkUserPass(useredtx,pwordedtx)
                if (checkuser==true){
                    Toast.makeText(this,"Login successful",Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext,MainPage::class.java)
                    intent.putExtra("CITY_NAME",city)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this,"Wrong Username or Password",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        Toast.makeText(this, "the best app", Toast.LENGTH_SHORT).show()

        // Permission is granted, proceed to get the current location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Get the latitude and longitude
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Get the city name based on the coordinates
                    val cityName = getCityName(latitude, longitude)

                    // Display the city name
                    Toast.makeText(this, "Current City: $cityName", Toast.LENGTH_SHORT).show()
                    city = cityName

                } else {
                    Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to get location: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }

    private fun getCityName(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val cityName = addresses[0].locality
                    return cityName ?: "Unknown"
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "Unknown"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed to get the current location
                    getCurrentLocation()
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}