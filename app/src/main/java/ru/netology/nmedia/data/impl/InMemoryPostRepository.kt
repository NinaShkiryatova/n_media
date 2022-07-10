package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий.",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста, и начать цепочку перемен → http://netolo.gy//fyb",
        published = "02 июня в 18:37",
        likedByMe = false,
        likesCount = 999,
        sharedCount = 999
    )
    val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data
    override fun like() {
        post = post.copy(
            likedByMe = !post.likedByMe,
            likesCount = if (post.likedByMe) {
                post.likesCount - 1
            } else {
                post.likesCount + 1
            }
        )
        data.value = post

    }

    override fun share() {
        post = post.copy(
            sharedCount = post.sharedCount + 1
        )
        data.value = post
    }
}