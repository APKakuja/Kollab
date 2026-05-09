package com.example.kollab.perfilesusers

import android.Manifest
import android.content.pm.PackageManager
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import com.bumptech.glide.Glide
import com.example.kollab.*
import com.example.kollab.chat.ChatListActivity
import com.example.kollab.service.RetrofitClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import java.text.Normalizer
import kotlin.math.abs
import java.util.Locale

class MainView : AppCompatActivity() {

    private var x1 = 0f
    private var x2 = 0f
    private val mindistance = 150

    private var perfilIndex = 0

    private lateinit var cardPerfil: View
    private lateinit var imgPerfil: ImageView
    private lateinit var txtNombre: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var btnVerPerfil: Button
    private lateinit var btnAceptar: ImageView
    private lateinit var btnDescartar: ImageView
    private lateinit var voiceDebugText: TextView

    private var perfilesAPI: List<Perfil> = emptyList()
    private var perfilesFiltrados: List<Perfil> = emptyList()

    private lateinit var statsDataStore: StatsDataStore
    private var sessionStartMs: Long? = null
    private var voiceEnabled = false
    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private val voiceHandler = Handler(Looper.getMainLooper())
    private var listeningToast: Toast? = null
    private var listeningToastShown = false
    private var isListening = false
    private var isStartingListening = false
    private var hasVoiceInput = false
    private var ttsReady = false
    private var pendingSpeechMessage: String? = null
    private var mainOptionsAnnounced = false

    private val recordAudioPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            iniciarAsistenteDeVoz()
        } else {
            Toast.makeText(this, R.string.voice_permission_required, Toast.LENGTH_SHORT).show()
            speak(getString(R.string.voice_permission_required))
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view)

        setSupportActionBar(findViewById(R.id.mainToolbar))

        statsDataStore = StatsDataStore(this)
        lifecycleScope.launch {
            statsDataStore.initializeNewAppSessionIfNeeded()
        }

        inicializarTextToSpeech()
        configurarSpeechRecognizer()

        lifecycleScope.launch {
            voiceEnabled = statsDataStore.voiceEnabled.first()
            invalidateOptionsMenu()
            announceMainOptionsIfNeeded()
        }

        cardPerfil = findViewById(R.id.cardPerfil)
        imgPerfil = findViewById(R.id.profileImage)
        txtNombre = findViewById(R.id.profileName)
        txtDescripcion = findViewById(R.id.profileDescription)
        btnVerPerfil = findViewById(R.id.viewProfileButton)
        btnAceptar = findViewById(R.id.btnAceptar)
        btnDescartar = findViewById(R.id.btnDescartar)
        voiceDebugText = findViewById(R.id.voiceDebugText)

        lifecycleScope.launch {
            try {
                val perfilesDTO = RetrofitClient.api.getPerfiles()
                perfilesAPI = perfilesDTO.map { it.toPerfil() }
                perfilesFiltrados = perfilesAPI
                mostrarPerfil()
                } catch (_: Exception) {
                    txtNombre.text = getString(R.string.voice_connection_error_title)
                    txtDescripcion.text = getString(R.string.voice_connection_error_message)
            }
        }

        cardPerfil.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = event.x
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.x - x1
                    view.translationX = deltaX
                    view.rotation = deltaX / 20
                    true
                }
                MotionEvent.ACTION_UP -> {
                    x2 = event.x
                    val deltaX = x2 - x1

                    if (abs(deltaX) > mindistance) {
                        if (deltaX < 0) animateSwipeLeft(view)
                        else animateSwipeRight(view)
                    } else {
                        resetCardView(view)
                    }
                    true
                }
                else -> false
            }
        }

        findViewById<ImageButton>(R.id.victoriaButton).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        btnAceptar.setOnClickListener { animateSwipeLeft(cardPerfil) }
        btnDescartar.setOnClickListener { animateSwipeRight(cardPerfil) }
    }

    private fun inicializarTextToSpeech() {
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
                textToSpeech?.language = Locale.forLanguageTag("es-ES")
                pendingSpeechMessage?.let { message ->
                    pendingSpeechMessage = null
                    textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "kollab-voice")
                }
            }
        }
    }

    private fun configurarSpeechRecognizer() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            return
        }

        if (speechRecognizer != null) return

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    isListening = true
                    isStartingListening = false
                    hasVoiceInput = false
                    if (!listeningToastShown) {
                        listeningToast = Toast.makeText(this@MainView, R.string.voice_listening, Toast.LENGTH_SHORT)
                        listeningToast?.show()
                        listeningToastShown = true
                    }
                }
                override fun onBeginningOfSpeech() {
                    hasVoiceInput = true
                }
                override fun onRmsChanged(rmsdB: Float) {
                    if (rmsdB > 0f) {
                        hasVoiceInput = true
                    }
                }
                override fun onBufferReceived(buffer: ByteArray?) = Unit
                override fun onEndOfSpeech() {
                    isListening = false
                    isStartingListening = false
                    listeningToast?.cancel()
                    listeningToast = null
                    listeningToastShown = false
                }
                override fun onPartialResults(partialResults: Bundle?) {
                    val partial = partialResults
                        ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        .orEmpty()
                        .firstOrNull()
                        .orEmpty()

                    if (partial.isNotBlank()) {
                        setVoiceDebug(getString(R.string.voice_heard_format, partial))
                        Toast.makeText(this@MainView, getString(R.string.voice_heard_format, partial), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onEvent(eventType: Int, params: Bundle?) = Unit

                override fun onError(error: Int) {
                    isListening = false
                    isStartingListening = false
                    listeningToast?.cancel()
                    listeningToast = null
                    listeningToastShown = false

                    val message = when (error) {
                        SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE,
                        SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED -> getString(R.string.voice_error_language_unavailable)
                        SpeechRecognizer.ERROR_NO_MATCH -> {
                            if (hasVoiceInput) getString(R.string.voice_error_no_match)
                            else getString(R.string.voice_error_no_audio)
                        }
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> getString(R.string.voice_error_timeout)
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> getString(R.string.voice_recognizer_busy)
                        else -> getString(R.string.voice_error_code_format, error)
                    }
                    setVoiceDebug(message)
                    Toast.makeText(this@MainView, message, Toast.LENGTH_SHORT).show()
                }

                override fun onResults(results: Bundle?) {
                    isListening = false
                    isStartingListening = false
                    listeningToast?.cancel()
                    listeningToast = null
                    listeningToastShown = false

                    val recognized = results
                        ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        .orEmpty()
                    android.util.Log.d("VOICE", "RESULTS: ${recognized.joinToString()}")

                    if (recognized.isNotEmpty()) {
                        setVoiceDebug("RESULTS: ${recognized.joinToString(" | ")}")
                    } else {
                        setVoiceDebug(getString(R.string.voice_no_results))
                    }

                    processVoiceCandidates(recognized)
                    Log.e("VOICE_DEBUG", "RESULTS SIZE: ${recognized.size}")
                    recognized.forEach {
                        Log.e("VOICE_DEBUG", "-> $it")
                    }
                }
            })
        }
    }

    private fun recognitionIntent(): Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)

        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000L)
        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000L)

        putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_prompt))
    }

    private fun scheduleListeningStart(delayMs: Long) {
        if (isStartingListening) return
        isStartingListening = true

        voiceHandler.postDelayed({
            try {
                speechRecognizer?.startListening(recognitionIntent())
            } catch (_: Exception) {
                isStartingListening = false
                isListening = false
                setVoiceDebug(getString(R.string.voice_error_code_format, SpeechRecognizer.ERROR_CLIENT))
                Toast.makeText(this, getString(R.string.voice_error_code_format, SpeechRecognizer.ERROR_CLIENT), Toast.LENGTH_SHORT).show()
            }
        }, delayMs)
    }

    private fun iniciarAsistenteDeVoz() {
        if (!voiceEnabled) {
            Toast.makeText(this, R.string.voice_not_enabled, Toast.LENGTH_SHORT).show()
            speak(getString(R.string.voice_not_enabled))
            return
        }

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, R.string.voice_not_supported, Toast.LENGTH_SHORT).show()
            speak(getString(R.string.voice_not_supported))
            return
        }

        configurarSpeechRecognizer()
        textToSpeech?.stop()
        pendingSpeechMessage = null
        setVoiceDebug(getString(R.string.voice_prompt))
        Toast.makeText(this, R.string.voice_prompt, Toast.LENGTH_SHORT).show()

        if (isListening || isStartingListening) {
            speechRecognizer?.cancel()
            isListening = false
            isStartingListening = false
            scheduleListeningStart(700)
        } else {
            scheduleListeningStart(250)
        }
    }

    private fun processVoiceCandidates(candidates: List<String>) {
        if (candidates.isEmpty()) {
            Toast.makeText(this, R.string.voice_no_results, Toast.LENGTH_SHORT).show()
            return
        }

        val rawTop = candidates.first()

        android.util.Log.d("VOICE", "RAW TOP: $rawTop")

        if (rawTop.isNotBlank()) {
            setVoiceDebug("RAW: $rawTop")
        }

        val matched = candidates.any { candidate ->
            processVoiceCommand(candidate)
        }

        if (!matched) {
            setVoiceDebug("❌ No match para: ${candidates.joinToString()}")
            Toast.makeText(this, R.string.voice_command_not_recognized, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setVoiceDebug(message: String) {
        voiceDebugText.text = message
    }

    private fun processVoiceCommand(rawCommand: String): Boolean {
        val command = normalizeCommand(rawCommand)

        android.util.Log.d("VOICE", "NORMALIZED: $command")
        setVoiceDebug("RAW: $rawCommand\nCMD: $command")

        if (command.contains("a")) {
            Toast.makeText(this, "DEBUG: detecta algo", Toast.LENGTH_SHORT).show()
        }

        return when {

            command.contains("chat") ||
                    command.contains("mensaje") -> {
                speakAndNavigate("Abriendo chats") {
                    startActivity(Intent(this, ChatListActivity::class.java))
                }
                true
            }

            command.contains("ajust") ||
                    command.contains("config") -> {
                speakAndNavigate("Abriendo ajustes") {
                    startActivity(Intent(this, AjustesActivity::class.java))
                }
                true
            }

            command.contains("estad") ||
                    command.contains("graf") ||
                    command.contains("reporte") -> {
                speakAndNavigate("Abriendo estadísticas") {
                    startActivity(Intent(this, StatsActivity::class.java))
                }
                true
            }

            command.contains("ayuda") ||
                    command.contains("opcion") -> {
                speak("Puedes decir: abrir chats, ajustes o estadísticas")
                true
            }

            else -> false
        }
    }

    private fun speakAndNavigate(message: String, action: () -> Unit) {
        speak(message)
        voiceHandler.postDelayed({
            action()
        }, 450)
    }

    private fun speak(message: String) {
        if (!ttsReady) {
            pendingSpeechMessage = message
            return
        }
        textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "kollab-voice")
    }

    private fun announceMainOptionsIfNeeded() {
        if (!voiceEnabled || mainOptionsAnnounced) return

        if (!ttsReady) {
            pendingSpeechMessage = getString(R.string.voice_main_options_message)
            return
        }

        mainOptionsAnnounced = true
        speak(getString(R.string.voice_main_options_message))
    }

    private fun containsAny(command: String, keywords: List<String>): Boolean {
        return keywords.any { keyword ->
            command.contains(keyword, ignoreCase = true)
        }
    }

    private fun normalizeCommand(input: String): String {
        val lowerCase = input.lowercase(Locale.ROOT)
        val normalized = Normalizer.normalize(lowerCase, Normalizer.Form.NFD)
        val noAccents = normalized.replace("\\p{Mn}+".toRegex(), "")
        return noAccents
            .replace("[^\\p{L}\\p{Nd} ]".toRegex(), " ")
            .replace("\\s+".toRegex(), " ")
            .trim()
    }

    private fun mostrarPerfil() {
        if (perfilesFiltrados.isEmpty()) {
            txtNombre.text = getString(R.string.no_profiles_title)
            txtDescripcion.text = getString(R.string.no_profiles_message)
            return
        }

        if (perfilIndex >= perfilesFiltrados.size) perfilIndex = 0

        val perfil = perfilesFiltrados[perfilIndex]

        Glide.with(this).load(perfil.fotoUrl).into(imgPerfil)

        txtNombre.text = perfil.nombre
        txtDescripcion.text = perfil.descripcion

        btnVerPerfil.setOnClickListener {
            val intent = Intent(this, ProfileView::class.java)
            intent.putExtra("perfilId", perfil.id)
            startActivity(intent)
        }
    }

    private fun animateSwipeLeft(view: View) {
        val perfil = perfilesFiltrados[perfilIndex]

        view.animate()
            .translationX(-1000f)
            .rotation(-30f)
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                perfilIndex++
                resetCardView(view)
                mostrarPerfil()

                lifecycleScope.launch {
                    RetrofitClient.api.crearChat(perfil.id)
                    startActivity(Intent(this@MainView, ChatListActivity::class.java))
                }
            }
            .start()
    }

    private fun animateSwipeRight(view: View) {
        view.animate()
            .translationX(1000f)
            .rotation(30f)
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                perfilIndex++
                resetCardView(view)
                mostrarPerfil()
            }
            .start()
    }

    private fun resetCardView(view: View) {
        view.translationX = 0f
        view.rotation = 0f
        view.alpha = 1f
    }

    override fun onResume() {
        super.onResume()
        sessionStartMs = System.currentTimeMillis()
        mainOptionsAnnounced = false

        lifecycleScope.launch {
            voiceEnabled = statsDataStore.voiceEnabled.first()
            invalidateOptionsMenu()
            announceMainOptionsIfNeeded()
        }
    }

    override fun onPause() {
        super.onPause()
        isListening = false
        speechRecognizer?.cancel()
        voiceHandler.removeCallbacksAndMessages(null)
        mainOptionsAnnounced = false
        listeningToast?.cancel()
        listeningToast = null
        listeningToastShown = false

        val inicio = sessionStartMs ?: return
        val duracionMs = System.currentTimeMillis() - inicio
        sessionStartMs = null

        lifecycleScope.launch {
            statsDataStore.addTiempoUsoMs(duracionMs)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_chats -> {
                startActivity(Intent(this, ChatListActivity::class.java))
                return true
            }

            R.id.menu_ajustes -> {
                startActivity(Intent(this, AjustesActivity::class.java))
                return true
            }

            R.id.menu_estadisticas -> {
                startActivity(Intent(this, StatsActivity::class.java))
                return true
            }

            R.id.menu_voz -> {
                if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    iniciarAsistenteDeVoz()
                } else {
                    recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceHandler.removeCallbacksAndMessages(null)
        listeningToast?.cancel()
        listeningToast = null
        speechRecognizer?.destroy()
        speechRecognizer = null
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}