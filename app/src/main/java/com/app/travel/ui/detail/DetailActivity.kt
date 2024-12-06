package com.app.travel.ui.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.app.travel.R
import com.app.travel.data.repo.Injection
import com.app.travel.ui.ViewModelFactory
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailActivity() : AppCompatActivity() {
    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(Injection.provideRepository(this))
    }

    private lateinit var id: String
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailViewModel.logToken()

        id = intent.getStringExtra("PLACE_ID") ?: return

        lifecycleScope.launch {
            detailViewModel.getSession().collect { userModel ->
                token = userModel.token
                detailViewModel.getPlaceDetail(id, token)
            }
        }
        setupObserver()
    }

    private fun setupObserver() {
        detailViewModel.placeDetail.observe(this) { placeDetail ->
            findViewById<TextView>(R.id.descTextView).text = placeDetail.description
            findViewById<TextView>(R.id.ratingTextView).text = placeDetail.rating.toString()
            findViewById<TextView>(R.id.categoryTextView).text = placeDetail.category
            findViewById<TextView>(R.id.kotaTextView).text = placeDetail.city
            Glide.with(this)
                .load(placeDetail.image_url)
                .into(findViewById<ImageView>(R.id.colorImageView))
        }
    }
}