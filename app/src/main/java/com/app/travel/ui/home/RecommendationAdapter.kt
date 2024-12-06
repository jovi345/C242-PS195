package com.app.travel.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.travel.R
import com.app.travel.data.response.RecommendationResponse
import com.app.travel.databinding.ItemRecomendationRowBinding
import com.bumptech.glide.Glide

class RecommendationAdapter(private var recommendations: List<RecommendationResponse?>,
                            private val onItemClick: (String) -> Unit
    ) : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecomendationRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        recommendations[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = recommendations.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newRecommendations: List<RecommendationResponse?>) {
        recommendations = newRecommendations
        println("Adapter updated with new data: $newRecommendations")
        notifyDataSetChanged()
    }

    inner class RecommendationViewHolder(private val binding: ItemRecomendationRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendation: RecommendationResponse) {
            Glide.with(binding.root)
                .load(recommendation.imageUrl)
                .error(R.drawable.example_image)
                .into(binding.imageView)
            binding.textViewTitle.text = recommendation.placeName
            binding.textViewDescription.text = recommendation.description
            binding.textViewCity.text = recommendation.city
            binding.textViewCategory.text = recommendation.category

            itemView.setOnClickListener {
                onItemClick(recommendation.id.toString())
            }
        }
    }
}