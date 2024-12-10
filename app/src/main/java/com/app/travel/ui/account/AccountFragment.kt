package com.app.travel.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.travel.R
import com.app.travel.data.pref.UserModel
import com.app.travel.data.pref.UserPreference
import com.app.travel.data.pref.dataStore
import com.app.travel.data.repo.Injection
import com.app.travel.databinding.FragmentAccountBinding
import com.app.travel.ui.ViewModelFactory
import com.app.travel.ui.auth.login.LoginActivity
import com.app.travel.ui.auth.login.LoginViewModel
import com.app.travel.ui.home.HomeViewModel
import com.app.travel.ui.wishlist.WishlistActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        val repository = Injection.provideRepository(requireContext())
        homeViewModel = ViewModelProvider(this, ViewModelFactory(repository))[HomeViewModel::class.java]

        // Tombol Logout
        binding.logoutButton.setOnClickListener {
            loginViewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            requireActivity().overridePendingTransition(R.transition.animation_enter, R.transition.animation_exit)
        }

        // Navigasi ke Wishlist
        binding.wishlistTextView.setOnClickListener {
            val intent = Intent(requireContext(), WishlistActivity::class.java)
            startActivity(intent)
        }
        homeViewModel.getSession().observe(viewLifecycleOwner) { user: UserModel ->
            binding.welcomeText.text = getString(R.string.welcome_home, user.username)
            binding.emailTextView.text = user.email
        }

        binding.darkModeSwitch.isChecked = isDarkModeEnabled()

        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            saveDarkModePreference(isChecked) // Simpan preferensi pengguna
        }

        return binding.root
    }

    private fun isDarkModeEnabled(): Boolean {
        return runBlocking {
            UserPreference.getInstance(requireContext().dataStore).isDarkModeEnabled().first()
        }
    }

    private fun saveDarkModePreference(isEnabled: Boolean) {
        lifecycleScope.launch {
            UserPreference.getInstance(requireContext().dataStore).saveDarkModePreference(isEnabled)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
