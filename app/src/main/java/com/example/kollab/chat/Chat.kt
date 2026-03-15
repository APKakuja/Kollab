package com.example.kollab.chat

data class Chat(
    val id: Int,
    val perfilId: Int,
    val ultimaFrase: String,
    val fechaUltimoMensaje: String,
    val nickname: String? = null,
    val mensajes: MutableList<Mensaje> = mutableListOf()
)


