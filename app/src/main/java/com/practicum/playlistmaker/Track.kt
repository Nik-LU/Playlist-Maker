import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
) {
    // Добавляем обработку нулевого значения времени
    fun getFormattedTime(): String {
        return if (trackTimeMillis > 0) {
            SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(Date(trackTimeMillis))
        } else {
            ""
        }
    }
}