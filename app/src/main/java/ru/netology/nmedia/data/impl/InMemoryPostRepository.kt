package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_POSTS_AMOUNT.toLong()

    private val posts
        get() = checkNotNull(data.value) {
            "Data value shall not be null"
        }

    override val data = MutableLiveData(
        List(GENERATED_POSTS_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = "Нетология.",
                content = "Пост ${index+1}",
                published = "02 июня в 18:37",
                likedByMe = false
            )
        }
    )

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        data.value = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likesCount = if (it.likedByMe) {
                    it.likesCount - 1
                } else {
                    it.likesCount + 1
                }
            )
        }
        data.value = posts

    }

    override fun shareById(id: Long) {
        data.value = posts.map {
            if (it.id != id) it else it.copy(sharedCount = it.sharedCount + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        data.value = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post: Post) {
        data.value = posts.map{
            if(it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        data.value = listOf(post.copy(id = ++nextId)) + posts

    }


    private companion object {
        const val GENERATED_POSTS_AMOUNT = 10
    }
}