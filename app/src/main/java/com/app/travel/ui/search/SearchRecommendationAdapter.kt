package com.app.travel.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.travel.R
import com.app.travel.data.response.CategoryResponseItem
import com.app.travel.databinding.ItemRecomendationRowBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class SearchRecommendationAdapter(
    private var recommendations: List<CategoryResponseItem?>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SearchRecommendationAdapter.RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecomendationRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        recommendations[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = recommendations.size

    fun updateData(newRecommendations: List<CategoryResponseItem?>) {
        val diffCallback = RecommendationDiffCallback(recommendations, newRecommendations)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        recommendations = newRecommendations
        diffResult.dispatchUpdatesTo(this)
    }

    inner class RecommendationViewHolder(private val binding: ItemRecomendationRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendation: CategoryResponseItem) {
            with(binding) {
                Glide.with(root)
                    .load(recommendation.imageUrl)
                    .error(R.drawable.example_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)

                textViewTitle.text = recommendation.placeName
                textViewCity.text = recommendation.city
                textViewCategory.text = recommendation.category

                root.setOnClickListener {
                    recommendation.id?.let { onItemClick(it.toString()) }
                }
            }
        }
    }

    private class RecommendationDiffCallback(
        private val oldList: List<CategoryResponseItem?>,
        private val newList: List<CategoryResponseItem?>
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