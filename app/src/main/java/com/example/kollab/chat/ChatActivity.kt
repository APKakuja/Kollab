package com.example.kollab.chat

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kollab.R
import com.example.kollab.service.RetrofitClient
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private var chatId: Int = -1
    private lateinit var adapter: MensajeAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var input: EditText
    private lateinit var sendButton: ImageView
    private lateinit var startMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatId = intent.getIntExtra("chatId", -1)
        if (chatId == -1) return

        recycler = findViewById(R.id.chatRecycler)
        input = findViewById(R.id.input_message)
        sendButton = findViewById(R.id.send_button)
        startMessage = findViewById(R.id.chat_start_message)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = MensajeAdapter(emptyList())
        recycler.adapter = adapter

        cargarChat()
        cargarMensajes()

        sendButton.setOnClickListener {
            enviarMensaje()
        }
    }

    private fun cargarChat() {
        lifecycleScope.launch {
            try {
                val chatDTO = RetrofitClient.api.getChat(chatId)
                val chat = chatDTO.toChat()

                val perfil = RetrofitClient.api.getPerfil(chat.perfilId)

                val profileImage = findViewById<ImageView>(R.id.profile_image)
                val chatUsername = findViewById<TextView>(R.id.chat_username)

                Glide.with(this@ChatActivity)
                    .load(perfil.fotoUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(profileImage)

                chatUsername.text = perfil.nombre
                startMessage.text = "Chatea con ${perfil.nombre}"

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun cargarMensajes() {
        lifecycleScope.launch {
            try {
                val mensajesDTO = RetrofitClient.api.getMensajes(chatId)
                val mensajes = mensajesDTO.map { it.toMensaje() }

                adapter.actualizarLista(mensajes)

                if (mensajes.isNotEmpty()) {
                    startMessage.alpha = 0f
                }

                recycler.scrollToPosition(mensajes.size - 1)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun enviarMensaje() {
        val texto = input.text.toString().trim()
        if (texto.isEmpty()) return

        lifecycleScope.launch {
            try {
                val nuevoMensaje = MensajeDTO(
                    id = 0,
                    chatId = chatId,
                    texto = texto,
                    enviadoPorMi = true,
                    fecha = ""
                )

                RetrofitClient.api.enviarMensaje(nuevoMensaje)

                input.setText("")
                cargarMensajes()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
