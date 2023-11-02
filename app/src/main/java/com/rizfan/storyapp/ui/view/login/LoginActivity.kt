package com.rizfan.storyapp.ui.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.rizfan.storyapp.data.Result
import com.rizfan.storyapp.data.pref.UserModel
import com.rizfan.storyapp.data.response.LoginResponse
import com.rizfan.storyapp.databinding.ActivityLoginBinding
import com.rizfan.storyapp.ui.model.LoginViewModel
import com.rizfan.storyapp.ui.model.ViewModelFactory
import com.rizfan.storyapp.ui.view.main.MainActivity
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
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
        binding.loginButton.setOnClickListener {
            try {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                if (email.isEmpty()) {
                    binding.emailEditText.error = "Email tidak boleh kosong"
                } else if (password.isEmpty()) {
                    binding.passwordEditText.error = "Password tidak boleh kosong"
                } else {
                    lifecycleScope.launch {
                        viewModel.login(email, password).observe(this@LoginActivity) { result ->
                            if (result != null) {
                                when (result) {
                                    is Result.Loading -> {
                                        showLoading(true)
                                    }

                                    is Result.Success -> {
                                        showLoading(false)
                                        showToast("Login berhasil!")
                                        lifecycleScope.launch {
                                            save(
                                                UserModel(
                                                    result.data.loginResult?.token.toString(),
                                                    result.data.loginResult?.name.toString(),
                                                    result.data.loginResult?.userId.toString(),
                                                    true
                                                )
                                            )
                                        }
                                    }

                                    is Result.Error -> {
                                        showLoading(false)
                                        showToast(result.error)
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (e: HttpException) {
                showLoading(false)
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                showToast(errorResponse.message)
            }

        }
    }

    private fun save(session: UserModel) {
        lifecycleScope.launch {
            viewModel.saveSession(session)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ViewModelFactory.clearInstance()
            startActivity(intent)
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) VISIBLE else GONE
        binding.loginButton.isEnabled = !isLoading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}