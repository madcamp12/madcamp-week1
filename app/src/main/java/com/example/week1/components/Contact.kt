package com.example.week1.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun contact_card(@DrawableRes img: Int, name: String, digit: String){
    Row {
        Image(painterResource(id = img), "Contact Images")
        Column {
            Text(text = "$name")
            Text(text = "$digit")
        }
    }
}