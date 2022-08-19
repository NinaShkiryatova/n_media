package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    var content: String,
    var video: String? = null,
    val published: String,
    var likedByMe: Boolean = false,
    var likesCount: Int = 0,
    var sharedCount: Int = 0
)

fun formatCount(quantity: Int): String {
    val result: String = when (quantity) {
        in 0..999 -> quantity.toString()
        in 1000..1099 -> (quantity / 1000).toString() + "K"
        in 1100..9999 -> ((quantity / 100) / 10.0).toString() + "K"
        in 10000..999999 -> (quantity / 1000).toString() + "K"
        in 1000000..1099999 -> ((quantity / 1000000)).toString() + "M"
        else -> ((quantity / 100000) / 10.0).toString() + "M"
    }
    return result
}