package com.app.travel.ui.auth.register

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.travel.R
import com.app.travel.data.pref.UserModel
import com.app.travel.data.repo.Injection
import com.app.travel.databinding.ActivityRegisterBinding
import com.app.travel.databinding.LayoutRegisterBinding
import com.app.travel.ui.ViewModelFactory
import com.app.travel.ui.auth.login.LoginActivity
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var selectedCity: String
    private var email: String = ""
    private var username: String = ""
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory(Injection.provideRepository(this))
    }
    private val registerButton: Button by lazy { findViewById(R.id.registerButton) }
    private val tvLogin: TextView by lazy { findViewById(R.id.textViewLogin) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }



        setupView()
        setupObserver()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        val spinner: Spinner = findViewById(R.id.spinnerCities)
        val cities = resources.getStringArray(R.array.cities_array).toList()
        val arrayAdapter = CustomArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    selectedCity = parent.getItemAtPosition(position).toString().toLowerCase(Locale.ROOT)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        registerButton.setOnClickListener {
            val regisUname = findViewById<TextView>(R.id.regisUname)
            username = regisUname.text.toString().trim()

            val regisEmail = findViewById<TextView>(R.id.regisEmail)
            email = regisEmail.text.toString().trim()

            val regisPass = findViewById<TextView>(R.id.regisPass)
            val password = regisPass.text.toString().trim()

            val regisConfirmPass = findViewById<TextView>(R.id.regisConfirmPass)
            val confirmPassword = regisConfirmPass.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerViewModel.register(username, email, password, confirmPassword, selectedCity)
        }
    }

    private fun setupObserver() {
        registerViewModel.registerResult.observe(this) { message ->
            AlertDialog.Builder(this).apply {
                setTitle("Register Status")
                setMessage(message)
                setPositiveButton("OK") { _, _ ->
                    if (message?.contains("success", true) == true) {
                        // Simpan lokasi ke sesi setelah registrasi berhasil
                        registerViewModel.saveSession(
                            UserModel(
                                email = email,
                                username = username,
                                userLocation = selectedCity,
                                token = "",
                                isLogin = true
                            )
                        )
                        finish() // Close Register Activity dan kembali ke halaman Login
                    }
                }
                create()
                show()
            }
        }

        registerViewModel.isLoading.observe(this) { isLoading ->
            registerButton.isEnabled = !isLoading
        }
    }
}