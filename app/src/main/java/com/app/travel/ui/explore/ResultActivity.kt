package com.app.travel.ui.explore

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.travel.R
import com.app.travel.data.response.SurveyResponseItem
import com.app.travel.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var resultAdapter: SurveyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val surveyResults: ArrayList<SurveyResponseItem>? =
            intent.getParcelableArrayListExtra("SURVEY_RESULTS")

        if (surveyResults != null) {
            setupRecyclerView(surveyResults)
        } else {
            Toast.makeText(this, "No survey results found", Toast.LENGTH_SHORT).show()
        }

        supportActionBar?.hide()
    }

    private fun setupRecyclerView(results: List<SurveyResponseItem>) {
        resultAdapter = SurveyAdapter(results) { id ->
            Log.d("ResultActivity", "Clicked item with ID: $id")
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            adapter = resultAdapter
        }
    }
}

