package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.EditPostActivityBinding
import ru.netology.nmedia.dto.Post

class EditPostActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = EditPostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val postContent = bundle?.get("text_to_edit")

        if(postContent != null){
            binding.textOfEditedPost.setText(postContent.toString())
            binding.textToEdit.setText(postContent.toString())
        }
        binding.textToEdit.requestFocus()
        binding.ok.setOnClickListener {
            val intent = Intent()
            if(binding.textToEdit.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.textToEdit.text.toString()
                intent.putExtra(RESULT_KEY, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    object ResultContract : ActivityResultContract<String?, String?>() {
        override fun createIntent(context: Context, input: String?) =
            Intent(context, EditPostActivity::class.java).putExtra("text_to_edit", input)

        override fun parseResult(resultCode: Int, intent: Intent?): String? =
            if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(RESULT_KEY)
            } else null

    }

    private companion object {
        private const val RESULT_KEY = "postEditedContent"
    }
}