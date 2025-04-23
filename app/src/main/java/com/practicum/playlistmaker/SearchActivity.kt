package com.practicum.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SAVED_TEXT_KEY = "SAVED_TEXT"
    }

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageButton
    private var currentSearchText = "" // Переменная для сохранения текста

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Инициализация элементов
        searchEditText = findViewById(R.id.searchEditText)
        clearButton = findViewById(R.id.clearButton)

        // Настройка кнопки назад
        findViewById<TextView>(R.id.searchTitle).setOnClickListener {
            finish()
        }

        // Настройка поля поиска
        setupSearchField()
    }

    private fun setupSearchField() {
        // TextWatcher для отслеживания ввода
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                currentSearchText = s?.toString() ?: "" // Сохраняем текущий текст
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Обработчик клика на кнопку очистки
        clearButton.setOnClickListener {
            searchEditText.text.clear()
            currentSearchText = "" // Очищаем сохранённый текст
            hideKeyboard()
        }

        // Фокус на поле ввода при открытии
        searchEditText.requestFocus()
        showKeyboard()
    }

    // Сохранение состояния перед поворотом экрана
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT_KEY, currentSearchText)
    }

    // Восстановление состояния после поворота экрана
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentSearchText = savedInstanceState.getString(SAVED_TEXT_KEY, "")
        searchEditText.setText(currentSearchText)
    }

    private fun showKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }
}