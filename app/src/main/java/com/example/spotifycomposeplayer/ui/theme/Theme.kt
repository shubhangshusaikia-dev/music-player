package com.example.spotifycomposeplayer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Dark = darkColorScheme(primary = Color(0xFF1DB954), secondary = Color(0xFF65D48A), background = Color(0xFF050505), surface = Color(0xFF121212))
private val Light = lightColorScheme(primary = Color(0xFF1DB954), secondary = Color(0xFF16883E), background = Color(0xFFF7FFF9), surface = Color.White)

@Composable
fun MusicPlayerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val scheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else if (darkTheme) Dark else Light
    MaterialTheme(colorScheme = scheme, typography = Typography(), content = content)
}
