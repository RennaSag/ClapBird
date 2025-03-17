package com.example.relogio.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import com.example.relogio.R

class Obstacle(
    var x: Int,
    var gapY: Int,
    context: Context  // Adicionar context como parâmetro
) {
    val width: Int = 70
    val gapHeight: Int = 160    //espaço entre os dois canos, mais apertado ou mais largo
    var passed: Boolean = false

    // Adicionar os bitmaps para os canos
    private val pipeBitmap: Bitmap
    private val pipeTopBitmap: Bitmap  // Bitmap para o topo do cano
    private val pipeBottomBitmap: Bitmap  // Bitmap para o cano inferior
    private val matrix = Matrix()

    init {
        // Carregar a imagem do cano
        val originalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.cano)

        // Redimensionar para a largura desejada, preservando proporção
        val scale = width.toFloat() / originalBitmap.width
        val newHeight = (originalBitmap.height * scale).toInt()

        // Bitmap para o cano normal
        pipeBitmap = Bitmap.createScaledBitmap(originalBitmap, width, newHeight, true)

        // Criar as versões dos canos superior e inferior
        // Para o cano superior, invertemos a imagem verticalmente
        matrix.setScale(1f, -1f)
        pipeTopBitmap = Bitmap.createBitmap(pipeBitmap, 0, 0,
            pipeBitmap.width, pipeBitmap.height, matrix, true)

        // O cano inferior permanece normal
        pipeBottomBitmap = pipeBitmap
    }

    fun update() {
        x -= 5
    }

    fun draw(canvas: Canvas, paint: Paint) {
        // Desenhar o cano superior (invertido)
        val topPipeBottom = gapY
        canvas.drawBitmap(pipeTopBitmap, x.toFloat(),
            (gapY - pipeTopBitmap.height).toFloat(), paint)

        // Desenhar o cano inferior
        val bottomPipeTop = gapY + gapHeight
        canvas.drawBitmap(pipeBottomBitmap, x.toFloat(),
            (gapY + gapHeight).toFloat(), paint)
    }

    // Método de colisão superioir e  inferior
    fun checkCollision(birdX: Float, birdY: Float, birdSize: Int): Boolean {
        val birdRadius = birdSize / 2

        // Colisão com o cano superior
        if (birdX + birdRadius > x && birdX - birdRadius < x + width &&
            birdY - birdRadius < gapY) {
            return true
        }

        // Colisão com o cano inferior
        if (birdX + birdRadius > x && birdX - birdRadius < x + width &&
            birdY + birdRadius > gapY + gapHeight) {
            return true
        }

        return false
    }
}