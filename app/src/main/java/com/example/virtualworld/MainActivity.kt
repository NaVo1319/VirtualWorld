package com.example.virtualworld

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.INTERNET
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import com.example.virtualworld.ui.theme.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.virtualworld.data.User
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import java.util.*

class MainActivity : ComponentActivity() {
    lateinit var launcher: ActivityResultLauncher<Intent>
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account!=null){
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            }catch (e: ApiException){
                Log.d("My log","Api exception")}
        }
        setContent {
            var internetStatus by remember{ mutableStateOf(InternetTest().isInternetConnected(this))}
            if(internetStatus) {
                InternetTest().ShowError()
                LaunchedEffect(true){
                    while (internetStatus){
                        delay(10000)
                        internetStatus = InternetTest().isInternetConnected(this@MainActivity)
                    }
                }
            }
            else{
                checkAuthState()
                var errorMes by remember { mutableStateOf(false)}
                Box(modifier = Modifier.fillMaxSize()){
                    Image(
                        painter = painterResource(id = R.drawable.background_login),
                        contentDescription = "Background Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }
                form { errorMes = it }
                errorMessage(errorMes)
            }
        }
    }
    @Composable
    private fun errorMessage(errorMes: Boolean){
        if(errorMes)Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter, ){
            Card(modifier = Modifier
                .padding(10.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = 5.dp){
                Text(text = getString(R.string.errorEmailPassword), fontSize = 15.sp,modifier = Modifier.padding(10.dp))
            }
        }
    }
    @Composable
    private fun form(setError: (Boolean) ->Unit){
        val colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            cursorColor = Color.Black,
            disabledLabelColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
        var email by remember { mutableStateOf("")}
        var password by remember { mutableStateOf("")}
        val authButton by remember { mutableStateOf(true)}
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp), contentAlignment = Alignment.Center){
            Card(modifier = Modifier.border(BorderStroke(4.dp, Color.White)),backgroundColor = Color.Black.copy(alpha = 0.7f)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = getString(R.string.email),modifier = Modifier.padding(10.dp), color = Color.White, fontSize = 24.sp)
                    TextField(value = email, onValueChange = {email = it}, modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .testTag("email"),
                        colors = colors)
                    Text(text = getString(R.string.password),modifier = Modifier.padding(10.dp), color = Color.White, fontSize = 24.sp)
                    TextField(value = password, onValueChange = {password = it}, modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .testTag("password"),
                        colors = colors)
                    Row() {
                        Button(modifier = Modifier
                            .padding(10.dp)
                            .testTag("reg"),
                            enabled = authButton,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            onClick = { setError(authCheck(email,password).not()) }) {
                            Text(text =getString(R.string.register),color = Color.Black)
                        }
                        Button(modifier = Modifier
                            .padding(10.dp)
                            .testTag("login"),
                            enabled = authButton,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            onClick = { setError(signIn(email,password).not()) }) {
                            Text(text =getString(R.string.sign_in),color = Color.Black)
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.google_login_icon),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .padding(10.dp)
                            .clickable { signInWithGoogle() }
                            .testTag("login google")
                    )
                }
            }
        }
    }
    private fun authCheck(email_: String, password_: String): Boolean{
        val email = email_.replace(" ","")
        val password = password_.replace(" ","")
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        Log.d("AuthStatus", "$email $password ${emailPattern.matches(email)} and ${password.length>=8}")
        if(emailPattern.matches(email.replace(" ",""))&& password.length>=8){
            auth.createUserWithEmailAndPassword(email.replace(" ",""), password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        auth.currentUser!!.sendEmailVerification().addOnSuccessListener{
                            Toast.makeText(baseContext, "Please verify your email",Toast.LENGTH_SHORT).show()
                            if(task.isSuccessful){
                                Log.d("AuthStatus", "createUserWithEmail:success")
                                saveUser()
                            }else{
                                Toast.makeText(baseContext, task.exception.toString(),Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(baseContext, it.message, Toast.LENGTH_SHORT).show()
                            // add code here to handle failed email verification
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("AuthStatus", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, getString(R.string.register_failed),Toast.LENGTH_SHORT).show()
                    }
                }
            return true
        }else{
            return false
        }
    }
    private fun signIn(email: String, password: String): Boolean{
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful ) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AuthStatus", "signInWithEmail:success")
                    val user = auth.currentUser
                    if(user!!.isEmailVerified)checkAuthState()
                    else Toast.makeText(baseContext, "Email is not verify",Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("AuthStatus", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, getString(R.string.sing_in_failed),
                        Toast.LENGTH_SHORT).show()
                }
            }
        return true
    }
    private fun getClient(): GoogleSignInClient {
        val sign = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this,sign)
    }
    private fun signInWithGoogle(){
        checkAuthState()
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }
    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener{ it ->
            if(it.isSuccessful){
                Log.d("MyLog","Auth is done")
                FirebaseDatabase.getInstance().getReference("/users").child(auth.uid.toString()).get().addOnSuccessListener {user->
                    if (!user.exists())saveUser()
                }
                val i = Intent(this,ActionActivity::class.java)
                startActivity(i)
            }else{
                Log.d("MyLog","Auth is error")
            }
        }
    }
    private fun checkAuthState(){
        if(auth.currentUser!=null){
            val i = Intent(this,ActionActivity::class.java)
            startActivity(i)
        }
    }
    private fun saveUser(){
        val database = Firebase.database
        val ref = database.getReference("users/${auth.currentUser?.uid}")
        val user = User(
            auth.currentUser?.uid,
            auth.currentUser?.displayName ?: "New user",
            "New User",
            0,
            false,
            Locale.getDefault().getLanguage().toString()
        )
        ref.setValue(user)
    }
}
