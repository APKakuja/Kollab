package com.example.kollab.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()

    val loginSuccess = MutableLiveData<Boolean>()

    fun onLoginClicked() {
        val emailValue = email.value ?: ""
        val passValue = password.value ?: ""

        var valid = true

        // Validació de l'email

        if (!emailValue.contains("@") || !emailValue.contains(".")) {
            emailError.value = "Email no es válid"
            valid = false
        } else {
            emailError.value = null
        }

        // Validació password
        if (passValue.length < 6) {
            passwordError.value = "La contrasenya ha de tenir un minim de 6 caracteres"
            valid = false
        } else {
            passwordError.value = null
        }

        loginSuccess.value = valid
    }
}
