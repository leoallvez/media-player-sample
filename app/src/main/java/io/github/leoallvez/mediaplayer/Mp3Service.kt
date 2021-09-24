package io.github.leoallvez.mediaplayer

interface Mp3Service {

    val currentSong: String?
    val totalTime: Int
    val elapsedTime: Int

    fun play(file: String)
    fun pause()
    fun stop()
}