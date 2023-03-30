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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.virtualworld.R
import com.example.virtualworld.data.Message
import com.example.virtualworld.data.Messages
import com.example.virtualworld.data.User
import kotlinx.coroutines.delay

class ChatMessageField {
    @Composable
    fun Show(showEdit:(Boolean)-> Unit, messages: Messages, user: User){
        Log.d("Message Log","index: ${messages.numMess} ")
        var text by remember{ mutableStateOf("") }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd){
            if(!messages.StartPrint)Image(painter = painterResource(id = R.drawable.exclamation_mark), contentDescription ="", modifier = Modifier
                .padding(30.dp, 60.dp)
                .size(80.dp)
                .clickable { if(messages.numMess<=messages.messages.size-1)++messages.numMess else messages.numMess=0; messages.StartPrint =true; text=""} )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
            Column() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(painter = painterResource(id = R.drawable.edit), contentDescription ="", modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp)
                        .clickable { showEdit(true) })
                }
                if(messages.numMess>0)Image(painter = painterResource(id = R.drawable.custom_arrow_left), contentDescription ="", modifier = Modifier.size(50.dp).clickable { --messages.numMess; text="" } )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .border(BorderStroke(1.dp, Color.White))
                    ,backgroundColor = Color.Black.copy(alpha = 0.7f)
                ) {
                    if(!messages.StartPrint){
                        Text(text = text, color = Color.White, modifier = Modifier.padding(10.dp))
                        LaunchedEffect(Unit) {
                            for (i in "....".indices) {
                                text+="...."[i].toString()
                                delay(40)
                            }
                        }
                    }
                    else {
                        Text(text = text, color = Color.White, modifier = Modifier.padding(10.dp))
                        LaunchedEffect(messages.numMess) {
                            val mess = messages.messages[messages.numMess]
                            for (i in mess.content?.indices!!) {
                                text+= mess.content?.get(i).toString()
                                delay(40 )
                            }
                        }
                    }
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd){
            if(messages.numMess<messages.messages.size-1 && messages.StartPrint)Image(painter = painterResource(id = R.drawable.custom_arrow), contentDescription ="", modifier = Modifier
                .padding(5.dp)
                .size(40.dp).clickable { ++messages.numMess; text=""} )
        }
    }
}