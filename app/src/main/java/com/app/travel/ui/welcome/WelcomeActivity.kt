package com.app.travel.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.travel.MainActivity
import com.app.travel.R
import com.app.travel.ui.ViewModelFactory
import com.app.travel.ui.auth.login.LoginActivity
import com.app.travel.ui.auth.login.LoginViewModel
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        supportActionBar?.hide()

        val btnStart: Button = findViewById(R.id.btnStart)
        btnStart.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        lifecycleScope.launch {
            loginViewModel.getSession().collect { user ->
                if (user.isLogin) {
                    // User is already logged in, proceed to the main screen
                    val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // User is not logged in, redirect to login screen
                    val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}