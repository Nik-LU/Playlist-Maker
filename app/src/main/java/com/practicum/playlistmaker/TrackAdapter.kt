import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Track

class TrackAdapter(private val tracks: List<Track>) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName = itemView.findViewById<TextView>(R.id.trackName)
        private val artistAndTime = itemView.findViewById<TextView>(R.id.artistAndTime)
        private val trackCover = itemView.findViewById<ImageView>(R.id.trackCover)
        private val agreementIcon = itemView.findViewById<ImageView>(R.id.agreementIcon)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistAndTime.text = "${track.artistName} • ${track.trackTime}"

            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(trackCover)

            // Установка иконки соглашения
            agreementIcon.setImageResource(R.drawable.ic_agreement)
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

    override fun getItemCount() = tracks.size
}