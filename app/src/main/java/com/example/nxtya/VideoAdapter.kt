package com.example.nxtya

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nxtya.databinding.ListVideoBinding
import com.example.nxtya.profil.ProfilActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class VideoAdapter(
    var context: Context,
    var videos: ArrayList<Video>,
    var videoPreparedListener: OnVideoPreparedListener,
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(
        val binding: ListVideoBinding,
        var context: Context,
        var videoPreparedListener: OnVideoPreparedListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var exoPlayer: ExoPlayer
        private lateinit var mediaSource: MediaSource

        fun setVideoPath(url: String) {

            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(context, "Can't play this video", Toast.LENGTH_SHORT).show()
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState == Player.STATE_BUFFERING) {
                        binding.pbLoading.visibility = View.VISIBLE
                    } else if (playbackState == Player.STATE_READY) {
                        binding.pbLoading.visibility = View.GONE
                    }
                }
            })

            binding.playerView.player = exoPlayer

            exoPlayer.seekTo(0)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

            val dataSourceFactory = DefaultDataSource.Factory(context)

            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))

            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()

            if (absoluteAdapterPosition == 0) {
                exoPlayer.playWhenReady = true
                exoPlayer.play()
            }

            videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoPlayer, absoluteAdapterPosition))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = ListVideoBinding.inflate(LayoutInflater.from(context), parent, false)
        return VideoViewHolder(view, context, videoPreparedListener)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val model = videos[position]

        var fav: ImageView
        var speaker: ImageView
        var profil: ImageView
        var more: ImageView
        var isFav = false
        var isAudioMuted = false
        holder.binding.tvTitle.text = model.title
        holder.binding.description.text = model.descri
        holder.setVideoPath(model.url)
        fav = holder.binding.favorites
        speaker = holder.binding.ivSpeaker
        profil = holder.binding.profil
        more = holder.binding.more
        more.setOnClickListener {
            //more button
        }


        profil.setOnClickListener {
         // share button
            val intent = Intent(it.context, ProfilActivity::class.java)
            it.context.startActivity(intent)


        }
        speaker.setOnClickListener {
            isAudioMuted = !isAudioMuted
            var player = holder.binding.playerView.player!!
            player.volume = if (isAudioMuted) 0f else 1f
            if (isAudioMuted) {
                holder.binding.ivSpeaker.setImageResource(R.drawable.speaker_muted)
            } else {
                holder.binding.ivSpeaker.setImageResource(R.drawable.speaker_normal)
            }


        }

        fav.setOnClickListener()
        {
            if (!isFav) {
                fav.setImageResource(R.drawable.ic_fill_favorite)
                isFav = true
            } else {
                fav.setImageResource(R.drawable.ic_favorite)
                isFav = false
            }
        }



        holder.itemView.setOnClickListener {

            if (holder.binding.playerView.player!!.isPlaying) {

                holder.binding.playerView.player!!.pause()
                holder.binding.ivPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)

            } else {
                Log.i("test", "play")
                holder.binding.playerView.player!!.play()

            }

            holder.binding.ivPlay.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                holder.binding.ivPlay.visibility = View.GONE
            }, 100)

        }


    }

    override fun getItemCount(): Int {
        return videos.size
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }
}