package com.example.virtualworld.data

data class Message (
    var content: String? ="",
    var fromUserName: String? ="",
    var toUserName: String? ="",
    var viewStatus: Boolean? =false,
    var sendDate: String? ="",
)