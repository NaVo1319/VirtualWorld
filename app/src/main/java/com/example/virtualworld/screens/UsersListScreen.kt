package com.example.virtualworld.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.virtualworld.R
import com.example.virtualworld.data.MenuState
import com.example.virtualworld.data.User
val menuState = MenuState()
@Composable
fun UsersListScreen(users: List<User> ,onNavigateToFriends: () -> Unit) {
    Log.d("swipeItCool",users.toString())
    Column() {
        LazyColumn() {
            users.map {
                item {
                    listItem(name = it.name!!, state = it.description!!,onNavigateToFriends = onNavigateToFriends)
                }
            }
        }
    }
}
@Composable
fun listItem(name: String, state:String,onNavigateToFriends: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                menuState.state = 1
                onNavigateToFriends()
                       },
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp
    ) {
        Box(){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.empty_avatar),
                    contentDescription =  "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(5.dp)
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Column() {
                    Text(text = name)
                    Text(text = state)
                }
            }
        }
    }
}
