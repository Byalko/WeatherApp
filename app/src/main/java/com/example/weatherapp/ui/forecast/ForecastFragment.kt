package com.example.weatherapp.ui.forecast

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.weatherapp.R
import com.example.weatherapp.adapter.ContainerAdapter
import com.example.weatherapp.databinding.ForecastFragmentBinding
import com.example.weatherapp.ui.today.TodayViewModel
import com.example.weatherapp.ui.today.TodayViewModel.AllEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.forecast_fragment) {

    private val binding by viewBinding(ForecastFragmentBinding::bind)
    private val viewModel: TodayViewModel by activityViewModels()

    private lateinit var adapter: ContainerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.location.observe(viewLifecycleOwner, {
            val location = viewModel.location.value
            if (location != null) {
                binding.city.text = location.name
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.list.collect {
                when (it) {
                    is AllEvent.Success -> {
                        adapter.submitList(it.result)
                    }
                    is AllEvent.Failure -> {
                        Toast.makeText(requireContext(), it.errorText, Toast.LENGTH_LONG).show()
                    }
                    is AllEvent.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                    }
                    is AllEvent.Empty -> {
                        binding.txtError.visibility = View.VISIBLE
                        binding.txtError.text = resources.getString(R.string.no_data)
                    }
                }
            }
        }

    }

    private fun setupRecyclerView() {
        adapter = ContainerAdapter(requireContext())
        binding.RecyclerWeek.adapter = adapter
        binding.RecyclerWeek.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
    }
}