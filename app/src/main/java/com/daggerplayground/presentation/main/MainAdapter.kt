package com.daggerplayground.presentation.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daggerplayground.R
import com.daggerplayground.domain.Post
import kotlinx.android.synthetic.main.item_template_post.view.*

class MainAdapter(
    private val posts: List<Post>,
    private val callback: IMain
): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_template_post, parent, false))

    override fun getItemCount(): Int = posts.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.itemView.tvTitle.text = posts[position].title
        holder.itemView.tvBody.text = posts[position].body
        holder.itemView.tvToggleLike.text = "LIKE" + if(posts[position].isLiked == true) "D" else ""
        holder.itemView.setOnClickListener { callback.onClickPost(posts[position]) }
        holder.itemView.tvToggleLike.setOnClickListener { callback.onToggleLike(posts[position]) }
    }

    class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}

interface IMain {
    fun onClickPost(post: Post)
    fun onToggleLike(post: Post)
}