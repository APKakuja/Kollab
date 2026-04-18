package com.example.kollab

import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AjustesActivity : AppCompatActivity() {

    private lateinit var statsDataStore: StatsDataStore
    private lateinit var switchVoice: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        statsDataStore = StatsDataStore(this)

        switchVoice = findViewById(R.id.switch_voice)

        lifecycleScope.launch {
            val enabled = statsDataStore.voiceEnabled.first()
            switchVoice.setOnCheckedChangeListener(null)
            switchVoice.isChecked = enabled
            switchVoice.setOnCheckedChangeListener { _, isChecked ->
                lifecycleScope.launch {
                    statsDataStore.setVoiceEnabled(isChecked)
                }
            }
        }
    }

    // 🔥 AQUÍ SE CUENTA BIEN
    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            statsDataStore.incrementAjustes()
        }
    }
}