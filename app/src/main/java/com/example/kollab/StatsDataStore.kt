package com.example.kollab

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
val Context.dataStore by preferencesDataStore(name = "estadisticas")

class StatsDataStore(private val context: Context) {

    private val db = Firebase.firestore

    companion object {
        val VISITAS_CHATS = intPreferencesKey("visitas_chats")
        val VISITAS_AJUSTES = intPreferencesKey("visitas_ajustes")
        val TIEMPO_USO_MS = longPreferencesKey("tiempo_uso_ms")
        private val SESSION_LOCK = Any()
        @Volatile private var sessionInitialized = false

        // Factores configurables para la estimacion ambiental.
        const val POTENCIA_MEDIA_W = 2.5
        const val FACTOR_CO2_KG_POR_KWH = 0.18
    }

    suspend fun initializeNewAppSessionIfNeeded() {
        val shouldReset = synchronized(SESSION_LOCK) {
            if (sessionInitialized) false else {
                sessionInitialized = true
                true
            }
        }

        if (!shouldReset) return

        context.dataStore.edit { prefs ->
            prefs[VISITAS_CHATS] = 0
            prefs[VISITAS_AJUSTES] = 0
            prefs[TIEMPO_USO_MS] = 0L
        }
        guardarEnFirestore()
    }

    val visitasChats: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[VISITAS_CHATS] ?: 0 }

    val visitasAjustes: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[VISITAS_AJUSTES] ?: 0 }

    val tiempoUsoMs: Flow<Long> = context.dataStore.data
        .map { prefs -> prefs[TIEMPO_USO_MS] ?: 0L }

    suspend fun incrementChats() {
        context.dataStore.edit { prefs ->
            prefs[VISITAS_CHATS] = (prefs[VISITAS_CHATS] ?: 0) + 1
        }
        guardarEnFirestore()
    }

    suspend fun incrementAjustes() {
        context.dataStore.edit { prefs ->
            prefs[VISITAS_AJUSTES] = (prefs[VISITAS_AJUSTES] ?: 0) + 1
        }
        guardarEnFirestore()
    }

    suspend fun addTiempoUsoMs(ms: Long) {
        if (ms <= 0L) return

        context.dataStore.edit { prefs ->
            prefs[TIEMPO_USO_MS] = (prefs[TIEMPO_USO_MS] ?: 0L) + ms
        }
        guardarEnFirestore()
    }

    private suspend fun guardarEnFirestore() {
        val chats = visitasChats.first()
        val ajustes = visitasAjustes.first()
        val tiempoUso = tiempoUsoMs.first()

        val datos = hashMapOf(
            "visitas_chats" to chats,
            "visitas_ajustes" to ajustes,
            "tiempo_uso_ms" to tiempoUso
        )

        db.collection("estadisticas")
            .document("stats_usuario")
            .set(datos)
    }

    fun loadFromFirestore() {
        val scope = CoroutineScope(Dispatchers.IO)

        db.collection("estadisticas")
            .document("stats_usuario")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val chats = document.getLong("visitas_chats")?.toInt() ?: 0
                    val ajustes = document.getLong("visitas_ajustes")?.toInt() ?: 0
                    val tiempoUso = document.getLong("tiempo_uso_ms") ?: 0L

                    scope.launch {
                        context.dataStore.edit { prefs ->
                            prefs[VISITAS_CHATS] = chats
                            prefs[VISITAS_AJUSTES] = ajustes
                            prefs[TIEMPO_USO_MS] = tiempoUso
                        }
                    }
                }
            }
    }
}
