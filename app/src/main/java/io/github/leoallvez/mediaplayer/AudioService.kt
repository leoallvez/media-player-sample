//package io.github.leoallvez.mediaplayer
//
//import android.app.PendingIntent
//import android.app.Service
//import android.content.Intent
//import android.media.AudioManager
//import android.media.MediaPlayer
//import android.media.MediaPlayer.OnPreparedListener
//import android.os.IBinder
//import android.util.Log
//import android.media.AudioAttributes
//import androidx.core.app.NotificationCompat
//import io.github.leoallvez.mediaplayer.Constants.Companion.channelID
//import io.github.leoallvez.mediaplayer.Constants.Companion.foregroundServiceNotificationTitle
//
//
//class AudioService : Service() {
//
//    private var isPlayerReady: Boolean = false
//    private lateinit var mediaPlayer: MediaPlayer
//
//    override fun onBind(intent: Intent?): IBinder? = null
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val input = intent?.getStringExtra(Constants.inputExtra)
//
//        val notificationIntent = Intent(this, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
//
//        val notification = NotificationCompat.Builder(this, channelID)
//            .setContentTitle(foregroundServiceNotificationTitle)
//            .setContentText(input)
//            .setSmallIcon(R.drawable.ic_baseline_music_note_24)
//            .setContentIntent(pendingIntent)
//            .build()
//
//        startForeground(1, notification)
//
//        return START_NOT_STICKY
//    }
//
//    override fun onCreate() {
//        val url = "https://demo.azuracast.com/radio/8000/radio.mp3"
//        mediaPlayer = MediaPlayer().apply {
//            setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build()
//            )
//            setDataSource(url)
//            prepareAsync()
//        }
//
//        mediaPlayer.setOnPreparedListener {
//            isPlayerReady = true
//            Log.d("audio_service", "media_is_ready")
//            mediaPlayer.start()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mediaPlayer.stop()
//        mediaPlayer.release()
//        Log.w("audio_service", "on_destroy")
//    }
//}