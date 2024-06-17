package com.capstone.psyheart.ui.discover_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.psyheart.adapter.ListSongAdapter
import com.capstone.psyheart.adapter.ListSongCategoryAdapter
import com.capstone.psyheart.databinding.ActivityDetailDiscoverBinding
import com.capstone.psyheart.model.CategoryItem
import com.capstone.psyheart.model.Detail
import com.capstone.psyheart.model.Songs
import com.capstone.psyheart.ui.ViewModelFactory
import com.capstone.psyheart.ui.discover.DiscoverFragment
import com.capstone.psyheart.ui.discover.DiscoverViewModel
import com.capstone.psyheart.ui.guide.GuideActivity
import com.capstone.psyheart.ui.home_detail.HomeDetailActivity
import com.capstone.psyheart.ui.login.LoginViewModel
import com.capstone.psyheart.utils.ResultData

class DiscoverDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDiscoverBinding

    private lateinit var listSongAdapter: ListSongAdapter
    private val viewModel by viewModels<DiscoverDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiscoverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryID = intent.getIntExtra(DiscoverFragment.DISCOVER_FRAGMENT, 0)

        viewModel.getSongCategories(categoryID)
        handleSong()
    }

    private fun handleSong() {
        viewModel.resultSongs.observe(this@DiscoverDetailActivity) { result ->
            if (result != null) {
                when (result) {
                    is ResultData.Loading -> {
                        loadingHandler(true)
                    }

                    is ResultData.Failure -> {
                        loadingHandler(false)
                    }

                    is ResultData.Success -> {
                        loadingHandler(false)
                        setupViewHeader(result.data.data.detail)
                        setupView(this@DiscoverDetailActivity, result.data.data.songs)
                    }
                }
            }
        }
    }

    private fun loadingHandler(isLoading: Boolean) {
        // Handle loading state (e.g., show/hide progress bar)
    }

    private fun setupViewHeader(detail: Detail) {
        binding.homeTitleText.text = detail.name
        binding.descHomeText.text = detail.description
    }

    private fun setupView(context: Context, songs: List<Songs>) {
        val discoverRv = binding.playlistRecyclerView
        discoverRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        listSongAdapter = ListSongAdapter(songs)
        listSongAdapter.setOnItemClickCallback(object : ListSongAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Songs, position: Int) {
                val intent = Intent(context, HomeDetailActivity::class.java).apply {
                    putParcelableArrayListExtra(MUSIC_PLAYER, ArrayList(songs))
                    putExtra("CURRENT_SONG_INDEX", position)
                }
                context.startActivity(intent)
            }
        })
        discoverRv.adapter = listSongAdapter
    }

    companion object {
        const val MUSIC_PLAYER = "music_player"
    }
}
