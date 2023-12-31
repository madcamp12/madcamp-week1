//package com.example.week1.screens
//
//import android.annotation.SuppressLint
//import android.graphics.Bitmap
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.window.Dialog
//import androidx.compose.ui.window.DialogProperties
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import java.io.ByteArrayOutputStream
//
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun GalleryScreen(navController: NavController) {
//    val selectedImageUris = remember {
//        mutableStateListOf<Uri?>()
//    }
//    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(),
//        onResult = { uri ->
//            if(uri is Uri) {
//                selectedImageUris.add(uri)
//            }
//        }
//    )
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background
//    ) {
//        Scaffold(
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = {
//                        singlePhotoPickerLauncher.launch(
//                            PickVisualMediaRequest(
//                                ActivityResultContracts.PickVisualMedia.ImageOnly
//                            )
//                        )
//                    },
//                    shape = CircleShape
//                ) {
//                    Icon(
//                        Icons.Default.Add,
//                        "",
//                    )
//                }
//            }
//        ) {
//
//            var openDialog by remember { mutableStateOf(false) }
//            var dialogUri by remember { mutableStateOf(null as Uri?) }
//
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(3),
//                content = {
//                    items(selectedImageUris.size) {
//                        if (openDialog and (dialogUri == selectedImageUris[it])) {
//                            Dialog(
//                                onDismissRequest = { openDialog = false },
//                                DialogProperties(
//                                    usePlatformDefaultWidth = false
//                                )
//                            ) {
//                                AsyncImage(
//                                    model = selectedImageUris[it],
//                                    contentDescription = null,
//                                    contentScale = ContentScale.Fit,
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .clickable { openDialog = false }
//                                )
//                            }
//                        }
//                        AsyncImage(
//                            model = selectedImageUris[it],
//                            contentDescription = null,
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .aspectRatio(1f / 1f)
//                                .clip(RectangleShape)
//                                .clickable {
//                                    openDialog = true
//                                    dialogUri = selectedImageUris[it]
//                                }
//                        )
//                    }
//                }
//            )
//        }
//    }
//}