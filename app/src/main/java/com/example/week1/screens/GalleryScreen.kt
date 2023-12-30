package com.example.week1.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.week1.ui.theme.Week1Theme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(navController: NavController) {
    Week1Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { /*TODO*/ },
                        shape = CircleShape
                    ) {
                        Icon(
                            Icons.Default.Add,
                            "",
                        )
                    }
                }
            ) {
                gridView(LocalContext.current)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun gridView(context: Context) {
    var openDialog by remember { mutableStateOf(false) }
    var dialogId by remember { mutableStateOf(0) }

    lateinit var imageList: List<GridModal>
    imageList = ArrayList<GridModal>()

    for (i in 1..22){
        val img = "image$i"
        val imgId = context.resources.getIdentifier(img, "drawable", context.packageName)
        imageList = imageList + GridModal(img,imgId)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        content = {
            items(imageList.size) {
                if(openDialog and (dialogId == imageList[it].languageImg)){
                    Dialog(
                        onDismissRequest = {openDialog = false},
                        DialogProperties(
                            usePlatformDefaultWidth = false
                        )
                    ) {
                        Image(
                            painter = painterResource(id = dialogId),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { openDialog = false }
                        )
                    }
                }
                Image(
                    painter = painterResource(id = imageList[it].languageImg),
                    contentDescription = "image",
                    modifier = Modifier
                        .aspectRatio(1f / 1f)
                        .clip(RectangleShape)
                        .clickable {
                            openDialog = true
                            dialogId = imageList[it].languageImg
                        },
                    contentScale = ContentScale.Crop
                )
            }
        })
}
