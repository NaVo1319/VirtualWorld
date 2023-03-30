package com.example.virtualworld.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class choiceUserData: ViewModel() {
    var user by  mutableStateOf(User())
}