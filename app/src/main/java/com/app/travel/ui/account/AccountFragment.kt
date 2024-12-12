package com.app.travel.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.app.travel.R
import com.app.travel.data.pref.UserModel
import com.app.travel.data.pref.dataStore
import com.app.travel.data.repo.Injection
import com.app.travel.data.repo.UserRepository
import com.app.travel.databinding.FragmentAccountBinding
import com.app.travel.ui.ViewModelFactory
import com.app.travel.ui.auth.login.LoginActivity
import com.app.travel.ui.auth.login.LoginViewModel
import com.app.travel.ui.home.HomeViewModel
import com.app.travel.ui.wishlist.WishlistActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        val repository = Injection.provideRepository(requireContext())
        homeViewModel = ViewModelProvider(this, ViewModelFactory(repository))[HomeViewModel::class.java]

        val switchTheme = binding.darkModeSwitch
        accountViewModel = ViewModelProvider(this, ViewModelFactory(repository))[AccountViewModel::class.java]

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

        accountViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            accountViewModel.saveThemeSetting(isChecked)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
