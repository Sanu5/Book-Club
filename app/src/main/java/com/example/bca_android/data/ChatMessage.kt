package com.example.bca_android.data

data class ChatMessage(
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
