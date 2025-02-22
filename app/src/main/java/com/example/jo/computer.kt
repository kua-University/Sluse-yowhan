package com.example.jo

import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivityAdvanced : AppCompatActivity() {

    private val buttons = Array(3) { arrayOfNulls<Button>(3) }
    private var playerXTurn = true
    private var roundCount = 0
    private lateinit var tvStatus: TextView
    private lateinit var btnSound: Button
    private lateinit var btnMusic: Button
    private lateinit var btnReset: Button

    private var soundPool: SoundPool? = null
    private var soundId = 0
    private var winSoundId = 0
    private var soundEnabled = true
    private var musicEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        for (i in 0..2) {
            for (j in 0..2) {
                val buttonID = "btn${i * 3 + j + 1}"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                buttons[i][j] = findViewById(resID)
                buttons[i][j]?.setOnClickListener { onCellClick(it) }
            }
        }

        soundPool = SoundPool.Builder().setMaxStreams(2).build()
        soundId = soundPool?.load(this, R.raw.jowen, 1) ?: 0
        winSoundId = soundPool?.load(this, R.raw.win, 1) ?: 0

        btnSound.setOnClickListener { toggleSound() }
        btnMusic.setOnClickListener { toggleMusic() }
        btnReset.setOnClickListener { resetGame() }
    }

    private fun onCellClick(view: View) {
        val button = view as Button
        if (button.text.isNotEmpty()) return

        if (soundEnabled && soundId != 0) {
            soundPool?.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
        }

        button.text = if (playerXTurn) "X" else "O"
        roundCount++

        if (checkForWin()) {
            tvStatus.text = if (playerXTurn) "Player X Wins!" else "Player O Wins!"
            disableButtons()
            if (soundEnabled) soundPool?.play(winSoundId, 1.0f, 1.0f, 0, 0, 1.0f)
        } else if (roundCount == 9) {
            tvStatus.text = "Draw!"
        } else {
            playerXTurn = !playerXTurn
            updateStatus()

            if (!playerXTurn) {
                disableButtons()
                Handler(Looper.getMainLooper()).postDelayed({ makeAIMove() }, 1000)
            }
        }
    }

    private fun makeAIMove() {
        for (row in buttons) {
            for (button in row) {
                if (button?.text.isNullOrEmpty()) {
                    button?.performClick()
                    return
                }
            }
        }
    }

    private fun checkForWin(): Boolean {
        val field = Array(3) { arrayOfNulls<String>(3) }
        for (i in 0..2) {
            for (j in 0..2) {
                field[i][j] = buttons[i][j]?.text?.toString()
            }
        }

        for (i in 0..2) {
            if (field[i][0] == field[i][1] && field[i][0] == field[i][2] && !field[i][0].isNullOrEmpty()) return true
            if (field[0][i] == field[1][i] && field[0][i] == field[2][i] && !field[0][i].isNullOrEmpty()) return true
        }

        return (field[0][0] == field[1][1] && field[0][0] == field[2][2] && !field[0][0].isNullOrEmpty()) ||
                (field[0][2] == field[1][1] && field[0][2] == field[2][0] && !field[0][2].isNullOrEmpty())
    }

    private fun updateStatus() {
        tvStatus.text = if (playerXTurn) "Player X's Turn" else "Player O's Turn"
    }

    private fun disableButtons() {
        for (row in buttons) {
            for (button in row) {
                button?.isEnabled = false
            }
        }
    }

    private fun resetGame() {
        for (row in buttons) {
            for (button in row) {
                button?.text = ""
                button?.isEnabled = true
            }
        }
        roundCount = 0
        playerXTurn = true
        updateStatus()
    }

    private fun toggleSound() {
        soundEnabled = !soundEnabled
    }

    private fun toggleMusic() {
        musicEnabled = !musicEnabled
    }
}
