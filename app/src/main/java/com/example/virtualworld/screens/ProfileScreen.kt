package com.example.virtualworld.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.virtualworld.MainActivity
import com.example.virtualworld.R
import com.example.virtualworld.data.EditProfileData
import com.example.virtualworld.ui.element.Avatar
import com.example.virtualworld.ui.element.EditAvatar
import com.example.virtualworld.ui.element.EditProfile
import com.example.virtualworld.ui.theme.ButtonColor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.internal.cache2.Relay.Companion.edit

@Composable
fun ProfileScreen(profileData: EditProfileData) {
    val auth = Firebase.auth
    val activity = (LocalContext.current as? Activity)
    val editProfile = EditProfile()
    val avatarEdit = EditAvatar()
    var edit by remember { mutableStateOf(false)}
    var editAvatar by remember { mutableStateOf(false)}
    Avatar().ShowAvatar(profileData.user)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart){
        singOutButton(auth = auth, activity = activity!!)
    }
    if(editAvatar)avatarEdit.Edit(user = profileData.user)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd){
        CircleButton(onClick = {editAvatar = editAvatar == false}, icon = if(edit)Icons.Outlined.Close  else Icons.Default.Face, contentDescription = "Setting avatar Button")
    }
    if(edit)editProfile.showEditProfile(profileData,{ edit = it })
    CircleButton(onClick = { edit = edit==false }, icon = if(edit)Icons.Outlined.Close  else Icons.Default.Edit, contentDescription = "Setting Button")
}
@Composable
fun CircleButton(onClick: () -> Unit, icon: ImageVector,contentDescription: String) {
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
@Composable
fun singOutButton(auth: FirebaseAuth, activity: Activity){
    Button(onClick = {
        auth.signOut()
        val Intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(Intent)
        activity.finish()
    },
        modifier = Modifier
            .padding(8.dp)
            .size(50.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor)
    ){
        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "",
            tint = Color.White
        )
    }
}
