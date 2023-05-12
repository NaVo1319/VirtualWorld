package com.example.virtualworld.screens

import android.content.Intent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualworld.data.EditProfileData
import com.example.virtualworld.data.Message
import com.example.virtualworld.data.Messages
import com.example.virtualworld.data.User
import com.example.virtualworld.ui.element.Avatar
import com.example.virtualworld.ui.element.ChatMessageField
import com.example.virtualworld.ui.element.ChatMessageFieldEnter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
@Composable
fun ChatScreen(user: User, messages: Messages, speechRecognizer: SpeechRecognizer, intent: Intent,profileData: EditProfileData) {
    Log.d("User Choice", "ChatScreen is Drawing")
    var showEditField by remember{ mutableStateOf(false) }
    var mesLoad by remember{ mutableStateOf(false) }
    // Корутины зе бест
    LaunchedEffect(Unit) {
        try {
            val uid = user.uid!!
            Log.d("User Choice", "user name: John uid: $uid")
            messages.messages = onChangeMessage(uid)
            mesLoad = true
        }catch (ex: Exception){
            Log.e("User Choice", "Coroutine error: $ex")
        }
    }
    val avatar = Avatar()
    avatar.ShowAvatar(user = user,user.description!!)
    if(!showEditField && mesLoad)ChatMessageField().Show( { showEditField = it },messages, user)
    if(showEditField && mesLoad)ChatMessageFieldEnter().Show({ showEditField = it },messages,speechRecognizer,intent, user,profileData)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ChatScreen")
        ,backgroundColor = Color.Black.copy(alpha = 0.7f)
    ){
        Text(text = user.name ?: "No Data", color = Color.White, fontSize = 25.sp, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), textAlign = TextAlign.Center)
    }

}
suspend fun onChangeMessage(uid:String): ArrayList<Message> = suspendCoroutine { continuation ->
    Log.d("User Choice", "onChangeMessage() resume")
    val auth = Firebase.auth.currentUser?.uid
    Firebase.database.getReference("/messages/$auth").addValueEventListener(object  : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val arr = arrayListOf<Message>()
            for (s in snapshot.children){
                val message = s.getValue(Message::class.java)!!
                if(message.toUserName == uid || message.fromUserName == uid){
                    arr.add(message)
                }
            }
            try {
                continuation.resume(arr)
            }catch (ex: Exception){
                Log.e("User Choice", "Coroutine error: $ex")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("User Choice", "Storage Error: $error")
            continuation.resume(arrayListOf())
        }

    })
}
