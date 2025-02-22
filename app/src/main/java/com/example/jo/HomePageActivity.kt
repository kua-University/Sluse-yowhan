package com.example.jo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomePageActivity  : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)


        val multiplay = findViewById<Button>(R.id.multiPlay)

        val settingButton = findViewById<Button>( R.id.settingButton)


        settingButton.setOnClickListener {

            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)

        }
        val settingButton2 = findViewById<Button>( R.id.multiPlay)

        settingButton2.setOnClickListener {

            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)

        }

    }
}