package com.example.virtualworld.ui.element

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.virtualworld.R
import com.example.virtualworld.data.Message
import com.example.virtualworld.data.Messages
import com.example.virtualworld.data.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

class ChatMessageField {
    @Composable
    fun Show(showEdit:(Boolean)-> Unit, messages: Messages, user: User){
        var speakerName by remember { mutableStateOf("")}
        val messageList by remember { mutableStateOf(if(messages.messages.size!=0)messages.messages else arrayListOf(Message(viewStatus = true,content = "Empty message")))}
        var mesIt by remember { mutableStateOf(correctIndex(messageList.indexOfFirst { it.viewStatus==false },messageList.size))}
        var StartPrint by remember {mutableStateOf(messageList.indexOfFirst { it.viewStatus==false }!=-1)}
        var text by remember{ mutableStateOf("") }
        Log.d("mess number"," ${messageList.size}  $mesIt")
        var anim by remember { mutableStateOf(false)}
        AvatarForChat().CustomAnime(filename = "Body/Mouth.png", contentDescription = "", state = anim)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd){
            if(StartPrint)Image(painter = painterResource(id = R.drawable.exclamation_mark), contentDescription ="Show new message", modifier = Modifier
                .padding(30.dp, 60.dp)
                .size(80.dp))
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
            Column() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(painter = painterResource(id = R.drawable.edit), contentDescription ="Write Message", modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp)
                        .clickable { showEdit(true) })
                }
                if(mesIt>0)Image(painter = painterResource(id = R.drawable.custom_arrow_left), contentDescription ="Previous  message", modifier = Modifier
                    .size(50.dp)
                    .clickable { --mesIt; text = "" } )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .border(BorderStroke(1.dp, Color.White))
                    ,backgroundColor = Color.Black.copy(alpha = 0.7f)
                ) {
                    Column(){
                        if (Firebase.auth.currentUser?.uid!=messageList[mesIt].toUserName)speakerName="You" else speakerName=user.name!!
                        Text(text = speakerName, color = Color.White, modifier = Modifier.padding(10.dp))
                        Text(text = text, color = Color.White, modifier = Modifier.padding(10.dp))
                    }
                    LaunchedEffect(mesIt) {
                        updateViewedStatus(messageList[mesIt]);
                        val mess = messageList[mesIt]
                        for (i in mess.content?.indices!!) {
                            text+= mess.content?.get(i).toString()
                            delay(40 )
                            if(i%2==0)anim = anim==false
                        }
                    }
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd){
            if(mesIt<messageList.size-1)Image(painter = painterResource(id = R.drawable.custom_arrow), contentDescription ="Next message", modifier = Modifier
                .padding(5.dp)
                .size(40.dp)
                .clickable { ++mesIt; text = "" } )
            else StartPrint = false
        }
    }
    fun correctIndex(i: Int,size: Int): Int{
        if(i==-1)return size-1
        else return i
    }
    fun updateViewedStatus(message: Message){
        Log.d("mess number"," $message")
        if(message.viewStatus == false && message.uid?.length!=0){
            val updates = HashMap<String, Any>()
            val auth = Firebase.auth.currentUser?.uid
            updates["messages/$auth/${message.uid}/viewStatus"] = true
            val messagesRef = FirebaseDatabase.getInstance().getReference("/")
            messagesRef.updateChildren(updates)
        }
    }
}

