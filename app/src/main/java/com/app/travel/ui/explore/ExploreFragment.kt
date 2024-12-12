package com.app.travel.ui.explore

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.travel.R
import com.app.travel.data.response.SurveyRequest
import com.app.travel.data.retrofit.ApiConfig
import com.app.travel.databinding.FragmentExploreBinding
import com.app.travel.ui.auth.register.CustomArrayAdapter
import com.app.travel.ui.detail.DetailActivity
import kotlinx.coroutines.launch
import java.util.Locale

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var resultAdapter: SurveyAdapter
    private val exploreViewModel: ExploreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)

        setupRecyclerView()

        observeViewModel()

        setupSpinner(binding.spinnerMbti, R.array.mbti)
        setupSpinner(binding.spinnerLocation, R.array.location_array)
        setupSpinner(binding.spinnerPreferredCategory, R.array.category_array)
        setupSpinner(binding.spinnerTravelStyle, R.array.travel_style_array)
        setupSpinner(binding.spinnerTravelFrequency, R.array.travel_frequency_array)

        binding.mbtiButton.setOnClickListener {
            val url = "https://www.16personalities.com/free-personality-test"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        binding.submit.setOnClickListener {
            submitSurvey()
        }

        return binding.root
    }

    private fun setupSpinner(spinner: Spinner, arrayResId: Int, defaultPosition: Int = 0) {
        val items = resources.getStringArray(arrayResId).toList()
        val arrayAdapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ) {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).setTextColor(
                    if (position == defaultPosition) resources.getColor(R.color.gray, null)
                    else resources.getColor(android.R.color.black, null)
                )
                return view
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(
                    if (position == defaultPosition) resources.getColor(R.color.gray, null)
                    else resources.getColor(android.R.color.black, null)
                )
                return view
            }
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == defaultPosition) {
                    // Tidak ada aksi untuk pilihan default
                    spinner.setSelection(defaultPosition) // Tetap pada default jika dipilih
                } else {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    Log.d("SpinnerSelection", "Selected item: $selectedItem")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun submitSurvey() {
        val mbti = binding.spinnerMbti.selectedItem.toString()
        val location = binding.spinnerLocation.selectedItem.toString()
        val category = binding.spinnerPreferredCategory.selectedItem.toString()
        val travelStyle = binding.spinnerTravelStyle.selectedItem.toString()
        val age = binding.editTextAge.text.toString().toIntOrNull()
        val travelFrequency = binding.spinnerTravelFrequency.selectedItem.toString().toIntOrNull()

        if (mbti == "Select MBTI" || location == "Select Location" ||
            category == "Select Category" || travelStyle == "Select Travel Style" ||
            age == null || age <= 0 || travelFrequency == null) {
            Toast.makeText(requireContext(), "Fill in all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        val request = SurveyRequest(
            mbti = mbti,
            location = location.lowercase(),
            preferredCategory = category.lowercase(),
            travelStyle = travelStyle.lowercase(),
            age = age,
            travelFrequency = travelFrequency
        )

        exploreViewModel.submitSurvey(request)

        lifecycleScope.launch {
            exploreViewModel.surveyResults.collect { results ->
                if (results.isNotEmpty()) {
                    val intent = Intent(requireContext(), ResultActivity::class.java).apply {
                        putParcelableArrayListExtra("SURVEY_RESULTS", ArrayList(results))
                    }
                    startActivity(intent)
                }
            }
        }
    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            exploreViewModel.surveyResults.collect { results ->
                if (::resultAdapter.isInitialized) {
                    resultAdapter.updateData(results)
                } else {
                    Log.e("ExploreFragment", "Result adapter not initialized!")
                }
            }
        }
//        lifecycleScope.launch {
//            exploreViewModel.loading.collect { isLoading ->
//                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//            }
//        }
        lifecycleScope.launch {
            exploreViewModel.errorMessage.collect { errorMessage ->
                errorMessage?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        resultAdapter = SurveyAdapter(emptyList()) { id ->
            navigateToDetail(id)
        }

    }

    private fun navigateToDetail(placeId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("PLACE_ID", placeId)
        startActivity(intent)
    }
}