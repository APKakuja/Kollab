package com.example.kollab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AjustesActivity : AppCompatActivity() {

    private lateinit var statsDataStore: StatsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        statsDataStore = StatsDataStore(this)
    }

    // 🔥 AQUÍ SE CUENTA BIEN
    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            statsDataStore.incrementAjustes()
        }
    }
}