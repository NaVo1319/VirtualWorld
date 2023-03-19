package com.example.virtualworld

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.virtualworld.data.DataModel
import com.example.virtualworld.data.EditProfileData
import com.example.virtualworld.data.MenuState
import com.example.virtualworld.data.User
import com.example.virtualworld.screens.ChatScreen
import com.example.virtualworld.screens.ProfileScreen
import com.example.virtualworld.screens.UsersListScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ActionActivity : ComponentActivity() {
    val dataModel=DataModel()
    val menuState = MenuState()
    val profileData = EditProfileData()
    lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database
        auth = Firebase.auth
        checkAuthState()
        onChangeListener(database.getReference("/users"))
        onChangeUser(database.getReference("/users/${auth.uid}"))
        setContent {
            val titles = listOf(
                stringResource(id = R.string.users_list),
                stringResource(id = R.string.profile)
            )
            Column() {
                val navController = rememberNavController()
                TabRow(selectedTabIndex = menuState.state) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = menuState.state == index,
                            onClick = {
                                if(menuState.state!= index){
                                    menuState.state = index
                                    if(menuState.state == 0)navController.navigate("users")
                                    if(menuState.state == 1)navController.navigate("profile")
                                }
                            },
                            modifier = Modifier.background(Color(0xFF5085A2))
                        )
                    }
                }
                NavHost(navController = navController, startDestination = "profile") {
                    composable("users") { UsersListScreen(users = dataModel.users, onNavigateToFriends = { navController.navigate("chat") })}
                    composable("chat") { ChatScreen() }
                    composable("profile") { ProfileScreen(profileData) }
                }
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
                    if(user!=null ) {
                        users.add(user)

                    }
                    dataModel.users = users
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun onChangeUser(dRef: DatabaseReference){
        dRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    profileData.user = user
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun checkAuthState(){
        if(auth.currentUser==null){
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }
    }
}

