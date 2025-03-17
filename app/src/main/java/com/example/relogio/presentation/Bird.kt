package com.example.relogio.presentation

class Bird {
    var x: Float = 0f  // Será sobrescrito no surfaceCreated
    var y: Float = 0f  // Será sobrescrito no surfaceCreated
    var velocity: Float = 0f
    var rotation: Float = 0f  // Adiciona rotação para animação do pássaro

    fun update() {
        velocity += GRAVITY  // Usando FLOAT para gravidade
        y += velocity  // Atualiza a posição do pássaro com base na velocidade

        // Calcular a rotação com base na velocidade
        rotation = when {
            velocity < 0 -> -10f  // Apontando para cima quando subindo
            velocity > 10 -> 40f  // Apontando para baixo quando caindo rápido
            else -> velocity * 5f  // Rotação suave entre subida e descida
        }
    }

    fun flap() {
        velocity = -FLAP_STRENGTH  // A força do flap é negativa para subir
    }

    // Método para limitar a posição do pássaro à tela
    fun constrain(minY: Int, maxY: Int) {
        if (y < minY) {
            y = minY.toFloat()
            velocity = 0f  // Reseta a velocidade quando chega ao limite inferior
        }
        if (y > maxY) {
            y = maxY.toFloat()
            velocity = 0f  // Reseta a velocidade quando chega ao limite superior
        }
    }

    companion object {
        const val GRAVITY: Float = 0.7f  // Gravidade ajustada para Float
        const val FLAP_STRENGTH: Float = 9f  // Força do flap ajustada para Float
    }
}
