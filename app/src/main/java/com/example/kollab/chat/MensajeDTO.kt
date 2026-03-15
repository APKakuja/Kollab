package com.example.kollab.chat

data class MensajeDTO(
    val id: Int,
    val chatId: Int,
    val texto: String,
    val enviadoPorMi: Boolean,
    val fecha: Any? = null
)

