package com.example.courseproject

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.text.capitalize
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForecastActivity : AppCompatActivity() {

    lateinit var City: TextView
    lateinit var toMainPage: Button

    val API: String = "d34dd910c8867e7be19902be7bb7109d"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        val message = intent.getStringExtra("CITY_NAME")

        toMainPage = findViewById(R.id.backToMainPage)
        City = findViewById(R.id.cityName)

        val capitalCityName = message?.capitalize()

        City.text = capitalCityName



        toMainPage.setOnClickListener{
            val intent = Intent(this, MainPage::class.java)
            intent.putExtra("CITY_NAME",message)
            startActivity(intent)
        }

        forecastTask().execute()
    }

    fun getWeatherIconResource(weatherDescription: String): Int {
        return when (weatherDescription.toLowerCase(Locale.ROOT)) {
            "broken clouds" -> R.drawable.brokenclouds
            "scattered clouds" -> R.drawable.scatteredclouds
            "overcast clouds" -> R.drawable.overcastclouds
            "light rain" -> R.drawable.lightrain
            "few clouds" -> R.drawable.fewclouds
            "clear sky" -> R.drawable.clearsky
            "light snow" -> R.drawable.lightsnow
            "moderate rain" -> R.drawable.moderaterain
            "shower rain" -> R.drawable.showerain
            "heavy rain" -> R.drawable.heavyrain
            "very heavy rain" -> R.drawable.heavyrain
            "snow shower" -> R.drawable.snowshower



            // Add more mappings for other weather descriptions as needed
            else -> R.drawable.sunrise // Default icon if the description doesn't match any known ones
        }
    }



    inner class forecastTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            try {
                val cityName = City.text.toString()
                response = URL("https://api.openweathermap.org/data/2.5/forecast?q=$cityName&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            } catch (e: Exception) {
                e.printStackTrace()
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null) {
                // Process the result here
                try {
                    val jsonObj = JSONObject(result)
                    val listArray = jsonObj.getJSONArray("list")

                    for (i in 0 until listArray.length()) {
                        if(i % 8 == 0){
                        val listItem = listArray.getJSONObject(i)
                        val main = listItem.getJSONObject("main")
                        val wind = listItem.getJSONObject("wind")
                        val weather = listItem.getJSONArray("weather").getJSONObject(0)

                        val timestamp: Long = listItem.getLong("dt")
                        val date = SimpleDateFormat("EEEE", Locale.ENGLISH).format(
                            Date(timestamp * 1000)
                        )
                        val temp = main.getString("temp") + "°C"
                        val humidity = main.getString("humidity")
                        val windSpeed = wind.getString("speed")
                        val weatherDescription = weather.getString("description")
                            val weatherIconResource = getWeatherIconResource(weatherDescription)

                        // Print or use the data as needed
                        //println("Date: $date, Temp: $temp, Humidity: $humidity, Wind Speed: $windSpeed, Weather: $weatherDescription")
                            if (i == 8) {
                                findViewById<TextView>(R.id.tomorrowTemp).text = temp
                                findViewById<TextView>(R.id.tomorrowStatusSmall).text = weatherDescription
                                findViewById<TextView>(R.id.tomorrowWindSpeedSmall).text = windSpeed
                                findViewById<TextView>(R.id.tomorrowHumiditySmall).text = humidity
                                findViewById<TextView>(R.id.tomorrowDate).text = date
                                findViewById<ImageView>(R.id.tomorrowState).setImageResource(weatherIconResource)
                                findViewById<ImageView>(R.id.tomorrowStateSmall).setImageResource(weatherIconResource)

                            }
                            else if(i == 16) {
                                findViewById<TextView>(R.id.tomorrowTemp2).text = temp
                                findViewById<TextView>(R.id.tomorrowStatusSmall2).text = weatherDescription
                                findViewById<TextView>(R.id.tomorrowWindSpeedSmall2).text = windSpeed
                                findViewById<TextView>(R.id.tomorrowHumiditySmall2).text = humidity
                                findViewById<TextView>(R.id.tomorrowDate2).text = date
                                findViewById<ImageView>(R.id.tomorrowState2).setImageResource(weatherIconResource)
                                findViewById<ImageView>(R.id.tomorrowStateSmall2).setImageResource(weatherIconResource)
                            }
                            else if(i == 24) {
                                findViewById<TextView>(R.id.tomorrowTemp52).text = temp
                                findViewById<TextView>(R.id.tomorrowStatusSmall53).text = weatherDescription
                                findViewById<TextView>(R.id.tomorrowWindSpeedSmall54).text = windSpeed
                                findViewById<TextView>(R.id.tomorrowHumiditySmall54).text = humidity
                                findViewById<TextView>(R.id.tomorrowDate52).text = date
                                findViewById<ImageView>(R.id.tomorrowState52).setImageResource(weatherIconResource)
                                findViewById<ImageView>(R.id.tomorrowStateSmall53).setImageResource(weatherIconResource)
                            }
                            else if(i == 32) {
                                findViewById<TextView>(R.id.tomorrowTemp62).text = temp
                                findViewById<TextView>(R.id.tomorrowStatusSmall63).text = weatherDescription
                                findViewById<TextView>(R.id.tomorrowWindSpeedSmall64).text = windSpeed
                                findViewById<TextView>(R.id.tomorrowHumiditySmall64).text = humidity
                                findViewById<TextView>(R.id.tomorrowDate62).text = date
                                findViewById<ImageView>(R.id.tomorrowState62).setImageResource(weatherIconResource)
                                findViewById<ImageView>(R.id.tomorrowStateSmall63).setImageResource(weatherIconResource)
                            }

}
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // Display a toast indicating success
                Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show()
            } else {
                // Display a toast indicating failure
                Toast.makeText(applicationContext, "Failed to fetch data", Toast.LENGTH_LONG).show()
            }
        }
    }
}