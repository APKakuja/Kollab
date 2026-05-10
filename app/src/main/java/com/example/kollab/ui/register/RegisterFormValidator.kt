package com.example.kollab.ui.register

data class RegisterValidationResult(
    val nameError: String? = null,
    val surnameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
) {
    val isValid: Boolean
        get() = nameError == null && surnameError == null && emailError == null &&
            passwordError == null && confirmPasswordError == null
}

object RegisterFormValidator {
    const val ERROR_NAME_EMPTY = "El nombre no puede estar vacío"
    const val ERROR_NAME_INVALID = "El nombre solo puede contener letras"
    const val ERROR_SURNAME_EMPTY = "El apellido no puede estar vacío"
    const val ERROR_SURNAME_INVALID = "El apellido solo puede contener letras"
    const val ERROR_EMAIL_INVALID = "Email no es válido"
    const val ERROR_PASSWORD_SHORT = "La contraseña debe tener mínimo 6 caracteres"
    const val ERROR_CONFIRM_PASSWORD_EMPTY = "Debes confirmar la contraseña"
    const val ERROR_CONFIRM_PASSWORD_MISMATCH = "Las contraseñas no coinciden"

    private val soloLetrasRegex = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")

    fun validate(
        name: String,
        surname: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): RegisterValidationResult {
        val nameError = when {
            name.isBlank() -> ERROR_NAME_EMPTY
            !soloLetrasRegex.matches(name) -> ERROR_NAME_INVALID
            else -> null
        }

        val surnameError = when {
            surname.isBlank() -> ERROR_SURNAME_EMPTY
            !soloLetrasRegex.matches(surname) -> ERROR_SURNAME_INVALID
            else -> null
        }

        val emailError = when {
            !email.contains("@") || !email.contains(".") || !email.contains("com") -> ERROR_EMAIL_INVALID
            else -> null
        }

        val passwordError = when {
            password.length < 6 -> ERROR_PASSWORD_SHORT
            else -> null
        }

        val confirmPasswordError = when {
            confirmPassword.isBlank() -> ERROR_CONFIRM_PASSWORD_EMPTY
            password != confirmPassword -> ERROR_CONFIRM_PASSWORD_MISMATCH
            else -> null
        }

        return RegisterValidationResult(
            nameError = nameError,
            surnameError = surnameError,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError,
        )
    }
}
