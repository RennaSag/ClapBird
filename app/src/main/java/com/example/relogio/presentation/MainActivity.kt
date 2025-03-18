package com.example.relogio.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MotionEvent
import android.view.SurfaceView
import com.example.relogio.R

class MainActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa a SurfaceView
        val gameView = findViewById<SurfaceView>(R.id.gameView)

        // Inicializa o GameEngine
        gameEngine = GameEngine(this, gameView)

        // Configura o OnTouchListener para a SurfaceView
        gameView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    gameEngine.flapBird() // Certifica que o jogo inicia ao tocar
                    gameEngine.startFlap()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    gameEngine.stopFlap()
                    true
                }
                else -> false
            }
        }
    }
}