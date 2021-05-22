package com.example.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.RecyclerViewSectionDB
import com.example.weatherapp.databinding.ItemContainerBinding

class ContainerAdapter(private val context: Context) :
    ListAdapter<RecyclerViewSectionDB, ContainerAdapter.SectionViewHolder>(ContainerComparator()) {

    class SectionViewHolder(private val binding: ItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(section: RecyclerViewSectionDB, context: Context) {
            binding.apply {
                sectionName.text = section.label
                val adapter = ItemAdapter(context)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(
                    binding.recyclerView.context,
                    LinearLayoutManager.VERTICAL, false
                )

                adapter.submitList(section.items)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding =
            ItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem, context)
        }
    }

    class ContainerComparator : DiffUtil.ItemCallback<RecyclerViewSectionDB>() {
        override fun areItemsTheSame(
            oldItem: RecyclerViewSectionDB,
            newItem: RecyclerViewSectionDB
        ): Boolean = oldItem.label == newItem.label

        override fun areContentsTheSame(
            oldItem: RecyclerViewSectionDB,
            newItem: RecyclerViewSectionDB
        ): Boolean = oldItem.items == newItem.items
    }
}
