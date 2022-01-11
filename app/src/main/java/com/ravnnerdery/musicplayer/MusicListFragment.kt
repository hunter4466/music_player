package com.ravnnerdery.musicplayer

import android.content.res.AssetManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MusicListFragment : Fragment() {
    companion object {
        private const val MUSIC_FOLDER = "music/"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val allMusic = context?.assets?.list(MUSIC_FOLDER)
        if (allMusic != null) {
            for (elm in allMusic) {
                println(elm.length)
                println(elm.indices)
            }
        }
        return inflater.inflate(R.layout.fragment_music_list, container, false)
    }


}