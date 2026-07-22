package com.example.spotifycomposeplayer.di

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.room.Room
import com.example.spotifycomposeplayer.data.local.MusicDatabase
import com.example.spotifycomposeplayer.playback.MusicPlaybackService
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton fun database(@ApplicationContext context: Context) = Room.databaseBuilder(context, MusicDatabase::class.java, "music.db").build()
    @Provides fun dao(db: MusicDatabase) = db.dao()
    @Provides @Singleton fun player(@ApplicationContext context: Context): ExoPlayer = ExoPlayer.Builder(context).build().apply { setAudioAttributes(AudioAttributes.Builder().setUsage(C.USAGE_MEDIA).setContentType(C.AUDIO_CONTENT_TYPE_MUSIC).build(), true); prepare() }
    @Provides @Singleton fun controller(@ApplicationContext context: Context): ListenableFuture<MediaController> = MediaController.Builder(context, SessionToken(context, ComponentName(context, MusicPlaybackService::class.java))).buildAsync()
}
