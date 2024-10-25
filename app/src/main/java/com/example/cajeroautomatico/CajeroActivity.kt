package com.example.cajeroautomatico

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import android.text.Editable
import android.text.TextWatcher

class CajeroActivity : AppCompatActivity() {

    private lateinit var tvSaldo: TextView
    private lateinit var etCantidad: EditText
    private lateinit var btnConsignar: Button
    private lateinit var btnRetirar: Button
    private lateinit var btnCerrarSesion: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cajero)

        tvSaldo = findViewById(R.id.tvSaldo)
        etCantidad = findViewById(R.id.etCantidad)
        btnConsignar = findViewById(R.id.btnConsignar)
        btnRetirar = findViewById(R.id.btnRetirar)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)

        sharedPreferences = getSharedPreferences("CajeroPrefs", Context.MODE_PRIVATE)
        val saldoInicial = intent.getIntExtra("saldoInicial", 0)
        actualizarSaldo(saldoInicial)

        btnConsignar.setOnClickListener { consignar() }
        btnRetirar.setOnClickListener { retirar() }
        btnCerrarSesion.setOnClickListener { CerrarSesion() }

        etCantidad.addTextChangedListener(object : TextWatcher {
            private var isEditing = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isEditing) return  // Evitar bucles infinitos
                isEditing = true

                val cleanString = s.toString().replace("[,.]".toRegex(), "")
                val parsed = cleanString.toDoubleOrNull() ?: 0.0
                val formatter = NumberFormat.getNumberInstance(Locale("es", "CO"))
                val formatted = formatter.format(parsed)

                etCantidad.setText(formatted)
                etCantidad.setSelection(formatted.length)  // Mover el cursor al final

                isEditing = false
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun actualizarSaldo(nuevoSaldo: Int) {
        with(sharedPreferences.edit()) {
            putInt("saldo", nuevoSaldo)
            apply()
        }
        tvSaldo.text = "Saldo: $${DecimalFormat("#,##0").format(nuevoSaldo)}"
    }

    private fun consignar() {
        val cantidad = obtenerCantidad()
        if (cantidad > 0) {
            val saldoActual = sharedPreferences.getInt("saldo", 0)
            actualizarSaldo(saldoActual + cantidad)
            Toast.makeText(this, "Consignación exitosa", Toast.LENGTH_SHORT).show()
            etCantidad.text.clear()
        } else {
            Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show()
        }
    }

    private fun retirar() {
        val cantidad = obtenerCantidad()
        val saldoActual = sharedPreferences.getInt("saldo", 0)

        if (cantidad > saldoActual) {
            Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show()
        } else {
            actualizarSaldo(saldoActual - cantidad)
            Toast.makeText(this, "Retiro exitoso", Toast.LENGTH_SHORT).show()
            etCantidad.text.clear()
        }
    }

    private fun obtenerCantidad(): Int {
        val cleanString = etCantidad.text.toString().replace("[,.]".toRegex(), "")
        return cleanString.toIntOrNull() ?: 0
    }

    private fun CerrarSesion() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}