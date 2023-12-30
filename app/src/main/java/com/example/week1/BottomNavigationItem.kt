package com.example.week1

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.AccountCircle,
    val route : String = ""
) {
    @Composable
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Contact",
                icon = ImageVector.vectorResource(id = R.drawable.baseline_contact_phone_24),
                route = Screens.Contact.route
            ),
            BottomNavigationItem(
                label = "Gallery",
                icon = ImageVector.vectorResource(id = R.drawable.collections_black_24dp),
                route = Screens.Gallery.route
            ),
            BottomNavigationItem(
                label = "Tap3",
                icon = ImageVector.vectorResource(id = R.drawable.baseline_window_24),
                route = Screens.Tap3.route
            )
        )
    }
}

