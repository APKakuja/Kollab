package com.example.kollab.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kollab.R

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val foto: ImageView = view.findViewById(R.id.chatFoto)
    val nombre: TextView = view.findViewById(R.id.chatNombre)
    val ultimaFrase: TextView = view.findViewById(R.id.chatUltimaFrase)
}
