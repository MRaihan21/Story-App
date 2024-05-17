package com.rad21.storyapp.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.rad21.storyapp.MainActivity
import com.rad21.storyapp.data.PreferenceManager
import com.rad21.storyapp.data.User
import com.rad21.storyapp.data.UserRepository
import com.rad21.storyapp.data.dataStore
import com.rad21.storyapp.data.retrofit.ApiConfig
import com.rad21.storyapp.databinding.ActivityLoginBinding
import com.rad21.storyapp.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var preferenceManager : PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        preferenceManager = PreferenceManager.getInstance(this.dataStore)

        viewModel =
            LoginViewModel(UserRepository.getInstance(preferenceManager, ApiConfig.getApiService()))

        CoroutineScope(Dispatchers.Main).launch {
            val user = preferenceManager.getSession().first()
            if (user.isLogin) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        binding.btnDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {

            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.isLoading.observe(this) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            viewModel.login(email, password).observe(this) { response ->
                if (response.error == false) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val saveToken = async(Dispatchers.IO) {
                            preferenceManager.saveSession(
                                User(
                                    response.loginResult?.name.toString(),
                                    AUTH_KEY + (response.loginResult?.token.toString()),
                                    true
                                )
                            )
                        }

                        saveToken.await()

                        Log.d(
                            "Login Activity",
                            "response ${response.loginResult?.name} ${response.loginResult?.token}"

                        )

                    }

                    val builder = AlertDialog.Builder(this@LoginActivity)

                    builder.setTitle("Login Successful")
                    builder.setMessage("Welcome ${response.loginResult?.name}")
                    builder.setPositiveButton("OK") { _, _ ->
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                    val dialog = builder.create()
                    dialog.show()
                }

            }
        }

        viewModel.login.observe(this) { response ->
            CoroutineScope(Dispatchers.Main).launch {
                val builder = AlertDialog.Builder(this@LoginActivity)
                builder.setTitle("Login Failed")
                builder.setMessage(response)
                builder.setPositiveButton("OK") { _, _ ->

                }
                val dialog = builder.create()
                dialog.show()
            }
        }


    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvTitleLogin, View.ALPHA, 1f).setDuration(1000)
        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val buttonMasuk = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val view = ObjectAnimator.ofFloat(binding.view, View.ALPHA, 1f).setDuration(500)
        val textLogin = ObjectAnimator.ofFloat(binding.tvTextLogin, View.ALPHA, 1f).setDuration(500)
        val buttonDaftar = ObjectAnimator.ofFloat(binding.btnDaftar, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, email, password, buttonMasuk, view, textLogin, buttonDaftar)
            start()
        }

    }

    companion object {
        private const val AUTH_KEY = "Bearer "
    }

}