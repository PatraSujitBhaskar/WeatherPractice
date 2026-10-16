package com.example.weatherpractice

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherpractice.adapter.WeatherAdapter
import com.example.weatherpractice.api.RetrofitClient
import com.example.weatherpractice.databinding.ActivityMainBinding
import com.example.weatherpractice.model.IndianCities
import com.example.weatherpractice.model.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherList=mutableListOf<WeatherResponse>()
    private lateinit var adapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        getWeather()
    }
    private fun setupRecyclerView(){
        adapter= WeatherAdapter(weatherList, IndianCities.cities.map{it.name

        })
        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        binding.recyclerView.adapter=adapter
    }
    private fun getWeather(){
        val latitude = IndianCities.cities.joinToString(","){it.latitude.toString()}
        val longitude = IndianCities.cities.joinToString(","){it.longitude.toString()}
        RetrofitClient.api.getWeather(
            latitude = latitude,
            longitude = longitude).enqueue(object:Callback<List<WeatherResponse>>{
            override fun onResponse(
                call: Call<List<WeatherResponse>>,
                response:Response<List<WeatherResponse>>) {
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    weatherData?.let { weather ->
                        weatherList.clear()
                        weatherList.addAll(weatherData)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "API Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(
                call:Call<List<WeatherResponse>>,
                t:Throwable
            ){
                Toast.makeText(this@MainActivity, "API Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
            }
        )
}
}