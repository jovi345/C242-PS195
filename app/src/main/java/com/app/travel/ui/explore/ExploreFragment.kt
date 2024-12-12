package com.app.travel.ui.explore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.travel.data.response.SurveyRequest
import com.app.travel.data.retrofit.ApiConfig
import com.app.travel.databinding.FragmentExploreBinding
import com.app.travel.ui.detail.DetailActivity
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var resultAdapter: SurveyAdapter
    private val exploreViewModel: ExploreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        binding.mbtiButton.setOnClickListener {
            val url = "https://www.16personalities.com/free-personality-test"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.submit.setOnClickListener {
            submitSurvey()
        }

        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun submitSurvey() {
        val mbti = binding.spinnerMbti.selectedItem.toString()
        val location = binding.spinnerLocation.selectedItem.toString().lowercase()
        val category = binding.spinnerPreferredCategory.selectedItem.toString().lowercase().replace(" ", "_")
        val travelStyle = binding.spinnerTravelStyle.selectedItem.toString().lowercase()
        val age = binding.editTextAge.text.toString().toIntOrNull()
        val travelFrequency = binding.spinnerTravelFrequency.selectedItem.toString().toIntOrNull()

        if (age == null || travelFrequency == null || age <= 0) {
            Toast.makeText(requireContext(), "Fill in the required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val request = SurveyRequest(mbti, location, category, travelStyle, age, travelFrequency)
        exploreViewModel.submitSurvey(request)

        lifecycleScope.launch {
            try {
                val apiService = ApiConfig.getRecommendationService()
                val response = apiService.getSurveyRecommendations(request)
                Log.d("ExploreFragment", "Survey submitted successfully: $response")
                resultAdapter.updateData(response)
                Toast.makeText(requireContext(), "Survey submitted successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to submit survey: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("ExploreFragment", "Failed to submit survey", e)
            }
        }
    }
    private fun setupRecyclerView() {
        resultAdapter = SurveyAdapter(emptyList()) { id ->
            navigateToDetail(id)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = resultAdapter
        }

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            exploreViewModel.surveyResults.collect { results ->
                resultAdapter.updateData(results)
            }
        }
    }

    private fun navigateToDetail(placeId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("PLACE_ID", placeId)
        startActivity(intent)
    }
}