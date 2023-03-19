package com.example.virtualworld.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

open class MenuState: ViewModel() {
    var state by  mutableStateOf(1)
}