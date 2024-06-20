package com.capstone.psyheart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.psyheart.databinding.ItemSongBinding
import com.capstone.psyheart.model.Songs

class ListSongAdapter(private val listStories: List<Songs>) :
    RecyclerView.Adapter<ListSongAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        var title: TextView = binding.songTitleTextView
        var artist: TextView = binding.songArtistTextView
        var duration: TextView = binding.timeSongTextView
        var buttonPlay: ImageView = binding.iconPlayMusic
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemSongBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val song: Songs = listStories[position]

        holder.title.text = song.name
        holder.artist.text = song.artist
        holder.duration.text = song.duration
        holder.buttonPlay.setOnClickListener {
            onItemClickCallback.onItemClicked(listStories[holder.adapterPosition], position)
        }
    }

    override fun getItemCount(): Int = listStories.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Songs, position: Int)
    }
}