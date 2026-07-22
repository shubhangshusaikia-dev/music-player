package com.example.spotifycomposeplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: String,
    val artworkUri: String?,
    val dateAdded: Long,
    val isFavorite: Boolean = false,
    val lastPlayed: Long = 0,
    val playCount: Int = 0,
)

@Entity(tableName = "playlists")
data class Playlist(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String)

@Entity(tableName = "playlist_songs", primaryKeys = ["playlistId", "songId"])
data class PlaylistSong(val playlistId: Long, val songId: Long)
