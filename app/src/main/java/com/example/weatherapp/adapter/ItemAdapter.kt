package com.example.weatherapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.RecyclerViewSection
import com.example.weatherapp.data.model.WeatherList
import com.example.weatherapp.databinding.RowBinding
import com.example.weatherapp.di.LocationUtils.parseDate

class ItemAdapter() : ListAdapter<WeatherList, ItemAdapter.ItemViewHolder>(ItemComparator()) {

    class ItemViewHolder(private val binding: RowBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(currentItem: WeatherList) {
            binding.apply {
                TimeItem.text = parseDate(currentItem.dt_txt,"HH:mm")
                DesItem.text = currentItem.weather[0].description
                DegreeItem.text="${currentItem.main.temp.toInt()}°C"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemAdapter.ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem!=null){
            holder.bind(currentItem)
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<WeatherList>() {
        override fun areItemsTheSame(
            oldItem: WeatherList,
            newItem: WeatherList
        ): Boolean = oldItem.dt_txt == newItem.dt_txt

        override fun areContentsTheSame(
            oldItem: WeatherList,
            newItem: WeatherList
        ): Boolean = oldItem.main.temp == newItem.main.temp
    }
}