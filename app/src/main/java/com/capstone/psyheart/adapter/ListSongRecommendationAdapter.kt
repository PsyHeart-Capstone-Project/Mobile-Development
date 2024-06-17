package com.capstone.psyheart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.psyheart.databinding.ItemSongBinding
import com.capstone.psyheart.model.Recommendation

class ListSongRecommendationAdapter(private val songs: List<Recommendation>) : RecyclerView.Adapter<ListSongRecommendationAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    inner class SongViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Recommendation) {
            binding.songTitleTextView.text = song.url
            binding.songArtistTextView.text = song.name
            binding.timeSongTextView.text = song.duration
        }
    }
}
