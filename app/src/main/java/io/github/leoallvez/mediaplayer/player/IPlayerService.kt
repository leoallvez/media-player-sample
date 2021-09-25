package io.github.leoallvez.mediaplayer.player

interface IPlayerService {

    fun play(url: String?)
    fun pause()
    fun stop()
}