package com.example.week1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.week1.ui.theme.DarkColorScheme
import com.example.week1.ui.theme.LightColorScheme
import com.example.week1.default_layout


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var darkMode:Boolean = false

        setContent {
            default_layout(title = "내 연락처") {
                LazyColumn{
                    items (100) { idx: Int ->
                        contact_card(contact = Contact(img = R.drawable.person, name = "김동하$idx", digit = "010-3404-4624"))
                    }
                }
            }
        }
    }
}

data class Contact(@DrawableRes val img : Int, val name: String, val digit: String)

@Composable
fun contact_card(contact: Contact){
    var isExpanded by remember { mutableStateOf(false) }
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
                    val intent = Intent(context, ContactInfo::class.java)
                    context.startActivity(intent)
                }){
                    Image(painter = painterResource(id = R.drawable.info), contentDescription = "info"
                        , modifier = Modifier
                            .width(30.dp)
                            .height(30.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = Color.Gray, modifier = Modifier.fillMaxWidth().height(1.dp))
    }
}