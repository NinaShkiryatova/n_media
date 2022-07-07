package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.setPadding
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.formatCount

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post (
            id = 1,
            author = "Нетология. Университет интернет-профессий.",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста, и начать цепочку перемен → http://netolo.gy//fyb",
            published = "02 июня в 18:37",
            likedByMe = false,
            likesCount = 999,
            sharedCount = 999
        )
        with(binding) {
            authorName.text = post.author
            date.text = post.published
            postText.text = post.content
            likesCount.text = post.likesCount.toString()
            shareCount.text = post.sharedCount.toString()
            if(post.likedByMe){
                likes.setImageResource(R.drawable.ic_redlikes_24dp)
            }
            likes.setOnClickListener{
                post.likedByMe = !post.likedByMe
                if (post.likedByMe) {
                    likes.setImageResource(R.drawable.ic_redlikes_24dp)
                    post.likesCount++
                    likesCount.text = formatCount(post.likesCount)

                }
                else {
                    likes.setImageResource(R.drawable.ic_likes_24dp)
                    post.likesCount--
                    likesCount.text = formatCount(post.likesCount)
                }
            }
            share.setOnClickListener{
                post.sharedCount++
                shareCount.text = formatCount(post.sharedCount)
            }
        }
    }
}

