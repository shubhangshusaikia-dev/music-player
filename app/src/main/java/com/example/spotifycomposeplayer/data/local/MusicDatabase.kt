package com.example.spotifycomposeplayer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spotifycomposeplayer.data.model.*

@Database(entities = [Song::class, Playlist::class, PlaylistSong::class], version = 1)
abstract class MusicDatabase : RoomDatabase() { abstract fun dao(): MusicDao }
