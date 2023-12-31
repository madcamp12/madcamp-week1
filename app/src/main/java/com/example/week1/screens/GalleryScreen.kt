package com.example.week1.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(navController: NavController) {

    val context = LocalContext.current
    lateinit var imgList: List<imgClass>
    imgList = remember {
        mutableStateListOf<imgClass>()
    }

    val dbHandler = SqliteHelper(context,"imgTable3",6)
    imgList = dbHandler.selectImg()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if(uri is Uri) {
                if (!dbHandler.insertImg(uri.toString())){
                    Toast.makeText(context, "이미 추가된 사진입니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.Add,
                        "",
                    )
                }
            }
        ) {

            var openDialog by remember { mutableStateOf(false) }
            var dialogUri by remember { mutableStateOf(null as Uri?) }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                content = {
                    itemsIndexed(imgList) {index, item->
                        if (openDialog and (dialogUri == Uri.parse(item.image))) {
                            Dialog(
                                onDismissRequest = { openDialog = false },
                                DialogProperties(
                                    usePlatformDefaultWidth = false
                                )
                            ) {
//                                Card(
//                                    modifier = Modifier.fillMaxWidth()
//                                        .wrapContentHeight()
//                                ) {
                                    AsyncImage(
                                        model = Uri.parse(item.image),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable { openDialog = false }
                                    )
                                    Text(
                                        text = item.image,
                                        modifier = Modifier.padding(8.dp)
                                    )
//                                    Row(
//                                        modifier = Modifier.fillMaxWidth(),
//                                        horizontalArrangement = Arrangement.End
//                                    ){
//                                        Column {
//                                            Text(
//                                                text = item.image,
//                                                modifier = Modifier.padding(8.dp)
//                                            )
//                                            Text(
//                                                text = item.datetime,
//                                                modifier = Modifier.padding(8.dp)
//                                            )
//                                        }
//                                    }
//                                }
                            }
                        }
                        AsyncImage(
                            model = Uri.parse(item.image),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f / 1f)
                                .clip(RectangleShape)
                                .clickable {
                                    openDialog = true
                                    dialogUri = Uri.parse(item.image)
                                }
                        )
                    }
                }
            )
        }
    }
}