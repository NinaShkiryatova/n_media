package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostContentFragmentBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewModel.PostViewModel

class PostContentFragment : Fragment() {

    companion object {
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

        val binding = PostContentFragmentBinding.inflate(inflater, container, false)
        val text = arguments?.textArg
        if(text != null) {
            binding.editHeader.visibility = View.VISIBLE
            binding.textOfEditedPost.visibility = View.VISIBLE
            text.let(binding.textOfEditedPost::setText)
            binding.textOfPost.setText(text)
            binding.textOfPost.requestFocus()
        }

        binding.ok.setOnClickListener {
            viewModel.editResult(binding.textOfPost.text.toString())
            viewModel.saveClicked()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        return binding.root
    }


    /*object ResultContract : ActivityResultContract<Unit, String?>() {
        override fun createIntent(context: Context, input: Unit) =
            Intent(context, PostContentFragment::class.java)

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(RESULT_KEY)
            } else null

    }

    companion object {
        private const val RESULT_KEY = "postNewContent"
    }*/
}