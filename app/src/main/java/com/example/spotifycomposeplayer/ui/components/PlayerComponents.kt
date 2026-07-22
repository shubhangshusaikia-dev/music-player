package com.example.spotifycomposeplayer.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.spotifycomposeplayer.data.model.Song

@Composable fun SongList(songs: List<Song>, onPlay: (Song) -> Unit, onFavorite: (Song) -> Unit) = LazyColumn(contentPadding = PaddingValues(bottom = 112.dp)) { items(songs, key = { it.id }) { SongRow(it, onPlay, onFavorite) } }
@Composable fun SongRow(song: Song, onPlay: (Song) -> Unit, onFavorite: (Song) -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onPlay(song) }.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(song.artworkUri, null, Modifier.size(56.dp).clip(RoundedCornerShape(14.dp)), contentScale = ContentScale.Crop)
        Column(Modifier.weight(1f).padding(horizontal = 14.dp)) { Text(song.title, style = MaterialTheme.typography.titleMedium); Text("${song.artist} • ${song.album}", color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1) }
        IconButton({ onFavorite(song) }) { Icon(if (song.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder, null, tint = if (song.isFavorite) Color(0xFF1DB954) else LocalContentColor.current) }
    }
}
@Composable fun GlassCard(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) = Box(modifier.clip(RoundedCornerShape(28.dp)).background(Brush.linearGradient(listOf(Color.White.copy(.22f), Color.White.copy(.06f)))).padding(16.dp), content = content)
@Composable fun MiniPlayer(song: Song?, playing: Boolean, onToggle: () -> Unit, onOpen: () -> Unit) { AnimatedVisibility(song != null, enter = slideInVertically { it } + fadeIn(), exit = slideOutVertically { it } + fadeOut()) { GlassCard(Modifier.fillMaxWidth().padding(12.dp).clickable { onOpen() }) { Row(verticalAlignment = Alignment.CenterVertically) { AsyncImage(song?.artworkUri, null, Modifier.size(52.dp).clip(RoundedCornerShape(16.dp))); Column(Modifier.weight(1f).padding(12.dp)) { Text(song?.title.orEmpty(), maxLines = 1); Text(song?.artist.orEmpty(), color = MaterialTheme.colorScheme.onSurfaceVariant) }; IconButton(onToggle) { Icon(if (playing) Icons.Rounded.PauseCircle else Icons.Rounded.PlayCircle, null, Modifier.size(38.dp), tint = Color(0xFF1DB954)) } } } } }
@Composable fun WaveformSeek(progress: Float, onSeek: (Float) -> Unit) { Canvas(Modifier.fillMaxWidth().height(52.dp).clickable { onSeek(.5f) }) { val bars = 54; repeat(bars) { i -> val h = (8 + ((i * 17) % 34)).dp.toPx(); val x = size.width * i / bars; drawLine(if (i / bars.toFloat() <= progress) Color(0xFF1DB954) else Color.Gray.copy(.45f), androidx.compose.ui.geometry.Offset(x, size.height / 2 - h / 2), androidx.compose.ui.geometry.Offset(x, size.height / 2 + h / 2), 5.dp.toPx(), StrokeCap.Round) } } }
