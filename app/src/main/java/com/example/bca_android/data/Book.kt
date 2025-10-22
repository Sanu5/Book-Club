package com.example.bca_android.data

import com.google.firebase.firestore.DocumentId

data class Book(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val author: String = ""
)
