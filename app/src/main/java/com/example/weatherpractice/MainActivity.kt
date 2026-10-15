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
        adapter= WeatherAdapter(weatherList)
        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        binding.recyclerView.adapter=adapter
    }
    private fun getWeather(){
        RetrofitClient.api.getWeather(
            latitude = 12.9716,
            longitude = 77.5946).enqueue(object:Callback<WeatherResponse>{
            override fun onResponse(
                call: Call<WeatherResponse>,
                response:Response<WeatherResponse>){
                if(response.isSuccessful){
                    response.body()?.let{weather->weatherList.add(weather)
                        adapter.notifyDataSetChanged()}
                }
            }
            override fun onFailure(
                call:Call<WeatherResponse>,
                t:Throwable
            ){
                Toast.makeText(this@MainActivity, "API Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
            }
        )
    }
}