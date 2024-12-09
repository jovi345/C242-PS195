package com.app.travel.ui.explore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.travel.R
import com.app.travel.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false)


        binding.mbtiButton.setOnClickListener {
            val url = "https://www.16personalities.com/free-personality-test"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        return binding.root
    }


}