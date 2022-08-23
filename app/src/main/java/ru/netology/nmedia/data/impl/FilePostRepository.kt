package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.R
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class FilePostRepository(
    private val application: Application
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private var nextId = GENERATED_POSTS_AMOUNT.toLong()

    private var posts
        get() = checkNotNull(data.value) {
            "Data value shall not be null"
        }
    set(value) {
        application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(value))
        }
        data.value = value
    }

    override val data: MutableLiveData<List<Post>>

    init{
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if(postsFile.exists()){
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use{
                gson.fromJson(it, type)
            }
        } else emptyList()
        data = MutableLiveData(posts)
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likesCount = if (it.likedByMe) {
                    it.likesCount - 1
                } else {
                    it.likesCount + 1
                }
            )
        }
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(sharedCount = it.sharedCount + 1)
        }
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        posts = listOf(post.copy(id = ++nextId)) + posts

    }

    private companion object {
        const val GENERATED_POSTS_AMOUNT = 10
        const val FILE_NAME = "posts.json"

    }
}