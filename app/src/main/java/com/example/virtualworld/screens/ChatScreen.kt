package com.example.virtualworld.screens

import android.content.Context
import android.content.Intent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualworld.R
import com.example.virtualworld.data.Message
import com.example.virtualworld.data.Messages
import com.example.virtualworld.data.User
import com.example.virtualworld.ui.element.Avatar
import com.example.virtualworld.ui.element.AvatarForChat
import com.example.virtualworld.ui.element.ChatMessageField
import com.example.virtualworld.ui.element.ChatMessageFieldEnter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun ChatScreen(user: User, messages: Messages, speechRecognizer: SpeechRecognizer, intent: Intent) {
    onChangeMessage(messages)
    var showEditField by remember{ mutableStateOf(false) }
    val avatar = AvatarForChat()
    avatar.ShowAvatar(user = user)
    if(!showEditField)ChatMessageField().Show( { showEditField = it },messages, user)
    if(showEditField)ChatMessageFieldEnter().Show({ showEditField = it },messages,speechRecognizer,intent, user)
    Card(
        modifier = Modifier
            .fillMaxWidth()
        ,backgroundColor = Color.Black.copy(alpha = 0.7f)
    ){
        Text(text = user.name ?: "No Data", color = Color.White, fontSize = 25.sp, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), textAlign = TextAlign.Center)
    }

}
private fun onChangeMessage(messages: Messages){
    val auth = Firebase.auth.currentUser?.uid
    Firebase.database.getReference("/messages/$auth").addValueEventListener(object  : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val arr = arrayListOf<Message>()
            for (s in snapshot.children){
                val message = s.getValue(Message::class.java)!!
                arr.add(message)
            }
            messages.messages = arr

        }

        override fun onCancelled(error: DatabaseError) {

        }

    })
}
