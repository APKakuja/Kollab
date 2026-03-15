package com.example.kollab.chat

data class Mensaje(
    val id: Int,
    val chatId: Int,
    val texto: String,
    val enviadoPorMi: Boolean,
    val fecha: String
)



