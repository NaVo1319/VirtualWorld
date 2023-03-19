package com.example.virtualworld.data

data class User(
    var name: String? ="",
    var description: String? ="",
    var age: Int? =0,
    val close: Boolean? =false,
    var lang: String?  ="en",
    var body:String? = "body",
    var hair:String? = "hair1",
    var cloths:String? = "cloths1",
    var eye:String? = "eye1",
    var background:String? = "Back1",
)