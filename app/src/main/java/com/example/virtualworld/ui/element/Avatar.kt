package com.example.virtualworld.ui.element

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virtualworld.R
import com.example.virtualworld.data.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
/*
Класс отвечает за отображение аватара в профиле пользователя и чатах, для это необходимо передать
объект User с данными пользователя которого необходимо отобразить
 */
open class Avatar {
    //Главная функция отображения
    @Composable
    fun ShowAvatar(user: User){
        //Отвечает за отображение подсказки о назначении профиля
        var open by remember{ mutableStateOf(false)}
        var opentext by remember{ mutableStateOf(false)}
        //Ссылки на объекты в Firebase Storage
        var back by remember{ mutableStateOf("")}
        var body by remember{ mutableStateOf("")}
        var hair by remember{ mutableStateOf("")}
        var cloths by remember{ mutableStateOf("")}
        //Анимация движения рта
        var mouthOpen by remember{ mutableStateOf("")}
        // Текст подсказки
        var text by remember{ mutableStateOf("")}
        //Заполняем состояния ссылками на Firebase Storage
        getUrl("Background/${ user.background }.jfif") { back = it }
        getUrl("Body/${ user.body }.png") { body = it }
        getUrl("Hair/${ user.hair }.png") { hair = it }
        getUrl("Cloths/${ user.cloths }.png" ) { cloths = it }
        getUrl("Body/Mouth.png" ) { mouthOpen = it }
        //Отчет о получении ссылок
        Log.d("AvatarMyLog","body: ${body}\nhair: $hair\ncloth:  $cloths\nBack:  $back")
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable { if (!opentext) opentext = true }){
            ImageLayer(back,"Background")
            ImageLayer(body,"Body")
            ImageLayer(hair,"Hair")
            ImageLayer(cloths,"Cloths")
            if(open)AnimLayer(mouthOpen, "Mouth Open" )
        }
        MessageField({ opentext = false; text = "" },{ text += it },{open=open==it},text,opentext)
        Log.d("OpenLog","$open")
    }
    //Получить ссылку
    private fun getUrl(filename: String, callback:(String) -> Unit){
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://authchatavatar.appspot.com/avatar/${filename}")
        storageRef.downloadUrl.addOnSuccessListener{
            callback(it.toString())
        }.addOnFailureListener{
            callback("Error: $it")
        }
    }
    //Слой с элементом картинки
    @Composable
    open fun ImageLayer(model: String, contentDescription: String){
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
        )
    }
    //Слой с картинкой для анимации
    @Composable
    open fun AnimLayer(model: String, contentDescription: String){
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
        )
    }
    @Composable
    open fun CustomAnime(filename: String, contentDescription: String, state: Boolean){
        var model by remember { mutableStateOf("")}
        getUrl(filename) { model = it }
        if(state)Box(modifier = Modifier
            .fillMaxSize()){
           AsyncImage(
                model = model,
                contentDescription = contentDescription,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
    //Поле для сообщений
    @Composable
    open fun MessageField(textLayerF1:()->Unit,textSayF1:(String)->Unit,textSayF2:(Boolean)->Unit,text:String,opentext:Boolean){
        if(opentext) {
            TextLayer (textLayerF1,text)
            TextSay(textSayF1,textSayF2)
        }
    }
    //Текстовое поле для сообщений
    @Composable
    open fun TextLayer(f:()->Unit,text:String){
        Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
            Card(modifier = Modifier
                .offset(0.dp, 40.dp)
                .border(BorderStroke(1.dp, Color.White))
                .clickable { f() },backgroundColor = Color.Black.copy(alpha = 0.7f)) {
                Text(text = text, color = Color.White, fontSize = 15.sp, modifier = Modifier.padding(10.dp))
            }
        }
    }
    //Поток анимации и вывода текста
    @Composable
    open fun TextSay(add:(String)->Unit,move:(Boolean)->Unit){
        //Анимация
        Thread{
            var  i=0
            while (i<20){
                move(false)
                Thread.sleep(300)
                i++
            }
        }.start()
        //Вывод текста
        LaunchedEffect(Unit) {
            val textStatic = "Это твой профиль, здесь ты можешь изменить свои данные и свой внешний вид!"
            for (i in textStatic.indices) {
                add(textStatic[i].toString())
                delay(40)
            }
        }
    }
}