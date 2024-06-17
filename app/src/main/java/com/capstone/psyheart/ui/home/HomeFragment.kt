package com.capstone.psyheart.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.psyheart.adapter.ListSongAdapter
import com.capstone.psyheart.databinding.FragmentHomeBinding
import com.capstone.psyheart.model.Songs
import com.capstone.psyheart.ui.ViewModelFactory
import com.capstone.psyheart.ui.discover_detail.DiscoverDetailActivity
import com.capstone.psyheart.ui.guide.GuideActivity
import com.capstone.psyheart.ui.home_detail.HomeDetailActivity
import com.capstone.psyheart.ui.register.RegisterActivity
import com.capstone.psyheart.utils.ResultData

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var listSongAdapter: ListSongAdapter
    private lateinit var factory: ViewModelFactory
    private val viewModel: HomeViewModel by viewModels { factory }
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        factory = ViewModelFactory.getInstance(requireContext())
        root = binding.root

        viewModel.getSongRecommendation()
        handleSong()
        handleUpdateQuestionnaire(requireContext())
        return root
    }

    private fun handleSong() {
        viewModel.resultSongs.observe(viewLifecycleOwner) { result ->
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
                        setupViewHeader(result.data.data.mood)
                        setupView(requireContext(), result.data.data.recommendation)
                    }
                }
            }
        }
    }

    private fun loadingHandler(isLoading: Boolean) {
        // Handle loading state (e.g., show/hide progress bar)
    }

    private fun setupViewHeader(mood: String) {
        binding.homeTitleText.append(" $mood")
    }

    private fun setupView(context: Context, songs: List<Songs>) {
        val discoverRv = binding.playlistRecyclerView
        discoverRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        listSongAdapter = ListSongAdapter(songs)
        listSongAdapter.setOnItemClickCallback(object : ListSongAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Songs, position: Int) {
                val intent = Intent(context, HomeDetailActivity::class.java).apply {
                    putParcelableArrayListExtra(DiscoverDetailActivity.MUSIC_PLAYER, ArrayList(songs))
                    putExtra("CURRENT_SONG_INDEX", position)
                }
                context.startActivity(intent)
            }
        })
        discoverRv.adapter = listSongAdapter
    }

    private fun navigateToGuide(context: Context) {
        val intent = Intent(context, GuideActivity::class.java).apply {
            putExtra(RegisterActivity.IS_NEW_USER, false)
        }
        context.startActivity(intent)
    }

    private fun handleUpdateQuestionnaire(context: Context) {
        binding.updateButton.setOnClickListener {
            navigateToGuide(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
