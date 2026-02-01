package com.example.kollab.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kollab.R
import com.example.kollab.ui.login.LoginActivity

/**
 * Activity que gestiona la pantalla de registro de usuario.
 *
 * Inicializa los componentes de la interfaz, observa los LiveData del [RegisterViewModel],
 * muestra errores y realiza la navegación hacia la pantalla de login cuando el registro es exitoso.
 */
class RegisterActivity : AppCompatActivity() {

    /** ViewModel que contiene la lógica de validación del registro. */
    private lateinit var vm: RegisterViewModel

    /**
     * Se ejecuta al crear la Activity.
     *
     * Encargado de:
     * - Inicializar el ViewModel.
     * - Configurar listeners de los botones.
     * - Observar los LiveData para mostrar errores y éxito de registro.
     *
     * @param savedInstanceState Bundle que contiene el estado guardado de la Activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        vm = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val emailEditText = findViewById<EditText>(R.id.inputEmail)
        val passwordEditText = findViewById<EditText>(R.id.inputPassword)
        val registerButton = findViewById<Button>(R.id.btnRegistrar)

        vm.emailError.observe(this) { error -> emailEditText.error = error }
        vm.passwordError.observe(this) { error -> passwordEditText.error = error }

        vm.registerSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Registro correcto", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        registerButton.setOnClickListener {
            vm.email.value = emailEditText.text.toString()
            vm.password.value = passwordEditText.text.toString()
            vm.onRegisterClicked()
        }
    }
}
