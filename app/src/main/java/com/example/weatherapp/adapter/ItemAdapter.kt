package com.example.weatherapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.data.model.WeatherList
import com.example.weatherapp.databinding.RowBinding
import com.example.weatherapp.di.LocationUtils
import com.example.weatherapp.di.LocationUtils.parseDate

class ItemAdapter(private val context: Context) : ListAdapter<WeatherList, ItemAdapter.ItemViewHolder>(ItemComparator()) {

    class ItemViewHolder(private val binding: RowBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(currentItem: WeatherList, context: Context) {
            Glide.with(context).load(
                LocationUtils.DEFAULT_IMG+currentItem
                .weather[0].icon + "@2x.png").centerCrop().into(binding.ImageItem)
            binding.apply {
                TimeItem.text = parseDate(currentItem.dt_txt,"HH:mm")
                DesItem.text = currentItem.weather[0].description
                DegreeItem.text="${currentItem.main.temp.toInt()-273}°C"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem!=null){
            holder.bind(currentItem,context)
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