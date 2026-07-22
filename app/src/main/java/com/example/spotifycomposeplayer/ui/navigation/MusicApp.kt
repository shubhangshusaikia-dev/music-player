package com.example.spotifycomposeplayer.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.spotifycomposeplayer.ui.components.MiniPlayer
import com.example.spotifycomposeplayer.ui.screens.*
import com.example.spotifycomposeplayer.viewmodel.PlayerViewModel

sealed class Tab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) { data object Home: Tab("home", "Home", Icons.Rounded.Home); data object Search: Tab("search", "Search", Icons.Rounded.Search); data object Library: Tab("library", "Library", Icons.Rounded.LibraryMusic); data object Settings: Tab("settings", "Settings", Icons.Rounded.Settings) }

@Composable fun MusicApp(vm: PlayerViewModel) {
    val nav = rememberNavController(); val current by vm.current.collectAsState(); val playing by vm.playing.collectAsState(); val tabs = listOf(Tab.Home, Tab.Search, Tab.Library, Tab.Settings)
    Scaffold(bottomBar = { Column { MiniPlayer(current, playing, vm::toggle) { nav.navigate("player") }; NavigationBar { val route = nav.currentBackStackEntryAsState().value?.destination?.route; tabs.forEach { NavigationBarItem(route == it.route, { nav.navigate(it.route) { launchSingleTop = true } }, icon = { Icon(it.icon, null) }, label = { Text(it.label) }) } } } }) { padding ->
        NavHost(nav, startDestination = Tab.Home.route, Modifier.padding(padding)) {
            composable("home") { HomeScreen(vm) }
            composable("search") { SearchScreen(vm) }
            composable("library") { LibraryScreen(vm) }
            composable("settings") { SettingsScreen() }
            composable("player") { FullPlayerScreen(vm) }
        }
    }
}
