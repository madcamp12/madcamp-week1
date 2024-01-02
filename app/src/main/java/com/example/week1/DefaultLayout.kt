package com.example.week1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.week1.screens.ContactScreen
import com.example.week1.screens.GalleryScreen
import com.example.week1.screens.Tap3Screen
import com.example.week1.typography


private val LightGray = Color(0xFFAFAFAF)
private val DarkGray = Color(0xFF6F6F6F)
private val LightBlue = Color(0xFF3C68FF)
private val DarkBlue = Color(0xFF182861)
private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF000000)
private val PastelBlue_1 = Color(0xFFA5D8EA)
private val PastelBle_2 = Color(0xFFA1B4FF)
private val DarkPastelBlue_1 = Color(0xFF416072)
private val DarkPastelBle_2 = Color(0xFF3D509B)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPastelBlue_1,
    onPrimary = PastelBle_2,
    secondary = DarkGray,
    onSecondary = White,
    tertiary = DarkBlue
)

private val LightColorScheme = lightColorScheme(
    primary = PastelBlue_1,
    onPrimary = DarkPastelBle_2,
    secondary = LightGray,
    onSecondary = Black,
    tertiary = LightBlue


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun default_layout(){

    var title:String by remember { mutableStateOf("내 연락처") }
    var darkMode by remember { mutableStateOf(false) }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = { MaterialTheme (colorScheme = if(darkMode) DarkColorScheme else LightColorScheme){
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text("$title", style = typography.titleLarge) },
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
        } },

        bottomBar = {
            MaterialTheme(colorScheme = if(darkMode) DarkColorScheme else LightColorScheme){
                NavigationBar(modifier = Modifier.background(color = MaterialTheme.colorScheme.primary)) {
                    bottomNavigationItems().forEachIndexed { _, navigationItem ->
                        NavigationBarItem(
                            selected = navigationItem.route == currentDestination?.route,
                            label = {
                                Text(text = navigationItem.label, style = typography.titleSmall)
                            },
                            icon = {
                                Icon(
                                    navigationItem.icon,
                                    contentDescription = navigationItem.label
                                )
                            },
                            onClick = {
                                navController.navigate(navigationItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.onPrimary, selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = LightGray, unselectedTextColor = DarkGray, indicatorColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        MaterialTheme(colorScheme = if (darkMode) DarkColorScheme else LightColorScheme) {
            Surface (modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .fillMaxHeight()){
                NavHost(
                    navController = navController,
                    startDestination = Screens.Contact.route,
                ) {
                    composable(Screens.Contact.route) {
                        title = "내 연락처"
                        ContactScreen(
                            navController
                        )
                    }
                    composable(Screens.Gallery.route) {
                        title = "사진"
                        GalleryScreen(
                            navController
                        )
                    }
                    composable(Screens.Tap3.route) {
                        title = "제비뽑기"
                        Tap3Screen(
                            navController
                        )
                    }
                }
            }
        }
    }
}

data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.AccountCircle,
    val route : String = ""
)

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