package com.example.tuapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class PlaceholderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aquí solo mostramos un texto para saber que llegamos
        val textView = TextView(this)
        textView.text = "Pantalla de destino (placeholder)"
        textView.textSize = 20f
        setContentView(textView)
    }
}
