package com.example.bca_android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
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

        viewModel.books.observe(this) {books ->
            bookAdapter.setBooks(books)
        }

        viewModel.chatMessages.observe(this) { messages ->
            chatAdapter.setMessage(messages)
            binding.rvChat.scrollToPosition(messages.size - 1)
        }
    }

    private fun setupUI() {

        bookAdapter = BookAdapter { book ->
            viewModel.selectedBook(book)
            binding.selectedBookTitle.text = "Live Chat For '${book.title}' (Realtime DB)"
            binding.sendChatMessageButton.isEnabled = true
        }
        binding.rvBooks.layoutManager = LinearLayoutManager(this)
        binding.rvBooks.adapter = bookAdapter

        chatAdapter = ChatAdapter()
        binding.rvChat.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        binding.rvChat.adapter = chatAdapter

        binding.addBookButton.setOnClickListener {
            val title = binding.bookTitleInput.text.toString()
            val author = binding.bookAuthorInput.text.toString()

            if(title.isNotEmpty() && author.isNotEmpty()) {
                viewModel.addBook(title, author)
                binding.bookTitleInput.text.clear()
                binding.bookAuthorInput.text.clear()
            }
        }

        binding.sendChatMessageButton.setOnClickListener {
            val message = binding.chatMessageInput.text.toString()
            if(message.isNotBlank()) {
                viewModel.sendChatMessage(message)
                binding.chatMessageInput.text.clear()
            }
        }
    }
}