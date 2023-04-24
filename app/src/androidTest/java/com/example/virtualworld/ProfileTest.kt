package com.example.virtualworld

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class ProfileTest {
    @get:Rule
    val rule = createAndroidComposeRule<ActionActivity>()
    @Test
    fun clickExitCheck(){
        rule.onNodeWithText("Profile").assertExists().performClick()
        rule.onNodeWithTag("Exit").assertExists().performClick()
        rule.onNodeWithText("Email").assertExists()
    }
    @Test
    fun clickDataEditCheck(){

    }
    @Test
    fun clickAvatarEditCheck(){

    }
    @Test
    fun dataEditCheck(){

    }
    @Test
    fun avatarEditCheck(){

    }
    @Test
    fun avatarClickCheck(){

    }
}