package com.example.kollab.ui.register

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterViewModelTest {

    @Test
    fun `validate valid form returns success`() {
        val result = RegisterFormValidator.validate(
            name = "Adria",
            surname = "Gomez",
            email = "adria@test.com",
            password = "123456",
            confirmPassword = "123456",
        )

        assertTrue(result.isValid)
        assertEquals(null, result.nameError)
        assertEquals(null, result.surnameError)
        assertEquals(null, result.emailError)
        assertEquals(null, result.passwordError)
        assertEquals(null, result.confirmPasswordError)
    }

    @Test
    fun `validate mismatch passwords returns confirm error`() {
        val result = RegisterFormValidator.validate(
            name = "Adria",
            surname = "Gomez",
            email = "adria@test.com",
            password = "123456",
            confirmPassword = "654321",
        )

        assertFalse(result.isValid)
        assertEquals(RegisterFormValidator.ERROR_CONFIRM_PASSWORD_MISMATCH, result.confirmPasswordError)
    }
}
