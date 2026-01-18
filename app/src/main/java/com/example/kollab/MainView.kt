package com.example.kollab

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class MainView : AppCompatActivity() {

    private var x1 = 0f
    private var x2 = 0f
    private val mindistance = 150

    private var perfilIndex = 0

    private lateinit var cardPerfil: View
    private lateinit var imgPerfil: ImageView
    private lateinit var txtNombre: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var btnVerPerfil: Button
    private lateinit var btnAceptar: ImageView
    private lateinit var btnDescartar: ImageView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view)

        // Referencias a vistas
        cardPerfil = findViewById(R.id.cardPerfil)
        imgPerfil = findViewById(R.id.profileImage)
        txtNombre = findViewById(R.id.profileName)
        txtDescripcion = findViewById(R.id.profileDescription)
        btnVerPerfil = findViewById(R.id.viewProfileButton)
        btnAceptar = findViewById(R.id.btnAceptar)
        btnDescartar = findViewById(R.id.btnDescartar)

        mostrarPerfil()

        // Swipe sobre la tarjeta completa
        cardPerfil.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = event.x
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.x - x1
                    view.translationX = deltaX
                    view.rotation = deltaX / 20
                    true
                }
                MotionEvent.ACTION_UP -> {
                    x2 = event.x
                    val deltaX = x2 - x1

                    if (abs(deltaX) > mindistance) {
                        if (deltaX < 0) animateSwipeLeft(view)
                        else animateSwipeRight(view)
                    } else {
                        resetView(view)
                    }
                    true
                }
                else -> false
            }
        }

        val victoriaButton: ImageButton = findViewById(R.id.victoriaButton)

        victoriaButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Botón aceptar (✅)
        btnAceptar.setOnClickListener {
            animateSwipeLeft(cardPerfil)
        }

        // Botón descartar (❌)
        btnDescartar.setOnClickListener {
            animateSwipeRight(cardPerfil)
        }
    }

    private fun mostrarPerfil() {
        if (perfilIndex >= PerfilData.perfiles.size) {
            finish()
            return
        }

        val perfil = PerfilData.perfiles[perfilIndex]

        imgPerfil.setImageResource(perfil.foto)
        txtNombre.text = perfil.nombre
        txtDescripcion.text = perfil.descripcion

        btnVerPerfil.setOnClickListener {
            val intent = Intent(this, ProfileView::class.java)
            intent.putExtra("perfilId", perfil.id)
            startActivity(intent)
        }
    }

    private fun animateSwipeLeft(view: View) {
        val perfil = PerfilData.perfiles[perfilIndex]
        ChatData.chats.add(
            Chat(
                id = perfil.id,
                nombre = perfil.nombre,
                ultimaFrase = "Nuevo match!",
                foto = perfil.foto )
        )

        view.animate()
            .translationX(-1000f)
            .rotation(-30f)
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                abrirChat()
                perfilIndex++
                resetView(view)
                mostrarPerfil()
            }
            .start()
    }

    private fun animateSwipeRight(view: View) {
        view.animate()
            .translationX(1000f)
            .rotation(30f)
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                perfilIndex++
                resetView(view)
                mostrarPerfil()
            }
            .start()
    }

    private fun abrirChat() {
        val intent = Intent(this, ChatListActivity::class.java)
        startActivity(intent)
    }

    private fun resetView(view: View) {
        view.translationX = 0f
        view.rotation = 0f
        view.alpha = 1f
    }
}
