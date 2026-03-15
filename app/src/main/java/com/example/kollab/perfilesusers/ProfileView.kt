package com.example.kollab.perfilesusers

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.kollab.R
import com.example.kollab.service.RetrofitClient
import kotlinx.coroutines.launch

class ProfileView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)

        val perfilId = intent.getIntExtra("perfilId", -1)
        if (perfilId == -1) {
            finish()
            return
        }

        val imgPerfil = findViewById<ImageView>(R.id.profileImageLarge)
        val txtNombre = findViewById<TextView>(R.id.profileNameLarge)
        val txtDescripcion = findViewById<TextView>(R.id.profileDescription)
        val txtSkills = findViewById<TextView>(R.id.profileSkills)
        val txtExperiencia = findViewById<TextView>(R.id.experienceCardText)

        lifecycleScope.launch {
            try {
                val perfilDTO = RetrofitClient.api.getPerfil(perfilId)
                val perfil = perfilDTO.toPerfil()

                // 2. Cargar imagen real
                Glide.with(this@ProfileView)
                    .load(perfil.fotoUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imgPerfil)

                txtNombre.text = perfil.nombre
                txtDescripcion.text = perfil.descripcion

                val title = "Skills:\n"
                val full = title + perfil.skills
                val ssb = SpannableStringBuilder(full)
                ssb.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    title.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                txtSkills.text = ssb

                txtExperiencia.text = perfil.experiencia

            } catch (e: Exception) {
                e.printStackTrace()
                txtNombre.text = "Error"
                txtDescripcion.text = "No se pudo cargar el perfil"
            }
        }

        findViewById<Button>(R.id.backButton).setOnClickListener { finish() }
    }
}
