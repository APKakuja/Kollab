package com.example.kollab.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kollab.R

class ChatAdapter(
    private var chats: List<ChatUI>,
    private val onClick: (ChatUI) -> Unit,
    private val onLongClick: (ChatUI) -> Unit
) : RecyclerView.Adapter<ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]

        Glide.with(holder.itemView.context)
            .load(chat.fotoUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.foto)

        holder.nombre.text = chat.nombre
        holder.ultimaFrase.text = chat.ultimaFrase

        holder.itemView.setOnClickListener { onClick(chat) }
        holder.itemView.setOnLongClickListener {
            onLongClick(chat)
            true
        }
    }

    override fun getItemCount(): Int = chats.size

    fun actualizarLista(nuevaLista: List<ChatUI>) {
        chats = nuevaLista
        notifyDataSetChanged()
    }
}