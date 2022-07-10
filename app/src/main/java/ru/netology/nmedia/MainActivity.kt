package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.formatCount
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        viewModel.data.observe(this) { post ->
            with(binding) {
                authorName.text = post.author
                date.text = post.published
                postText.text = post.content
                likesCount.text = formatCount(post.likesCount)
                shareCount.text = formatCount(post.sharedCount)
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_redlikes_24dp else R.drawable.ic_likes_24dp
                )
            }
        }

        binding.likes.setOnClickListener {
            viewModel.like()
        }
        binding.share.setOnClickListener{
            viewModel.share()
        }
    }
}

