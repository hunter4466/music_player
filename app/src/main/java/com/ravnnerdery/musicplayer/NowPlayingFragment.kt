package com.ravnnerdery.musicplayer

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
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
        val trackPic = binding.findViewById<ImageView>(R.id.albumPic)
        val args = NowPlayingFragmentArgs.fromBundle(requireArguments())
        val imgUri = "android.resource://"+activity?.packageName+"/drawable/${args.name}"
        trackPic.setImageURI(Uri.parse(imgUri))
        name = args.name
        trackNameTitle.text = args.title
        playBtn = binding.findViewById(R.id.play_btn)
        playBtn.setOnClickListener{
            if ((activity as MainActivity).getIfPlaying(name)) {
                (activity as MainActivity).pauseSong(name)
            } else {
                (activity as MainActivity).playSong(name)
            }
        }
        if (!(activity as MainActivity).getIfPlaying(name)){
            (activity as MainActivity).stopCurrent()
            (activity as MainActivity).playSong(name)
        }
        return binding
    }
}
