package com.example.jo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ensure this layout file exists

        // Ensure the TextView exists in the layout before accessing it
        val tvStatus = findViewById<TextView?>(R.id.tvStatus)

        // Check if tvStatus is not null before using it
        tvStatus?.let {
            // Retrieve the game mode from the intent
            val isSinglePlayer = intent.getBooleanExtra("isSinglePlayer", true)

            if (isSinglePlayer) {
                // Start the game in "Vs Computer" mode
                it.text = "Vs Computer Mode - Player X's Turn"
            } else {
                // Start the game in "Two Players" mode
                it.text = "Two Players Mode - Player X's Turn"
            }
        }
    }
}
