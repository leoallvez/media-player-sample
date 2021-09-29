package io.github.leoallvez.mediaplayer.player

interface IPlayerService {

    fun play()
    fun pause()
    fun stop()

    fun getSteamUri(): String
}