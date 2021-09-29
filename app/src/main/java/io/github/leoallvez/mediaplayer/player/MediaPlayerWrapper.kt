package io.github.leoallvez.mediaplayer.player

import android.content.Context
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Player

class MediaPlayerWrapper(private val service: IPlayerService, context: Context) {

    private val player: SimpleExoPlayer by lazy {
        SimpleExoPlayer.Builder(context).build()
    }

    fun isPlaying(): Boolean {
        return player.playbackState == Player.STATE_READY
    }

    fun isNotPlaying(): Boolean = isPlaying().not()

    fun prepare() {
        val uri = service.getSteamUri()
        val mediaItem: MediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    fun start() {
        player.playWhenReady = true
        player.play()
    }

    fun pause() {
        if(player.isPlaying) {
            player.pause()
        }
    }

    fun stop() {
        if (player.isPlaying) {
            player.playWhenReady = false
            player.stop()
        }
    }
}
