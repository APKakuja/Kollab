package com.example.kollab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kollab.Chat
import com.example.kollab.R
import com.example.kollab.holders.ChatViewHolder

class ChatAdapter(
    private var chats: List<Chat>,
    private val onClick: (Chat) -> Unit
) : RecyclerView.Adapter<ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]

        holder.foto.setImageResource(chat.foto)
        holder.nombre.text = chat.nombre
        holder.ultimaFrase.text = chat.ultimaFrase

        holder.itemView.setOnClickListener { onClick(chat) }
    }

    override fun getItemCount(): Int = chats.size

    fun actualizarLista(nuevaLista: List<Chat>) {
        chats = nuevaLista
        notifyDataSetChanged()
    }
}
