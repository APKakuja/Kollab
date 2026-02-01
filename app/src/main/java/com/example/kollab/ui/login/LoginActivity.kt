package com.example.kollab.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.kollab.MainView
import com.example.kollab.R
import com.example.kollab.ui.register.RegisterActivity

/**
 * Activity que se encarga de gestionar la pantalla de inicio de sesión.
 *
 * Esta clase inicializa los components de la interfaz, y
 * observa los cambios que vienen de el LoginViewModel,
 * tamb gestiona la navegación hacia otras pantallas dependiendo del resultado del login
 */
class LoginActivity : AppCompatActivity() {

    /**
     * ViewModel que tiene la logica de validacion del login.
     */
    private lateinit var vm: LoginViewModel

    /**
     * El metodo que se llama al crear la Activity.
     * encargado de:
     * - Instalar el SplashScreen con la animación.
     * - Iniciar el ViewModel
     * - Configurar los listeners de los botones.
     * - Observa los LiveData del ViewModel para mostrar errores o navegar.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        setContentView(R.layout.activity_login)

        vm = ViewModelProvider(this).get(LoginViewModel::class.java)

        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val registerButton = findViewById<Button>(R.id.buttonRegister)

        // Observadores de errores
        vm.emailError.observe(this) { error ->
            emailEditText.error = error
        }

        vm.passwordError.observe(this) { error ->
            passwordEditText.error = error
        }

        // Observador de éxito

        vm.loginSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainView::class.java))
                finish()
            }
        }

        // Listener del botón Login
        loginButton.setOnClickListener {
            vm.email.value = emailEditText.text.toString()
            vm.password.value = passwordEditText.text.toString()
            vm.onLoginClicked()
        }

        // Listener del botón Login
        //loginButton.setOnClickListener {

          //  val intent = Intent(this, MainView::class.java)
           // startActivity(intent)
           // finish()
       // }

        // Listener del botón Register
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}