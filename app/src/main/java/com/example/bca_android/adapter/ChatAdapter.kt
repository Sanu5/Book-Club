package com.example.bca_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bca_android.data.ChatMessage
import com.example.bca_android.databinding.ListItemChatBinding

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var messages : List<ChatMessage> = emptyList()

    fun setMessage(newMessage: List<ChatMessage>) {
        messages = newMessage
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ListItemChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size

    class ChatViewHolder(private val binding: ListItemChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.itemChatMessage.text = message.message
        }
    }
}