package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.adapters.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter(
            viewModel
        )
        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.saveClicked(content)
                clearFocus()
                hideKeyboard()
            }
        }
        binding.cancelEditButton.setOnClickListener {
            with(binding){
                cancelEditGroup.visibility = View.GONE
                contentEditText.text.clear()
            }
        }
        viewModel.currentPost.observe(this) { currentPost ->
            with(binding){
                cancelEditGroup.visibility =
                if (currentPost != null) View.VISIBLE else View.GONE
                textToEdit.setText(currentPost?.content)
                contentEditText.setText(currentPost?.content)
            }
        }
    }

}

