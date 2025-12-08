package com.example.kollab

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
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

            // Validaciones individuales
            if (n.isEmpty()) {
                nombre.error = "Por favor, introduce tu nombre"
                nombre.requestFocus()
                return@setOnClickListener
            }

            if (a.isEmpty()) {
                apellidos.error = "Por favor, introduce tus apellidos"
                apellidos.requestFocus()
                return@setOnClickListener
            }

            if (e.isEmpty()) {
                email.error = "Por favor, introduce tu correo"
                email.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                email.error = "Correo electrónico inválido"
                email.requestFocus()
                return@setOnClickListener
            }

            if (p.isEmpty()) {
                password.error = "Por favor, introduce tu contraseña"
                password.requestFocus()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Registro exitoso")
                .setMessage("¡Bienvenido $n! Tu cuenta ha sido registrada correctamente.")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                .setCancelable(false)
                .show()
        }
    }
}