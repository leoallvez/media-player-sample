package io.github.leoallvez.mediaplayer.player

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer

class PlayerService : Service(), IPlayerService {

    private lateinit var mediaPlayer: MediaPlayer
    private var isPaused: Boolean = false
    private var currentUrl: String? = ""
    private var notification: MediaNotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.getStringExtra(EXTRA_ACTION)) {
                ACTION_PLAY  -> play(it.getStringExtra(EXTRA_FILE))
                ACTION_PAUSE -> pause()
                ACTION_STOP  -> stop()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent) = PlayerBinder(service = this)

    override fun play(url: String?) {
        if (mediaPlayer.isPlaying.not() && isPaused.not()) {
            try {
                preparePlayer(url)
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }
        }
        isPaused = false
        mediaPlayer.start()
        createNotification()
    }

    private fun preparePlayer(url: String?) = with(mediaPlayer) {
        try {
            reset()
            setAudioAttributes(getAudioAttributes())
            setDataSource(url)
            prepare()
            currentUrl = url
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
    }

    private fun getAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) {
            isPaused = true
            mediaPlayer.pause()
        }
    }

    override fun stop() {
        if (mediaPlayer.isPlaying || isPaused) {
            isPaused = false
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
        removeNotification()
    }

    private fun createNotification() {
        notification = MediaNotificationManager(service = this)
        notification?.start(url = null)
    }

    private fun removeNotification() {
        notification?.destroy()
    }

    companion object {
        private val BASE = PlayerService::class.java.`package`?.name
        val EXTRA_ACTION = "$BASE.EXTRA_ACTION"
        val EXTRA_FILE   = "$BASE.EXTRA_FILE"
        val ACTION_PLAY  = "$BASE.ACTION_PLAY"
        val ACTION_PAUSE = "$BASE.ACTION_PAUSE"
        val ACTION_STOP  = "$BASE.ACTION_STOP"
    }
}
