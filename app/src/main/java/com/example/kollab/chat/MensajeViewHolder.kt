package com.example.kollab.chat

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kollab.R

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val texto: TextView = view.findViewById(R.id.textoMensaje)
}
