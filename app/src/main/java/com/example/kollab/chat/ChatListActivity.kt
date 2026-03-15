package com.example.kollab.chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kollab.R
import com.example.kollab.Session
import com.example.kollab.service.RetrofitClient
import kotlinx.coroutines.launch

class ChatListActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        recycler = findViewById(R.id.chatRecycler)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = ChatAdapter(
            emptyList(),
            onClick = { chatUI ->
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("chatId", chatUI.id)
                startActivity(intent)
            },
            onLongClick = { chatUI ->
                AlertDialog.Builder(this)
                    .setTitle("Borrar chat")
                    .setMessage("¿Quieres borrar el chat con ${chatUI.nombre}?")
                    .setPositiveButton("Borrar") { _, _ ->
                        lifecycleScope.launch {
                            try {
                                RetrofitClient.api.borrarChat(chatUI.id)
                                cargarChats()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        recycler.adapter = adapter
        cargarChats()
    }

    private fun cargarChats() {
        lifecycleScope.launch {
            try {
                val chatsDTO = RetrofitClient.api.getChats()
                val chats = chatsDTO.map { it.toChat() }

                val chatsUI = chats.map { chat ->
                    val perfil = RetrofitClient.api.getPerfil(chat.perfilId)
                    ChatUI(
                        id = chat.id,
                        nombre = perfil.nombre,
                        fotoUrl = perfil.fotoUrl,
                        ultimaFrase = chat.ultimaFrase
                    )
                }

                adapter.actualizarLista(chatsUI)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}