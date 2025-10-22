package com.example.bca_android.repository

import com.example.bca_android.data.Book
import com.example.bca_android.data.ChatMessage
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class BookClubRepository {

    private val firestoreDb = Firebase.firestore.collection("books")

    private val realtimeDb = Firebase.database.getReference("chat")

    suspend fun addBook(title: String, author: String) {
        try {
            val book = hashMapOf(
                "title" to title,
                "author" to author
            )
            firestoreDb.add(book).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun sendChatMessage(bookId: String, message: String) {
        try {
            val chatMessage = ChatMessage(message = message)
            realtimeDb.child(bookId).push().setValue(chatMessage).await()
        } catch (e: Exception) {
            throw e
        }
    }

    fun getBookStream(): Flow<List<Book>> = callbackFlow {
        val registration = firestoreDb.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val books = snapshot.toObjects<Book>()
                trySend(books)
            }
        }

        awaitClose { registration.remove() }
    }

    fun getChatMessageStream(bookId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull {
                    it.getValue(ChatMessage::class.java)
                }
                trySend(messages.sortedBy { it.message })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val dbRef = realtimeDb.child(bookId)
        dbRef.addValueEventListener(listener)

        awaitClose { dbRef.removeEventListener(listener) }
    }
}