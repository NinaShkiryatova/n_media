package ru.netology.nmedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.formatCount
import kotlin.properties.Delegates

internal class PostsAdapter(
    private val interactionListener: PostInteractionListener
): ListAdapter<Post, PostsAdapter.PostViewHolder>(DiffCallback) {

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
            PopupMenu(itemView.context, binding.menu).apply{
                inflate(R.menu.options_post)
                setOnMenuItemClickListener{item ->
                    when(item.itemId){
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
            binding.likes.setOnClickListener {
                listener.likeById(post.id)
            }
            binding.share.setOnClickListener {
                listener.shareById(post.id)
            }
            binding.menu.setOnClickListener{popupMenu.show()}
        }

        fun bind(post: Post) {
            this.post = post
            binding.apply {
                authorName.text = post.author
                date.text = post.published
                postText.text = post.content
                likes.text = formatCount(post.likesCount)
                likes.isChecked = post.likedByMe
                share.text = formatCount(post.sharedCount)
            }
        }
    }

    private object DiffCallback: DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

    }
}