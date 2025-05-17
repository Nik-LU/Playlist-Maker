package com.practicum.playlistmaker

import Track

data class TracksResponse(
    val resultCount: Int,     // Количество результатов
    val results: List<Track> // Список треков
)