package com.example.kollab.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()

    val registerSuccess = MutableLiveData<Boolean>()

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
