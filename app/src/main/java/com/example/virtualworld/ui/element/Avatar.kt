package com.example.virtualworld.ui.element

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.virtualworld.R
import com.example.virtualworld.data.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Avatar {
    @Composable
    fun ShowAvatar(user: User){
        var back by remember{ mutableStateOf("")}
        var body by remember{ mutableStateOf("")}
        var hair by remember{ mutableStateOf("")}
        var cloths by remember{ mutableStateOf("")}
        getUrl("Background/${ user.background }.jfif") { back = it }
        getUrl("Body/${ user.body }.png") { body = it }
        getUrl("Hair/${ user.hair }.png") { hair = it }
        getUrl("Cloths/${ user.cloths }.png" ) { cloths = it }
        Log.d("AvatarmyLog",back)
        Box(modifier = Modifier.fillMaxSize()){
            AsyncImage(
                model = back,
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            AsyncImage(
                model = body,
                contentDescription = "Body",
                contentScale = ContentScale.FillBounds
            )
            AsyncImage(
                model = hair,
                contentDescription = "hair",
                contentScale = ContentScale.FillBounds
            )
            AsyncImage(
                model = cloths,
                contentDescription = "cloth",
                contentScale = ContentScale.FillBounds
            )
        }
    }
    private fun getUrl(filename: String, callback:(String) -> Unit){
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://authchatavatar.appspot.com/avatar/${filename}")
        storageRef.downloadUrl.addOnSuccessListener{
            callback(it.toString())
        }.addOnFailureListener{
        }
    }
}