import com.practicum.playlistmaker.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApi {
    // Базовый URL: https://itunes.apple.com
    // Параметр term - поисковый запрос
    @GET("/search?entity=song")
    fun searchTracks(@Query("term") query: String): Call<TracksResponse>
}