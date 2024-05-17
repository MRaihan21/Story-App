package com.rad21.storyapp.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.rad21.storyapp.MainActivity
import com.rad21.storyapp.databinding.ActivityRegisterBinding
import com.rad21.storyapp.viewmodel.RegisterViewModel
import com.rad21.storyapp.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[RegisterViewModel::class.java]

        binding.btnSignUp.setOnClickListener {

            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            viewModel.isLoading.observe(this) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }


            Log.d("RegisterActivity", "Before registrasi: $name, $email, $password")
            viewModel.register(name, email, password)
            Log.d("RegisterActivity", "After registrasi")
            viewModel.registration.observe(this) { response ->
                if (response.error == false) {
                    Log.d("RegisterActivity", "onCreate: ${response.message}")
                    binding.edRegisterName.text?.clear()
                    binding.edRegisterEmail.text?.clear()
                    binding.edRegisterPassword.text?.clear()

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Success")
                    builder.setMessage("Register Successful")
                    builder.setPositiveButton("Continue") { _, _ ->
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }

        }

        viewModel.isRegist.observe(this) { response ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Register Failed")
            builder.setMessage(response)
            builder.setNegativeButton("OK") { _, _ ->

            }
            val dialog = builder.create()
            dialog.show()

        }

        binding.btnMasuk.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }



    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvTitleSignup, View.ALPHA, 1f).setDuration(1000)
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val buttonDaftar = ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 1f).setDuration(500)
        val view2 = ObjectAnimator.ofFloat(binding.view2, View.ALPHA, 1f).setDuration(500)
        val textSignup = ObjectAnimator.ofFloat(binding.tvTextSignup, View.ALPHA, 1f).setDuration(500)
        val buttonMasuk = ObjectAnimator.ofFloat(binding.btnMasuk, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, name, email, password, buttonDaftar, view2, textSignup, buttonMasuk)
            start()
        }

    }
}