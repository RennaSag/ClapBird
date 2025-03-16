package com.example.relogio.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.relogio.R

class GameEngine(context: Context, surfaceView: SurfaceView) : SurfaceHolder.Callback {

    private lateinit var birdBitmap: Bitmap
    private val matrix = Matrix()  // Para rotacionar o pássaro

    private val holder: SurfaceHolder = surfaceView.holder
    val bird: Bird = Bird()
    private val obstacles: MutableList<Obstacle> = mutableListOf()
    private val paint: Paint = Paint()
    private var score: Int = 0
    private var gameRunning: Boolean = false
    private var gameStarted: Boolean = false
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var gameOver: Boolean = false

    init {
        holder.addCallback(this)
        paint.color = Color.WHITE
        paint.textSize = 30f  // Tamanho reduzido para relógio

        // Carregar a imagem do pássaro
        try {
            birdBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bird)
            // Redimensionar o bitmap para tamanho adequado à tela do relógio
            birdBitmap = Bitmap.createScaledBitmap(birdBitmap, 40, 40, true)
            Log.d("GameEngine", "Bird bitmap loaded successfully")
        } catch (e: Exception) {
            Log.e("GameEngine", "Error loading bird bitmap: ${e.message}")
            // Caso haja erro, criar um bitmap vazio
            birdBitmap = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // Obter as dimensões da tela
        val canvas = holder.lockCanvas()
        screenWidth = canvas.width
        screenHeight = canvas.height
        holder.unlockCanvasAndPost(canvas)

        Log.d("GameEngine", "Surface created: width=$screenWidth, height=$screenHeight")

        // Posicionar o pássaro
        bird.x = screenWidth / 4
        bird.y = screenHeight / 4

        // Iniciar o thread de renderização imediatamente
        gameRunning = true
        gameThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("GameEngine", "Surface changed: width=$width, height=$height")
        screenWidth = width
        screenHeight = height
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("GameEngine", "Surface destroyed")
        stopGame()
    }

    private fun startGame() {
        gameStarted = true
        gameOver = false
        score = 0
        obstacles.clear()
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
        if (gameStarted && !gameOver) {
            bird.update()
            // Limitar o pássaro à tela
            bird.constrain(0, screenHeight - birdBitmap.height)

            obstacles.forEach { it.update() }

            // Adicionar novos obstáculos
            if (obstacles.isEmpty() || obstacles.last().x < screenWidth - 300) {
                val minGap = (screenHeight * 0.2).toInt()
                val maxGap = (screenHeight * 0.6).toInt()
                obstacles.add(Obstacle(screenWidth, (minGap..maxGap).random()))
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

            // Verificar colisões
            checkCollisions()
        }
    }

    private fun checkCollisions() {
        // Verificar colisão com o chão ou teto
        if (bird.y <= 0 || bird.y >= screenHeight - birdBitmap.height) {
            gameOver = true
        }

        // Verificar colisão com obstáculos
        for (obstacle in obstacles) {
            if (obstacle.checkCollision(bird.x + birdBitmap.width/2, bird.y + birdBitmap.height/2, birdBitmap.width)) {
                gameOver = true
                break
            }
        }
    }

    private fun draw(canvas: Canvas) {
        // Limpar a tela com fundo preto
        canvas.drawColor(Color.BLACK)

        // Desenhar os obstáculos
        obstacles.forEach { it.draw(canvas, paint) }

        // Desenhar o pássaro com rotação
        matrix.reset()
        matrix.postTranslate(bird.x.toFloat(), bird.y.toFloat())
        matrix.postRotate(bird.rotation, bird.x.toFloat() + birdBitmap.width/2,
            bird.y.toFloat() + birdBitmap.height/2)
        canvas.drawBitmap(birdBitmap, matrix, paint)

        // Desenhar a pontuação
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 30f
        canvas.drawText("Pontos: $score", 20f, 40f, paint)

        // Mostrar mensagem de "Toque para começar" se o jogo não tiver começado
        if (!gameStarted) {
            // Desenhar um retângulo semitransparente para destacar o texto
            paint.color = Color.argb(128, 0, 0, 0)
            canvas.drawRect(0f, screenHeight/2 - 60f, screenWidth.toFloat(), screenHeight/2 + 30f, paint)

            paint.color = Color.YELLOW
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 25f
            canvas.drawText("Toque para iniciar", screenWidth / 2f, screenHeight / 2f, paint)
        }

        // Mostrar mensagem de "Game Over" se o jogo acabou
        if (gameOver) {
            paint.color = Color.argb(128, 0, 0, 0)
            canvas.drawRect(0f, screenHeight/2 - 60f, screenWidth.toFloat(), screenHeight/2 + 60f, paint)

            paint.color = Color.RED
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 30f
            canvas.drawText("Game Over", screenWidth / 2f, screenHeight / 2f - 20f, paint)

            paint.color = Color.WHITE
            paint.textSize = 20f
            canvas.drawText("Toque para reiniciar", screenWidth / 2f, screenHeight / 2f + 20f, paint)
        }
    }

    // Método para fazer o pássaro bater as asas
    fun flapBird() {
        if (gameOver) {
            // Reiniciar o jogo
            resetGame()
        } else if (!gameStarted) {
            // Iniciar o jogo
            startGame()
        }

        // Fazer o pássaro pular
        bird.flap()
    }

    private fun resetGame() {
        bird.y = screenHeight / 4
        bird.velocity = 0
        obstacles.clear()
        score = 0
        gameOver = false
        gameStarted = true
    }

    // Extensão para gerar números aleatórios dentro de um range
    private fun IntRange.random(): Int {
        return kotlin.random.Random.nextInt(this.first, this.last)
    }
}