package com.example.virtualworld.ui.element

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.virtualworld.data.User
import com.google.firebase.storage.FirebaseStorage

class AvatarForUserList {
    @Composable
    fun ShowAvatar(user: User){
        var back by remember{ mutableStateOf("") }
        var body by remember{ mutableStateOf("") }
        var hair by remember{ mutableStateOf("") }
        var cloths by remember{ mutableStateOf("") }
        getUrl("Background/${ user.background }.jfif") { back = it }
        getUrl("Body/${ user.body }.png") { body = it }
        getUrl("Hair/${ user.hair }.png") { hair = it }
        getUrl("Cloths/${ user.cloths }.png" ) { cloths = it }
        Log.d("AvatarMyLog","body: ${body}\nhair: $hair\ncloth:  $cloths\nBack:  $back")
        Box(){
            AsyncImage(
                model = back,
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(5.dp)
                    .size(84.dp)
                    .clip(CircleShape)
            )
            AsyncImage(
                model = body,
                contentDescription = "Body",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(5.dp)
                    .size(84.dp)
                    .clip(CircleShape)
            )
            AsyncImage(
                model = hair,
                contentDescription = "hair",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(5.dp)
                    .size(84.dp)
                    .clip(CircleShape)
            )
            AsyncImage(
                model = cloths,
                contentDescription = "cloth",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(5.dp)
                    .size(84.dp)
                    .clip(CircleShape)
            )
        }
    }
    private fun getUrl(filename: String, callback:(String) -> Unit){
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://authchatavatar.appspot.com/avatar/${filename}")
        storageRef.downloadUrl.addOnSuccessListener{
            callback(it.toString())
        }.addOnFailureListener{
            callback(it.toString())
        }
    }
}