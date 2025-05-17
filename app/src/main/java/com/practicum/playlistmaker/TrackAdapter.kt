package com.practicum.playlistmaker

import Track
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import android.view.LayoutInflater
import android.view.View

class TrackAdapter(
    private var tracks: List<Track>,
    private val onItemClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private var isNetworkAvailable: Boolean = true

    fun setNetworkAvailable(available: Boolean) {
        if (isNetworkAvailable != available) {
            isNetworkAvailable = available
            notifyDataSetChanged()
        }
    }

    fun updateTracks(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val artistName: TextView = itemView.findViewById(R.id.artistName)
        private val timeSeparator: TextView = itemView.findViewById(R.id.timeSeparator)
        private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
        private val trackCover: ImageView = itemView.findViewById(R.id.trackCover)
        private val agreementIcon: ImageView = itemView.findViewById(R.id.agreementIcon)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            timeSeparator.text = "•"
            trackTime.text = track.getFormattedTime()

            agreementIcon.setImageResource(R.drawable.ic_agreement)

            if (isNetworkAvailable) {
                Glide.with(itemView)
                    .load(track.artworkUrl100)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_corner_radius)))
                    .into(trackCover)
            } else {
                trackCover.setImageResource(R.drawable.ic_placeholder)
            }

            itemView.setOnClickListener {
                onItemClick(track) // Передаём трек в историю при клике
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int = tracks.size
}