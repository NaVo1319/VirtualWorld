package com.example.virtualworld

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class UserListTest {
    @get:Rule
    val rule = createAndroidComposeRule<ActionActivity>()
    @Test
    fun itemClick(){
        rule.onNodeWithTag("Search").assertExists().performClick()
        rule.onAllNodesWithTag("Item")[0].assertExists().performClick()
        rule.onNodeWithTag("ChatScreen").assertExists()
    }
    @Test
    fun searchPositiveResult(){
        val field = rule.onNodeWithTag("FindField").assertExists()
        val button = rule.onNodeWithTag("Search").assertExists().performClick()
        field.performTextInput("lupa")
        button.performClick()
        rule.onAllNodesWithTag("Item").assertAll(hasText("lupa", ignoreCase = true))
    }
    @Test
    fun searchNegativeResult(){
        val field = rule.onNodeWithTag("FindField").assertExists()
        val button = rule.onNodeWithTag("Search").assertExists().performClick()
        field.performTextInput("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        button.performClick()
        rule.onAllNodesWithTag("Item").fetchSemanticsNodes().isEmpty()
    }
    @Test
    fun searchEmptyRequest(){
        val field = rule.onNodeWithTag("FindField").assertExists()
        val button = rule.onNodeWithTag("Search").assertExists().performClick()
        field.performTextInput("")
        button.performClick()
        rule.onAllNodesWithTag("Item").fetchSemanticsNodes().isNotEmpty()
    }
}