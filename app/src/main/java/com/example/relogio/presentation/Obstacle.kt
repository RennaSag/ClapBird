package com.example.relogio.presentation

import android.graphics.Canvas
import android.graphics.Paint

class Obstacle(var x: Int, var gapY: Int) {
    val width: Int = 100
    val gapHeight: Int = 300
    var passed: Boolean = false

    fun update() {
        x -= 3 // Velocidade dos obstáculos (ajuste conforme necessário)sdfsf
    }

    fun draw(canvas: Canvas, paint: Paint) {
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
    }
}