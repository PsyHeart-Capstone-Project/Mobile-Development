package com.capstone.psyheart.ui.discover_detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.psyheart.databinding.ActivityDetailDiscoverBinding
import com.capstone.psyheart.ui.home_detail.HomeDetailActivity

class DiscoverDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDiscoverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiscoverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playHandler()
    }

    private fun playHandler() {
        binding.playlistRecyclerView.setOnClickListener {
            val intent = Intent(this@DiscoverDetailActivity, HomeDetailActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
