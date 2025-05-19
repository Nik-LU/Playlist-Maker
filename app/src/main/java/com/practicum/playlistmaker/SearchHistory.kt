package com.practicum.playlistmaker

import Track
import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    private val gson = Gson()
    private val key = "search_history"

    // Добавляем трек в историю (максимум 10)
    fun addTrack(track: Track) {
        val history = getHistory().toMutableList()

        // Удаляем старую запись, если трек уже есть
        history.removeAll { it.trackId == track.trackId }

        // Добавляем в начало
        history.add(0, track)

        // Обрезаем до 10 элементов
        val trimmedHistory = if (history.size > 10) history.take(10) else history

        // Сохраняем в SharedPreferences
        sharedPreferences.edit()
            .putString(key, gson.toJson(trimmedHistory))
            .apply()
    }

    // Получаем историю
    fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(key, null)
        return if (json != null) {
            gson.fromJson(json, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }
    }

    // Очищаем историю
    fun clearHistory() {
        sharedPreferences.edit()
            .remove(key)
            .apply()
    }
}