package com.example.weatherpractice

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpractice.api.RetrofitClient
import com.example.weatherpractice.databinding.ActivityMainBinding
import com.example.weatherpractice.model.IndianCities
import com.example.weatherpractice.model.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import  com.example.weatherpractice.adapter.WeatherPagingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val repository by lazy{
        WeatherRepository(applicationContext)
    }
    private lateinit var adapter: WeatherPagingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }
    private fun setupRecyclerView(){
        adapter= WeatherPagingAdapter()
        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        binding.recyclerView.adapter=adapter
        binding.recyclerView.isNestedScrollingEnabled=false
        binding.recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    Log.d("PagingScroll", "lastVisible=$lastVisibleItem,"+"itemCount = ${adapter.itemCount}")
                }
            }
        )
        adapter.addLoadStateListener { loadStates ->
            android.util.Log.d("PagingState","Refresh: ${loadStates.refresh}, Append: ${loadStates.append}")
        }
        lifecycleScope.launch{
            repository.getWeather().collectLatest { pagingData ->
                android.util.Log.d("MainActivity", "Paging data received")
                adapter.submitData(pagingData) }
        }
    }
    /*private fun getWeather(){
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
    }*/
}