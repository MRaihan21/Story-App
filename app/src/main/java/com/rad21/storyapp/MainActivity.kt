package com.rad21.storyapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rad21.storyapp.adapter.StoryAdapter
import com.rad21.storyapp.data.PreferenceManager
import com.rad21.storyapp.data.dataStore
import com.rad21.storyapp.databinding.ActivityMainBinding
import com.rad21.storyapp.ui.AddStoryActivity
import com.rad21.storyapp.ui.auth.LoginActivity
import com.rad21.storyapp.viewmodel.MainViewModel
import com.rad21.storyapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: StoryAdapter

    @SuppressLint("StringFormatInvalid")
    private suspend fun setUpToolbar() {
        preferenceManager.getUsername.collect {name ->
            binding.materialToolbar.title = name
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        moveTaskToBack(true)
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[MainViewModel::class.java]

        preferenceManager = PreferenceManager.getInstance(this.dataStore)
        adapter = StoryAdapter()
        val recyclerView = binding.rvStory
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        CoroutineScope(Dispatchers.Main).launch {
            setUpToolbar()
        }

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
        binding.materialToolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.action_logout -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Logging out")
                    builder.setMessage("Do you want to log out?")
                    builder.setPositiveButton("Yes") { _, _ ->
                        CoroutineScope(Dispatchers.IO).launch {
                            preferenceManager.logout()
                        }

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    builder.setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(menuItem)


                }
            }

        }

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                viewModel.getAllStories(user.token)
                viewModel.uploadResponse.observe(this) { response ->
                    if (response.error == false) {
                        Log.d("MainActivity", "onCreate: ${response.message}")
                        adapter.submitList(response.listStory)
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.d("MainActivity", "onCreate: ${response.message}")
                    }
                }
            }
        }


    }
}