package com.example.kollab.perfilesusers

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.kollab.AjustesActivity
import com.example.kollab.R
import com.example.kollab.chat.ChatListActivity
import com.example.kollab.service.RetrofitClient
import kotlinx.coroutines.launch
import kotlin.math.abs
import com.example.kollab.perfilesusers.toPerfil
import com.example.kollab.ProfileActivity
import com.example.kollab.chat.ChatDTO
import com.example.kollab.perfilesusers.ProfileView

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view)

        setSupportActionBar(findViewById(R.id.mainToolbar))

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
                e.printStackTrace()
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
                        resetView(view)
                    }
                    true
                }
                else -> false
            }
        }

        val victoriaButton: ImageButton = findViewById(R.id.victoriaButton)
        victoriaButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        btnAceptar.setOnClickListener { animateSwipeLeft(cardPerfil) }
        btnDescartar.setOnClickListener { animateSwipeRight(cardPerfil) }
    }

    private fun mostrarPerfil() {

        if (perfilesFiltrados.isEmpty()) {
            txtNombre.text = "No hay perfiles"
            txtDescripcion.text = "Prueba otro filtro"
            imgPerfil.setImageResource(R.drawable.ic_launcher_foreground)
            return
        }

        if (perfilIndex >= perfilesFiltrados.size) {
            perfilIndex = 0
        }

        val perfil = perfilesFiltrados[perfilIndex]

        Glide.with(this)
            .load(perfil.fotoUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imgPerfil)

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

        // Lanzar la llamada de red y abrir el chat SOLO cuando el servidor confirme que se creó
        lifecycleScope.launch {
            try {
                RetrofitClient.api.crearChat(perfil.id)
                abrirChat() // ← se ejecuta después de que el servidor responda con éxito
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@MainView,
                    "Error al crear el chat: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        view.animate()
            .translationX(-1000f)
            .rotation(-30f)
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                // abrirChat() ya no va aquí para evitar la race condition
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
        startActivity(Intent(this, ChatListActivity::class.java))
    }

    private fun resetView(view: View) {
        view.translationX = 0f
        view.rotation = 0f
        view.alpha = 1f
    }

    private fun aplicarFiltros() {
        var lista = perfilesAPI

        filtroGenero?.let { genero ->
            lista = lista.filter { it.genero == genero }
        }

        if (filtroPuestos.isNotEmpty()) {
            lista = lista.filter { filtroPuestos.contains(it.puesto) }
        }

        if (lista.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Sin coincidencias")
                .setMessage("No hay perfiles que coincidan con estos filtros.")
                .setPositiveButton("Aceptar") { _, _ ->
                    filtroGenero = null
                    filtroPuestos = emptyList()
                    perfilesFiltrados = perfilesAPI
                    perfilIndex = 0
                    mostrarPerfil()
                }
                .show()
            return
        }

        perfilesFiltrados = lista
        perfilIndex = 0
        mostrarPerfil()
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

            R.id.menu_filtrar_genero -> {
                val opciones = arrayOf("Masculino", "Femenino")

                AlertDialog.Builder(this)
                    .setTitle("Filtrar por género")
                    .setItems(opciones) { _, which ->
                        filtroGenero = opciones[which]
                        aplicarFiltros()
                    }
                    .show()

                return true
            }

            R.id.menu_filtrar_puesto -> {

                val puestos = arrayOf("Programador", "UX/UI Designer", "Marketing Manager")
                val seleccionados = BooleanArray(puestos.size)

                AlertDialog.Builder(this)
                    .setTitle("Filtrar por puesto")
                    .setMultiChoiceItems(puestos, seleccionados) { _, index, isChecked ->
                        seleccionados[index] = isChecked
                    }
                    .setPositiveButton("Aplicar") { _, _ ->
                        filtroPuestos = puestos.filterIndexed { index, _ -> seleccionados[index] }
                        aplicarFiltros()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
