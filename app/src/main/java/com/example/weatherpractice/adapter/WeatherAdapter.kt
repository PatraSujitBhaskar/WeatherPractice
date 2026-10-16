package com.example.weatherpractice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpractice.databinding.ItemWeatherBinding
import com.example.weatherpractice.model.WeatherResponse

class WeatherAdapter(private val weatherList: List<WeatherResponse>, private val cityNames: List<String>): RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    class WeatherViewHolder(private val binding: ItemWeatherBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(weather: WeatherResponse, cityName:String){
            binding.tvLocation.text=cityName
            binding.tvTemperature.text="${weather.current.temperature_2m}C"
            binding.tvHumidity.text="${weather.current.relative_humidity_2m}%"
            binding.tvWeatherCode.text="${weather.current.weather_code}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding= ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position], cityNames[position])
    }
    override fun getItemCount(): Int{
        return weatherList.size
        }
    }
