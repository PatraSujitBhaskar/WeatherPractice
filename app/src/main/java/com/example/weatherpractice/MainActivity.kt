package com.example.weatherpractice

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var currentPage=0
    private val pageSize = 10
    private val displayedCityNames = mutableListOf<String>()
    private var isLoading=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        getWeather()
    }
    private fun setupRecyclerView(){
        adapter= WeatherAdapter(weatherList, displayedCityNames
        )
        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        binding.recyclerView.adapter=adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView,dx:Int,dy:Int){
             super.onScrolled(recyclerView, dx, dy)
             val layoutManager=recyclerView.layoutManager as LinearLayoutManager
             val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
             if(!isLoading && lastVisibleItem >= weatherList.size-2 && weatherList.size<IndianCities.cities.size){
                 loadNextPage()
             }
            }
        })
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
                        displayedCityNames.clear()
                        weatherList.addAll(weatherData.take(pageSize))
                        displayedCityNames.addAll(IndianCities.cities.take(pageSize).map{it.name})
                        adapter.notifyDataSetChanged()
                        currentPage=0
                        isLoading=false                    }
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
    private fun loadNextPage(){
        if(isLoading) return
        val allCities = IndianCities.cities
        val startIndex = (currentPage+1)*pageSize
        val endIndex = minOf(startIndex+pageSize, allCities.size)
        if(startIndex>=allCities.size) return
        isLoading = true
        val nextCities =allCities.subList(startIndex, endIndex)
        val latitude = nextCities.joinToString(","){ it.latitude.toString()}
        val longitude = nextCities.joinToString(","){ it.longitude.toString()}
        RetrofitClient.api.getWeather(latitude=latitude, longitude=longitude).enqueue(object:Callback<List<WeatherResponse>>{
            override fun onResponse(call: Call<List<WeatherResponse>>, response: Response<List<WeatherResponse>>){
                if(response.isSuccessful){
                    val newWeather = response.body()
                    if(newWeather!=null){
                        weatherList.addAll(newWeather)
                        displayedCityNames.addAll(nextCities.map{it.name})
                        currentPage++
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this@MainActivity, "Loaded ${weatherList.size} of ${IndianCities.cities.size} cities", Toast.LENGTH_SHORT).show()
                    }
                }
                isLoading = false
            }
            override fun onFailure(call: Call<List<WeatherResponse>>, t:Throwable) {
                isLoading = false
                Toast.makeText(
                    this@MainActivity,
                    "error loading page: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}