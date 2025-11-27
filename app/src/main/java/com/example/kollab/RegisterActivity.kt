package com.example.kollab

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nombre = findViewById<EditText>(R.id.inputNombre)
        val apellidos = findViewById<EditText>(R.id.inputApellidos)
        val email = findViewById<EditText>(R.id.inputEmail)
        val password = findViewById<EditText>(R.id.inputPassword)
        val btn = findViewById<Button>(R.id.btnRegistrar)

        btn.setOnClickListener {

            val n = nombre.text.toString().trim()
            val a = apellidos.text.toString().trim()
            val e = email.text.toString().trim()
            val p = password.text.toString().trim()

            if (n.isEmpty() || a.isEmpty() || e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Usuario registrado (simulación)", Toast.LENGTH_SHORT).show()
        }
    }
}
