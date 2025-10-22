package com.example.bca_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.bca_android.data.Book
import com.example.bca_android.data.ChatMessage
import com.example.bca_android.repository.BookClubRepository
import kotlinx.coroutines.launch

class BookClubViewModel : ViewModel() {

    private val repository = BookClubRepository()

    val books: LiveData<List<Book>> = repository.getBookStream().asLiveData()

    private val _selectedBookId = MutableLiveData<String>()

    val chatMessages: LiveData<List<ChatMessage>> = _selectedBookId.switchMap { bookId ->
        repository.getChatMessageStream(bookId).asLiveData()
    }

    fun addBook(title: String, author: String) {
        viewModelScope.launch {
            repository.addBook(title, author)
        }
    }

    fun sendChatMessage(message: String) {
        val bookId = _selectedBookId.value ?: return
        viewModelScope.launch {
            repository.sendChatMessage(bookId, message)
        }
    }

    fun selectedBook(book: Book) {
        _selectedBookId.value = book.id
    }

}