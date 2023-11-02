package com.rizfan.storyapp.ui.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.rizfan.storyapp.data.Result
import com.rizfan.storyapp.data.response.RegisterResponse
import com.rizfan.storyapp.databinding.ActivityRegisterBinding
import com.rizfan.storyapp.ui.model.RegisterViewModel
import com.rizfan.storyapp.ui.model.ViewModelFactory
import com.rizfan.storyapp.ui.view.login.LoginActivity
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, android.view.View.TRANSLATION_X, -30f, 30f)
            .apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, android.view.View.ALPHA, 1f)
            .setDuration(100)
        val namaTextView = ObjectAnimator.ofFloat(binding.nameTextView, android.view.View.ALPHA, 1f)
            .setDuration(100)
        val namaEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, android.view.View.ALPHA, 1f)
                .setDuration(100)
        val namaEditText = ObjectAnimator.ofFloat(binding.nameEditText, android.view.View.ALPHA, 1f)
            .setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, android.view.View.ALPHA, 1f)
                .setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, android.view.View.ALPHA, 1f)
                .setDuration(100)
        val emailEditText =
            ObjectAnimator.ofFloat(binding.emailEditText, android.view.View.ALPHA, 1f)
                .setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, android.view.View.ALPHA, 1f)
                .setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, android.view.View.ALPHA, 1f)
                .setDuration(100)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding.passwordEditText, android.view.View.ALPHA, 1f)
                .setDuration(100)
        val signupButton = ObjectAnimator.ofFloat(binding.signupButton, android.view.View.ALPHA, 1f)
            .setDuration(100)

        val name = AnimatorSet().apply {
            playTogether(
                namaEditTextLayout,
                namaEditText
            )
        }
        val email = AnimatorSet().apply {
            playTogether(
                emailEditTextLayout,
                emailEditText
            )
        }
        val password = AnimatorSet().apply {
            playTogether(
                passwordEditTextLayout,
                passwordEditText
            )
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                namaTextView,
                name,
                emailTextView,
                email,
                passwordTextView,
                password,
                signupButton
            )
            start()
        }
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
        binding.signupButton.setOnClickListener {
            showLoading(true)
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (name.isEmpty()) {
                binding.nameEditTextLayout.error = "Nama tidak boleh kosong"
            } else if (email.isEmpty()) {
                binding.emailEditTextLayout.error = "Email tidak boleh kosong"
            } else if (password.isEmpty()) {
                binding.passwordEditTextLayout.error = "Password tidak boleh kosong"
            }

            lifecycleScope.launch {
                try {
                    viewModel.register(name, email, password).observe(this@RegisterActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }

                                is Result.Success -> {
                                    showLoading(false)
                                    showToast("Register berhasil!")
                                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }

                                is Result.Error -> {
                                    showLoading(false)
                                    showToast(result.error)
                                }
                            }
                        }
                    }
                } catch (e: HttpException) {
                    showLoading(false)
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    showToast(errorResponse.message)
                }
            }


        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.signupButton.isEnabled = !isLoading
    }
}