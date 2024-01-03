@file:OptIn(ExperimentalFoundationApi::class)

package com.example.week1.screens

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint.Align
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.week1.R
import com.example.week1.contacts
import com.example.week1.Contact
import com.example.week1.typography

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactScreen(navController: NavController) {
    val contact_list: List<Contact> = contacts.toList()
    var search by remember { mutableStateOf("")}

    Column() {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){

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

            Icon(Icons.Filled.Close, contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 10.dp)
                    .clickable { search = "" })
        }

        LazyColumn {
            val selected_list:List<Contact> = contact_list.filter { contact -> contact.name.contains(search, ignoreCase = true) || contact.digit.contains(search, ignoreCase = true)}
            var sticky_header:Char = '\n'

            item{
                Row(horizontalArrangement = Arrangement.End
                    , modifier = Modifier
                        .padding(bottom = 10.dp, end = 15.dp)
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


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun contact(){
    Box(modifier = Modifier.background(Color.White)){
        contact_card(contact = Contact("송지효","010-2998-4056","None"))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun contact_card(contact: Contact){
    var isExpanded by remember { mutableStateOf(false) }
    var infoClicked by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.clickable {
            isExpanded = !isExpanded
        }
    ){
        Spacer(modifier = Modifier.height(7.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(15.dp))

            if(contact.img == "None"){
                Icon(
                    Icons.Filled.AccountCircle, contentDescription = "image",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(50.dp)
                )
            }else{
                val path: Uri = Uri.parse(contact.img)
                val inputStream = LocalContext.current.contentResolver.openInputStream(path)
                val bitmap_image: Bitmap = BitmapFactory.decodeStream(inputStream)

                Image(
                    bitmap = bitmap_image.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(3.dp)
                        .clip(CircleShape)
                )
            }


            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = contact.digit,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        if(isExpanded){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 70.dp, end = 22.dp, top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){

//                Spacer(modifier = Modifier.width(75.dp))

                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(5.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .size(50.dp)
                    .clickable { val intent =
                        Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.digit}"))
                        context.startActivity(intent) }
                ){
                    Icon(Icons.Filled.Call, contentDescription = "call",
                        tint = MaterialTheme.colorScheme.onPrimary
                        , modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                    )
                }

                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(5.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .size(50.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("smsto:${contact.digit}")
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        Icons.Filled.Sms,
                        contentDescription = "call",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                    )
                }

                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(5.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .size(50.dp)
                    .clickable {
                        infoClicked = true
                    }
                ) {
                    Icon(Icons.Filled.Info, contentDescription = "call", tint = MaterialTheme.colorScheme.onPrimary
                        , modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(7.dp))
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

                Column (verticalArrangement = Arrangement.Center){
                    Spacer(modifier = Modifier.height(10.dp))

                    Row (horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                        , modifier = Modifier.fillMaxWidth()){
                        if(contact.img == "None"){
                            Icon(
                                Icons.Filled.AccountCircle, contentDescription = "image",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .size(100.dp)
                            )
                        }else{
                            val path: Uri = Uri.parse(contact.img)
                            val inputStream = LocalContext.current.contentResolver.openInputStream(path)
                            val bitmap_image: Bitmap = BitmapFactory.decodeStream(inputStream)

                            Image(
                                bitmap = bitmap_image.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                        }
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