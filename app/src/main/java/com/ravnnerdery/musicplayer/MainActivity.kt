package com.ravnnerdery.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {
    private val musicCollection: MutableMap<String, MediaPlayer> = mutableMapOf()
    private var nowPlaying: String = "##firstplay##"
    val allMusic: Array<Field> = R.raw::class.java.fields
    override fun onCreate(savedInstanceState: Bundle?) {

        for (elm in allMusic) {
            val name = elm.name
            val song: Int = resources.getIdentifier(name,"raw", this.packageName)
            val mediaSound = MediaPlayer.create(this, song)
            musicCollection[name] = mediaSound
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
    fun playSong(name: String){
        musicCollection[name]?.start()
        nowPlaying = name
    }
    fun pauseSong(name: String){
        musicCollection[name]?.pause()
    }
    fun getIfPlaying(name: String): Boolean {
        return musicCollection[name]?.isPlaying == true
    }
    fun stopCurrent(){
        if (nowPlaying !== "##firstplay##") musicCollection[nowPlaying]?.stop()
    }
}