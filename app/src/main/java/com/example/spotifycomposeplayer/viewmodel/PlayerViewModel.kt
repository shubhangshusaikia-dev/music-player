package com.example.spotifycomposeplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.spotifycomposeplayer.data.model.Song
import com.example.spotifycomposeplayer.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val repo: MusicRepository, private val player: ExoPlayer) : ViewModel() {
    val songs = repo.songs.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val favorites = repo.favorites.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val recent = repo.recent.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val albums = repo.albums.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val artists = repo.artists.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val playlists = repo.playlists.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private val _query = MutableStateFlow(""); val query = _query.asStateFlow()
    val searchResults = query.flatMapLatest { if (it.isBlank()) repo.songs else repo.search(it) }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private val _current = MutableStateFlow<Song?>(null); val current = _current.asStateFlow()
    private val _playing = MutableStateFlow(false); val playing = _playing.asStateFlow()
    private val _shuffle = MutableStateFlow(false); val shuffle = _shuffle.asStateFlow()
    private val _repeat = MutableStateFlow(Player.REPEAT_MODE_OFF); val repeat = _repeat.asStateFlow()
    fun scanDevice() = viewModelScope.launch { repo.scanDevice() }
    fun search(text: String) { _query.value = text }
    fun play(song: Song, queue: List<Song> = songs.value) { player.setMediaItems(queue.map { MediaItem.fromUri(it.uri) }, queue.indexOf(song).coerceAtLeast(0), 0); player.prepare(); player.play(); _current.value = song; _playing.value = true; viewModelScope.launch { repo.markPlayed(song) } }
    fun toggle() { if (player.isPlaying) player.pause() else player.play(); _playing.value = player.isPlaying }
    fun next() { player.seekToNextMediaItem() }
    fun previous() { player.seekToPreviousMediaItem() }
    fun seek(ms: Long) { player.seekTo(ms) }
    fun toggleShuffle() { _shuffle.value = !_shuffle.value; player.shuffleModeEnabled = _shuffle.value }
    fun cycleRepeat() { _repeat.value = when (_repeat.value) { Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL; Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE; else -> Player.REPEAT_MODE_OFF }; player.repeatMode = _repeat.value }
    fun favorite(song: Song) = viewModelScope.launch { repo.toggleFavorite(song) }
}
