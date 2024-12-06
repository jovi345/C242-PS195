package com.app.travel.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.travel.R
import com.app.travel.data.repo.Injection
import com.app.travel.databinding.FragmentHomeBinding
import com.app.travel.ui.ViewModelFactory
import com.app.travel.ui.auth.register.CustomArrayAdapter
import com.app.travel.ui.detail.DetailActivity
import com.app.travel.ui.welcome.WelcomeActivity
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inisialisasi ViewModel dengan Repository
        val repository = Injection.provideRepository(requireContext())
        homeViewModel = ViewModelProvider(this, ViewModelFactory(repository))[HomeViewModel::class.java]

        // Inflate layout
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupSpinner()
        setupRecyclerView()
        observeSession()
        observeRecommendations()

        return binding.root
    }

    private fun setupSpinner() {
        val spinner: Spinner = binding.spinnerCities
        val cities = resources.getStringArray(R.array.cities_array).toList()
        val arrayAdapter = CustomArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            cities
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    val selectedLocation = parent.getItemAtPosition(position).toString()
                        .lowercase(Locale.ROOT)
                    homeViewModel.fetchRecommendations(selectedLocation)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Tidak ada item yang dipilih
            }
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.rvRecomendation
        val adapter = RecommendationAdapter(emptyList()) { id ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("PLACE_ID", id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observasi rekomendasi dan perbarui data adapter
        homeViewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
            adapter.updateData(recommendations)
        }
    }

    private fun observeSession() {
        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                navigateToWelcomeScreen()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.welcome_back),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateToWelcomeScreen() {
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun observeRecommendations() {
        homeViewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
            println("Fragment received recommendations: $recommendations")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
