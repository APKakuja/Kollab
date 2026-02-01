package com.example.kollab.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel que gestiona la lógica de registro de usuarios.
 *
 * Contiene LiveData para los campos de nombre,apellido,email y contraseña,
 * sus posibles errores y el estado de éxito del registro.
 */
class RegisterViewModel : ViewModel() {

    /** Expresión regular para validar solo letras en el nombre y apellido. */
    val soloLetrasRegex = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")

    /** Nombre ingresado por el usuario. */
    val name = MutableLiveData<String>()

    /** Mensaje de error del nombre, si aplica. */
    val nameError = MutableLiveData<String>()

    /** Apellido ingresado por el usuario. */
    val surname = MutableLiveData<String>()

    /** Mensaje de error del apellido, si aplica. */
    val surnameError = MutableLiveData<String>()

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
     * Valida el nombre,el apellido, el email y la contraseña, actualiza los errores
     * correspondientes y establece [registerSuccess] según la validez.
     */
    fun onRegisterClicked() {
        val nameValue = name.value ?: ""
        val surnValue = surname.value ?: ""
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

        if (nameValue.isEmpty()) {
            nameError.value = "El nombre no puede estar vacío"
            valid = false
        } else if (!soloLetrasRegex.matches(nameValue)){
            nameError.value = "El nombre solo puede contener letras"
            valid = false
        } else {
            nameError.value = null
        }

        if (surnValue.isEmpty()) {
            surnameError.value = "El apellido no puede estar vacío"
            valid = false
        } else if (!soloLetrasRegex.matches(surnValue)){
            surnameError.value = "El apellido solo puede contener letras"
            valid = false
        } else {
            surnameError.value = null
        }

        registerSuccess.value = valid
    }
}
