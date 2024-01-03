package com.example.week1.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toIntRect
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import okio.IOException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GalleryScreen(navController: NavController) {

    val context = LocalContext.current

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun formatLastModifiedDate(lastModified: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = Date(lastModified)
        return dateFormat.format(date)
    }
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

    val comparator: Comparator<File> = compareBy{it.lastModified()}
    filesList.sortWith(comparator = comparator)
    val imgList = remember {
        mutableStateListOf<ImgClass>()
    }
    for (i in 0..filesList.size-1){
        imgList.add(ImgClass(filesList[i].name.substringBeforeLast('.').toInt(),filesList[i].name,formatLastModifiedDate(filesList[i].lastModified())))
        Log.d("imgList","${filesList[i].name.substringBeforeLast('.').toInt()}, "+filesList[i].name + ", " + Date(filesList[i].lastModified()).toString())
    }



    fun getFileName(uri: Uri?): String {
        if (uri == null){
            return ""
        }
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        return it.getString(displayNameIndex)
                    }
                }
            }
        }
        return uri.path?.substringAfterLast('/') ?: ""
    }

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

    fun saveBitmapToInternalStorage(bitmap: Bitmap, filename: String) {
        val fileOutputStream: FileOutputStream
        val file = File(context.filesDir, filename) // 저장할 파일명과 확장자를 지정합니다.
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

//    이미지 회전정보 가져오기
    fun getOrientationOfImage(uri: Uri): Int{
        val inputStream = context.contentResolver.openInputStream(uri)
        val exif:ExifInterface? = try {
            ExifInterface(inputStream!!)
        }catch (e: IOException){
            e.printStackTrace()
            return -1
        }
        inputStream.close()

//        회전된 각도 알아내기
        val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

//    이미지 다시 회전시키기
    @Throws(Exception::class)
    fun getRotatedBitmap(bitmap: Bitmap?, degrees: Float): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0F) return bitmap
        val m = Matrix()
        m.setRotate(degrees, bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if(imgList.map { it.fileName }.contains(getFileName(uri))){
                Toast.makeText(context, "이미 추가된 사진입니다", Toast.LENGTH_SHORT).show()
            }else{
                if(uri is Uri){
                    val bitmap: Bitmap? = uri?.let { uriToBitmap(it) }
                    val orientation = uri?.let { getOrientationOfImage(it).toFloat() }
                    val newBitmap = orientation?.let { getRotatedBitmap(bitmap, it) }

                    if (newBitmap != null) {
                        saveBitmapToInternalStorage(newBitmap,getFileName(uri))
                    }
                    imgList.add(ImgClass(getFileName(uri).substringBeforeLast('.').toInt(),getFileName(uri), LocalDateTime.now().format(formatter)))
                }
            }
        }
    )

    var selectedIds: MutableState<Set<Int>> = remember{ mutableStateOf(emptySet()) }

    fun Modifier.photoGridDragHandler(
        lazyGridState: LazyGridState,
        haptics: HapticFeedback,
        selectedIds: MutableState<Set<Int>>,
        autoScrollSpeed: MutableState<Float>,
        autoScrollThreshold: Float
    ) = pointerInput(Unit) {
        fun LazyGridState.gridItemKeyAtPosition(hitPoint: Offset): Int? {
//            Log.d("HitPoint", "X: ${hitPoint.x}, Y: ${hitPoint.y}")
            val item = layoutInfo.visibleItemsInfo.find { itemInfo ->
//                Log.d("ItemInfo", itemInfo.size.toIntRect().toString() + " ${hitPoint.round() - itemInfo.offset}")
                itemInfo.size.toIntRect().contains(hitPoint.round() - itemInfo.offset)
            }
//            Log.d("ItemInfo", item.toString()) // 아이템 정보 로그 출력
            return item?.key as? Int
        }

        var initialKey: Int? = null
        var currentKey: Int? = null
        detectDragGesturesAfterLongPress(
            onDragStart = { offset ->
                lazyGridState.gridItemKeyAtPosition(offset)?.let { key ->
                    Log.d("onDragStart", "gridItemKeyAtPosition ${key}, "+selectedIds.toString())
                    if (!selectedIds.value.contains(key)) {
                        Log.d("onDragStart", "if doesn't contain ${key}, "+selectedIds.toString())
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        initialKey = key
                        currentKey = key
                        selectedIds.value += key
                    }
                }
                Log.d("onDragStart", initialKey.toString()+selectedIds.toString())
            },
//            onDragCancel = {
//                initialKey = null
//                autoScrollSpeed.value = 0f
//                Log.d("onDragCancel", selectedIds.toString())
//            },
            onDragEnd = {
                initialKey = null
                autoScrollSpeed.value = 0f
                Log.d("onDragEnd", selectedIds.toString())
            },
            onDrag = { change, _ ->
                Log.d("onDrag", initialKey.toString()+change.toString())
                if (initialKey != null) {
                    val distFromBottom =
                        lazyGridState.layoutInfo.viewportSize.height - change.position.y
                    val distFromTop = change.position.y
                    autoScrollSpeed.value = when {
                        distFromBottom < autoScrollThreshold -> autoScrollThreshold - distFromBottom
                        distFromTop < autoScrollThreshold -> -(autoScrollThreshold - distFromTop)
                        else -> 0f
                    }

                    lazyGridState.gridItemKeyAtPosition(change.position)?.let { key ->
                        if (currentKey != key) {
                            selectedIds.value = selectedIds.value
                                .minus(initialKey!!..currentKey!!)
                                .minus(currentKey!!..initialKey!!)
                                .plus(initialKey!!..key)
                                .plus(key..initialKey!!)
                            currentKey = key
                        }
                    }
                    Log.d("onDrag", selectedIds.toString())
                }
            }
        )
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun decodeSampledBitmapFromFile(
        filePath: String,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            Log.d("insampleSize",inSampleSize.toString())
            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeFile(filePath, this)
        }
    }

    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels

    @Composable
    fun ImageItem(
        photo: ImgClass,
        reqWidth: Int,
        reqHeigiht: Int,
        inSelectionMode: Boolean,
        modifier: Modifier = Modifier
    ) {
        Surface(
            modifier = modifier.aspectRatio(1f),
            tonalElevation = 3.dp
        ) {
            Box {
                val selected by remember { derivedStateOf { selectedIds.value.contains(photo.id) } }
                val transition = updateTransition(selected, label = "selected")
                val padding by transition.animateDp(label = "padding") { asd ->
                    if (inSelectionMode && selected) 10.dp else 0.dp
                }
                val roundedCornerShape by transition.animateDp(label = "corner") { sasd ->
                    if (inSelectionMode && selected) 16.dp else 0.dp
                }
                AsyncImage(
                    model = decodeSampledBitmapFromFile(context.filesDir.path + "/" + photo.fileName,reqWidth,reqHeigiht),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .padding(padding)
                        .aspectRatio(1f / 1f)
                        .clip(RoundedCornerShape(roundedCornerShape))
                )
//                Image(
//                    painter = rememberAsyncImagePainter(decodeSampledBitmapFromFile(context.filesDir.path + "/" + photo.fileName,reqWidth,reqHeigiht)),
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .matchParentSize()
//                        .padding(padding)
//                        .aspectRatio(1f / 1f)
//                        .clip(RoundedCornerShape(roundedCornerShape))
//                )
                if (inSelectionMode) {
                    if (selected) {
                        val bgColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                        Icon(
                            Icons.Filled.CheckCircle,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .border(2.dp, bgColor, CircleShape)
                                .clip(CircleShape)
                                .background(bgColor)
                        )
                    } else {
                        Icon(
                            Icons.Filled.RadioButtonUnchecked,
                            tint = Color.White.copy(alpha = 0.7f),
                            contentDescription = null,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }
            }
        }
    }

    fun deleteImages() {
        for(i in selectedIds.value){
            val image = imgList.find { it.id == i }
            val fileToDelete = File(context.filesDir, image!!.fileName)
            imgList.remove(image)
            if(fileToDelete.delete()){
                Log.d("Delete","${i}, ${image.fileName}")
            }
        }

        selectedIds = mutableStateOf(emptySet())
    }

    var deleteAlert by remember { mutableStateOf(false) }
    var inSelectionMode by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d("inSelectionMode",inSelectionMode.toString())
                    if (selectedIds.value.isNotEmpty()){
                        deleteAlert = true
                    }else{
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Log.d("inSelectionMode",inSelectionMode.toString())
                if (selectedIds.value.isNotEmpty()){
                    Icon(Icons.Default.Delete, "")
                }else{
                    Icon(
                        Icons.Default.Add,
                        "",
                    )
                }
            }
        }
    ) {
        var openDialog by remember { mutableStateOf(false) }
        var dialogId = remember { mutableStateOf<Int?>(null) }
//        var inSelectionMode by remember { mutableStateOf(false) }
//        val inSelectionMode by remember { derivedStateOf { selectedIds.value.isNotEmpty() } }
        val state = rememberLazyGridState()
        val autoScrollSpeed = remember { mutableStateOf(0f) }

        if (deleteAlert) {
            AlertDialog(
                icon = {
                    Icon(Icons.Default.Delete, "")
                },
                title = {
                    Text(text = "삭제")
                },
                text = {
                    Text(text = "${selectedIds.value.size}장의 사진을 삭제하겠습니까?")
                },
                onDismissRequest = {
                    deleteAlert = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deleteImages()
                            deleteAlert = false
                            inSelectionMode = false
                        }
                    ) {
                        Text("삭제")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            deleteAlert = false
                        }
                    ) {
                        Text("취소")
                    }
                }
            )
        }
        LaunchedEffect(autoScrollSpeed.value) {
            if (autoScrollSpeed.value != 0f) {
                while (isActive) {
                    state.scrollBy(autoScrollSpeed.value)
                    delay(10)
                }
            }
        }
        BackHandler (enabled = (selectedIds.value.isNotEmpty())){
            selectedIds = mutableStateOf(emptySet())
            inSelectionMode = false
            Log.d("inSelectionMode",inSelectionMode.toString())
            Log.d("BackHandler", "enabled, "+selectedIds.toString()+inSelectionMode)
        }
        LazyVerticalGrid(
            state = state,
            columns = GridCells.Fixed(3),
            modifier = Modifier.photoGridDragHandler(
                lazyGridState = state,
                haptics = LocalHapticFeedback.current,
                selectedIds = selectedIds,
                autoScrollSpeed = autoScrollSpeed,
                autoScrollThreshold = with(LocalDensity.current) { 40.dp.toPx() })
        ) {
            itemsIndexed(
                imgList,
                key = {index, image->
                    image.id
                }
            ) xx@{ index, image->
                if (imgList.size == 0) return@xx

                val imgpath: String = context.filesDir.path + "/" + image.fileName // 내부 저장소에 저장되어 있는 이미지 경로
                val bm = BitmapFactory.decodeFile(imgpath)

                if(!(selectedIds.value.isNotEmpty()) && openDialog && (dialogId.value == image.id)){
                    Dialog(
                        onDismissRequest = { openDialog = false },
//                        DialogProperties(
//                            usePlatformDefaultWidth = false
//                        )
                    ) {
                        val pagerState = rememberPagerState(
                            initialPage = index,
                            pageCount = { imgList.size }
                        )
                        HorizontalPager(state = pagerState) { page ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Column(
                                    modifier = Modifier.wrapContentSize()
                                ) {
                                    AsyncImage(
                                        model = BitmapFactory.decodeFile(context.filesDir.path + "/" + imgList.find { it.id == imgList[page].id }!!.fileName),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { openDialog = false }
                                    )
                                    Row {
                                        Text(
                                            text = "${imgList.find { it.id == imgList[page].id }!!.datetime}",
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(5.dp),
                                            textAlign = TextAlign.Left
                                        )
                                        Text(
                                            text = "${page+1} / ${imgList.size}",
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(5.dp),
                                            textAlign = TextAlign.Right
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                ImageItem(
                    image,screenWidth/3,screenWidth/3, inSelectionMode,
                    Modifier.combinedClickable (
                        onClick = {
                            if(selectedIds.value.isNotEmpty()){
                                if (selectedIds.value.contains(image.id)) {
                                    selectedIds.value -= image.id
                                } else {
                                    selectedIds.value += image.id
                                }
                            }else{
                                openDialog = true
                                dialogId.value = image.id
                                Log.d(
                                    "ImageItem",
                                    "Clicked, ${image.fileName}, ${inSelectionMode}, ${selectedIds}"
                                )
                            }
                        },
                        onLongClick = {
                            if(!selectedIds.value.isNotEmpty()){
                                selectedIds.value += image.id
                                inSelectionMode = true
                                Log.d(
                                    "ImageItem",
                                    "LongClicked, ${image.fileName}, ${inSelectionMode}, ${selectedIds}"
                                )
                            }
                        }
                    )
                )
            }
        }
    }
}