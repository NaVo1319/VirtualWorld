package com.example.virtualworld.data

data class User(
    var uid: String? ="",
    var name: String? ="",
    var description: String? ="",
    var age: Int? =0,
    val close: Boolean? =false,
    var lang: String?  ="en",
    var body:String? = "Body",
    var hair:String? = "Hair1",
    var cloths:String? = "Cloth1",
    var eye:String? = "Eye1",
    var background:String? = "Back1",
)