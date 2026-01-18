package com.example.kollab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val editButton: Button = findViewById(R.id.btnEditProfile)
        val settingsButton: Button = findViewById(R.id.btnSettings)
        val backButton: Button = findViewById(R.id.btnBack)

        // Botón Editar Perfil
        editButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Botón Configuración
        //settingsButton.setOnClickListener {
        //    val intent = Intent(this, SettingsActivity::class.java)
        //    startActivity(intent)
        //}

        // Botón Atrás
        backButton.setOnClickListener {
            finish()
        }
    }
}
