package com.app.travel.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.travel.data.repo.Injection
import com.app.travel.databinding.FragmentSearchBinding
import com.app.travel.ui.ViewModelFactory
import com.app.travel.ui.detail.DetailActivity
import com.google.android.material.search.SearchBar

class SearchFragment : Fragment() {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding
    private lateinit var categoryAdapter: SearchRecommendationAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        val repository = Injection.provideRepository(requireContext())
        searchViewModel = ViewModelProvider(this, ViewModelFactory(repository))[SearchViewModel::class.java]

        setupCategory()
        setupRecyclerView()
        setupSearchView()

        return binding.root
    }

    private fun setupCategory() {
        binding.btnBeach.setOnClickListener {
            searchViewModel.getDestinationsByCategory("pantai")
        }
        binding.btnNatural.setOnClickListener {
            searchViewModel.getDestinationsByCategory("alam")
        }
        binding.btnTouristDestination.setOnClickListener {
            searchViewModel.getDestinationsByCategory("tujuan wisata")
        }
        binding.btnHistorical.setOnClickListener {
            searchViewModel.getDestinationsByCategory("budaya sejarah")
        }
        binding.btnPark.setOnClickListener {
            searchViewModel.getDestinationsByCategory("taman")
        }
        binding.btnHikingArea.setOnClickListener {
            searchViewModel.getDestinationsByCategory("hiking area")
        }
        binding.btnFamilyRecreation.setOnClickListener {
            searchViewModel.getDestinationsByCategory("rekreasi keluarga")
        }
        binding.btnZoo.setOnClickListener {
            searchViewModel.getDestinationsByCategory("kebun binatang")
        }
    }

    private fun setupRecyclerView() {
        categoryAdapter = SearchRecommendationAdapter(emptyList()) { id ->
            navigateToDetail(id)
        }
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = categoryAdapter
        }
        searchViewModel.recommendationsByCategory.observe(viewLifecycleOwner) { destinations ->
            categoryAdapter.updateData(destinations)
        }
        searchViewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            categoryAdapter.updateData(searchResults)
        }

    }

    private fun setupSearchView(){
        binding.destinationSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        searchViewModel.searchDestinations(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isNotEmpty()) {
                        searchViewModel.searchDestinations(it)
                    }
                }
                return true
            }
        })
    }

    private fun navigateToDetail(placeId: String) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("PLACE_ID", placeId)
        startActivity(intent)
    }

}
