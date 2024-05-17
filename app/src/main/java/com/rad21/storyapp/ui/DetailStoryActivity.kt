package com.rad21.storyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.rad21.storyapp.data.response.ListStoryItem
import com.rad21.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryItem>("story")

        if (story != null) {
            Glide.with(this)
                .load(story.photoUrl)
                .into(binding.ivDetailPhoto)
            binding.tvDetailName.text = story.name
            binding.tvDetailDescription.text = story.description
        }

    }
}