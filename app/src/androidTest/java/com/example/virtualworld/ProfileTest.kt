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
        //rule.onNodeWithText("Profile").assertExists().performClick()
        //rule.onNodeWithTag("Exit").assertExists().performClick()
        //rule.onNodeWithText("Email").assertExists()
    }
    @Test
    fun clickDataEditCheck(){
        rule.onNodeWithText("Profile").assertExists().performClick()
        rule.onNodeWithText("Profile").assertExists().performClick()
        rule.onNodeWithTag("Setting Button").assertExists().performClick()
        rule.onNodeWithText("Edit Your Profile").assertExists()
        rule.onNodeWithTag("Setting Button").assertExists().performClick()
        rule.onNodeWithText("Edit Your Profile").assertDoesNotExist()
    }
    @Test
    fun clickAvatarEditCheck(){
        rule.onNodeWithText("Profile").assertExists().performClick()
        rule.onNodeWithTag("Setting avatar Button").assertExists().performClick()
        rule.onNodeWithTag("AvatarSettings").assertExists()
        rule.onNodeWithTag("Setting avatar Button").assertExists().performClick()
        rule.onNodeWithTag("AvatarSettings").assertDoesNotExist()
    }
    @Test
    fun dataEditCheck(){
        rule.onNodeWithText("Profile").assertExists().performClick()
        rule.onNodeWithTag("Setting avatar Button").assertExists().performClick()
        rule.onNodeWithTag("AvatarSettings").assertExists()
        rule.onNodeWithTag("forward hair").assertExists().performClick()
        rule.onNodeWithTag("forward back").assertExists().performClick()
        rule.onNodeWithTag("forward body").assertExists().performClick()
        rule.onNodeWithTag("forward cloths").assertExists().performClick()
        rule.onNodeWithTag("Save data").assertExists().performClick()
        rule.onNodeWithTag("AvatarSettings").assertDoesNotExist()
    }
    @Test
    fun avatarEditCheck(){
        rule.onNodeWithText("Profile").assertExists().performClick()
        rule.onNodeWithTag("Setting Button").assertExists().performClick()
        rule.onNodeWithTag("DataSettings").assertExists()
        rule.onNodeWithText("Edit Your Profile").assertExists()
        rule.onNodeWithText("Nick name").assertExists()
        rule.onNodeWithText("Description").assertExists()
        rule.onNodeWithText("Lang").assertExists()
        rule.onNodeWithText("Age").assertExists()
        rule.onNodeWithText("Submit").assertExists().performClick()
        rule.onNodeWithText("Edit Your Profile").assertDoesNotExist()
    }
    @Test
    fun avatarClickCheck(){
        rule.onNodeWithText("Profile").assertExists().performClick()
        rule.onNodeWithTag("Avatar").assertExists().performClick()
        rule.onNodeWithTag("textSay").assertExists()
    }
}