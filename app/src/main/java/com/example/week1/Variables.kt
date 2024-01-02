package com.example.week1

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class Contact(@DrawableRes val img : Int, val name: String, val digit: String)

var contacts: MutableSet<Contact> = mutableSetOf<Contact>()

fun add_contact(name: String, digit: String){
    val new_digit = digit.replace("-", "", true)

    contacts.add(Contact(img = R.drawable.person, name = name, digit = new_digit))
}
private val dongle = FontFamily(
    Font(R.font.dongle_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.dongle_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.dongle_light, FontWeight.Light, FontStyle.Normal),
)

private val notosanskr = FontFamily(
    Font(R.font.notosanskr_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.notosanskr_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.notosanskr_light, FontWeight.Light, FontStyle.Normal)
)

@RequiresApi(Build.VERSION_CODES.O)
val typography = Typography(
    titleLarge = TextStyle(
        fontFamily = dongle,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = dongle,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    ),
    titleSmall = TextStyle(
        fontFamily = dongle,
        fontWeight = FontWeight.Light,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = notosanskr,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = notosanskr,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    bodySmall = TextStyle(
        fontFamily = notosanskr,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    ),
)
