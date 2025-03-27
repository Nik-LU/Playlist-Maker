package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Обработка клика по заголовку (кнопка назад)
        findViewById<TextView>(R.id.settingsTitle).setOnClickListener {
            // Простой переход назад без флагов
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Закрываем текущую активность
        }
    }
}