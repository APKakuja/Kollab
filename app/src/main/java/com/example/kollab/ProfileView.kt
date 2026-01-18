package com.example.kollab

import android.os.Bundle
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
            txtSkills.text = perfil.skills
            txtExperiencia.text = perfil.experiencia
        }

        findViewById<Button>(R.id.backButton).setOnClickListener { finish() }
    }
}
