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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val repository = Injection.provideRepository(requireContext())
        homeViewModel = ViewModelProvider(this, ViewModelFactory(repository))[HomeViewModel::class.java]

        setupSpinner()
        setupRecyclerView()

        // Observasi lokasi yang dipilih untuk fetch rekomendasi
        binding.spinnerCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    val location = parent.getItemAtPosition(position).toString().lowercase(Locale.ROOT)
                    homeViewModel.fetchRecommendationsByLocation(location)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Ambil data rekomendasi berdasarkan riwayat pengguna
        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                homeViewModel.fetchRecommendationsByHistory(user.token)
            }
        }

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
                    homeViewModel.fetchRecommendationsByLocation(selectedLocation)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Tidak ada item yang dipilih
            }
        }
    }

    private fun setupRecyclerView() {
        // RecyclerView 1: Berdasarkan Lokasi
        val locationAdapter = RecommendationAdapter(emptyList()) { id ->
            navigateToDetail(id)
        }
        binding.rvRecommendationsByLocation.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = locationAdapter
        }

        // RecyclerView 2: Berdasarkan Riwayat
        val historyAdapter = RecommendationAdapter(emptyList()) { id ->
            navigateToDetail(id)
        }
        binding.rvRecommendationsByHistory.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = historyAdapter
        }

        // Observasi data untuk kedua RecyclerView
        homeViewModel.recommendationsByLocation.observe(viewLifecycleOwner) { recommendations ->
            locationAdapter.updateData(recommendations)
        }

        homeViewModel.recommendationsByHistory.observe(viewLifecycleOwner) { recommendations ->
            historyAdapter.updateData(recommendations)
        }
    }


    private fun observeSession() {
        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                navigateToWelcomeScreen()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.welcome_back, user.username),
                    Toast.LENGTH_SHORT
                ).show()
                binding.textHome.text = getString(R.string.welcome_home, user.username)
            }
        }
    }

    private fun navigateToDetail(placeId: String) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("PLACE_ID", placeId)
        startActivity(intent)
    }


    private fun navigateToWelcomeScreen() {
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

//    private fun observeRecommendations() {
//        homeViewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
//            println("Fragment received recommendations: $recommendations")
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
