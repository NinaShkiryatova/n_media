package ru.netology.nmedia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.formatCount

internal class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.PostViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(
        private val binding: PostCardBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menu).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            listener.removeById(post.id)
                            true
                        }
                        R.id.edit -> {
                            listener.editPost(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.videoPicture.setOnClickListener {
                listener.playVideo(post)
            }

            binding.playVideoButton.setOnClickListener {
                listener.playVideo(post)
            }

            binding.likes.setOnClickListener {
                listener.likeById(post.id)
            }
            binding.share.setOnClickListener {
                listener.shareById(post)
            }
            binding.menu.setOnClickListener { popupMenu.show() }
        }

        fun bind(post: Post) {
            this.post = post
            binding.apply {
                authorName.text = post.author
                date.text = post.published
                postText.text = post.content
                videoPicture.setImageResource(R.drawable.picture_for_post)
                videoPicture.visibility =
                    if (post.video.isNullOrEmpty()) View.GONE else View.VISIBLE
                videoPicture.isClickable = post.video != null
                playVideoButton.visibility =
                    if (post.video.isNullOrEmpty()) View.GONE else View.VISIBLE
                playVideoButton.isClickable = post.video != null
                likes.text = formatCount(post.likesCount)
                likes.isChecked = post.likedByMe
                share.text = formatCount(post.sharedCount)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

    }
}