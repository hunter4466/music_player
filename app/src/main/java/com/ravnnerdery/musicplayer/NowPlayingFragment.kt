package com.ravnnerdery.musicplayer

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class NowPlayingFragment : Fragment() {
    private lateinit var playBtn: ImageButton
    lateinit var name: String
    private var nowPlaying = false
    private lateinit var metaRetriever: MediaMetadataRetriever
    private lateinit var uriPath: String
    private lateinit var uri: Uri
    private lateinit var imgUri: String

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ------------- BINDINGS --------------------------
        var nowPlayingTrackName: String = "firstPlay"
        val binding = inflater.inflate(R.layout.fragment_now_playing, container, false)
        val trackNameTitle = binding.findViewById<TextView>(R.id.titleView)
        val trackPic = binding.findViewById<ImageView>(R.id.albumPic)
        val seekBar = binding.findViewById<SeekBar>(R.id.seekBar)
        val nextBtn = binding.findViewById<ImageButton>(R.id.nextTrackBtn)
        val previousBtn = binding.findViewById<ImageButton>(R.id.previousTrackBtn)
        val args = NowPlayingFragmentArgs.fromBundle(requireArguments())
        metaRetriever = MediaMetadataRetriever()
        playBtn = binding.findViewById(R.id.play_btn)
        name = args.name
        nowPlaying = true

        // ------------ META RETRIEVER ------------------
        fun setDisplayInfo(data: String){
            nowPlayingTrackName = data
            imgUri = "android.resource://"+activity?.packageName+"/drawable/${data}"
            uriPath = "android.resource://"+activity?.packageName+"/raw/${data}"
            uri = Uri.parse(uriPath)
            metaRetriever.setDataSource(context, uri)
            val titleName = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
            trackPic.setImageURI(Uri.parse(imgUri))
            trackNameTitle.text = titleName
            val mainExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
            fun runner(){
                mainExecutor.schedule({
                    /*(activity as MainActivity).getCurrentSongPosition()?.let { seekBar.setProgress(it, true) }*/
                    runner()
                }, 1, TimeUnit.SECONDS)
            }
            runner()
        }
        setDisplayInfo(name)
        // ------------ LISTENERS ----------------------
        playBtn.setOnClickListener{
            if ((activity as MainActivity).getIfPlaying(nowPlayingTrackName)) {
                (activity as MainActivity).pauseSong()
            } else {
                (activity as MainActivity).playSong(nowPlayingTrackName)
            }
        }
        nextBtn.setOnClickListener{
            val trackName = (activity as MainActivity).playNext()
            nowPlayingTrackName = trackName
            setDisplayInfo(nowPlayingTrackName)
        }
        previousBtn.setOnClickListener{
            val trackName = (activity as MainActivity).playPrevious()
            nowPlayingTrackName = trackName
            setDisplayInfo(nowPlayingTrackName)
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                (activity as MainActivity).playCurrentOn(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                println("onStartTracking")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                println("onStopTracking")
            }
        })
        // ------------ SEEK BAR --------------------------

        // ----------- ON CREATE VIEW MAIN EXECUTION --------
        if (!(activity as MainActivity).getIfPlaying(name)){
                println("execution: $name")
            (activity as MainActivity).stopCurrent()
            (activity as MainActivity).playSong(name)
        }
        seekBar.max = (activity as MainActivity).getCurrentSongDuration()!!

        return binding
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}

