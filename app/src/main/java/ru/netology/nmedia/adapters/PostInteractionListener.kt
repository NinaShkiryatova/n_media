package ru.netology.nmedia.adapters

import ru.netology.nmedia.dto.Post

interface PostInteractionListener {
    fun likeById (id: Long)
    fun shareById (post: Post)
    fun removeById (id: Long)
    fun editPost(post: Post)
    fun playVideo(post: Post)
}