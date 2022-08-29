package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

private val defaultPost = Post(
    id = 0,
    author = "",
    content = "",
    likedByMe = false,
    published = ""
)

class PostViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: PostRepository = FilePostRepository(application)
    val data = repository.getAll()
    private var currentPost = MutableLiveData(defaultPost)


    //val sharePostContent = SingleLiveEvent<String>()
    //val playVideoFromPostEvent = SingleLiveEvent<String?>()



    fun saveClicked() {
        currentPost.value?.let{
            repository.save(it)
        }
        currentPost.value = defaultPost
    }

    fun editPost(post: Post) {
        currentPost.value = post
    }

    fun editResult(content: String) {
        val postText = content.trim()
        if(currentPost.value?.content == postText){
            return
        }
        currentPost.value = currentPost.value?.copy(content = postText)
    }

    fun likeById(id: Long) = repository.likeById(id)

    fun removeById(id: Long) = repository.removeById(id)

    fun showPost(post: Post) {
        currentPost.value = post
    }
}