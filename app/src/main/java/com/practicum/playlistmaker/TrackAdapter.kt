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

    // Флаг доступности сети (по умолчанию true - интернет есть)
    private var isNetworkAvailable: Boolean = true

    // Метод для обновления состояния сети
    fun setNetworkAvailable(available: Boolean) {
        if (isNetworkAvailable != available) {
            isNetworkAvailable = available
            notifyDataSetChanged() // Обновляем список
        }
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName = itemView.findViewById<TextView>(R.id.trackName)
        private val artistAndTime = itemView.findViewById<TextView>(R.id.artistAndTime)
        private val trackCover = itemView.findViewById<ImageView>(R.id.trackCover)
        private val agreementIcon = itemView.findViewById<ImageView>(R.id.agreementIcon)

        fun bind(track: Track) {
            // Устанавливаем текст
            trackName.text = track.trackName
            artistAndTime.text = "${track.artistName} • ${track.trackTime}"

            // Фиксированная иконка соглашения
            agreementIcon.setImageResource(R.drawable.ic_agreement)

            // Управление отображением обложки
            if (isNetworkAvailable) {
                // Загружаем обложку через Glide при наличии интернета
                Glide.with(itemView)
                    .load(track.artworkUrl100)
                    .placeholder(R.drawable.ic_placeholder)
                    .centerCrop()
                    .into(trackCover)
            } else {
                // Показываем только плейсхолдер при отсутствии интернета
                trackCover.setImageResource(R.drawable.ic_placeholder)
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