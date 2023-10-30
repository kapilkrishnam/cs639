package com.example.converterapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.converterapp.ui.theme.ConverterAppTheme

class MainActivity : AppCompatActivity() {

    private lateinit var litersEditText: EditText
    private lateinit var cupsEditText: EditText
    private lateinit var convertButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        litersEditText = findViewById(R.id.litersEditText)
        cupsEditText = findViewById(R.id.cupsEditText)
        convertButton = findViewById(R.id.convertButton)

        convertButton.setOnClickListener {
            val liters = litersEditText.text.toString().toDouble()
            val cups = convertLitersToCups(liters)
            cupsEditText.setText(cups.toString())
        }
    }

    fun convertLitersToCups(liters: Double): Double {
        return liters * 4.22
    }
}


