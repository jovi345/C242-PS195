package com.app.travel.ui.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.travel.R
import com.app.travel.data.response.CategoryResponseItem
import com.app.travel.data.response.SurveyResponseItem
import com.app.travel.databinding.ItemRecomendationRowBinding
import com.bumptech.glide.Glide

class SurveyAdapter(private var items: List<SurveyResponseItem>,
private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<SurveyAdapter.SurveyResultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyResultViewHolder {
        val binding = ItemRecomendationRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SurveyResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SurveyResultViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<SurveyResponseItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class SurveyResultViewHolder(private val binding: ItemRecomendationRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SurveyResponseItem) {
            binding.textViewTitle.text = item.placeName
            binding.textViewCity.text = item.city
            binding.textViewCategory.text = item.category
            Glide.with(binding.imageView.context)
                .load(item.imageUrl)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                item.id?.let { id -> onItemClick(id) }
            }
        }
    }
}