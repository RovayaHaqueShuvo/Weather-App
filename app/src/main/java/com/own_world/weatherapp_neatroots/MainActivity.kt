package com.own_world.weatherapp_neatroots

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.own_world.weatherapp_neatroots.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.SimpleTimeZone
import java.util.concurrent.locks.Condition

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        fetchExtras("Dhaka")
        SearchCity()
    }

    private fun SearchCity() {
        val SearchView = this.binding.searchView
        SearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchExtras(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchExtras(CityName :String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response =
            retrofit.getWeatherData("Dhaka", "4a4d0102e2ac2a7143f60fdbcd8c267b", "metric")
        response.enqueue(object : Callback<weatherAJSONCode> {
            override fun onResponse(
                call: Call<weatherAJSONCode>,
                response: Response<weatherAJSONCode>
            ) {
              val  responseBody = response.body()
                if (response.isSuccessful && responseBody!=null){
                    val temperature = responseBody.main.temp.toString()
                   // Log.d("TAG", "OnRespose : $temperature")


                    val humidity = responseBody.main.humidity.toString()
                    val windSpeed = responseBody.wind.speed.toString()
                    val sunrise = responseBody.sys.sunrise.toString()
                    val sunset = responseBody.sys.sunset.toString()
                    val sealevel = responseBody.main.pressure.toString()
                    val condition = responseBody.weather.firstOrNull()?.main.toString()
                    val max_temp = responseBody.main.temp_max.toString()
                    val min_temp = responseBody.main.temp_min.toString()




                    binding.tempeture.text = "$temperature °C"
                    binding.condition.text = condition
                    binding.Humidity.text = "$humidity %"
                    binding.windspeed.text = "$windSpeed m/s"
                    binding.minTemp.text = "$min_temp °C"
                    binding.maxTemp.text = "$max_temp °C"
                    binding.seaLevel.text = "$sealevel hPa"
                    binding.sunrise.text = sunrise
                    binding.city.text = CityName
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())


                    changecontionachange(condition)
                }
            }

            override fun onFailure(call: Call<weatherAJSONCode>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun changecontionachange(condition: Any) {

        when(condition){
            "Clouds" -> binding.root.setBackgroundResource(R.drawable.cloud_black)
            "Clear" -> binding.root.setBackgroundResource(R.drawable.sunny)
        }

    }

    fun dayName(timestamp: Long):String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }
}