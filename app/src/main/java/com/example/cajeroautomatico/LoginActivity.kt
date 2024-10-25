package com.example.cajeroautomatico

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etContrasena: EditText
    private lateinit var btnIngresar: Button
    private lateinit var tvMensajeError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsuario = findViewById(R.id.etUsuario)
        etContrasena = findViewById(R.id.etContrasena)
        btnIngresar = findViewById(R.id.btnIngresar)
        tvMensajeError = findViewById(R.id.tvMensajeError)

        btnIngresar.setOnClickListener {
            val usuario = etUsuario.text.toString()
            val contrasena = etContrasena.text.toString()

            when {
                usuario == "andresborja6" && contrasena == "5678" -> iniciarCajero(289300)
                usuario == "santiagochaverra4" && contrasena == "4321" -> iniciarCajero(635700)
                usuario == "profesor" && contrasena == "1234" -> iniciarCajero(754650)
                else -> mostrarError()
            }
        }
    }

    private fun iniciarCajero(saldo: Int) {
        val intent = Intent(this, CajeroActivity::class.java)
        intent.putExtra("saldoInicial", saldo)
        startActivity(intent)
    }

    private fun mostrarError() {
        tvMensajeError.visibility = TextView.VISIBLE
    }
}