package com.app.travel.ui.explore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.travel.R
import com.app.travel.data.response.SurveyResponseItem
import com.app.travel.databinding.ActivityResultBinding
import com.app.travel.ui.detail.DetailActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var resultAdapter: SurveyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, binding.root)
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val surveyResults: ArrayList<SurveyResponseItem>? =
            intent.getParcelableArrayListExtra("SURVEY_RESULTS")

        if (surveyResults != null) {
            setupRecyclerView(surveyResults)
        } else {
            Toast.makeText(this, "No survey results found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView(results: List<SurveyResponseItem>) {
        resultAdapter = SurveyAdapter(results) { item ->
            item.id?.let { navigateToDetail(it.toString()) } ?: run {
                Toast.makeText(this, "Item ID is invalid", Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            adapter = resultAdapter
        }
    }

    private fun navigateToDetail(placeId: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("PLACE_ID", placeId) // Pass the place ID to DetailActivity
        startActivity(intent)
    }
}

