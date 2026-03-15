package com.example.kollab.service

import com.example.kollab.chat.ChatDTO
import com.example.kollab.chat.MensajeDTO
import com.example.kollab.perfilesusers.PerfilDTO
import retrofit2.http.*

interface ApiService {

    @GET("perfil")
    suspend fun getPerfiles(): List<PerfilDTO>

    @GET("perfil/{id}")
    suspend fun getPerfil(@Path("id") id: Int): PerfilDTO

    @GET("chats")
    suspend fun getChats(): List<ChatDTO>

    @POST("chats")
    suspend fun crearChat(@Query("perfilId") perfilId: Int)

    @PUT("chats/{id}/nickname")
    suspend fun actualizarNickname(
        @Path("id") id: Int,
        @Query("nickname") nickname: String
    )

    @GET("chats/{id}")
    suspend fun getChat(@Path("id") id: Int): ChatDTO  // ✅ por id

    @GET("chats/{id}/mensajes")
    suspend fun getMensajes(@Path("id") chatId: Int): List<MensajeDTO>

    @POST("mensajes")
    suspend fun enviarMensaje(@Body mensaje: MensajeDTO): MensajeDTO

    @DELETE("chats/{id}")
    suspend fun borrarChat(@Path("id") id: Int)
}