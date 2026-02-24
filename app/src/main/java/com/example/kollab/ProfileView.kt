package com.example.kollab

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)

        val perfilId = intent.getIntExtra("perfilId", -1)
        val perfil = PerfilData.perfiles.find { it.id == perfilId }

        val imgPerfil = findViewById<ImageView>(R.id.profileImageLarge)
        val txtNombre = findViewById<TextView>(R.id.profileNameLarge)
        val txtDescripcion = findViewById<TextView>(R.id.profileDescription)
        val txtSkills = findViewById<TextView>(R.id.profileSkills)
        val txtExperiencia = findViewById<TextView>(R.id.experienceCardText)

        if (perfil != null) {
            imgPerfil.setImageResource(perfil.foto)
            txtNombre.text = perfil.nombre
            txtDescripcion.text = perfil.descripcion

            // Construir Spannable: "Skills:" en negrita + el resto en normal
            val title = "Skills: \n"
            val skillsText = perfil.skills
            val full = title + skillsText
            val ssb = SpannableStringBuilder(full)
            ssb.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            txtSkills.text = ssb

            txtExperiencia.text = perfil.experiencia
        }

        findViewById<Button>(R.id.backButton).setOnClickListener { finish() }
    }
}
