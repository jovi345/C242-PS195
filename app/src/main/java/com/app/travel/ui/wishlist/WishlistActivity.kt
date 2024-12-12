package com.app.travel.ui.wishlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.travel.data.response.RecommendationResponse
import com.app.travel.databinding.ActivityWishlistBinding
import com.app.travel.ui.detail.DetailActivity
import com.app.travel.ui.home.RecommendationAdapter

class WishlistActivity : AppCompatActivity() {

    private val viewModel: WishlistViewModel by viewModels()
    private lateinit var adapter: RecommendationAdapter
    private lateinit var binding: ActivityWishlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        adapter = RecommendationAdapter(emptyList()) { id ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("PLACE_ID", id)
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@WishlistActivity)
            adapter = this@WishlistActivity.adapter
        }

        viewModel.allWishlist.observe(this) { wishlist ->
            val recommendations = wishlist.map { item ->
                RecommendationResponse(
                    id = item.id,
                    placeName = item.placeName,
                    imageUrl = item.imageUrl,
                )
            }
            adapter.updateData(recommendations)
        }

    }
}