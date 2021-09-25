package io.github.leoallvez.mediaplayer.player

import android.media.AudioAttributes
import android.media.MediaPlayer

class MediaPlayerWrapper {
    
    private var streamingUrl: String = ""
    private var isPaused: Boolean = false
    private val mediaPlayer: MediaPlayer by lazy { MediaPlayer() }

    val isNotPlaying: Boolean
        get() { return mediaPlayer.isPlaying.not() && isPaused.not() }

    fun prepare(url: String) = with(mediaPlayer) {
        reset()
        setAudioAttributes(getAudioAttributes())
        setDataSource(url)
        prepare()
        streamingUrl = url
    }

    private fun getAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
    }

    fun start() {
        isPaused = false
        mediaPlayer.start()
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            isPaused = true
            mediaPlayer.pause()
        }
    }

    fun stop() {
        if (mediaPlayer.isPlaying || isPaused) {
            isPaused = false
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
    }
}
