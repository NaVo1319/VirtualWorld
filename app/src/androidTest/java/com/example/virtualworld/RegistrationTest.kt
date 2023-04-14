package com.example.virtualworld

    import androidx.compose.ui.test.*
    import androidx.compose.ui.test.junit4.createAndroidComposeRule
    import androidx.compose.ui.test.junit4.createComposeRule
    import okhttp3.internal.wait
    import org.junit.Rule
    import org.junit.Test

class RegistrationTest {
    @get:Rule
    val rule = createComposeRule()
    @Test
    fun enterEmail(){
        rule.setContent { MainActivity() }
        rule.waitForIdle()
        rule.onNodeWithText("Email").assertExists()
    }
    @Test
    fun enterPassword(){

    }
    @Test
    fun validEmailPasswordCheck(){

    }
    @Test
    fun checkGoogleAuth(){

    }

}