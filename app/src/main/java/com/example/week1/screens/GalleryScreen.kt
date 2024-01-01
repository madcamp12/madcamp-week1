package com.example.week1.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import okio.IOException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(navController: NavController) {

    val context = LocalContext.current

    fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options()
            options.inSampleSize = 1 // 샘플링 크기, 필요에 따라 조절할 수 있습니다.
            BitmapFactory.decodeStream(inputStream, null, options)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun generateFileName(i:Int): String {
        val fileName = "${i + 1}.jpg" // 이미지 인덱스를 사용하여 파일명 생성
        return fileName
    }

    fun getSavedImagesList(): ArrayList<File> {
        val filesList = ArrayList<File>()
        val directory = context.filesDir
        val files = directory.listFiles()

        files?.let {
            for (file in files) {
                if (file.isFile && file.extension.equals("jpg", ignoreCase = true)) {
                    filesList.add(file)
                }
            }
        }
        return filesList
    }

    fun initializeImageIndex(): Int {
        val filesList = getSavedImagesList()
        if (filesList.isNotEmpty()) {
            val lastIndexFile = filesList.maxByOrNull { it.nameWithoutExtension.toIntOrNull() ?: 0 }
            lastIndexFile?.let {
                val lastIndex = it.nameWithoutExtension.toIntOrNull() ?: 0
                return lastIndex
            }
        }
        return 0
    }

    var imageIndex by remember{
        mutableStateOf(initializeImageIndex())
    }


    fun saveBitmapToInternalStorage(bitmap: Bitmap) {
        val fileOutputStream: FileOutputStream
        val file = File(context.filesDir, generateFileName(imageIndex)) // 저장할 파일명과 확장자를 지정합니다.
        try {
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream) // 비트맵을 파일로 저장합니다.
            fileOutputStream.flush()
            fileOutputStream.close()

            // 파일이 성공적으로 저장되었다면 알림 등을 표시할 수 있습니다.
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            val bitmap: Bitmap? = uri?.let { uriToBitmap(it) }

            bitmap?.let {
                // 내부 저장소에 비트맵 저장
                saveBitmapToInternalStorage(bitmap)
                imageIndex = initializeImageIndex()
            }
        }
    )

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
            var dialogUri by remember { mutableStateOf<String?>(null) }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                content = {
                    items(imageIndex) xx@{
                        if(imageIndex == 0) return@xx

                        val imgpath: String = context.filesDir.path + "/" + "${it+1}.jpg" // 내부 저장소에 저장되어 있는 이미지 경로
                        val bm = BitmapFactory.decodeFile(imgpath)
                        if (openDialog and (dialogUri == "${it+1}.jpg")){
                            Dialog(
                                onDismissRequest = { openDialog = false },
                                DialogProperties(
                                    usePlatformDefaultWidth = false
                                )
                            ) {
                                GlideImage(
                                    imageModel = bm,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable { openDialog = false },
//                                    circularReveal= CircularReveal(duration = 250),
                                )
                            }
                        }
                        GlideImage(
                            imageModel = bm,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f / 1f)
                                .clip(RectangleShape)
                                .clickable {
                                    openDialog = true
                                    dialogUri = "${it+1}.jpg"
                                },
//                            circularReveal= CircularReveal(duration = 250),
                        )
                    }
                }
            )
        }
}