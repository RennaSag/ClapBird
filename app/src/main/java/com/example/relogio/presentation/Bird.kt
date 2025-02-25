package com.example.relogio.presentation

class Bird {
    var x: Int = 100 // Será sobrescrito no surfaceCreated
    var y: Int = 300 // Será sobrescrito no surfaceCreated
    var velocity: Int = 0

    fun update() {
        velocity += GRAVITY
        y += velocity

        // Limitar a posição do pássaro
        if (y < 0) y = 0
        if (y > 1000) y = 1000
    }

    fun flap() {
        velocity = -FLAP_STRENGTH
    }

    companion object {
        const val GRAVITY = 1
        const val FLAP_STRENGTH = 15
    }
}