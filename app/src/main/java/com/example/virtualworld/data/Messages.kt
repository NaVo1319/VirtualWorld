package com.example.virtualworld.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class Messages : ViewModel() {
    var messages  by  mutableStateOf<ArrayList<Message>>(arrayListOf())
    var numMess by mutableStateOf(-1)
    var StartPrint by mutableStateOf(false)
}