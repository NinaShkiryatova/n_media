package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.PostContentFragment.Companion.textArg
import ru.netology.nmedia.activity.SinglePostFragment.Companion.numArg
import ru.netology.nmedia.adapters.PostInteractionListener
import ru.netology.nmedia.adapters.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val binding = FragmentFeedBinding.inflate(
            inflater, container, false)
        val adapter = PostsAdapter(object : PostInteractionListener{
            override fun likeById(id: Long) {
                viewModel.likeById(id)
            }

            override fun shareById(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun removeById(id: Long) {
                viewModel.removeById(id)
            }

            override fun editPost(post: Post) {
                viewModel.editPost(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_postContentFragment,
                    Bundle().apply { textArg = post.content })
            }

            override fun playVideo(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, post.video)
                    type = "video/mp4"
                }

                val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_play_video))
                startActivity(shareIntent)
            }

            override fun showPost(post: Post) {
                viewModel.showPost(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_singlePostFragment,
                    Bundle().apply { numArg = post.id }
                )
            }

        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_postContentFragment)
        }
        return binding.root
    }
}