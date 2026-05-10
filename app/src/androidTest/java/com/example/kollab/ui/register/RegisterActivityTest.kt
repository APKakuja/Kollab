package com.example.kollab.ui.register

import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.kollab.R
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterActivityTest {

    @Test
    fun showingErrorsWhenEmailAndPasswordAreInvalid() {
        val scenario = ActivityScenario.launch(RegisterActivity::class.java)
        try {
            onView(withId(R.id.inputNombre)).perform(replaceText("Adria"), closeSoftKeyboard())
            onView(withId(R.id.inputApellidos)).perform(replaceText("Gomez"), closeSoftKeyboard())
            onView(withId(R.id.inputEmail)).perform(replaceText("adria-test.com"), closeSoftKeyboard())
            onView(withId(R.id.inputPassword)).perform(replaceText("123456"), closeSoftKeyboard())
            onView(withId(R.id.inputConfirmPassword)).perform(replaceText("654321"), closeSoftKeyboard())
            onView(withId(R.id.btnRegistrar)).perform(click())

            onView(withId(R.id.inputEmail)).check(hasErrorText("Email no es válido"))
            onView(withId(R.id.inputConfirmPassword)).check(hasErrorText("Las contraseñas no coinciden"))
        } finally {
            scenario.close()
        }
    }

    @Test
    fun successfulRegistrationFinishesRegisterActivity() {
        val scenario = ActivityScenario.launch(RegisterActivity::class.java)
        try {
            onView(withId(R.id.inputNombre)).perform(replaceText("Adria"), closeSoftKeyboard())
            onView(withId(R.id.inputApellidos)).perform(replaceText("Gomez"), closeSoftKeyboard())
            onView(withId(R.id.inputEmail)).perform(replaceText("adria@test.com"), closeSoftKeyboard())
            onView(withId(R.id.inputPassword)).perform(replaceText("123456"), closeSoftKeyboard())
            onView(withId(R.id.inputConfirmPassword)).perform(replaceText("123456"), closeSoftKeyboard())
            onView(withId(R.id.btnRegistrar)).perform(click())

            scenario.onActivity { activity ->
                assertTrue(activity.isFinishing)
            }
        } finally {
            scenario.close()
        }
    }

    private fun hasErrorText(expected: String): ViewAssertion = ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) throw noViewFoundException

        val editText = view as EditText
        val actual = editText.error?.toString()
        if (actual != expected) {
            throw AssertionError("Expected error <$expected> but was <$actual>")
        }
    }
}
