package com.app.travel.ui.destination

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.travel.R
import com.app.travel.data.repo.Injection
import com.app.travel.databinding.ActivityDestinationBinding
import com.app.travel.ui.ViewModelFactory

class DestinationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDestinationBinding
    private val destinationViewModel: DestinationViewModel by viewModels {
        ViewModelFactory(Injection.provideRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}