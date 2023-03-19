package com.example.virtualworld.ui.element

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualworld.R
import com.example.virtualworld.data.EditProfileData
import com.example.virtualworld.data.User
import com.example.virtualworld.ui.theme.ButtonColor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EditProfile() {
    lateinit var profileData: EditProfileData
    lateinit var database: FirebaseDatabase
    lateinit var authuser:User
    @Composable
    fun showEditProfile(data: EditProfileData, function: (Boolean) -> Unit){
        profileData = data
        Log.d("ProfileMyLog","user exist: ${profileData.user}")
        database = Firebase.database
        authuser = profileData.user
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Background()
            var name by remember { mutableStateOf(authuser.name)}
            var lang by remember { mutableStateOf(authuser.lang)}
            var age by remember { mutableStateOf(authuser.age.toString())}
            var description by remember { mutableStateOf(   authuser.description)}
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = 5.dp
            ) {
                Box(){
                    Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Edit Your Profile", fontSize = 35.sp)
                        textField(nameField = "Nick name",{name = it},name.toString())
                        textField(nameField = "Description",{description = it},description.toString())
                        DropDawnMenu(items = stringArrayResource(R.array.lang_long), title = "Lang", 0)
                        numberField(nameField = "Age",{age = it },age)
                        SubmitButton(name.toString(),description.toString(), age = age,function)
                    }
                }
            }
        }
    }
    @Composable
    fun Background(){
        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            drawRect(SolidColor(Color.Black.copy(alpha = 0.8f)))
        })
    }
    @Composable
    fun numberField(nameField: String, f:(String) -> Unit,startValue: String){
        Column {
            val lightBlue = Color(0xffd8e6ff)
            val blue = Color(0xff76a9ff)
            Text(
                text = nameField,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Start,
                color = blue
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = startValue,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = lightBlue,
                    cursorColor = Color.Black,
                    disabledLabelColor = lightBlue,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = f,
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
        }
    }
    @Composable
    private fun textField(nameField: String, f:(String) -> Unit,startValue: String){
        Column {
            val lightBlue = Color(0xffd8e6ff)
            val blue = Color(0xff76a9ff)
            Text(
                text = nameField,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Start,
                color = blue
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = startValue,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = lightBlue,
                    cursorColor = Color.Black,
                    disabledLabelColor = lightBlue,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = f,
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
            )
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun DropDawnMenu(items: Array<String>, title: String, selected: Int) {
        val lightBlue = Color(0xffd8e6ff)
        val blue = Color(0xff76a9ff)
        val listItems = items
        // state of the menu
        var expanded by remember {
            mutableStateOf(false)
        }

        // remember the selected item
        var selectedItem by remember {
            mutableStateOf(listItems[selected])
        }
        // box
        Column() {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Start,
                color = blue
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                // text field
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = selectedItem,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = lightBlue,
                        cursorColor = Color.Black,
                        disabledLabelColor = lightBlue,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                // menu
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // this is a column scope
                    // all the items are added vertically
                    listItems.forEach { selectedOption ->
                        // menu item
                        DropdownMenuItem(onClick = {
                            selectedItem = selectedOption
                            profileData.user.lang = selectedOption
                            expanded = false
                        }) {
                            Text(text = selectedOption)
                        }
                    }
                }
            }
        }
    }
    @Composable
    private fun SubmitButton(name:String,description: String, age: String,function: (Boolean) -> Unit){
        Button(onClick = {
            val database  = Firebase.database.getReference("users/${Firebase.auth.currentUser?.uid}")
            database.child("name").setValue(name)
            database.child("description").setValue(description)
            database.child("age").setValue(age.toInt())
            function(false)
        },
            colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor)) {
            Text(text = stringResource(id = R.string.submit), color = Color.White)
        }
    }
}