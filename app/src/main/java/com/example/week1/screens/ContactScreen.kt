@file:OptIn(ExperimentalFoundationApi::class)

package com.example.week1.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.week1.R
import com.example.week1.contacts
import com.example.week1.Contact

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactScreen(navController: NavController) {
    val contact_list: List<Contact> = contacts.toList()
    var search by remember { mutableStateOf("")}

    Column() {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically){

            Text(text = "")

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp, end = 10.dp, bottom = 10.dp),
                value = search,
                placeholder = {Text("연락처 검색")},
                onValueChange = {
                    search = it
                },
            )

            Image(painter = painterResource(id = R.drawable.x), contentDescription = "X mark",
                modifier = Modifier
                    .width(50.dp)
                    .padding(end = 10.dp)
                    .clickable { search = "" })
        }

        LazyColumn {
            val selected_list:List<Contact> = contact_list.filter { contact -> contact.name.contains(search, ignoreCase = true) || contact.digit.contains(search, ignoreCase = true)}
            var sticky_header:Char = '\n'

            item{
                Row(horizontalArrangement = Arrangement.End
                , modifier = Modifier.padding(bottom = 10.dp, end = 15.dp)
                        .fillMaxWidth()) {
                    Text(text = "${selected_list.size}개의 연락처", fontSize = 12.sp)
                }
            }

            selected_list.forEach{contact ->
                if(contact.name.first() != sticky_header){
                    sticky_header = contact.name.first()

                    stickyHeader{
                        Text(text = contact.name.first().toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.secondary)
                                .padding(start = 5.dp))
                    }
                }

                item { contact_card(contact = contact) }
            }
        }
    }
}

@Composable
fun contact_card(contact: Contact){
    var isExpanded by remember { mutableStateOf(false) }
    var infoClicked by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column{
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }) {
            Spacer(modifier = Modifier.width(5.dp))

            Image(
                painter = painterResource(id = contact.img),
                contentDescription = "test",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Column {
                Text(text = contact.name)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = contact.digit)
            }
        }
        if(isExpanded){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp, top = 4.dp)
                , horizontalArrangement = Arrangement.SpaceBetween ){

                Button(onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.digit}"))
                    context.startActivity(intent)
                }) {
                    Image(painter = painterResource(id = R.drawable.call), contentDescription = "call"
                        , modifier = Modifier
                            .width(30.dp)
                            .height(30.dp))
                }

                Button(onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply { data = Uri.parse("smsto:${contact.digit}") }
                    context.startActivity(intent)
                }) {
                    Image(painter = painterResource(id = R.drawable.message), contentDescription = "message"
                        , modifier = Modifier
                            .width(30.dp)
                            .height(30.dp))
                }

                Button(onClick = {
                    infoClicked = true
                }){
                    Image(painter = painterResource(id = R.drawable.info), contentDescription = "info"
                        , modifier = Modifier
                            .width(30.dp)
                            .height(30.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = Color.Gray, modifier = Modifier
            .fillMaxWidth()
            .height(1.dp))
    }

    if(infoClicked) {
        Dialog(onDismissRequest = { infoClicked = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {

                Column {
                    Spacer(modifier = Modifier.height(10.dp))

                    Row (horizontalArrangement = Arrangement.Center
                        , modifier = Modifier.fillMaxWidth()){
                        Image(
                            painter = painterResource(id = contact.img), contentDescription = "image",
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(100.dp)
                                .height(100.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = contact.name,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = contact.digit,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}