package com.example.actividad17
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var textoVictoria: TextView
    private lateinit var botones: List<Button>

    private val tablero = IntArray(9) { 0 }
    private var estado = 0
    private var fichasPuestas = 0
    private var turno = 1
    private var posGanadora = intArrayOf(-1, -1, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn: Button = findViewById(R.id.button)
        btn.setOnClickListener {
            val intent = Intent(this, MainActivity:: class.java)
            startActivity(intent)
        }

        textoVictoria = findViewById(R.id.textoVictoria)
        textoVictoria.visibility = View.INVISIBLE


        botones = listOf(
            findViewById(R.id.b1),
            findViewById(R.id.b2),
            findViewById(R.id.b3),
            findViewById(R.id.b4),
            findViewById(R.id.b5),
            findViewById(R.id.b6),
            findViewById(R.id.b7),
            findViewById(R.id.b8),
            findViewById(R.id.b9)
        )


        botones.forEachIndexed { index, button ->
            button.setOnClickListener { ponerFicha(index, button) }
        }
    }

    private fun ponerFicha(index: Int, button: Button) {
        if (estado == 0 && tablero[index] == 0) {
            tablero[index] = 1
            button.setBackgroundResource(R.drawable.equis)
            fichasPuestas++
            estado = comprobarEstado()
            terminarPartida()

            if (estado == 0) {

                turno = -1
                ia()
                fichasPuestas++
                estado = comprobarEstado()
                terminarPartida()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun terminarPartida() {
        if (estado == 1 || estado == -1) {
            val fichaVictoria = if (estado == 1) R.drawable.equis_verde else R.drawable.circulo_verde

            textoVictoria.setBackgroundColor(Color.BLACK)

            textoVictoria.text = if (estado == 1) {
                "¡Has ganado!"
            } else {
                "Has perdido"
            }

            textoVictoria.setTextColor(
                if (estado == 1) Color.GREEN else Color.RED
            )
            textoVictoria.visibility = View.VISIBLE


            posGanadora.forEach { pos ->
                botones[pos].setBackgroundResource(fichaVictoria)
            }

        } else if (estado == 2) { // Empate
            textoVictoria.text = "Empate"
            textoVictoria.setBackgroundColor(Color.BLACK)
            textoVictoria.setTextColor(Color.YELLOW)
            textoVictoria.visibility = View.VISIBLE
        }
    }

    private fun ia() {
        val posicionesLibres = tablero.indices.filter { tablero[it] == 0 }
        if (posicionesLibres.isNotEmpty()) {
            val pos = posicionesLibres.random()
            tablero[pos] = -1
            botones[pos].setBackgroundResource(R.drawable.circulo_azul)
        }
        turno = 1
    }

    private fun comprobarEstado(): Int {
        val lineas = listOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
        )

        for (linea in lineas) {
            val suma = tablero[linea[0]] + tablero[linea[1]] + tablero[linea[2]]
            if (kotlin.math.abs(suma) == 3) {
                posGanadora = linea
                return if (suma == 3) 1 else -1
            }
        }


        return if (fichasPuestas == 9) 2 else 0
    }

}
