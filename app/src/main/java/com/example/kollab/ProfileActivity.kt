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

        //editButton.setOnClickListener {
            // Aquí lanzarías tu EditProfileActivity
          //  val intent = Intent(this, EditProfileActivity::class.java)
            //startActivity(intent)
        }

        //settingsButton.setOnClickListener {
            // Aquí lanzarías tu SettingsActivity
           // val intent = Intent(this, SettingsActivity::class.java)
           // startActivity(intent)
        //}
    //}
}
