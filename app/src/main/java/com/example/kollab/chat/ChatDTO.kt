package com.example.kollab.chat

data class ChatDTO(
    val id: Int,
    val perfilId: Int,
    val ultimaFrase: String,
    val fechaUltimoMensaje: Any? = null,
    val nickname: String? = null
)

