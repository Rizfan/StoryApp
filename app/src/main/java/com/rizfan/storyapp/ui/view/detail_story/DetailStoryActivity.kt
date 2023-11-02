package com.rizfan.storyapp.ui.view.detail_story

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rizfan.storyapp.data.response.ListStoryItem
import com.rizfan.storyapp.databinding.ActivityDetailStoryBinding
import com.rizfan.storyapp.ui.model.MainViewModel
import com.rizfan.storyapp.ui.model.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private var _binding: ActivityDetailStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val story = intent.getParcelableExtra<ListStoryItem>(DETAIL_STORY) as ListStoryItem
        setupData(story)
    }

    private fun setupData(storyItem: ListStoryItem) {
        Glide.with(applicationContext).load(storyItem.photoUrl).into(binding.ivAvatar)
        binding.tvName.text = storyItem.name
        binding.tvDesc.text = storyItem.description
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }

}