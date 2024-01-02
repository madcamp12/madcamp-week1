package com.example.week1.screens

import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.week1.R
import com.example.week1.ui.theme.Week1Theme
import kotlin.random.Random

private var player_number: Int = 1
private var candidates = mutableListOf<String>()

private val random = Random(seed = System.currentTimeMillis())

@Composable
fun Tap3Screen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        var content: Int by remember { mutableStateOf(1) }
        
        Card (modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.6f),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
        ){
            Box(modifier = Modifier.fillMaxSize()){
                Box(modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight(0.65f)
                    .padding(start = 10.dp, end = 10.dp),
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
                        player_number = 1
                        content = 1
                    }
                }, modifier = Modifier
                    .padding(bottom = 15.dp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun makeCandidates(){
    candidates = MutableList(player_number) { "" }
    var index by remember{ mutableStateOf(player_number) }
    val context = LocalContext.current

    LazyColumn{


        items(index){idx ->
            var text by remember { mutableStateOf("") }

            // make default candidates name
            if(candidates[idx] == ""){
                candidates.set(idx, "후보 ${idx + 1}")
            }else if (text != ""){
                candidates.set(idx, text)
            }

            Column {
                Row (
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()){
                    Image(painter = painterResource(id = R.drawable.delete), contentDescription = "del",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                if (index == 1) {
                                    Toast
                                        .makeText(context, "최소 인원은 1명입니다", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    candidates.removeAt(idx)
                                    player_number = --index
                                }
                            })
                    }

                OutlinedTextField(
                    modifier = Modifier.padding(bottom = 10.dp),
                    value = text,
                    onValueChange = {
                        text = it
                        if(text == ""){
                            candidates.set(idx, "후보 ${idx + 1}")
                        }else{
                          candidates.set(idx, text)
                        } },
                    label = {Text("후보 ${idx + 1}")}
                )
            }
        }

        item {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center
                ){
                Button(onClick = yy@{
                    if(index == 10) {
                        Toast.makeText(context, "최대 인원은 10명입니다", Toast.LENGTH_SHORT).show()
                        return@yy
                    }

                    player_number = ++index
                    candidates.add("후보 $index")
                    },
                    modifier = Modifier.clip(CircleShape)) {
                    Text(text = "+")
                }
            }
        }
    }
}

@Composable
fun selected_candidate(){
    val selected_idx:Int = random.nextInt(player_number)
    val selected_text:String = candidates.get(selected_idx)

    Box(modifier = Modifier.fillMaxSize()){
        Text(text = "선택 결과", modifier = Modifier.align(Alignment.TopCenter))

        Text(text = "$selected_text", modifier = Modifier.align(Alignment.Center))
    }
}