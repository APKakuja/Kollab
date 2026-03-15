package com.example.kollab.chat

fun ChatDTO.toChat(): Chat {
    return Chat(
        id = id,
        perfilId = perfilId,
        ultimaFrase = ultimaFrase,
        fechaUltimoMensaje = ""  // ignorar la fecha por ahora
    )
}