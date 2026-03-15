package com.example.kollab.chat

data class Chat(
    val id: Int,
    val perfilId: Int,
    val ultimaFrase: String,
    val fechaUltimoMensaje: String,
    val mensajes: MutableList<Mensaje> = mutableListOf()
)


