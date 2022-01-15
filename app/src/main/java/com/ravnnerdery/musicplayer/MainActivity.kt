package com.ravnnerdery.musicplayer

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import java.lang.reflect.Field
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var nowPlayingIndexer: MutableList<Pair<String, MediaPlayer>> = mutableListOf()
    private val allMusic: Array<Field> = R.raw::class.java.fields
    private var currentTrack: MediaPlayer? = null
    val mainExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
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
        println("locatorfinal $newArray")
        return newArray
    }
    @RequiresApi(Build.VERSION_CODES.P)

    fun playSong(name: String){

        currentTrack = nowPlayingIndexer.find {
            it.first == name
        }?.second
        currentTrack?.start()
        fun runner(){
            mainExecutor.schedule({
                var seekBar = {
                    
                }
                runner()
                println("name: $name")
            }, 1, TimeUnit.SECONDS)
        }
        runner()
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
    @RequiresApi(Build.VERSION_CODES.P)
    fun playNext(): String {
        stopCurrent()
        val nowPlaying = nowPlayingIndexer.find {
            it.second == currentTrack
        }
        val currentPlayingIndex =  nowPlayingIndexer.indexOf(nowPlaying)
        return if (currentPlayingIndex == nowPlayingIndexer.size-1){
            playSong(nowPlayingIndexer[0].first)
            nowPlayingIndexer[0].first
        } else {
            playSong(nowPlayingIndexer[currentPlayingIndex + 1].first)
            nowPlayingIndexer[currentPlayingIndex + 1].first
        }


    }
    @RequiresApi(Build.VERSION_CODES.P)
    fun playPrevious(): String {
        stopCurrent()
        val nowPlaying = nowPlayingIndexer.find {
            it.second == currentTrack
        }
        val currentPlayingIndex =  nowPlayingIndexer.indexOf(nowPlaying)
        println(currentPlayingIndex)
        println(nowPlayingIndexer.size)
        return if (currentPlayingIndex > 0){
            playSong(nowPlayingIndexer[currentPlayingIndex - 1].first)
            nowPlayingIndexer[currentPlayingIndex - 1].first
        } else {
            playSong(nowPlayingIndexer[nowPlayingIndexer.size-1].first)
            nowPlayingIndexer[nowPlayingIndexer.size-1].first
        }
    }
    fun stopCurrent(){
        println("stop called")
        currentTrack?.stop()
    }

}