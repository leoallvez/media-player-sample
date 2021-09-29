package io.github.leoallvez.mediaplayer.player

import android.app.Service
import android.content.Intent

class PlayerService : Service(), IPlayerService {

    private var streamUri: String = ""

    private val player: MediaPlayerWrapper by lazy {
        MediaPlayerWrapper(service = this, context = this)
    }

    private val notification: NotificationManager by lazy {
        NotificationManager(service = this)
    }

    override fun getSteamUri(): String = streamUri

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {

            streamUri = it.getStringExtra(EXTRA_URI) ?: ""

            when (it.getStringExtra(EXTRA_ACTION)) {
                ACTION_PLAY  -> play()
                ACTION_PAUSE -> pause()
                ACTION_STOP  -> stop()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?) = PlayerBinder(service = this)

    override fun play() {

        if (player.isNotPlaying() && streamUri.isNotEmpty()) {
            player.prepare()
        }
        startPlayer()
    }

    private fun startPlayer() {
        player.start()
        createNotification()
    }

    override fun pause() = player.pause()

    override fun stop() {
        player.stop()
        removeNotification()
    }

    private fun createNotification() = notification.start(streamUri)

    private fun removeNotification() = notification.destroy()

    companion object {
        private val BASE = PlayerService::class.java.`package`?.name
        val EXTRA_ACTION = "$BASE.EXTRA_ACTION"
        val EXTRA_URI    = "$BASE.EXTRA_FILE"
        val ACTION_PLAY  = "$BASE.ACTION_PLAY"
        val ACTION_PAUSE = "$BASE.ACTION_PAUSE"
        val ACTION_STOP  = "$BASE.ACTION_STOP"
    }
}
