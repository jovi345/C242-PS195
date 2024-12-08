package com.app.travel.ui.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.app.travel.R
import com.app.travel.data.database.Wishlist
import com.app.travel.data.repo.Injection
import com.app.travel.databinding.ActivityDetailBinding
import com.app.travel.ui.ViewModelFactory
import com.app.travel.ui.auth.login.LoginViewModel
import com.app.travel.ui.wishlist.WishlistViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailActivity() : AppCompatActivity() {
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory(Injection.provideRepository(this))
    }
    private lateinit var binding: ActivityDetailBinding
    private lateinit var id: String
    private lateinit var token: String
    private var isInWishlist: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: WishlistViewModel by viewModels()

        detailViewModel.logToken()

        id = intent.getStringExtra("PLACE_ID") ?: return

        lifecycleScope.launch {
            detailViewModel.getSession().collect { userModel ->
                token = userModel.token
                detailViewModel.getPlaceDetail(id, token)
            }
        }

        // Cek jika tempat ini sudah ada di wishlist
        viewModel.allWishlist.observe(this) { wishlist ->
            isInWishlist = wishlist.any { it.id == id }
            binding.fabFavorite.setImageResource(
                if (isInWishlist) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24
            )
        }

        binding.fabFavorite.setOnClickListener {
            val placeName = binding.nameTextView.text.toString()
            val imageUrl = detailViewModel.placeDetail.value?.image_url

            if (isInWishlist) {
                viewModel.removeFromWishlist(Wishlist(id = id))
            } else {
                viewModel.addToWishlist(Wishlist(id = id, placeName = placeName, imageUrl = imageUrl))
            }
        }

        setupObserver()
    }

    private fun setupObserver() {
        detailViewModel.placeDetail.observe(this) { placeDetail ->
            binding.nameTextView.text = placeDetail.place_name
            binding.provinsiTextView.text = placeDetail.state
            binding.kotaTextView.text = placeDetail.city
            binding.ratingTextView.text = placeDetail.rating.toString()
            binding.categoryTextView.text = placeDetail.category
            binding.descTextView.text = HtmlCompat.fromHtml(placeDetail.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

            Glide.with(this)
                .load(placeDetail.image_url)
                .error(R.drawable.example_image)
                .into(binding.previewImageView)
        }
    }
}