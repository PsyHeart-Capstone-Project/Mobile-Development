package com.capstone.psyheart.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.psyheart.adapter.ListSongRecommendationAdapter
import com.capstone.psyheart.api.ApiService
import com.capstone.psyheart.databinding.FragmentHomeBinding
import com.capstone.psyheart.preference.UserPreference
import com.capstone.psyheart.ui.guide.GuideActivity
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var apiService: ApiService
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreference = UserPreference(requireContext())

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        binding.playlistRecyclerView.layoutManager = LinearLayoutManager(context)

        fetchSongRecommendations()

        binding.playButton.setOnClickListener {
            navigateToGuideForUpdate()
        }
    }

    private fun fetchSongRecommendations() {
        lifecycleScope.launch {
            val token = userPreference.getUser().token
            try {
                val response = apiService.songRecommendation("Bearer $token")

                if (response.status == "success") {
                    val adapter = ListSongRecommendationAdapter(response.data.recommendation)
                    binding.playlistRecyclerView.adapter = adapter
                } else {
                    // Handle the error
                    // For example, show a toast or log the error
                }
            } catch (e: Exception) {
                // Handle exceptions (e.g., network errors)
                e.printStackTrace()
            }
        }
    }

    private fun navigateToGuideForUpdate() {
        val intent = Intent(requireContext(), GuideActivity::class.java).apply {
            putExtra(GuideActivity.EXTRA_UPDATE_FLOW, true)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
