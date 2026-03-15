package com.example.kollab.chat

fun MensajeDTO.toMensaje(): Mensaje {
    return Mensaje(
        id = id,
        chatId = chatId,
        texto = texto,
        enviadoPorMi = enviadoPorMi,
        fecha = ""  // ignorar la fecha por ahora
    )
}
