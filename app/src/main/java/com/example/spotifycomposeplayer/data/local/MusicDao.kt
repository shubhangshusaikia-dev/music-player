package com.example.spotifycomposeplayer.data.local

import androidx.room.*
import com.example.spotifycomposeplayer.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Query("SELECT * FROM songs ORDER BY title") fun songs(): Flow<List<Song>>
    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR album LIKE '%' || :query || '%' ORDER BY title") fun search(query: String): Flow<List<Song>>
    @Query("SELECT * FROM songs WHERE isFavorite = 1 ORDER BY title") fun favorites(): Flow<List<Song>>
    @Query("SELECT * FROM songs WHERE lastPlayed > 0 ORDER BY lastPlayed DESC LIMIT 50") fun recent(): Flow<List<Song>>
    @Query("SELECT DISTINCT album FROM songs ORDER BY album") fun albums(): Flow<List<String>>
    @Query("SELECT DISTINCT artist FROM songs ORDER BY artist") fun artists(): Flow<List<String>>
    @Query("SELECT * FROM playlists ORDER BY name") fun playlists(): Flow<List<Playlist>>
    @Upsert suspend fun upsertSongs(songs: List<Song>)
    @Update suspend fun update(song: Song)
    @Insert suspend fun createPlaylist(playlist: Playlist)
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun addToPlaylist(song: PlaylistSong)
}
