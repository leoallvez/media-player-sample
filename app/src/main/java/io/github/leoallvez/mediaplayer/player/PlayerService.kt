package io.github.leoallvez.mediaplayer.player

import android.app.Service
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerService : Service(), IPlayerService {

    private var streamingUrl: String = ""

    private val mediaPlayer: MediaPlayerWrapper by lazy {
        MediaPlayerWrapper()
    }

    private val notification: NotificationManager by lazy {
        NotificationManager(service = this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.getStringExtra(EXTRA_ACTION)) {
                ACTION_PLAY  -> {
                    streamingUrl = it.getStringExtra(EXTRA_FILE) ?: ""
                    play(streamingUrl)
                }
                ACTION_PAUSE -> pause()
                ACTION_STOP  -> stop()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?) = PlayerBinder(service = this)

    override fun play(url: String) {
        if (mediaPlayer.isNotPlaying && url.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    mediaPlayer.prepare(url)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@launch
                }
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        createNotification()
    }

    override fun pause() = mediaPlayer.pause()

    override fun stop() {
        mediaPlayer.stop()
        removeNotification()
    }

    private fun createNotification() = notification.start(streamingUrl)

    private fun removeNotification() = notification.destroy()

    companion object {
        private val BASE = PlayerService::class.java.`package`?.name
        val EXTRA_ACTION = "$BASE.EXTRA_ACTION"
        val EXTRA_FILE   = "$BASE.EXTRA_FILE"
        val ACTION_PLAY  = "$BASE.ACTION_PLAY"
        val ACTION_PAUSE = "$BASE.ACTION_PAUSE"
        val ACTION_STOP  = "$BASE.ACTION_STOP"
    }
}
