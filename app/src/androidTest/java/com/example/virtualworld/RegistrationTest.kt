package com.example.virtualworld

    import androidx.compose.ui.test.*
    import androidx.compose.ui.test.junit4.createAndroidComposeRule
    import androidx.compose.ui.test.junit4.createComposeRule
    import androidx.test.ext.junit.rules.ActivityScenarioRule
    import okhttp3.internal.wait
    import org.junit.Rule
    import org.junit.Test

class RegistrationTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()
    @Test
    fun enterEmail(){
        rule.onNodeWithText("Email").assertExists()
        val email = rule.onNodeWithTag("Email")
        email.assertExists()
        email.performTextInput("example@gmail.com")
        rule.onNodeWithText("example@gmail.com")
    }
    @Test
    fun enterPassword(){
        rule.onNodeWithText("Password").assertExists()
        val password = rule.onNodeWithTag("Password")
        password.assertExists()
        password.performTextInput("hgfhDWSW_23232_fds")
        rule.onNodeWithText("hgfhDWSW_23232_fds")
    }
    @Test
    fun validEmailPasswordCheck(){
        val regBut = rule.onNodeWithText("Registration")
        val singBut = rule.onNodeWithText("Sign In")
        val email = rule.onNodeWithTag("Email")
        val password = rule.onNodeWithTag("Password")
        email.performTextInput("examplegmail.com")
        singBut.performClick()
        rule.onNodeWithText("Error in email or password: The password must be longer than 8 characters, the email must be similar to example@gmail.com").assertExists()
        regBut.performClick()
        rule.onNodeWithText("Error in email or password: The password must be longer than 8 characters, the email must be similar to example@gmail.com").assertExists()
        email.performTextClearance()
        email.performTextInput("example@gmail.com")
        password.performTextInput("12345678")
        regBut.performClick()
        singBut.performClick()
        rule.onNodeWithText("Email").assertExists()
        rule.onNodeWithText("Password").assertExists()

    }
    @Test
    fun checkGoogleAuth(){
        rule.onNodeWithTag("login google").assertExists()
        rule.onNodeWithTag("login google").performClick()
    }

}