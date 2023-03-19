package com.example.virtualworld.ui.element

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualworld.R
import com.example.virtualworld.data.User
import com.example.virtualworld.ui.theme.ButtonColor

class EditAvatar {
    @Composable
    fun Edit(user: User){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = 5.dp
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()){
                    Row() {
                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            CircleButton(onClick = { /*TODO*/ }, icon = Icons.Default.ArrowBack, contentDescription ="back hair" )
                        }
                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(onClick = { /*TODO*/ },modifier = Modifier
                                .padding(8.dp)
                                .size(100.dp,50.dp),) {
                                Text(text = "Hair1")
                            }
                        }
                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            CircleButton(onClick = { /*TODO*/ }, icon = Icons.Default.ArrowForward, contentDescription ="forward hair" )
                        }
                    }
                }
            }
        }
    }
    @Composable
    private fun CircleButton(onClick: () -> Unit, icon: ImageVector, contentDescription: String) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.White
            )
        }
    }
}