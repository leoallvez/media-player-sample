package io.github.leoallvez.mediaplayer.player

import android.os.Binder

data class PlayerBinder(val service: PlayerService) : Binder()
