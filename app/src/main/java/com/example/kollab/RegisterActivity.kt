package com.example.kollab

import android.content.Intent
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

        val btn = findViewById<Button>(R.id.btnRegistrar)

        btn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()



            //AlertDialog.Builder(this)
              //  .setTitle("Registro exitoso")
                //.setMessage("¡Bienvenido $n! Tu cuenta ha sido registrada correctamente.")
                //.setPositiveButton("OK") { dialog, _ ->
                 //   dialog.dismiss()
                  //  finish()
                //}
                //.setCancelable(false)
                //.show()


        }
    }
}