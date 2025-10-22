package com.example.bca_android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bca_android.adapter.BookAdapter
import com.example.bca_android.adapter.ChatAdapter
import com.example.bca_android.databinding.ActivityMainBinding
import com.example.bca_android.viewmodel.BookClubViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: BookClubViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private lateinit var bookAdapter: BookAdapter
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun observeViewModel() {
        TODO("Not yet implemented")
    }

    private fun setupUI() {

        bookAdapter = BookAdapter { book ->
            viewModel.selectedBook(book)
            binding.selectedBookTitle.text = "Live Chat For '${book.title}' (Realtime DB)"
            binding.sendChatMessageButton.isEnabled = true
        }
    }
}