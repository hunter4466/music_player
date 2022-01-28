package com.ravnnerdery.musicplayer

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.akaita.android.circularseekbar.CircularSeekBar

class NowPlayingFragment : Fragment() {
    private lateinit var playBtn: ImageButton
    lateinit var name: String
    private var nowPlaying = true
    private lateinit var metaRetriever: MediaMetadataRetriever
    private lateinit var uriPath: String
    private lateinit var uri: Uri
    private lateinit var imgUri: String
    private lateinit var runnable: Runnable
    // TODO: handler is deprecated
    private var handler = Handler()

    //TODO: why build versions code N?
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ------------- BINDINGS --------------------------
        var nowPlayingTrackName = "firstPlay" //TODO: please avoid to use hadcode strings
        val binding = inflater.inflate(R.layout.fragment_now_playing, container, false)
        val trackNameTitle = binding.findViewById<TextView>(R.id.titleView)
        val trackAlbum = binding.findViewById<TextView>(R.id.trackAlbum)
        val trackPic = binding.findViewById<ImageView>(R.id.albumPic)
        val seekBar = binding.findViewById<SeekBar>(R.id.seekBar)
        val nextBtn = binding.findViewById<ImageButton>(R.id.nextTrackBtn)
        val previousBtn = binding.findViewById<ImageButton>(R.id.previousTrackBtn)
        val args = NowPlayingFragmentArgs.fromBundle(requireArguments())
        metaRetriever = MediaMetadataRetriever()
        playBtn = binding.findViewById(R.id.play_btn)
        name = args.name
        nowPlaying = true
        if (!(activity as MainActivity).getIfPlaying(name)) {
            (activity as MainActivity).stopCurrent()
            (activity as MainActivity).playSong(name)
        }
        seekBar.max = (activity as MainActivity).getCurrentSongDuration(name)!!

        // ------------ META RETRIEVER ------------------
        fun setDisplayInfo(data: String) {
            nowPlayingTrackName = data
            imgUri = "android.resource://" + activity?.packageName + "/drawable/${data}"
            uriPath = "android.resource://" + activity?.packageName + "/raw/${data}"
            uri = Uri.parse(uriPath)
            metaRetriever.setDataSource(context, uri)
            val titleName = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
            val albumName = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString()
            trackPic.setImageURI(Uri.parse(imgUri))
            trackNameTitle.text = titleName
            trackAlbum.text = albumName
        }
        setDisplayInfo(name)
        // ------------ LISTENERS ----------------------
        fun playNextTrack(direction: String) {
            when (direction) {
                // todo: those could be constants
                "next" -> {
                    val trackName = (activity as MainActivity).playNext()
                    nowPlayingTrackName = trackName
                    setDisplayInfo(nowPlayingTrackName)
                }
                "previous" -> {
                    val trackName = (activity as MainActivity).playPrevious()
                    nowPlayingTrackName = trackName
                    setDisplayInfo(nowPlayingTrackName)
                }
            }
        }
        playBtn.setOnClickListener {
            if ((activity as MainActivity).getIfPlaying(nowPlayingTrackName)) {
                (activity as MainActivity).pauseSong()
            } else {
                (activity as MainActivity).playSong(nowPlayingTrackName)
            }
        }
        nextBtn.setOnClickListener {
            // todo: the string next could a constant or a enum
            playNextTrack("next")
        }
        previousBtn.setOnClickListener {
            // todo: the string previous could a constant or a enum
            playNextTrack("previous")
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    (activity as MainActivity).playCurrentOn(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                println("onStartTracking")
                // todo: Not use println for debug
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                println("onStopTracking")
                // todo: Not use println for debug
            }
        })
        (activity as MainActivity).getCurrentSong()?.setOnCompletionListener {
            playNextTrack("next")
        }
        // ------------ VOLUME BAR --------------------------
        val volumeBar = binding.findViewById<CircularSeekBar>(R.id.radialSeekBar)
        volumeBar.setOnCircularSeekBarChangeListener(object : CircularSeekBar.OnCircularSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: CircularSeekBar?,
                progress: Float,
                fromUser: Boolean
            ) {
                (activity as MainActivity).getCurrentSong()?.setVolume(progress, progress)
            }

            override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {
                println("touching")
                // todo: Not use println for debug
            }

            override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
                println("touching")
                // todo: Not use println for debug
            }

        })
        // ------------------
        fun runSeekBar() {
            if (nowPlaying) {
                runnable = Runnable {
                    val seekBar: SeekBar = binding.findViewById(R.id.seekBar)
                    val currentTrack = (activity as MainActivity).getCurrentSong()
                    seekBar.max = currentTrack?.duration!!
                    seekBar.progress = currentTrack.currentPosition
                    runSeekBar()
                }
                handler.postDelayed(runnable, 1000)
            }
        }
        runSeekBar()

        return binding
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}

