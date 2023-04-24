package com.example.virtualworld.ui.element

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualworld.R
import com.example.virtualworld.data.EditProfileData
import com.example.virtualworld.data.User
import com.example.virtualworld.ui.theme.ButtonColor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class EditAvatar {
    @Composable
    fun Edit(user: EditProfileData, exit: (Boolean) -> Unit){
        var back by remember { mutableStateOf(user.user.background)}
        var hair by remember { mutableStateOf(user.user.hair)}
        var body by remember { mutableStateOf(user.user.body)}
        var cloths by remember { mutableStateOf(user.user.cloths)}
        Box(modifier = Modifier.fillMaxSize().testTag("AvatarSettings"), contentAlignment = Alignment.BottomCenter){
            Column() {
                Row() {
                    Column(modifier = Modifier
                        .padding(10.dp)
                        .weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        CircleButton(onClick = { hair =  tranString(user.user.hair ?: "Background",-1); user.user.hair = hair }, icon = Icons.Default.ArrowBack, contentDescription ="back hair" )
                        CircleButton(onClick = { back = tranString(user.user.background ?: "Background",-1); user.user.background = back }, icon = Icons.Default.ArrowBack, contentDescription ="back back" )
                        CircleButton(onClick = { body = tranString(user.user.body ?: "Body",-1); user.user.body = body }, icon = Icons.Default.ArrowBack, contentDescription ="back body" )
                        CircleButton(onClick = { cloths = tranString(user.user.cloths ?: "Body",-1); user.user.cloths = cloths }, icon = Icons.Default.ArrowBack, contentDescription ="back cloths" )
                        CircleButton(onClick = {}, icon = Icons.Default.FavoriteBorder, contentDescription ="filler" )
                    }
                    Column(modifier = Modifier
                        .padding(10.dp)
                        .weight(3f), horizontalAlignment = Alignment.CenterHorizontally) {
                        TextButton(name = hair ?: "no data")
                        TextButton(name = back ?: "no data")
                        TextButton(name = body ?: "no data")
                        TextButton(name = cloths ?: "no data")
                        CircleButton(onClick = {saveUser(user.user);exit(false)}, icon = Icons.Default.KeyboardArrowDown, contentDescription = "Save data")
                    }
                    Column(modifier = Modifier
                        .padding(10.dp)
                        .weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        CircleButton(onClick = { hair =  tranString(user.user.hair ?: "Background",1); user.user.hair = hair }, icon = Icons.Default.ArrowForward, contentDescription ="forward hair" )
                        CircleButton(onClick = {back = tranString(user.user.background ?: "Background",1); user.user.background = back}, icon = Icons.Default.ArrowForward, contentDescription ="forward back" )
                        CircleButton(onClick = { body = tranString(user.user.body ?: "Body",1); user.user.body = body }, icon = Icons.Default.ArrowForward, contentDescription ="forward body" )
                        CircleButton(onClick = { cloths = tranString(user.user.cloths ?: "Body",1); user.user.cloths = cloths }, icon = Icons.Default.ArrowForward, contentDescription ="forward cloths" )
                        CircleButton(onClick = {}, icon = Icons.Default.FavoriteBorder, contentDescription ="filler" )
                    }
                }
            }
        }
    }
    @Composable
    private fun TextButton(name:String){
        Button(onClick = { /*TODO*/ },modifier = Modifier
            .padding(8.dp)
            .size(100.dp, 50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black.copy(alpha = 0.7f))) {
            Text(text = name, color = Color.White)
        }
    }
    @Composable
    private fun CircleButton(onClick: () -> Unit, icon: ImageVector, contentDescription: String) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp).testTag(contentDescription),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black.copy(alpha = 0.7f))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.White
            )
        }
    }
    private fun tranString(str: String, inc: Int): String{
        return try {
            val num = "${str[str.length - 1]}".toInt()
            "${str.dropLast(1)}${num+inc}"
        }catch (e: NumberFormatException){
            "error"
        }
    }
    private fun saveUser(user: User){
        val database = Firebase.database
        val auth = Firebase.auth
        val ref = database.getReference("users/${auth.currentUser?.uid}")
        ref.setValue(user)
    }
}