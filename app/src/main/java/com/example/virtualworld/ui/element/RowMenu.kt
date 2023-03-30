package com.example.virtualworld.ui.element

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.virtualworld.R

class RowMenu {
    @Composable
    fun Show(navController: NavHostController,startState: Int) {
        val titles = listOf(
            stringResource(id = R.string.users_list),
            stringResource(id = R.string.profile)
        )
        var state by remember { mutableStateOf(startState) }
            TabRow(
                selectedTabIndex = state,
                backgroundColor = Color.Black.copy(alpha = 0.7f),
                contentColor = Color.White
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title, fontSize = 20.sp) },
                        selected = state == index,
                        onClick = {
                            state = index
                            if (state == 1) {
                                navController.navigate("profile")
                            }
                            if (state == 0) {
                                navController.navigate("users")
                            }
                        },
                    )
                }
            }
            Log.d("MenuSelect", "$state")
        }
}