package com.capstone.psyheart.ui.home_detail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.psyheart.R
import com.capstone.psyheart.model.Songs
import com.capstone.psyheart.ui.discover_detail.DiscoverDetailActivity

class HomeDetailActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var playPauseButton: ImageButton
    private lateinit var imageView: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var titleSong: TextView
    private lateinit var artistName: TextView
    private lateinit var currentTime: TextView
    private lateinit var totalTime: TextView

    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                seekBar.progress = mediaPlayer.currentPosition
                currentTime.text = formatTime(mediaPlayer.currentPosition)
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupMediaPlayer()
    }

    private fun initializeViews() {
        playPauseButton = findViewById(R.id.playPauseButton)
        imageView = findViewById(R.id.imageView)
        seekBar = findViewById(R.id.seekBar)
        titleSong = findViewById(R.id.titleSong)
        artistName = findViewById(R.id.artistName)
        currentTime = findViewById(R.id.currentTime)
        totalTime = findViewById(R.id.totalTime)

        playPauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }
    }

    private fun setupMediaPlayer() {
        val song = intent.getParcelableExtra<Songs>(DiscoverDetailActivity.MUSIC_PLAYER) ?: return
//        val songs = intent.getParcelableExtra<List<Songs>>(DiscoverDetailActivity.MUSIC_PLAYER) ?: return
        titleSong.text = song.name
        artistName.text = song.artist

        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.url)
            prepare()
            setOnPreparedListener {
                seekBar.max = duration
                totalTime.text = formatTime(duration)
            }
            setOnCompletionListener {
                pauseMusic()
                mediaPlayer.seekTo(0)
                seekBar.progress = 0
                currentTime.text = formatTime(0)
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                    currentTime.text = formatTime(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun playMusic() {
        mediaPlayer.start()
        playPauseButton.setImageResource(R.drawable.ic_pause)
        startImageRotation()
        handler.post(updateSeekBarRunnable)
    }

    private fun pauseMusic() {
        mediaPlayer.pause()
        playPauseButton.setImageResource(R.drawable.ic_play)
        stopImageRotation()
        handler.removeCallbacks(updateSeekBarRunnable)
    }

    private fun startImageRotation() {
        val rotateAnimation = imageView.animate()
            .rotationBy(360f)
            .setDuration(10000)
            .setInterpolator(LinearInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (mediaPlayer.isPlaying) {
                        startImageRotation()
                    }
                }
            })
        rotateAnimation.start()
    }

    private fun stopImageRotation() {
        imageView.animate().cancel()
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        handler.removeCallbacks(updateSeekBarRunnable)
    }
}
