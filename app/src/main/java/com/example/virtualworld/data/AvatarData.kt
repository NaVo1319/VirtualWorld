package com.example.virtualworld.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

open class AvatarData : ViewModel() {
    var background  by  mutableStateOf("")
    var body  by  mutableStateOf("")
    var hair  by  mutableStateOf("")
    var cloth  by  mutableStateOf("")
    var eye  by  mutableStateOf("")
}