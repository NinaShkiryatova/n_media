package ru.netology.nmedia.data.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.formatCount
import kotlin.properties.Delegates

typealias OnItemListener = (Long) -> Unit

internal class PostsAdapter(
    private val likeById: OnItemListener,
    private val shareById: OnItemListener
): ListAdapter<Post, PostsAdapter.PostViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, likeById, shareById)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class PostViewHolder(
        private val binding: PostCardBinding,
        private val likeById: OnItemListener,
        private val shareById: OnItemListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init{
            binding.likes.setOnClickListener {
                likeById(post.id)
            }
            binding.share.setOnClickListener {
                shareById(post.id)
            }
        }

        fun bind(post: Post) {
            this.post = post
            binding.apply {
                authorName.text = post.author
                date.text = post.published
                postText.text = post.content
                likesCount.text = formatCount(post.likesCount)
                shareCount.text = formatCount(post.sharedCount)
                likes.setImageResource(
                    if (post.likedByMe) ru.netology.nmedia.R.drawable.ic_redlikes_24dp else ru.netology.nmedia.R.drawable.ic_likes_24dp
                )
            }

        }
    }

    private object DiffCallback: DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

    }
}