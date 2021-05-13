package com.example.weatherapp.ui.forecast

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.weatherapp.R
import com.example.weatherapp.adapter.ContainerAdapter
import com.example.weatherapp.data.RecyclerViewSection
import com.example.weatherapp.data.model.WeatherList
import com.example.weatherapp.databinding.ForecastFragmentBinding
import com.example.weatherapp.di.LocationUtils.parseDate
import com.example.weatherapp.ui.today.TodayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class ForecastFragment : Fragment(R.layout.forecast_fragment) {

    private val binding by viewBinding(ForecastFragmentBinding::bind)
    private val viewModel : TodayViewModel by activityViewModels()

    private lateinit var adapter : ContainerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.list.observe(viewLifecycleOwner,{
            adapter.submitList(it)
        })

        viewModel.result.observe(viewLifecycleOwner,{
            binding.city.text = it.city.name
        })
    }

    private fun setupRecyclerView(){
        adapter = ContainerAdapter(requireContext())
        binding.RecyclerWeek.adapter = adapter
        binding.RecyclerWeek.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)
    }
}