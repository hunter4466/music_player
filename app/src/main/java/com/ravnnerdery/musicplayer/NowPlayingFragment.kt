package com.ravnnerdery.musicplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView


class NowPlayingFragment : Fragment() {
    private lateinit var playBtn: ImageButton
    lateinit var name: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_now_playing, container, false)
        val trackNameTitle = binding.findViewById<TextView>(R.id.titleView)
        var args = NowPlayingFragmentArgs.fromBundle(requireArguments())
        name = args.name
        trackNameTitle.text = name
        playBtn = binding.findViewById(R.id.play_btn)
        if (!(activity as MainActivity).getIfPlaying(name)){
            (activity as MainActivity).stopCurrent()
            (activity as MainActivity).playSong(name)
        }
        return binding
    }
}