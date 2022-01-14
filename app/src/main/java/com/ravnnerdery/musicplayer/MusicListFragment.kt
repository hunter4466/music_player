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
    private lateinit var authorName: String
    private lateinit var titleName: String
    private lateinit var metaRetriever: MediaMetadataRetriever
    private lateinit var uriPath: String
    private lateinit var uri: Uri
    private lateinit var binding: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflate(R.layout.fragment_music_list, container, false)
        val allMusic = (activity as MainActivity).listAllMusic()
        val musicListContainer = binding.findViewById<View>(R.id.musicList) as LinearLayout
        for (elm in allMusic) {
            val newButton = Button(context)
            metaRetriever = MediaMetadataRetriever()
            uriPath = "android.resource://"+activity?.packageName+"/raw/$elm"
            uri = Uri.parse(uriPath)
            metaRetriever.setDataSource(context, uri)
            authorName = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString()
            titleName = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
            newButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            newButton.text = elm
            newButton.setOnClickListener{
                requireView().findNavController()
                    .navigate(MusicListFragmentDirections.actionMusicListFragmentToNowPlayingFragment(elm))
            }
            musicListContainer.addView(newButton)
        }
        return binding
    }


}