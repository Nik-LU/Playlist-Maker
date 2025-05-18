package com.practicum.playlistmaker

import MusicApi
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SAVED_TEXT_KEY = "SAVED_TEXT"
    }

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var searchContainer: MaterialCardView
    private lateinit var placeholderGroup: View
    private lateinit var placeholderIcon: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var retryButton: Button
    private lateinit var clearHistoryButton: Button
    private lateinit var historyContainer: View
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var searchHistory: SearchHistory
    private var currentSearchText = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val musicApi = retrofit.create(MusicApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViews()
        setupSearchHistory()
        setupSearchField()
        initRecyclerView()
        checkNetworkState()
    }

    private fun initViews() {
        searchEditText = findViewById(R.id.searchEditText)
        clearButton = findViewById(R.id.clearButton)
        recyclerView = findViewById(R.id.tracksRecyclerView)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        searchContainer = findViewById(R.id.searchContainer)
        placeholderGroup = findViewById(R.id.placeholderGroup)
        placeholderIcon = findViewById(R.id.placeholderIcon)
        placeholderText = findViewById(R.id.placeholderText)
        retryButton = findViewById(R.id.retryButton)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        historyContainer = findViewById(R.id.historyContainer)

        findViewById<TextView>(R.id.searchTitle).apply {
            setOnClickListener { finish() }
            text = getString(R.string.search_button)
        }

        retryButton.setOnClickListener {
            performSearch(searchEditText.text.toString())
        }
    }

    private fun setupSearchHistory() {
        searchHistory = SearchHistory(getSharedPreferences("search_prefs", Context.MODE_PRIVATE))
        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            updateHistoryVisibility()
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TrackAdapter(emptyList()) { track ->
            searchHistory.addTrack(track)
            updateHistoryVisibility()
        }

        historyAdapter = TrackAdapter(emptyList()) { track ->
            searchHistory.addTrack(track)
            updateHistoryVisibility()
        }

        recyclerView.adapter = adapter
        historyRecyclerView.adapter = historyAdapter
    }

    private fun performSearch(query: String) {
        if (query.isEmpty()) return

        showLoading(true)
        musicApi.searchTracks(query).enqueue(object : Callback<TracksResponse> {
            override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val tracks = response.body()?.results ?: emptyList()
                    if (tracks.isNotEmpty()) {
                        adapter.updateTracks(tracks)
                        showPlaceholder(false)
                        historyContainer.isVisible = false
                    } else {
                        showPlaceholder(true, R.string.nothing_found)
                    }
                } else {
                    showPlaceholder(true, R.string.server_error, true)
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                showLoading(false)
                showPlaceholder(true, R.string.network_error, true)
            }
        })
    }

    private fun updateHistoryVisibility() {
        val shouldShowHistory = searchEditText.text.isEmpty() &&
                searchEditText.hasFocus() &&
                searchHistory.getHistory().isNotEmpty()

        historyContainer.isVisible = shouldShowHistory

        if (shouldShowHistory) {
            historyAdapter.updateTracks(searchHistory.getHistory())
        }
    }

    private fun showLoading(show: Boolean) {
        recyclerView.isVisible = !show
        searchEditText.isEnabled = !show
    }

    private fun showPlaceholder(show: Boolean, messageRes: Int? = null, showRetry: Boolean = false) {
        if (show) {
            recyclerView.isVisible = false
            placeholderGroup.isVisible = true
            messageRes?.let { placeholderText.setText(it) }

            when (messageRes) {
                R.string.nothing_found -> placeholderIcon.setImageResource(R.drawable.ic_no_search)
                else -> placeholderIcon.setImageResource(R.drawable.ic_no_network)
            }

            retryButton.isVisible = showRetry
        } else {
            placeholderGroup.isVisible = false
            recyclerView.isVisible = true
        }
    }

    private fun setupSearchField() {
        searchEditText.apply {
            hint = getString(R.string.search_button)
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    performSearch(text.toString())
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    clearButton.isVisible = !s.isNullOrEmpty()
                    currentSearchText = s?.toString() ?: ""
                    updateHistoryVisibility()
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            setOnFocusChangeListener { _, hasFocus ->
                updateHistoryVisibility()
            }
        }

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            currentSearchText = ""
            hideKeyboard()
            adapter.updateTracks(emptyList())
            showPlaceholder(false)
            updateHistoryVisibility()
        }
    }

    private fun checkNetworkState() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true
        adapter.setNetworkAvailable(isConnected)
        historyAdapter.setNetworkAvailable(isConnected)

        if (!isConnected) {
            showPlaceholder(true, R.string.network_error, true)
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT_KEY, currentSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentSearchText = savedInstanceState.getString(SAVED_TEXT_KEY, "")
        searchEditText.setText(currentSearchText)
    }

    override fun onResume() {
        super.onResume()
        checkNetworkState()
        updateHistoryVisibility()
    }
}