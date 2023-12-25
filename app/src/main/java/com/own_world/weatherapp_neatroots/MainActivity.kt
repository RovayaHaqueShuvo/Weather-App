package com.own_world.weatherapp_neatroots

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        fetchExtras()
    }

    private fun fetchExtras() {
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
                    Log.d("TAG", "OnRespose : $temperature")

                }
            }

            override fun onFailure(call: Call<weatherAJSONCode>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}