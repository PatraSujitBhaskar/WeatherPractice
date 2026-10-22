package com.example.weatherpractice.adapter

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpractice.model.IndianCities
import com.example.weatherpractice.databinding.ItemWeatherBinding
import com.example.weatherpractice.database.WeatherEntity
class WeatherPagingAdapter : PagingDataAdapter<WeatherEntity, WeatherPagingAdapter.WeatherViewHolder>(
    WeatherDiffCallback()
){
    inner class WeatherViewHolder(private val binding: ItemWeatherBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(weather: WeatherEntity,position:Int){
            binding.tvLocation.text= IndianCities.cities.getOrNull(position)?.name ?: "Unknown City"
            binding.tvTemperature.text = "${weather.temperature}C"
            binding.tvHumidity.text = "${weather.humidity}"
            binding.tvWeatherCode.text="${weather.weatherCode}"
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
        android.util.Log.d("WeatherPagingAdapter", "Binding position: $position, itemCount: $itemCount")
        android.util.Log.d("WeatherPagingAdapter", "LastVisible: $position")
        getItem(position)?.let{ weather -> holder.bind(weather, position)}
    }
class WeatherDiffCallback: DiffUtil.ItemCallback<WeatherEntity>(){
    override fun areItemsTheSame(
        oldItem: WeatherEntity,
        newItem: WeatherEntity
    ): Boolean {
        return oldItem.locationKey == newItem.locationKey
    }

    override fun areContentsTheSame(
        oldItem: WeatherEntity,
        newItem: WeatherEntity
    ): Boolean {
        return oldItem==newItem
    }

}
}