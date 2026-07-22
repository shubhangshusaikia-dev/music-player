package com.example.spotifycomposeplayer

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.example.spotifycomposeplayer.ui.navigation.MusicApp
import com.example.spotifycomposeplayer.ui.components.RequestMusicPermissions
import com.example.spotifycomposeplayer.ui.theme.MusicPlayerTheme
import com.example.spotifycomposeplayer.viewmodel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                val vm: PlayerViewModel = hiltViewModel()
                RequestMusicPermissions()
                LaunchedEffect(Unit) { vm.scanDevice() }
                MusicApp(vm)
            }
        }
    }
}
