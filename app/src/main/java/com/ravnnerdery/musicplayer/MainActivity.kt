package com.ravnnerdery.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {
    private var nowPlayingIndexer: MutableList<Pair<String, MediaPlayer>> = mutableListOf()
    private val allMusic: Array<Field> = R.raw::class.java.fields
    private var currentTrack: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
       allMusic.forEachIndexed{ _, elm ->
            val name = elm.name
            val song: Int = resources.getIdentifier(name,"raw", this.packageName)
            val mediaSound = MediaPlayer.create(this, song)
            val pairing = Pair(name, mediaSound)
            nowPlayingIndexer.add(pairing)
                   }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun listAllMusic(): ArrayList<String> {
        val newArray: ArrayList<String> = arrayListOf()
        for(elm in allMusic) {
            newArray.add(elm.name)
        }
        return newArray
    }

    fun playSong(name: String){
        currentTrack = nowPlayingIndexer.find {
            it.first == name
        }?.second
        currentTrack?.start()
    }
    fun pauseSong(){
        currentTrack?.pause()
    }
    fun getIfPlaying(name: String): Boolean {
        return nowPlayingIndexer.find {
            it.first == name
        }?.second?.isPlaying == true
    }
    fun getCurrentSongDuration(name: String): Int? {
        val nowPlaying = nowPlayingIndexer.find {
            it.first == name
        }?.second
        return nowPlaying?.duration
    }
    fun getCurrentSong(): MediaPlayer? {
        return currentTrack
    }
    fun playCurrentOn(position: Int) {
        currentTrack?.seekTo(position)
    }
    fun playNext(): String {
        stopCurrent()
        val nowPlaying = nowPlayingIndexer.find {
            it.second == currentTrack
        }
        val currentPlayingIndex =  nowPlayingIndexer.indexOf(nowPlaying)
        return if (currentPlayingIndex == nowPlayingIndexer.size-1){
            playSong(nowPlayingIndexer[0].first)
            val seekBar: SeekBar = findViewById(R.id.seekBar)
            seekBar.progress = 0
            nowPlayingIndexer[0].first
        } else {
            playSong(nowPlayingIndexer[currentPlayingIndex + 1].first)
            val seekBar: SeekBar = findViewById(R.id.seekBar)
            seekBar.progress = 0
            nowPlayingIndexer[currentPlayingIndex + 1].first
        }


    }
    fun playPrevious(): String {
        stopCurrent()
        val nowPlaying = nowPlayingIndexer.find {
            it.second == currentTrack
        }
        val currentPlayingIndex =  nowPlayingIndexer.indexOf(nowPlaying)
        return if (currentPlayingIndex > 0){
            playSong(nowPlayingIndexer[currentPlayingIndex - 1].first)
            val seekBar: SeekBar = findViewById(R.id.seekBar)
            seekBar.progress = 0
            nowPlayingIndexer[currentPlayingIndex - 1].first
        } else {
            playSong(nowPlayingIndexer[nowPlayingIndexer.size-1].first)
            val seekBar: SeekBar = findViewById(R.id.seekBar)
            seekBar.progress = 0
            nowPlayingIndexer[nowPlayingIndexer.size-1].first
        }
    }
    fun stopCurrent(){
        currentTrack?.seekTo(0)
        currentTrack?.pause()
    }

}