package io.github.leoallvez.mediaplayer.player

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerService : Service(), IPlayerService {
    //TODO: create a class to mediaPlayer and isPaused
    private lateinit var mediaPlayer: MediaPlayer
    private var isPaused: Boolean = false

    private var currentUrl: String = ""
    private val notification: NotificationManager by lazy {
        NotificationManager(service = this)
    }

    private fun log(msg: String) = Log.w("player_service", msg)

    override fun onCreate() {
        log("start onCreate()")
        super.onCreate()
        mediaPlayer = MediaPlayer()
        log("end onCreate()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("start onStartCommand()")
        intent?.let {
            when (it.getStringExtra(EXTRA_ACTION)) {
                ACTION_PLAY  -> {
                    val url = it.getStringExtra(EXTRA_FILE) ?: ""
                    play(url)
                }
                ACTION_PAUSE -> pause()
                ACTION_STOP  -> stop()
            }
        }
        log("end onStartCommand()")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): PlayerBinder {
        log("start onBind")
        log("end onBind()")
        return PlayerBinder(service = this)
    }

    override fun play(url: String) {
        log("start play()")
        CoroutineScope(Dispatchers.IO).launch {
            log("inside of coroutine scope in play()")
            if (isNotPlaying() && url.isNotEmpty()) {
                log("inside in 'if' in play()")
                try {
                    log("inside in 'try' in play()")
                    preparePlayer(url)
                } catch (e: Exception) {
                    log("inside in 'catch' in play()")
                    e.printStackTrace()
                    return@launch
                }
            }
            startPlayer()
        }
        log("end play()")
    }

    private fun isNotPlaying(): Boolean {
        return mediaPlayer.isPlaying.not() && isPaused.not()
    }

    private fun preparePlayer(url: String) = with(mediaPlayer) {
        log("start preparePlayer()")
        reset()
        setAudioAttributes(getAudioAttributes())
        setDataSource(url)
        prepare()
        currentUrl = url
        log("end preparePlayer()")
    }

    private fun startPlayer() {
        isPaused = false
        mediaPlayer.start()
        createNotification()
    }

    private fun getAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
    }

    override fun pause() {
        log("start pause()")
        if (mediaPlayer.isPlaying) {
            log("inside of 'if' in pause()")
            isPaused = true
            mediaPlayer.pause()
        }
        log("end pause()")
    }

    override fun stop() {
        log("start stop()")
        if (mediaPlayer.isPlaying || isPaused) {
            log("inside of 'if' in stop()")
            isPaused = false
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
        removeNotification()
        log("end stop()")
    }

    private fun createNotification() {
        log("start createNotification()")
        notification.start(url = null)
        log("end createNotification()")
    }

    private fun removeNotification() {
        notification.destroy()
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
