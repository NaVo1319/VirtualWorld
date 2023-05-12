package com.example.virtualworld

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import okhttp3.internal.wait
import org.junit.Rule
import org.junit.Test

class ChatTest {
    @get:Rule
    val rule = createAndroidComposeRule<ActionActivity>()
    @Test
    fun turnForwardMessageCheck(){
        rule.onNodeWithTag("Search").assertExists().performClick()
        rule.onAllNodesWithTag("Item")[3].assertExists().performClick()
        rule.onNodeWithText("petyAAA").assertExists()
        rule.onNodeWithTag("ChatScreen").assertExists()
        rule.onNodeWithTag("Previous  message").assertExists().performClick()
        rule.onNodeWithText("ку-ку").assertExists()
        rule.onNodeWithTag("Next message").assertExists().performClick()
        rule.onNodeWithText("Привет Как дела").assertExists()
    }
    @Test
    fun turnBackMessageCheck(){
        rule.onNodeWithTag("Search").assertExists().performClick()
        rule.onAllNodesWithTag("Item")[3].assertExists().performClick()
        rule.onNodeWithText("petyAAA").assertExists()
        rule.onNodeWithTag("ChatScreen").assertExists()
        rule.onNodeWithTag("Previous  message").assertExists().performClick()
        rule.onNodeWithText("ку-ку").assertExists()
        rule.onNodeWithTag("Next message").assertExists().performClick()
        rule.onNodeWithText("Привет Как дела").assertExists()

    }
    @Test
    fun sendMessageCheck(){
        rule.onNodeWithTag("Search").assertExists().performClick()
        rule.onAllNodesWithTag("Item")[3].assertExists().performClick()
        rule.onNodeWithText("petyAAA").assertExists()
        rule.onNodeWithTag("ChatScreen").assertExists()
        rule.onNodeWithTag("Write Message").assertExists().performClick()
        rule.onNodeWithTag("Enter message").assertExists().performTextInput("Hello, my dear")
        rule.onNodeWithText("Hello, my dear").assertExists()

    }
    @Test
    fun sendVoiceMessageCheck(){
        rule.onNodeWithTag("Search").assertExists().performClick()
        rule.onAllNodesWithTag("Item")[3].assertExists().performClick()
        rule.onNodeWithText("petyAAA").assertExists()
        rule.onNodeWithTag("ChatScreen").assertExists()
        rule.onNodeWithTag("Write Message").assertExists().performClick()
        rule.onNodeWithTag("Start Listen").assertExists().performClick()
        rule.onNodeWithTag("Start Listen").assertExists().performClick()
    }
}