package com.ravnnerdery.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private lateinit var playBtn: ImageButton
    var playing: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playBtn = findViewById(R.id.play_btn)
        var mediaSound = MediaPlayer.create(this, R.raw.backinblack)
        playBtn.setOnClickListener{
            playing = if (playing) {
                mediaSound.pause()
                false
            } else {
                mediaSound.start()
                true
            }
        }
    }
}