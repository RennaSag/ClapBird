package com.example.relogio.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.SurfaceView
import com.example.relogio.R

class MainActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gameView = findViewById<SurfaceView>(R.id.gameView)
        gameEngine = GameEngine(this, gameView)

        gameView.setOnClickListener {
            gameEngine.flapBird()
        }
    }
}