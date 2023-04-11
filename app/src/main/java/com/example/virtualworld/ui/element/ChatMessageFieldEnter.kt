package com.example.virtualworld.ui.element

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualworld.R
import com.example.virtualworld.data.EditProfileData
import com.example.virtualworld.data.Message
import com.example.virtualworld.data.Messages
import com.example.virtualworld.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatMessageFieldEnter {
    @Composable
    fun Show(showEdit:(Boolean)-> Unit,messages: Messages,speechRecognizer: SpeechRecognizer, intent: Intent,user: User,profileData: EditProfileData){
        var performingSpeechSetup by remember{ mutableStateOf(true) }
        var textField by remember{ mutableStateOf("") }
        val maxLength by remember{ mutableStateOf(100) }
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {performingSpeechSetup = false; Log.d("Speech","onReadyForSpeech")}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                if (performingSpeechSetup && error == SpeechRecognizer.ERROR_NO_MATCH) return
                Log.d("Speech","onError")
                textField = "Error: $error"
            }
            override fun onResults(results: Bundle?) {
                Log.d("Speech","onResults")
                results?.let {
                    val result = it.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (result != null && result.isNotEmpty()) {
                        val recognizedText = result[0]
                        textField = recognizedText
                        println(recognizedText)
                    }
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
            Column(horizontalAlignment = Alignment.End) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Image(painter = painterResource(id = R.drawable.custom_arrow), contentDescription ="", modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp)
                        .clickable {
                            if (textField.isNotEmpty()) {
                                showEdit(false)
                                messages.messages.add(Message("",textField, "", "", false, "0.0.20"))
                                saveInFirebase(user.name!!,textField, user.lang!!,profileData.user.lang!!)
                            }
                        })
                    Image(painter = painterResource(id = R.drawable.speaker_icon), contentDescription ="Start Listen", modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp)
                        .clickable {speechRecognizer.startListening(intent)} )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .border(BorderStroke(1.dp, Color.White))
                    ,backgroundColor = Color.Black.copy(alpha = 0.7f)
                ){
                    Column() {
                        Row(modifier = Modifier.fillMaxWidth().weight(1f),horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "${textField.length}/$maxLength", color = Color.White, fontSize = 15.sp)
                            Button(onClick = { textField = "" },colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)) {
                                Icon(Icons.Default.Close,"", tint = Color.White)
                            }
                        }
                        TextField(value = textField, onValueChange = {textField = it}, modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.White,
                                cursorColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ))
                        Button(onClick = { showEdit(false) }, modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)) {
                            Icon(Icons.Default.KeyboardArrowDown,"", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun saveInFirebase(name: String, content: String, toLang: String, fromLang: String):Boolean{
        Log.d("User Choice"," user name: $name ")
        val ref = FirebaseDatabase.getInstance().getReference("users")
        val auth = Firebase.auth.currentUser?.uid
        ref.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    val userId = userSnapshot.key // получаем идентификатор пользователя
                    Log.d("User Choice"," ---- id: $userId")
                    GlobalScope.launch {
                        var ref = FirebaseDatabase.getInstance().getReference("messages/$auth")
                        var uid = ref.push().key
                        val message = Message(
                            uid,content,auth,userId,true,
                            LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a"))
                        )
                        ref.child(uid!!).setValue(message)
                        val url = "https://translation-service-iota.vercel.app/api/index.py"
                        val postData = "{\"text\":\"$content\",\"to\":\"$toLang\",\"from\":\"$fromLang\"}"
                        val transContent = sendPostRequest(url, postData)
                        ref = FirebaseDatabase.getInstance().getReference("messages/$userId")
                        uid = ref.push().key
                        message.uid = uid
                        message.content = transContent
                        message.viewStatus = false
                        ref.child(uid!!).setValue(message)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // обрабатываем ошибку
            }
        })
        return true
    }

    fun sendPostRequest(url: String, postData: String): String {
        val urlObj = URL(url)
        val conn = urlObj.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("charset", "utf-8")
        conn.setRequestProperty("Accept", "application/json")
        conn.connectTimeout = 5000
        conn.readTimeout = 5000

        val postDataBytes = postData.toByteArray(StandardCharsets.UTF_8)
        conn.outputStream.write(postDataBytes)

        return conn.inputStream.bufferedReader().use { it.readText() }
    }
}