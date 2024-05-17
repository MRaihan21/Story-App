package com.rad21.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rad21.storyapp.data.response.ListStoryItem
import com.rad21.storyapp.databinding.ItemStoryBinding
import com.rad21.storyapp.ui.DetailStoryActivity

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryAdapter.StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryAdapter.StoryViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)

        Log.d("StoryListAdapter", "onBindViewHolder: $position")
    }

    class StoryViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(story: ListStoryItem) {
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
            binding.tvItemName.text = story.name
            binding.tvItemDescription.text = story.description

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra("story", story)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "image"),
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.tvItemDescription, "description")

                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}