package com.example.kollab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val registerButton = findViewById<Button>(R.id.buttonRegister)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            //if (email.isNotEmpty() && password.isNotEmpty()) {
            // Aquí podrías validar credenciales más adelante
            // val intent = Intent(this, PlaceholderActivity::class.java)
            //startActivity(intent)
            //} else {
            //   emailEditText.error = "Campo requerido"
            //  passwordEditText.error = "Campo requerido"
        }
    }

    //registerButton.setOnClickListener {
    //val intent = Intent(this, RegisterActivity::class.java)
    //startActivity(intent)
    //}
}