package com.example.relogio.presentation

import android.graphics.Canvas
import android.graphics.Paint

class Obstacle(var x: Int, var gapY: Int) {
    val width: Int = 70  // Reduzido para melhor visualização em telas pequenas
    val gapHeight: Int = 200  // Reduzido para aumentar a dificuldade
    var passed: Boolean = false

    fun update() {
        x -= 5  // Velocidade aumentada para melhor fluidez em telas pequenas
    }

    fun draw(canvas: Canvas, paint: Paint) {
        // Salvar a cor original da paint
        val originalColor = paint.color

        // Definir a cor para verde para os obstáculos
        paint.color = android.graphics.Color.GREEN

        // Desenhar o cano superior
        canvas.drawRect(x.toFloat(), 0f, (x + width).toFloat(), gapY.toFloat(), paint)

        // Desenhar o cano inferior
        canvas.drawRect(
            x.toFloat(),
            (gapY + gapHeight).toFloat(),
            (x + width).toFloat(),
            canvas.height.toFloat(),
            paint
        )

        // Restaurar a cor original da paint
        paint.color = originalColor
    }

    // Verifica se o pássaro colidiu com este obstáculo
    fun checkCollision(birdX: Int, birdY: Int, birdSize: Int): Boolean {
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