package com.example.jo

import android.content.Context
import android.media.MediaPlayer

class MusicManager private constructor(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    companion object {
        private var instance: MusicManager? = null

        fun getInstance(context: Context): MusicManager {
            if (instance == null) {
                instance = MusicManager(context.applicationContext)
            }
            return instance!!
        }
    }

    fun startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.jowen) // Replace jowen with your music file name
            mediaPlayer?.isLooping = true
        }
        mediaPlayer?.start()
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying?: false
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun stopMusic() {
        TODO("Not yet implemented")
    }
}