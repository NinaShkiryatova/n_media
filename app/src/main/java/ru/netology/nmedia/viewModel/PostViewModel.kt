package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapters.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.dto.Post

class PostViewModel: ViewModel(), PostInteractionListener {
    private val repository: PostRepository = InMemoryPostRepository()

    val data = repository.getAll()

    val currentPost = MutableLiveData<Post?>(null)

    fun saveClicked(content: String){
        if(content.isBlank())return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Author",
            content = content,
            published = "Today"
        )
        repository.save(post)
        currentPost.value = null
    }


    override fun likeById(id: Long) = repository.likeById(id)

    override fun shareById(id: Long) = repository.shareById(id)

    override fun removeById(id: Long) = repository.removeById(id)

    override fun editPost(post: Post) {
        currentPost.value = post
    }
}