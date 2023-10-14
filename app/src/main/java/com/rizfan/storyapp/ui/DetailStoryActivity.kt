package com.rizfan.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.rizfan.storyapp.R
import com.rizfan.storyapp.data.response.ListStoryItem
import com.rizfan.storyapp.databinding.ActivityDetailStoryBinding
import com.rizfan.storyapp.model.MainViewModel
import com.rizfan.storyapp.model.ViewModelFactory
import com.rizfan.storyapp.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class DetailStoryActivity : AppCompatActivity() {
    private var _binding: ActivityDetailStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val story = intent.getParcelableExtra<ListStoryItem>(DETAIL_STORY) as ListStoryItem
        setupData(story)
    }

    private fun setupData(storyItem: ListStoryItem) {
        Glide.with(applicationContext)
            .load(storyItem.photoUrl)
            .into(binding.ivAvatar)
        binding.tvName.text = storyItem.name
        binding.tvDesc.text = storyItem.description
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                lifecycleScope.launch {
                    viewModel.logout()
                }
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            R.id.settings->{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}