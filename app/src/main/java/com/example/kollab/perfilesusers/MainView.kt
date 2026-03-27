package com.example.kollab.perfilesusers

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.kollab.*
import com.example.kollab.chat.ChatListActivity
import com.example.kollab.service.RetrofitClient
import kotlinx.coroutines.launch
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

    private var filtroGenero: String? = null
    private var filtroPuestos: List<String> = emptyList()

    private var perfilesAPI: List<Perfil> = emptyList()
    private var perfilesFiltrados: List<Perfil> = emptyList()

    private lateinit var statsDataStore: StatsDataStore
    private var sessionStartMs: Long? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view)

        setSupportActionBar(findViewById(R.id.mainToolbar))

        statsDataStore = StatsDataStore(this)
        lifecycleScope.launch {
            statsDataStore.initializeNewAppSessionIfNeeded()
        }

        cardPerfil = findViewById(R.id.cardPerfil)
        imgPerfil = findViewById(R.id.profileImage)
        txtNombre = findViewById(R.id.profileName)
        txtDescripcion = findViewById(R.id.profileDescription)
        btnVerPerfil = findViewById(R.id.viewProfileButton)
        btnAceptar = findViewById(R.id.btnAceptar)
        btnDescartar = findViewById(R.id.btnDescartar)

        lifecycleScope.launch {
            try {
                val perfilesDTO = RetrofitClient.api.getPerfiles()
                perfilesAPI = perfilesDTO.map { it.toPerfil() }
                perfilesFiltrados = perfilesAPI
                mostrarPerfil()
            } catch (e: Exception) {
                txtNombre.text = "Error de conexión"
                txtDescripcion.text = "No se pudieron cargar los perfiles"
            }
        }

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
                        resetCardView(view)
                    }
                    true
                }
                else -> false
            }
        }

        findViewById<ImageButton>(R.id.victoriaButton).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        btnAceptar.setOnClickListener { animateSwipeLeft(cardPerfil) }
        btnDescartar.setOnClickListener { animateSwipeRight(cardPerfil) }
    }

    private fun mostrarPerfil() {
        if (perfilesFiltrados.isEmpty()) {
            txtNombre.text = "No hay perfiles"
            txtDescripcion.text = "Prueba otro filtro"
            return
        }

        if (perfilIndex >= perfilesFiltrados.size) perfilIndex = 0

        val perfil = perfilesFiltrados[perfilIndex]

        Glide.with(this).load(perfil.fotoUrl).into(imgPerfil)

        txtNombre.text = perfil.nombre
        txtDescripcion.text = perfil.descripcion

        btnVerPerfil.setOnClickListener {
            val intent = Intent(this, ProfileView::class.java)
            intent.putExtra("perfilId", perfil.id)
            startActivity(intent)
        }
    }

    private fun animateSwipeLeft(view: View) {
        val perfil = perfilesFiltrados[perfilIndex]

        view.animate()
            .translationX(-1000f)
            .rotation(-30f)
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                perfilIndex++
                resetCardView(view)
                mostrarPerfil()

                lifecycleScope.launch {
                    RetrofitClient.api.crearChat(perfil.id)
                    startActivity(Intent(this@MainView, ChatListActivity::class.java))
                }
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
                resetCardView(view)
                mostrarPerfil()
            }
            .start()
    }

    private fun resetCardView(view: View) {
        view.translationX = 0f
        view.rotation = 0f
        view.alpha = 1f
    }

    override fun onResume() {
        super.onResume()
        sessionStartMs = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()

        val inicio = sessionStartMs ?: return
        val duracionMs = System.currentTimeMillis() - inicio
        sessionStartMs = null

        lifecycleScope.launch {
            statsDataStore.addTiempoUsoMs(duracionMs)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_chats -> {
                startActivity(Intent(this, ChatListActivity::class.java))
                return true
            }

            R.id.menu_ajustes -> {
                startActivity(Intent(this, AjustesActivity::class.java))
                return true
            }

            R.id.menu_estadisticas -> {
                startActivity(Intent(this, StatsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}