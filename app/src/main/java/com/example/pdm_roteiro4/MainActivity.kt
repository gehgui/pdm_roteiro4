package com.example.pdm_roteiro4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etVisor: EditText
    private var isResultOnScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etVisor = findViewById(R.id.etVisor)

        val btn0 = findViewById<Button>(R.id.btn0)
        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)
        val btn3 = findViewById<Button>(R.id.btn3)
        val btn4 = findViewById<Button>(R.id.btn4)
        val btn5 = findViewById<Button>(R.id.btn5)
        val btn6 = findViewById<Button>(R.id.btn6)
        val btn7 = findViewById<Button>(R.id.btn7)
        val btn8 = findViewById<Button>(R.id.btn8)
        val btn9 = findViewById<Button>(R.id.btn9)
        val btnDot = findViewById<Button>(R.id.btnDot)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnSub = findViewById<Button>(R.id.btnSub)
        val btnMult = findViewById<Button>(R.id.btnMult)
        val btnDiv = findViewById<Button>(R.id.btnDiv)

        val btnC = findViewById<Button>(R.id.btnC)
        val btnDel = findViewById<Button>(R.id.btnDel)
        val btnEqual = findViewById<Button>(R.id.btnEqual)

        fun addExpression(string: String, isClear: Boolean) {
            if (isClear || etVisor.text.toString() == "Erro") {
                etVisor.setText("")
            }
            if (isResultOnScreen && !listOf("+", "-", "*", "/").contains(string)) {
                etVisor.setText("")
            }
            isResultOnScreen = false
            etVisor.append(string)
        }

        btn0.setOnClickListener { addExpression("0", false) }
        btn1.setOnClickListener { addExpression("1", false) }
        btn2.setOnClickListener { addExpression("2", false) }
        btn3.setOnClickListener { addExpression("3", false) }
        btn4.setOnClickListener { addExpression("4", false) }
        btn5.setOnClickListener { addExpression("5", false) }
        btn6.setOnClickListener { addExpression("6", false) }
        btn7.setOnClickListener { addExpression("7", false) }
        btn8.setOnClickListener { addExpression("8", false) }
        btn9.setOnClickListener { addExpression("9", false) }
        btnDot.setOnClickListener { addExpression(".", false) }

        btnAdd.setOnClickListener { addExpression("+", false) }
        btnSub.setOnClickListener { addExpression("-", false) }
        btnMult.setOnClickListener { addExpression("*", false) }
        btnDiv.setOnClickListener { addExpression("/", false) }

        btnC.setOnClickListener {
            etVisor.setText("")
        }

        btnDel.setOnClickListener {
            val text = etVisor.text.toString()
            if (text.isNotEmpty() && text != "Erro") {
                etVisor.setText(text.substring(0, text.length - 1))
            }
        }

        btnEqual.setOnClickListener {
            try {
                val expressao = etVisor.text.toString()
                if(expressao.isEmpty()) return@setOnClickListener

                val resultado = eval(expressao)

                val resultadoFormatado = if (resultado == resultado.toLong().toDouble()) {
                    resultado.toLong().toString()
                } else {
                    resultado.toString()
                }

                etVisor.setText(resultadoFormatado)
                isResultOnScreen = true

            } catch (e: Exception) {
                etVisor.setText("Erro")
                isResultOnScreen = true
            }
        }
    }

    private fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < str.length) str[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch.toChar())
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when {
                        eat('+'.code) -> x += parseTerm()
                        eat('-'.code) -> x -= parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when {
                        eat('*'.code) -> x *= parseFactor()
                        eat('/'.code) -> x /= parseFactor()
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()
                var x: Double
                val startPos = pos

                if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) {
                    while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                return x
            }
        }.parse()
    }
}