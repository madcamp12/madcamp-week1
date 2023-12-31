package com.example.week1.screens

import android.graphics.Color
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.week1.R
import com.example.week1.ui.theme.Week1Theme
import kotlin.random.Random

private var player_number: Int = 0
private var candidates = mutableListOf<String>()

@Composable
fun Tap3Screen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        var content: Int by remember { mutableStateOf(0) }
        
        Box (modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.6f)
            .background(color = androidx.compose.ui.graphics.Color.LightGray)){
            Box(modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight(0.6f),
                contentAlignment = Alignment.Center) {
                if (content == 0) {
                    checkNumber()
                }else if(content == 1){
                    makeCandidates()
                }else if(content == 2){
                    selected_candidate()
                }
            }

            Button(onClick = ff@{
                if (content == 0){
                    if(player_number == 0) {return@ff}
                    content++
                } else if(content == 1){
                    content++
                } else if (content == 2) {
                    player_number = 0
                    content = 0
                }
            }, modifier = Modifier
                .padding(bottom = 15.dp)
                .align(Alignment.BottomCenter)) {
                if (content == 0) {
                    Text("설정 완료")
                } else if (content == 1) {
                    Text("뽑기!")
                } else if (content == 2) {
                    Text("다시 하기")
                }
            }
        }

    }
}

@Composable
fun checkNumber() {
    var number: Int by remember { mutableStateOf(player_number) }

    Column(modifier = Modifier.background(color = androidx.compose.ui.graphics.Color.LightGray)){

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "인원수 설정")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(text = "⏪",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable {
                        if (number > 0) {
                            number -= 1
                            player_number = number
                        }
                    })

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        color = androidx.compose.ui.graphics.Color.Gray,
                        shape = CircleShape
                    )
                    .width(30.dp)
                    .height(30.dp)
            ) {

                Text(text = "$number", modifier = Modifier.align(Alignment.Center))
            }

            Text(text = "⏩",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .clickable {
                        if (number < 10) {
                            number += 1
                            player_number = number
                        }
                    })
        }
    }
}

@Composable
fun makeCandidates(){
    candidates = MutableList(player_number) { "" }

    LazyColumn{
        items(player_number){idx ->
            input_filed(idx = idx)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun input_filed(idx: Int){
    val idx: Int = idx
    var text by remember { mutableStateOf("")}

    OutlinedTextField(
         value = text,
        onValueChange = {
            text = it
            candidates.set(idx, text)},
        label = {Text("후보 ${idx + 1}")}
    )
}

@Composable
fun selected_candidate(){
    val random = Random(seed = System.currentTimeMillis())
    val selected_idx:Int = random.nextInt(player_number)
    val selected_text:String = candidates.get(selected_idx)

    Text(text = "$selected_text")
}