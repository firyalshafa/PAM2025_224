package com.example.peyewaan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.peyewaan.navigation.PengelolaHalaman
import com.example.peyewaan.ui.theme.PeyewaanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengaktifkan tampilan layar penuh (tanpa bar atas bawaan sistem)
        enableEdgeToEdge()

        setContent {
            PeyewaanTheme {
                // Surface dengan warna Cream sebagai dasar aplikasi
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFFDD0) // Warna Cream
                ) {
                    // Memanggil fungsi navigasi utama
                    PengelolaHalaman()
                }
            }
        }
    }
}