package com.example.peyewaan.uicontroller.view.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun HalamanSplash(
    onSplashFinished: () -> Unit
) {
    // Warna Rose Gold & Cream
    val roseGold = Color(0xFFB76E79)
    val creamBackground = Color(0xFFFFFDD0)

    // Animasi sederhana untuk teks (muncul perlahan/fade-in)
    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1500),
        label = "alphaAnimation"
    )

    LaunchedEffect(key1 = Unit) {
        delay(2000) // Layar splash muncul selama 2 detik
        onSplashFinished() // Pindah ke halaman Login
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(creamBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Teks Logo / Nama Aplikasi
            Text(
                text = "Penyewaan Alat",
                color = roseGold,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.alpha(alpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sport Equipment Rental",
                color = Color.Gray,
                fontSize = 16.sp,
                letterSpacing = 2.sp,
                modifier = Modifier.alpha(alpha)
            )
        }
    }
}