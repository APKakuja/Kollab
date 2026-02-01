package com.example.kollab.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel que gestiona la lógica de registro de usuarios.
 *
 * Contiene LiveData para los campos de email y contraseña,
 * sus posibles errores y el estado de éxito del registro.
 */
class RegisterViewModel : ViewModel() {

    /** Email ingresado por el usuario. */
    val email = MutableLiveData<String>()

    /** Contraseña ingresada por el usuario. */
    val password = MutableLiveData<String>()

    /** Mensaje de error del email, si aplica. */
    val emailError = MutableLiveData<String?>()

    /** Mensaje de error de la contraseña, si aplica. */
    val passwordError = MutableLiveData<String?>()

    /** Indica si el registro fue exitoso. */
    val registerSuccess = MutableLiveData<Boolean>()

    /**
     * Se llama al hacer clic en el botón de registro.
     *
     * Valida el email y la contraseña, actualiza los errores
     * correspondientes y establece [registerSuccess] según la validez.
     */
    fun onRegisterClicked() {
        val emailValue = email.value ?: ""
        val passValue = password.value ?: ""

        var valid = true

        if (!emailValue.contains("@") || !emailValue.contains(".") || !emailValue.contains("com")) {
            emailError.value = "Email no es válido"
            valid = false
        } else {
            emailError.value = null
        }

        if (passValue.length < 6) {
            passwordError.value = "La contraseña debe tener mínimo 6 caracteres"
            valid = false
        } else {
            passwordError.value = null
        }

        registerSuccess.value = valid
    }
}
