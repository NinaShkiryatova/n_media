package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.PostContentFragment.Companion.textArg
import ru.netology.nmedia.databinding.SinglePostFragmentBinding
import ru.netology.nmedia.dto.formatCount
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewModel.PostViewModel

class SinglePostFragment : Fragment() {

    companion object {
        var Bundle.numArg: Long by LongArg
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SinglePostFragmentBinding.inflate(inflater, container, false)
        val postId = arguments?.numArg
        for (focusPost in viewModel.data.value!!) {
            if (focusPost.id == postId)
                binding.singlePost.apply {
                    postText.text = focusPost.content
                    authorName.text = focusPost.author
                    date.text = focusPost.published
                    likes.text = formatCount(focusPost.likesCount)
                    likes.isChecked = focusPost.likedByMe
                    videoPicture.setImageResource(R.drawable.picture_for_post)
                    videoPicture.visibility =
                        if (focusPost.video.isNullOrEmpty()) View.GONE else View.VISIBLE
                    videoPicture.isClickable = focusPost.video != null
                    playVideoButton.visibility =
                        if (focusPost.video.isNullOrEmpty()) View.GONE else View.VISIBLE
                    playVideoButton.isClickable = focusPost.video != null
                    share.text = formatCount(focusPost.sharedCount)
                    menu.setOnClickListener {
                        PopupMenu(context, menu).apply {
                            inflate(R.menu.options_post)
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.remove -> {
                                        viewModel.removeById(postId)
                                        findNavController().navigateUp()
                                        true
                                    }
                                    R.id.edit -> {
                                        viewModel.editPost(focusPost)
                                        findNavController().navigate(
                                            R.id.action_singlePostFragment_to_postContentFragment,
                                            Bundle().apply { textArg = postText.text.toString() })
                                        true
                                    }
                                    else -> false
                                }
                            }
                        }.show()
                    }
                    playVideoButton.setOnClickListener {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_STREAM, focusPost.video)
                            type = "video/mp4"
                        }

                        val shareIntent =
                            Intent.createChooser(intent, getString(R.string.chooser_play_video))
                        startActivity(shareIntent)
                    }

                    videoPicture.setOnClickListener {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_STREAM, focusPost.video)
                            type = "video/mp4"
                        }

                        val shareIntent =
                            Intent.createChooser(intent, getString(R.string.chooser_play_video))
                        startActivity(shareIntent)
                    }
                }
        }
        binding.singlePost.likes.setOnClickListener {
            viewModel.likeById(postId!!)
            for(item in viewModel.data.value!!) {if(item.id == postId)
                binding.singlePost.likes.text = formatCount(item.likesCount)
            }
        }
        binding.singlePost.share.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, binding.singlePost.postText.text)
                type = "text/plain"
            }

            val shareIntent =
                Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }
        return binding.root
    }

}