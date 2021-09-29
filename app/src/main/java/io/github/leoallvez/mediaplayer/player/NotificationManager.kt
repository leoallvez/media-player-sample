package io.github.leoallvez.mediaplayer.player

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.github.leoallvez.mediaplayer.PlayerActivity
import io.github.leoallvez.mediaplayer.R
import io.github.leoallvez.mediaplayer.player.PlayerService.Companion.ACTION_PAUSE
import io.github.leoallvez.mediaplayer.player.PlayerService.Companion.ACTION_PLAY
import io.github.leoallvez.mediaplayer.player.PlayerService.Companion.ACTION_STOP
import io.github.leoallvez.mediaplayer.player.PlayerService.Companion.EXTRA_ACTION
import java.io.File

class NotificationManager(
    private val service: PlayerService
) {

    fun start(url: String?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =  service.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    PLAYER_CHANNEL,
                    service.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }

        val itPlay = Intent(service, PlayerService::class.java).apply {
            putExtra(EXTRA_ACTION, ACTION_PLAY)
//            putExtra(EXTRA_FILE, url)
        }

        val itPause = Intent(service, PlayerService::class.java).apply {
            putExtra(EXTRA_ACTION, ACTION_PAUSE)
        }

        val itStop = Intent(service, PlayerService::class.java).apply {
            putExtra(EXTRA_ACTION, ACTION_STOP)
        }

        val intent = Intent(service, PlayerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pitActivity = PendingIntent.getActivity(service, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pitPlay = PendingIntent.getService(service, 1, itPlay, PendingIntent.FLAG_UPDATE_CURRENT)
        val pitPause = PendingIntent.getService(service, 2, itPause, 0)
        val pitStop = PendingIntent.getService(service, 3, itStop, 0)
        val views = RemoteViews(service.packageName, R.layout.layout_notification)

        views.setOnClickPendingIntent(R.id.btnPlay, pitPlay)
        views.setOnClickPendingIntent(R.id.btnPause, pitPause)
        views.setOnClickPendingIntent(R.id.btnClose, pitStop)
        views.setOnClickPendingIntent(R.id.txtMusic, pitActivity)

        views.setTextViewText(
            R.id.txtMusic,
            url?.substring((url?.lastIndexOf(File.separator) ?: 0) + 1)
        )

        val notification = NotificationCompat.Builder(service, PLAYER_CHANNEL)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContent(views)
            .setOngoing(true)
            .build()

        NotificationManagerCompat.from(service).notify(NOTIFICATION_ID, notification)
    }

    fun destroy() {
        NotificationManagerCompat.from(service).cancel(NOTIFICATION_ID)
    }

    companion object {
        const val NOTIFICATION_ID = 999
        private const val PLAYER_CHANNEL = "PLAYER_CHANNEL"
    }
}