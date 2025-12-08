package com.example.kollab

import android.content.Intent
import android.view.View
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view)

        val profileButton: Button = findViewById(R.id.viewProfileButton)

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileView::class.java)
            startActivity(intent)
        }

        val victoriaButton: ImageButton = findViewById(R.id.victoriaButton)

        victoriaButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}