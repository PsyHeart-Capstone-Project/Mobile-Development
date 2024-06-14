package com.capstone.psyheart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.psyheart.databinding.ItemDiscoverBinding
import com.capstone.psyheart.model.CategoryItem

class ListSongCategoryAdapter(private val listStories: List<CategoryItem>) : RecyclerView.Adapter<ListSongCategoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(binding: ItemDiscoverBinding) : RecyclerView.ViewHolder(binding.root) {
        var imageDiscover: ImageView = binding.imageDiscover
        var textDiscover: TextView = binding.textDiscover
        var discoverCardView: CardView = binding.discoverCardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemDiscoverBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val category: CategoryItem = listStories[position]
        Glide.with(holder.itemView.context)
            .load(category.imageUrl)
            .fitCenter()
            .into(holder.imageDiscover)
        holder.textDiscover.text = category.name

        holder.discoverCardView.setOnClickListener {
            onItemClickCallback.onItemClicked(listStories[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listStories.size

    interface OnItemClickCallback {
        fun onItemClicked(data: CategoryItem)
    }
}
