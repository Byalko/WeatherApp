package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.RecyclerViewSection
import com.example.weatherapp.databinding.ItemContainerBinding

class ContainerAdapter : ListAdapter<RecyclerViewSection, ContainerAdapter.SectionViewHolder>(ContainerComparator()) {

    class SectionViewHolder(private val binding: ItemContainerBinding) : RecyclerView.ViewHolder( binding.root ) {
        fun bind(section: RecyclerViewSection) {
           binding.apply {
               sectionName.text = section.label
               val adapter = ItemAdapter()
               binding.recyclerView.adapter = adapter
               binding.recyclerView.layoutManager = LinearLayoutManager(binding.recyclerView.context,
                   LinearLayoutManager.VERTICAL,false)

               adapter.submitList(section.items)

           }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem!=null){
            holder.bind(currentItem)
        }
    }

    class ContainerComparator : DiffUtil.ItemCallback<RecyclerViewSection>() {
        override fun areItemsTheSame(
            oldItem: RecyclerViewSection,
            newItem: RecyclerViewSection
        ): Boolean = oldItem.label == newItem.label

        override fun areContentsTheSame(
            oldItem: RecyclerViewSection,
            newItem: RecyclerViewSection
        ): Boolean = oldItem.items == newItem.items
    }
}
