package ru.netology.nmedia.adapters

import ru.netology.nmedia.dto.Post

interface PostInteractionListener {
    fun likeById (id: Long)
    fun shareById (id: Long)
    fun removeById (id: Long)
    fun editPost(post: Post)
}