package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Получаем ссылки на кнопки
        val searchButton = findViewById<MaterialButton>(R.id.button1)
        val libraryButton = findViewById<MaterialButton>(R.id.button2)
        val settingsButton = findViewById<MaterialButton>(R.id.button3)

        // Способ 1: Анонимный класс для кнопки поиска
        val searchButtonClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Создаем Intent для перехода на SearchActivity
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        // Способ 2: Лямбда-выражение для кнопки медиатеки
        libraryButton.setOnClickListener {
            // Переход на MediaActivity с использованием лямбды
            startActivity(Intent(this@MainActivity, MediaActivity::class.java))
        }

        // Кнопка настроек (оставляем Toast для примера или можно тоже заменить на переход)
        settingsButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }
    }
}