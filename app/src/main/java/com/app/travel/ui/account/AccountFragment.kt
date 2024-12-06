package com.app.travel.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.travel.R
import com.app.travel.ui.ViewModelFactory
import com.app.travel.ui.auth.login.LoginActivity
import com.app.travel.ui.auth.login.LoginViewModel
import com.google.android.material.button.MaterialButton

class AccountFragment : Fragment() {
    private val loginViewModel: LoginViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val logoutButton: MaterialButton = requireView().findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            loginViewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        return inflater.inflate(R.layout.fragment_account, container, false)

    }

}