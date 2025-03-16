package com.example.relogio.presentation

class Bird {
    var x: Int = 0  // Será sobrescrito no surfaceCreated
    var y: Int = 0  // Será sobrescrito no surfaceCreated
    var velocity: Int = 0
    var rotation: Float = 0f  // Adiciona rotação para animação do pássaro

    fun update() {
        velocity += GRAVITY
        y += velocity

        // Calcular a rotação com base na velocidade
        rotation = when {
            velocity < 0 -> -20f  // Apontando para cima quando subindo
            velocity > 10 -> 70f  // Apontando para baixo quando caindo rápido
            else -> velocity * 5f  // Rotação suave entre subida e descida
        }
    }

    fun flap() {
        velocity = -FLAP_STRENGTH
    }

    // Método para limitar a posição do pássaro à tela
    fun constrain(minY: Int, maxY: Int) {
        if (y < minY) {
            y = minY
            velocity = 0
        }
        if (y > maxY) {
            y = maxY
            velocity = 0
        }
    }

    companion object {
        const val GRAVITY = 1
        const val FLAP_STRENGTH = 12  // Reduzido para controle mais preciso em tela pequena
    }
}