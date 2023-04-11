package com.example.virtualworld.screens

import android.app.Activity
import android.app.ProgressDialog.show
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.virtualworld.R
import com.example.virtualworld.data.MenuState
import com.example.virtualworld.data.User
import com.example.virtualworld.data.choiceUserData
import com.example.virtualworld.ui.element.Avatar
import com.example.virtualworld.ui.element.AvatarForUserList
import com.example.virtualworld.ui.element.RowMenu

@Composable
fun UsersListScreen(users: List<User> ,navController: NavHostController, choiceUser: choiceUserData) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_login),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
    Column() {
        RowMenu().Show(navController,0)
        LazyColumn() {
            users.map {
                item {
                    listItem(user = it,navController =  navController,choiceUser = choiceUser)
                }
            }
        }
    }
}
@Composable
fun listItem(user: User, navController:  NavHostController, choiceUser: choiceUserData){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { choiceUser.user=user;navController.navigate("chat") },
        shape = RoundedCornerShape(60.dp),
        elevation = 25.dp
    ) {
        Box(){
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarForUserList().ShowAvatar(user = user)
                Column(modifier = Modifier.padding(start = 20.dp)) {
                    user.name?.let { Text(text = it, fontSize = 25.sp) }
                }
            }
        }
    }
}
