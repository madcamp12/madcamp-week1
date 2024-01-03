package com.example.week1.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.week1.R
import com.example.week1.typography
import com.example.week1.ui.theme.Week1Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

private var player_number: Int = 1
private var candidates = MutableList(player_number) { "" }

private val random = Random(seed = System.currentTimeMillis())

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Tap3Screen(navController: NavController) {
    player_number = 1
    candidates = MutableList(player_number) { "" }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        var content: Int by remember { mutableStateOf(1) }
        
        Card (modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.6f)
//            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
//            border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
        ){
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)){
                Box(modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxHeight(0.8f),
//                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                    contentAlignment = Alignment.Center) {
                    if(content == 1){
                        makeCandidates()
                    }else if(content == 2){
                        selected_candidate()
                    }
                }

                Button(onClick = ff@{
                    if(content == 1){
                        content++
                    } else if (content == 2) {
                        content = 1
                    }
                }, modifier = Modifier
                    .align(Alignment.BottomCenter)) {
                    if (content == 1) {
                        Text("뽑기!")
                    } else if (content == 2) {
                        Text("다시 하기")
                    }
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun makeCandidates(){
    var index by remember{ mutableStateOf(player_number) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LazyColumn(state = listState){
        items(index){idx ->
            var text by remember { mutableStateOf("") }

            // make default candidates name
            if(candidates[idx] == ""){
                candidates.set(idx, "후보 ${idx + 1}")
            } else if (candidates[idx] != "후보 ${idx + 1}"){
                if(candidates[idx] == "후보 ${idx + 2}"){
                    candidates.set(idx, "후보 ${idx + 1}")
                    text = ""
                }else{
                    text = candidates[idx]
                }
            }

            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()){

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .height(60.dp)
                            .fillMaxWidth(),
                        value = text,
                        onValueChange = {
                            text = it
                            if(text == ""){
                                candidates.set(idx, "후보 ${idx + 1}")
                            }else{
                                candidates.set(idx, text)
                            }},
                        label = {Text("후보 ${idx + 1}")}
                    )
                    Icon(
                        Icons.Filled.Close,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(40.dp)
                            .padding(end = 10.dp)
                            .clickable {
                                if (index == 1) {
                                    Toast
                                        .makeText(context, "최소 인원은 1명입니다", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    candidates.removeAt(idx)
                                    player_number = --index
                                }
                            }
                    )
                }
            }
        }

        item {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center
                ){
                OutlinedButton(
                    onClick = yy@{
                        if(index == 10) {
                            Toast.makeText(context, "최대 인원은 10명입니다", Toast.LENGTH_SHORT).show()
                            return@yy
                        }


                        player_number = ++index
                        candidates.add("")
                        coroutineScope.launch {
                            listState.scrollToItem(player_number, 1)
                        }

                    },
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "add",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(30.dp))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun selected_candidate(){
    val selected_idx:Int = random.nextInt(player_number)
    val selected_text:String = candidates.get(selected_idx)
    var isVisible:Boolean by remember{ mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()){
        Text(text = "선택 결과", modifier = Modifier.align(Alignment.TopCenter), style = typography.bodyLarge)

        LaunchedEffect(Unit) {
            delay(500) // 2초 지연
            isVisible = true
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
            modifier = Modifier.align(Alignment.Center)
            ) {
            Text(text = "$selected_text", style = typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}