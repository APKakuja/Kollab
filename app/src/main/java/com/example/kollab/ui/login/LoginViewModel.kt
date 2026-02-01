package com.example.kollab.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel con la logica de validación de login.
 *
 * Tiene los campos introducidos por el usuario, los mensajes de error asociados y
 * un indicador de login con exito cuando las credenciales son correctas.
 */

class LoginViewModel : ViewModel() {

    /**
     * Email introducido por el usuario.
     */
    val email = MutableLiveData<String>()

    /**
     * Contrasenya introducida por el usuario.
     */
    val password = MutableLiveData<String>()

    /**
     * Mensaje de error si formato del email no es correcto.
     */
    val emailError = MutableLiveData<String?>()
    /**
     * Mensaje de error si formato de la contraseña no es correcto.
     */
    val passwordError = MutableLiveData<String?>()

    /**
     * Indica si login ha sido exitoso.
     */
    val loginSuccess = MutableLiveData<Boolean>()

    /**
     * Metodo que se llama cuando el usuario pulsa el boton de login.
     *
     * Hace:
     * - Validación del email
     * - Validación de la contraseña
     * - Actualiza los LiveData
     * - Emite el resultado del login
     */
    fun onLoginClicked() {
        val emailValue = email.value ?: ""
        val passValue = password.value ?: ""

        var valid = true

        // Validació de l'email

        if (!emailValue.contains("@") || !emailValue.contains(".") || !emailValue.contains("com")) {
            emailError.value = "Email no es válid"
            valid = false
        } else {
            emailError.value = null
        }

        // Validació password
        if (passValue.length < 6 || passValue.length > 50) {
            passwordError.value = "Contrasenya incorrecta"
            valid = false
        } else {
            passwordError.value = null
        }

        loginSuccess.value = valid
    }
}
