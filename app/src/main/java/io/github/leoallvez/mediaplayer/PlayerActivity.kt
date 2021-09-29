package io.github.leoallvez.mediaplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import io.github.leoallvez.mediaplayer.player.PlayerBinder
import io.github.leoallvez.mediaplayer.player.PlayerService
import kotlinx.android.synthetic.main.activity_main.*

class PlayerActivity : AppCompatActivity(), ServiceConnection {

    private var service: PlayerService? = null
    private var url: String = "https://radio.vitaminanerd.com.br/radio/8000/radio.mp3?1555371883/stream?type=.mp3"
//    private val url: String = "https://demo.azuracast.com/radio/8000/radio.mp3"
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        btnPlay.setOnClickListener { play() }
        btnPause.setOnClickListener { pause() }
        btnStop.setOnClickListener { stop() }
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, PlayerService::class.java)
        intent.putExtra(PlayerService.EXTRA_URI, url)
        startService(intent)
        bindService(intent, this, 0)
    }

    override fun onPause() {
        super.onPause()
        unbindService(this)
    }

    private fun play() {
        service?.play()
    }

    private fun pause() {
        service?.pause()
    }

    private fun stop() {
        service?.stop()
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        service = (binder as PlayerBinder).service
        service?.play()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        service = null
    }
}
