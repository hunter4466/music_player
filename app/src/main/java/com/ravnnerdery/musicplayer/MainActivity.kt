package com.ravnnerdery.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var play_btn: Button
    var playing: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        play_btn = findViewById(R.id.play_btn)
        var mediaSound = MediaPlayer.create(this, R.raw.backinblack)
        play_btn.setOnClickListener{
            if (playing) {
                mediaSound.pause()
                playing = false
            } else {
                mediaSound.start()
                playing = true
            }

        }



    }
}