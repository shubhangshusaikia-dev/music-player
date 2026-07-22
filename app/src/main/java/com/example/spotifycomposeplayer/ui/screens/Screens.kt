package com.example.spotifycomposeplayer.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.spotifycomposeplayer.ui.components.*
import com.example.spotifycomposeplayer.viewmodel.PlayerViewModel

@Composable fun ScreenFrame(title: String, content: @Composable ColumnScope.() -> Unit) = Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF1DB954).copy(.34f), MaterialTheme.colorScheme.background))).padding(20.dp)) { Text(title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black); Spacer(Modifier.height(16.dp)); content() }
@Composable fun HomeScreen(vm: PlayerViewModel) { val songs by vm.songs.collectAsState(); val recent by vm.recent.collectAsState(); ScreenFrame("Good evening") { Text("Recently played", fontWeight = FontWeight.Bold); SongList(if (recent.isEmpty()) songs else recent, { vm.play(it, songs) }, vm::favorite) } }
@Composable fun SearchScreen(vm: PlayerViewModel) { val q by vm.query.collectAsState(); val results by vm.searchResults.collectAsState(); ScreenFrame("Search") { OutlinedTextField(q, vm::search, Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Rounded.Search, null) }, placeholder = { Text("Songs, artists, albums") }, shape = RoundedCornerShape(22.dp)); SongList(results, { vm.play(it, results) }, vm::favorite) } }
@Composable fun LibraryScreen(vm: PlayerViewModel) { val fav by vm.favorites.collectAsState(); val albums by vm.albums.collectAsState(); val artists by vm.artists.collectAsState(); val playlists by vm.playlists.collectAsState(); ScreenFrame("Your Library") { LazyColumn(contentPadding = PaddingValues(bottom = 112.dp)) { item { LibrarySection("Favorite Songs", "${fav.size} tracks", Icons.Rounded.Favorite) }; item { LibrarySection("Recently Played", "Last 50 plays", Icons.Rounded.History) }; item { Text("Albums", fontWeight = FontWeight.Bold) }; items(albums) { LibrarySection(it, "Album", Icons.Rounded.Album) }; item { Text("Artists", fontWeight = FontWeight.Bold) }; items(artists) { LibrarySection(it, "Artist", Icons.Rounded.Person) }; item { Text("Playlists", fontWeight = FontWeight.Bold) }; items(playlists) { LibrarySection(it.name, "Playlist", Icons.Rounded.QueueMusic) } } } }
@Composable fun LibrarySection(title: String, sub: String, icon: androidx.compose.ui.graphics.vector.ImageVector) { ListItem(headlineContent = { Text(title) }, supportingContent = { Text(sub) }, leadingContent = { Icon(icon, null, tint = Color(0xFF1DB954)) }, modifier = Modifier.animateContentSize()) }
@Composable fun SettingsScreen() = ScreenFrame("Settings") { Text("Dynamic Material You, dark mode, glass Spotify-inspired cards, smooth transitions, background playback notifications, and lock-screen controls are enabled by the Compose theme and Media3 session.") }
@Composable fun FullPlayerScreen(vm: PlayerViewModel) { val song by vm.current.collectAsState(); val playing by vm.playing.collectAsState(); ScreenFrame("Now Playing") { Spacer(Modifier.height(20.dp)); AsyncImage(song?.artworkUri, null, Modifier.fillMaxWidth().aspectRatio(1f).padding(18.dp), contentScale = ContentScale.Crop); Text(song?.title ?: "Pick a song", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black); Text(song?.artist.orEmpty(), color = MaterialTheme.colorScheme.onSurfaceVariant); WaveformSeek(.35f) { }; Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) { IconButton(vm::toggleShuffle) { Icon(Icons.Rounded.Shuffle, null) }; IconButton(vm::previous) { Icon(Icons.Rounded.SkipPrevious, null, Modifier.size(40.dp)) }; FilledIconButton(vm::toggle, Modifier.size(70.dp)) { Icon(if (playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, null, Modifier.size(40.dp)) }; IconButton(vm::next) { Icon(Icons.Rounded.SkipNext, null, Modifier.size(40.dp)) }; IconButton(vm::cycleRepeat) { Icon(Icons.Rounded.Repeat, null) } } } }
