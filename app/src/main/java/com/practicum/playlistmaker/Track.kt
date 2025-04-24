package com.practicum.playlistmaker

data class Track(
    val trackName: String,       // Название трека
    val artistName: String,      // Исполнитель
    val trackTime: String,       // Длительность
    val artworkUrl100: String    // Ссылка на обложку
)