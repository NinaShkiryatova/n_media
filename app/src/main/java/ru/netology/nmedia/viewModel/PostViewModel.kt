package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapters.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel : ViewModel(), PostInteractionListener {
    private val repository: PostRepository = InMemoryPostRepository()

    val data = repository.getAll()

    val sharePostContent = SingleLiveEvent<String>()
    val playVideoFromPostEvent = SingleLiveEvent<String?>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<Unit>()
    val navigateToEditPostScreenEvent = SingleLiveEvent<Unit>()
    val currentPost = MutableLiveData<Post?>(null)

    fun saveClicked(content: String) {
        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Author",
            content = content,
            video = "ytr",
            published = "Today"
        )
        repository.save(post)
        currentPost.value = null
    }

    fun onAddClicked() {
        navigateToPostContentScreenEvent.call()
    }

    fun editResult(content: String) {
        currentPost.value?.content = content
        repository.save(currentPost.value!!)
    }

    override fun likeById(id: Long) = repository.likeById(id)

    override fun shareById(post: Post) {
        repository.shareById(post.id)
        sharePostContent.value = post.content
    }

    override fun removeById(id: Long) = repository.removeById(id)

    override fun editPost(post: Post) {
        currentPost.value = post
        navigateToEditPostScreenEvent.call()
    }

    override fun playVideo(post: Post) {
        playVideoFromPostEvent.value = post.video
    }

}