package pollingapp.rassellworld.voicerecorderapp.player

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import androidx.media2.exoplayer.external.ExoPlayerFactory
import androidx.media2.exoplayer.external.Player
import androidx.media2.exoplayer.external.extractor.DefaultExtractorsFactory
import androidx.media2.exoplayer.external.source.ExtractorMediaSource
import androidx.media2.exoplayer.external.upstream.DefaultDataSourceFactory
import androidx.media2.exoplayer.external.util.Util


class PlayerViewModel(itemPath: String, application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?> get() = _player
    private var contentPosition = 0L
    private var playWhenReady = true
    var itemPath: String? = itemPath

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForegrounded() {
        setUpPlayer()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackgrounded() {
        releaseExoPlayer()
    }

    @SuppressLint("RestrictedApi")
    private fun setUpPlayer() {
        val dataSourceFactory = DefaultDataSourceFactory(
            getApplication(),
            Util.getUserAgent(getApplication(), "recorder")
        )

        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .setExtractorsFactory(DefaultExtractorsFactory())
            .createMediaSource(Uri.parse(itemPath))

        val player = ExoPlayerFactory.newSimpleInstance(getApplication())
        player.prepare(mediaSource)
        player.playWhenReady = playWhenReady
        player.seekTo(contentPosition)

        this._player.value = player
    }

    @SuppressLint("RestrictedApi")
    private fun releaseExoPlayer() {
        val player = _player.value ?: return
        this._player.value = null

        contentPosition = player.contentPosition
        playWhenReady = player.playWhenReady
        player.release()
    }

    override fun onCleared() {
        super.onCleared()
        releaseExoPlayer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }
}














