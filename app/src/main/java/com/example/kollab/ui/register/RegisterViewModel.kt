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

    /** Confirmación de la contraseña ingresada por el usuario. */
    val confirmPassword = MutableLiveData<String>()

    /** Mensaje de error del email, si aplica. */
    val emailError = MutableLiveData<String?>()

    /** Mensaje de error de la contraseña, si aplica. */
    val passwordError = MutableLiveData<String?>()

    /** Mensaje de error de la confirmación de contraseña, si aplica. */
    val confirmPasswordError = MutableLiveData<String?>()

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
        val confirmPasswordValue = confirmPassword.value ?: ""

        val result = RegisterFormValidator.validate(
            name = nameValue,
            surname = surnValue,
            email = emailValue,
            password = passValue,
            confirmPassword = confirmPasswordValue,
        )

        nameError.value = result.nameError
        surnameError.value = result.surnameError
        emailError.value = result.emailError
        passwordError.value = result.passwordError
        confirmPasswordError.value = result.confirmPasswordError

        registerSuccess.value = result.isValid
    }
}
