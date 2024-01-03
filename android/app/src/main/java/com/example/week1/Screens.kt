package com.example.week1

sealed class Screens(val route : String) {
    object Contact : Screens("contact_route")
    object Gallery : Screens("gallery_route")
    object Tap3 : Screens("Tap3_route")
}
