package com.example.virtualworld.ui.element

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class AvatarForChat: Avatar() {
    @Composable
    override fun TextSay(add: (String) -> Unit, move: (Boolean) -> Unit) {
    }
    @Composable
    override fun MessageField(textLayerF1:()->Unit,textSayF1:(String)->Unit,textSayF2:(Boolean)->Unit,text:String,opentext:Boolean){
    }
    @Composable
    override fun TextLayer(f:()->Unit,text:String){
    }
}