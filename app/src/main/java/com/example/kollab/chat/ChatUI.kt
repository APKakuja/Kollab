package com.example.kollab.chat

data class ChatUI(
    val id: Int,
    val nombre: String,
    val fotoUrl: String,
    val ultimaFrase: String,
    val nickname: String? = null
)
