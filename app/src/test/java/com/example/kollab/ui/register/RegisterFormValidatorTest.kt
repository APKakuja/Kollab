package com.example.kollab.ui.register

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterFormValidatorTest {

    @Test
    fun `validate returns valid result when all fields are correct`() {
        val result = RegisterFormValidator.validate(
            name = "Adria",
            surname = "Gomez",
            email = "adria@test.com",
            password = "123456",
            confirmPassword = "123456",
        )

        assertTrue(result.isValid)
    }

    @Test
    fun `validate returns email error when email is invalid`() {
        val result = RegisterFormValidator.validate(
            name = "Adria",
            surname = "Gomez",
            email = "adria-test.com",
            password = "123456",
            confirmPassword = "123456",
        )

        assertFalse(result.isValid)
        assertTrue(result.emailError == RegisterFormValidator.ERROR_EMAIL_INVALID)
    }

    @Test
    fun `validate returns password error when password is too short`() {
        val result = RegisterFormValidator.validate(
            name = "Adria",
            surname = "Gomez",
            email = "adria@test.com",
            password = "123",
            confirmPassword = "123",
        )

        assertFalse(result.isValid)
        assertTrue(result.passwordError == RegisterFormValidator.ERROR_PASSWORD_SHORT)
    }

    @Test
    fun `validate returns mismatch error when passwords are different`() {
        val result = RegisterFormValidator.validate(
            name = "Adria",
            surname = "Gomez",
            email = "adria@test.com",
            password = "123456",
            confirmPassword = "1234567",
        )

        assertFalse(result.isValid)
        assertTrue(result.confirmPasswordError == RegisterFormValidator.ERROR_CONFIRM_PASSWORD_MISMATCH)
    }

    @Test
    fun `validate returns name error when name contains numbers`() {
        val result = RegisterFormValidator.validate(
            name = "Adr1a",
            surname = "Gomez",
            email = "adria@test.com",
            password = "123456",
            confirmPassword = "123456",
        )

        assertFalse(result.isValid)
        assertTrue(result.nameError == RegisterFormValidator.ERROR_NAME_INVALID)
    }
}

