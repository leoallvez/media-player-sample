package io.github.leoallvez.player

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import io.github.leoallvez.player.PlayerService.Companion.ACTION_PAUSE
import io.github.leoallvez.player.PlayerService.Companion.ACTION_PLAY
import io.github.leoallvez.player.PlayerService.Companion.ACTION_STOP
import io.github.leoallvez.player.PlayerService.Companion.EXTRA_ACTION
import io.github.leoallvez.player.PlayerService.Companion.EXTRA_FILE

class MediaNotificationManager(private val service: PlayerService, val cls: Class<Any>) {

    fun create(url: String) {

        val channelId = "channel1"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =  service.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    service.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }

        val itPlay = Intent(service, PlayerService::class.java).apply {
            putExtra(EXTRA_ACTION, ACTION_PLAY)
            putExtra(EXTRA_FILE, url)
        }

        val itPause = Intent(service, PlayerService::class.java).apply {
            putExtra(EXTRA_ACTION, ACTION_PAUSE)
        }

        val itStop = Intent(service, PlayerService::class.java).apply {
            putExtra(EXTRA_ACTION, ACTION_STOP)
        }

//        val pitActivity = PendingIntent.getActivity(
//            service, 0,
//            Intent(this, LoginActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//            }, PendingIntent.FLAG_UPDATE_CURRENT
//        )

        val intent = Intent(service, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pendingIntent = PendingIntent.getActivity(service, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    }
}