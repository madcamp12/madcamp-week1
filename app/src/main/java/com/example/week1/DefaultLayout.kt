package com.example.week1

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.week1.ui.theme.DarkColorScheme
import com.example.week1.ui.theme.LightColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun default_layout(title: String,
                   content: @Composable () -> Unit){
    var darkMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { MaterialTheme (colorScheme = if(darkMode) DarkColorScheme else LightColorScheme){
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("$title") },
                actions = { Image(
                    painter = painterResource(id = R.drawable.darkmode),
                    contentDescription = "dark mode",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .padding(end = 10.dp)
                        .clickable {
                            darkMode = !darkMode
                        })
                }
            )
        }
        },
    ) { innerPadding ->
        MaterialTheme(
            colorScheme = if(darkMode) DarkColorScheme else LightColorScheme
        ){

            Surface (modifier = Modifier.padding(innerPadding).fillMaxWidth().fillMaxHeight(), content = content)
        }
    }
}