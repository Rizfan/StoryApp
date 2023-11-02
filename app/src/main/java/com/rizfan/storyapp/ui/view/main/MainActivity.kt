package com.rizfan.storyapp.ui.view.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizfan.storyapp.R
import com.rizfan.storyapp.databinding.ActivityMainBinding
import com.rizfan.storyapp.ui.adapter.LoadingStateAdapter
import com.rizfan.storyapp.ui.adapter.StoryAdapter
import com.rizfan.storyapp.ui.model.MainViewModel
import com.rizfan.storyapp.ui.model.ViewModelFactory
import com.rizfan.storyapp.ui.view.maps.MapsActivity
import com.rizfan.storyapp.ui.view.upload_story.UploadActivity
import com.rizfan.storyapp.ui.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        val adapter = StoryAdapter()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                lifecycleScope.launch {
                    viewModel.getStory.observe(this@MainActivity) { result ->
                        adapter.submitData(lifecycle, result)
                        showLoading(false)
                    }
                }
            }
        }
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter =
                adapter.withLoadStateHeaderAndFooter(
                    header = LoadingStateAdapter { adapter.retry() },
                    footer = LoadingStateAdapter { adapter.retry() }
                )
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                lifecycleScope.launch {
                    viewModel.logout()
                }
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }

            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }

            R.id.settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}