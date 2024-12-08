package com.app.travel.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.travel.R
import com.app.travel.data.response.RecommendationResponse
import com.app.travel.databinding.ItemRecomendationRowBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class RecommendationAdapter(
    private var recommendations: List<RecommendationResponse?>,
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

    fun updateData(newRecommendations: List<RecommendationResponse?>) {
        val diffCallback = RecommendationDiffCallback(recommendations, newRecommendations)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        recommendations = newRecommendations // Perbarui referensi
        diffResult.dispatchUpdatesTo(this)
    }

    // Kelas ViewHolder tetap sama
    inner class RecommendationViewHolder(private val binding: ItemRecomendationRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendation: RecommendationResponse) {
            with(binding) {
                Glide.with(root)
                    .load(recommendation.imageUrl)
                    .error(R.drawable.example_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Optimisasi caching
                    .into(imageView)

                textViewTitle.text = recommendation.placeName
//                textViewDescription.text = recommendation.description
                textViewCity.text = recommendation.city
                textViewCategory.text = recommendation.category

                root.setOnClickListener {
                    recommendation.id?.let { onItemClick(it.toString()) }
                }
            }
        }
    }

    private class RecommendationDiffCallback(
        private val oldList: List<RecommendationResponse?>,
        private val newList: List<RecommendationResponse?>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition]?.id == newList[newItemPosition]?.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

