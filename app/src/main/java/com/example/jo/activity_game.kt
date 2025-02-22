
package com.example.jo

import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameActivity : AppCompatActivity() {
    private val buttons = Array(3) { arrayOfNulls<Button>(3) }
    private var playerXTurn = true
    private var roundCount = 0
    private var tvStatus: TextView? = null
    private var soundPool: SoundPool? = null
    private var soundId: Int = 0
    private var winSoundId: Int = 0
    private var soundEnabled = true
    private var musicEnabled = true
    private lateinit var btnSound: Button
    private lateinit var btnMusic: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvStatus = findViewById(R.id.tvStatus)
        btnSound = findViewById(R.id.btnSound)
        btnMusic = findViewById(R.id.btnMusic)

        // Initialize buttons
        for (i in 0..2) {
            for (j in 0..2) {
                val buttonID = "btn${i * 3 + j + 1}"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                buttons[i][j] = findViewById(resID)
                buttons[i][j]?.setOnClickListener { onCellClick(it) }
            }
        }

        // Initialize SoundPool
        soundPool = SoundPool.Builder().setMaxStreams(2).build()
        soundId = soundPool!!.load(this, R.raw.jowen, 1)
        winSoundId = soundPool!!.load(this, R.raw.win, 1)

        btnSound.setOnClickListener { onSoundClick() }
        btnMusic.setOnClickListener { onMusicClick() }

        MusicManager.getInstance(this).startMusic()
    }

    fun onCellClick(view: View) {
        val button = view as Button
        if (button.text.toString() != "") return

        if (soundEnabled && soundId != 0) {
            soundPool?.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
        }

        button.text = if (playerXTurn) "X" else "O"
        roundCount++

        if (checkForWin()) {
            tvStatus!!.text = if (playerXTurn) "Player X Wins!" else "Player O Wins!"
            disableButtons()
            if (soundEnabled && winSoundId != 0) {
                soundPool?.play(winSoundId, 1.0f, 1.0f, 0, 0, 1.0f)
            }
        } else if (roundCount == 9) {
            tvStatus!!.text = "Draw!"
        } else {
            playerXTurn = !playerXTurn
            updateStatus()

            // Trigger AI move if it's computer's turn
            if (!playerXTurn) {
                disableButtons()
                Handler(Looper.getMainLooper()).postDelayed({ makeAIMove() }, 1000)
            }
        }
    }

    private fun makeAIMove() {
        val emptyCells = mutableListOf<Button>()
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]?.let {
                    if (it.text.isEmpty()) emptyCells.add(it)
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val randomCell = emptyCells.random()
            randomCell.text = "O"
            roundCount++

            if (checkForWin()) {
                tvStatus!!.text = "Player O Wins!"
                disableButtons()
                if (soundEnabled && winSoundId != 0) {
                    soundPool?.play(winSoundId, 1.0f, 1.0f, 0, 0, 1.0f)
                }
            } else if (roundCount == 9) {
                tvStatus!!.text = "Draw!"
            } else {
                playerXTurn = true
                updateStatus()
                enableButtons()
            }
        }
    }


    private fun checkForWin(): Boolean {
        val field = Array(3) { arrayOfNulls<String>(3) }
        for (i in 0..2) {
            for (j in 0..2) {
                field[i][j] = buttons[i][j]?.text?.toString() ?: ""
            }
        }

        // Check rows and columns
        for (i in 0..2) {
            if (field[i][0] == field[i][1] && field[i][0] == field[i][2] && field[i][0] != "") return true
            if (field[0][i] == field[1][i] && field[0][i] == field[2][i] && field[0][i] != "") return true
        }

        // Check diagonals
        if (field[0][0] == field[1][1] && field[0][0] == field[2][2] && field[0][0] != "") return true
        if (field[0][2] == field[1][1] && field[0][2] == field[2][0] && field[0][2] != "") return true

        return false
    }

    private fun updateStatus() {
        tvStatus?.text = if (playerXTurn) "Player X's Turn" else "Player O's Turn"
    }

    private fun disableButtons() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]?.isEnabled = false
            }
        }
    }

    private fun enableButtons() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]?.isEnabled = true
            }
        }
    }

    private fun onSoundClick() {
        soundEnabled = !soundEnabled
        btnSound.text = if (soundEnabled) "Sound: ON" else "Sound: OFF"
    }

    private fun onMusicClick() {
        musicEnabled = !musicEnabled
        if (musicEnabled) {
            MusicManager.getInstance(this).startMusic()
            btnMusic.text = "Music: ON"
        } else {
            MusicManager.getInstance(this).stopMusic()
            btnMusic.text = "Music: OFF"
        }
    }
}