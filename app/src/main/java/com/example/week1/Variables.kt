package com.example.week1

import androidx.annotation.DrawableRes

data class Contact(@DrawableRes val img : Int, val name: String, val digit: String)

var contacts: MutableSet<Contact> = mutableSetOf<Contact>()

fun add_contact(name: String, digit: String){
    contacts.add(Contact(img = R.drawable.person, name = name, digit = digit))
}