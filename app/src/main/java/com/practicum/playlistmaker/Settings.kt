package com.practicum.playlistmaker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<TextView>(R.id.settingsTitle).setOnClickListener {
            finish()
        }
    }

    // Обработчик для кнопки "Поделиться приложением"
    fun onShareButtonClick(view: View) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_message, getString(R.string.course_url))
            )
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }

    // Обработчик для кнопки "Написать в поддержку"
    fun onSupportButtonClick(view: View) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_body))
        }

        try {
            startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.email_app_not_found), Toast.LENGTH_SHORT).show()
        }
    }

    // Обработчик для кнопки "Пользовательское соглашение"
    fun onAgreementClick(view: View) {
        val agreementIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(getString(R.string.offer_url))
        }

        try {
            startActivity(agreementIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.browser_not_found), Toast.LENGTH_SHORT).show()
        }
    }
}