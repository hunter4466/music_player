package com.ravnnerdery.musicplayer

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.findNavController

open class MusicListFragment : Fragment() {

    private lateinit var binding: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflate(R.layout.fragment_music_list, container, false)
        val allMusic = (activity as MainActivity).listAllMusic()
        val musicListContainer = binding.findViewById<View>(R.id.musicList) as LinearLayout
        for (elm in allMusic) {
            val metaRetriever = MediaMetadataRetriever()
            val uriPath = "android.resource://"+activity?.packageName+"/raw/${elm}"
            val uri = Uri.parse(uriPath)
            metaRetriever.setDataSource(context, uri)
            val titleName = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
            val newButton = Button(context)
            newButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            newButton.width = 1200
            newButton.text = titleName
            newButton.setOnClickListener{
                requireView().findNavController()
                    .navigate(MusicListFragmentDirections.actionMusicListFragmentToNowPlayingFragment(elm))
            }
            musicListContainer.addView(newButton)
        }
        return binding
    }


}