package com.app.travel.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.travel.R
import com.app.travel.data.repo.Injection
import com.app.travel.databinding.FragmentHomeBinding
import com.app.travel.ui.auth.register.CustomArrayAdapter
import com.app.travel.ui.detail.DetailActivity
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val repository = Injection.provideRepository(requireContext())
        val homeViewModel = ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinner: Spinner = binding.spinnerCities
        val cities = resources.getStringArray(R.array.cities_array).toList()
        val arrayAdapter = CustomArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        val recyclerView = binding.rvRecomendation
        val adapter = RecommendationAdapter(emptyList()) { id ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("PLACE_ID", id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    val selectedLocation = parent.getItemAtPosition(position).toString().toLowerCase(
                        Locale.ROOT)
                    homeViewModel.fetchRecommendations(selectedLocation)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        homeViewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
            println("Fragment received recommendations: $recommendations")
            adapter.updateData(recommendations)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}