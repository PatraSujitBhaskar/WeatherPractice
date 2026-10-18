package com.example.weatherpractice.adapter

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpractice.model.IndianCities
import com.example.weatherpractice.databinding.ItemWeatherBinding
import com.example.weatherpractice.model.WeatherResponse
class WeatherPagingAdapter : PagingDataAdapter<WeatherResponse, WeatherPagingAdapter.WeatherViewHolder>(
    WeatherDiffCallback()
){
    inner class WeatherViewHolder(private val binding: ItemWeatherBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(weather: WeatherResponse,position:Int){
            binding.tvLocation.text= IndianCities.cities.getOrNull(position)?.name ?: "Unknown City"
            binding.tvTemperature.text = "${weather.current.temperature_2m}C"
            binding.tvHumidity.text = "${weather.current.relative_humidity_2m}"
            binding.tvWeatherCode.text="${weather.current.weather_code}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: WeatherViewHolder,
        position: Int
    ) {
        android.util.Log.d("WeatherPagingAdapter", "Binding position: $position")
        getItem(position)?.let{ weather -> holder.bind(weather, position)}
    }
class WeatherDiffCallback: DiffUtil.ItemCallback<WeatherResponse>(){
    override fun areItemsTheSame(
        oldItem: WeatherResponse,
        newItem: WeatherResponse
    ): Boolean {
        return oldItem.latitude == newItem.latitude && oldItem.longitude == newItem.longitude
    }

    override fun areContentsTheSame(
        oldItem: WeatherResponse,
        newItem: WeatherResponse
    ): Boolean {
        return oldItem==newItem
    }

}
}