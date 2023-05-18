package com.example.virtualworld

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.virtualworld.data.*
import com.example.virtualworld.screens.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.suspendCoroutine

class ActionActivity : ComponentActivity() {
    private val allowedPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        it?.let{
            if(it){
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private lateinit var speechRecognizer: SpeechRecognizer
    var messages = Messages()
    val dataModel=DataModel()
    val profileData = EditProfileData()
    val choiceUser = choiceUserData()
    lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPermissionOver(this)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        database = Firebase.database
        auth = Firebase.auth
        checkAuthState()
        onChangeUser(database.getReference("/users/${auth.uid}"))
        onChangeListener(database.getReference("/users"))
        setContent {
            Column() {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "users") {
                    composable("users") { UsersListScreen(usersList = dataModel.users, navController = navController, choiceUser = choiceUser)}
                    composable("chat") { ChatScreen(choiceUser.user, messages, speechRecognizer, makeSpeechRecognitionIntent(),profileData = profileData) }
                    composable("profile") { ProfileScreen(profileData = profileData, navController = navController) }
                }
            }
            var error by remember { mutableStateOf(false)}
            Log.i("InternetErrorState", error.toString())
            LaunchedEffect(Unit) {
                while (true){
                    error = !InternetTest().isOnline(this@ActionActivity)
                    delay(5000)

                }
            }
            if(error)InternetTest().ShowError()
        }
    }
    private fun getPermissionOver(context: Context){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            if(ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.RECORD_AUDIO
            )==PackageManager.PERMISSION_GRANTED){
            }else{
                allowedPermission.launch(android.Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun onChangeListener(dRef: DatabaseReference){
        dRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users =  ArrayList<User>()
                for (s in snapshot.children){
                    val user = s.getValue(User::class.java)
                    Log.d("myLog","Read user")
                    Log.d("myLog",user.toString())
                    if(user!=null && user.name!=profileData.user.name) {
                        users.add(user)

                    }
                    dataModel.users = users
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("myLog", error.toString())
            }

        })
    }
    private fun onChangeUser(dRef: DatabaseReference){
        dRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null ) {
                    profileData.user = user
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("myLog", error.toString())
            }

        })
    }
    private fun checkAuthState(){
        if(auth.currentUser==null){
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }
    }
    private fun makeSpeechRecognitionIntent(): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().language)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
        return intent
    }
}

