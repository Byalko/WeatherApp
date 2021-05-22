package com.example.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.data.WeatherListDB
import com.example.weatherapp.databinding.RowBinding
import com.example.weatherapp.util.LocationUtils
import com.example.weatherapp.util.LocationUtils.parseDate

class ItemAdapter(private val context: Context) :
    ListAdapter<WeatherListDB, ItemAdapter.ItemViewHolder>(ItemComparator()) {

    class ItemViewHolder(private val binding: RowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: WeatherListDB, context: Context) {
            Glide.with(context).load(
                LocationUtils.DEFAULT_IMG + currentItem
                    .weather[0].icon + "@2x.png"
            ).centerCrop().into(binding.ImageItem)
            binding.apply {
                TimeItem.text = parseDate(currentItem.dt_txt, "HH:mm")
                DesItem.text = currentItem.weather[0].description
                DegreeItem.text = String.format(
                    context.resources.getString(R.string.gradusy_forecast),
                    currentItem.main.temp.toInt() - 273
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem, context)
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<WeatherListDB>() {
        override fun areItemsTheSame(
            oldItem: WeatherListDB,
            newItem: WeatherListDB
        ): Boolean = oldItem.dt_txt == newItem.dt_txt

        override fun areContentsTheSame(
            oldItem: WeatherListDB,
            newItem: WeatherListDB
        ): Boolean = oldItem.main.temp == newItem.main.temp
    }
}