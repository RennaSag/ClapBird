package com.example.relogio.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameEngine(context: Context, surfaceView: SurfaceView) : SurfaceHolder.Callback {

    private val holder: SurfaceHolder = surfaceView.holder
    val bird: Bird = Bird()
    private val obstacles: MutableList<Obstacle> = mutableListOf()
    private val paint: Paint = Paint()
    private var score: Int = 0
    private var gameRunning: Boolean = false
    private var gameStarted: Boolean = false // Estado para controlar se o jogo começou
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    init {
        holder.addCallback(this)
        paint.color = Color.WHITE
        paint.textSize = 50f
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // Obter as dimensões da tela
        val canvas = holder.lockCanvas()
        screenWidth = canvas.width
        screenHeight = canvas.height
        holder.unlockCanvasAndPost(canvas)

        // Posicionar o pássaro no centro da tela
        bird.x = screenWidth / 4
        bird.y = screenHeight / 4
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopGame()
    }

    private fun startGame() {
        gameRunning = true
        gameThread.start()
    }

    private fun stopGame() {
        gameRunning = false
        try {
            gameThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private val gameThread = Thread {
        while (gameRunning) {
            update()
            val canvas = holder.lockCanvas()
            if (canvas != null) {
                try {
                    draw(canvas)
                } finally {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
            Thread.sleep(16) // ~60 FPS
        }
    }

    private fun update() {
        if (gameStarted) { // Só atualizar o jogo se ele já tiver começado
            bird.update()
            obstacles.forEach { it.update() }

            // Adicionar novos obstáculos
            if (obstacles.isEmpty() || obstacles.last().x < 500) {
                obstacles.add(Obstacle(1000, (200..600).random()))
            }

            // Verificar se o pássaro ultrapassou um obstáculo
            obstacles.forEach { obstacle ->
                if (obstacle.x + obstacle.width < bird.x && !obstacle.passed) {
                    obstacle.passed = true
                    score++
                }
            }

            // Remover obstáculos que saíram da tela
            obstacles.removeAll { it.x + it.width < 0 }
        }
    }


    private fun draw(canvas: Canvas) {
        // Limpar a tela com fundo preto
        canvas.drawColor(Color.BLACK)

        // Desenhar o pássaro
        canvas.drawCircle(bird.x.toFloat(), bird.y.toFloat(), 10f, paint)

        // Desenhar os obstáculos
        obstacles.forEach { it.draw(canvas, paint) }

        // Desenhar a pontuação
        paint.color = Color.WHITE // Cor branca para a pontuação
        paint.textAlign = Paint.Align.LEFT // Alinhar o texto à esquerda
        canvas.drawText("Pontos: $score", 50f, 90f, paint)

        // Mostrar mensagem de "Clique para começar" se o jogo não tiver começado
        if (!gameStarted) {
            paint.color = Color.YELLOW // Cor amarela para o texto
            paint.textAlign = Paint.Align.CENTER // Alinhar o texto ao centro
            val text = "Clique para começar"
            val x = screenWidth / 2f // Posição X no centro da tela
            val y = screenHeight / 2f // Posição Y no centro da tela
            canvas.drawText(text, x, y, paint)
        }
    }

    // Método para fazer o pássaro bater as asas
    fun flapBird() {
        if (!gameStarted) {
            gameStarted = true // Iniciar o jogo no primeiro clique
            startGame()
        }
        bird.flap()
    }
}