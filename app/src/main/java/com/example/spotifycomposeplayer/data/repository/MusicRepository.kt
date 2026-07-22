package com.example.spotifycomposeplayer.data.repository

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.example.spotifycomposeplayer.data.local.MusicDao
import com.example.spotifycomposeplayer.data.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject constructor(@ApplicationContext private val context: Context, private val dao: MusicDao) {
    val songs = dao.songs(); val favorites = dao.favorites(); val recent = dao.recent(); val albums = dao.albums(); val artists = dao.artists(); val playlists = dao.playlists()
    fun search(query: String) = dao.search(query)
    suspend fun scanDevice() {
        val collection = if (Build.VERSION.SDK_INT >= 29) MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL) else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.ALBUM_ID)
        val list = mutableListOf<Song>()
        context.contentResolver.query(collection, projection, "${MediaStore.Audio.Media.IS_MUSIC}=1 AND ${MediaStore.Audio.Media.MIME_TYPE}=?", arrayOf("audio/mpeg"), "${MediaStore.Audio.Media.TITLE} ASC")?.use { c ->
            val id = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID); val title = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE); val artist = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST); val album = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM); val duration = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION); val added = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED); val albumId = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            while (c.moveToNext()) {
                val mediaId = c.getLong(id); val art = ContentUris.withAppendedId(android.net.Uri.parse("content://media/external/audio/albumart"), c.getLong(albumId)).toString()
                list += Song(mediaId, c.getString(title) ?: "Unknown", c.getString(artist) ?: "Unknown Artist", c.getString(album) ?: "Unknown Album", c.getLong(duration), ContentUris.withAppendedId(collection, mediaId).toString(), art, c.getLong(added))
            }
        }
        dao.upsertSongs(list)
    }
    suspend fun toggleFavorite(song: Song) = dao.update(song.copy(isFavorite = !song.isFavorite))
    suspend fun markPlayed(song: Song) = dao.update(song.copy(lastPlayed = System.currentTimeMillis(), playCount = song.playCount + 1))
}
