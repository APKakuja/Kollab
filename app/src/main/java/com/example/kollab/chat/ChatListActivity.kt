package com.example.kollab.chat

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kollab.R
import com.example.kollab.StatsDataStore
import com.example.kollab.service.RetrofitClient
import kotlinx.coroutines.launch

class ChatListActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var statsDataStore: StatsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        statsDataStore = StatsDataStore(this)

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
                                Toast.makeText(
                                    this@ChatListActivity,
                                    "Error al borrar el chat",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            },
            onEditClick = { chatUI ->
                val input = EditText(this)
                input.hint = "Escribe un nickname"
                input.setText(chatUI.nickname ?: "")

                AlertDialog.Builder(this)
                    .setTitle("Editar nickname de ${chatUI.nombre}")
                    .setView(input)
                    .setPositiveButton("Guardar") { _, _ ->
                        val nuevoNickname = input.text.toString().trim()
                        lifecycleScope.launch {
                            try {
                                RetrofitClient.api.actualizarNickname(chatUI.id, nuevoNickname)
                                cargarChats()
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(
                                    this@ChatListActivity,
                                    "Error al actualizar el nickname",
                                    Toast.LENGTH_SHORT
                                ).show()
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

    // 🔥 AQUÍ SE CUENTA BIEN
    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            statsDataStore.incrementChats()
        }
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
                        ultimaFrase = chat.ultimaFrase,
                        nickname = chat.nickname
                    )
                }

                adapter.actualizarLista(chatsUI)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@ChatListActivity,
                    "Error al cargar los chats",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}