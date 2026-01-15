package com.example.peyewaan.uicontroller.view.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.peyewaan.uicontroller.viewmodel.AppViewModelProvider
import com.example.peyewaan.uicontroller.viewmodel.AuthViewModel

@Composable
fun HalamanRegister(
    onNavigateBack: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory),

) {
    val roseGold = Color(0xFFB76E79)
    val creamBackground = Color(0xFFFFFDD0)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = creamBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Daftar Akun",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = roseGold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Input Nama
            OutlinedTextField(
                value = viewModel.nama,
                onValueChange = { viewModel.nama = it },
                label = { Text("Nama Lengkap") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Email
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Password
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            // Menampilkan Pesan Error jika ada
            viewModel.errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Daftar
            Button(
                onClick = {
                    // Memanggil fungsi register dan pindah halaman jika sukses
                    viewModel.register(onSuccess = { onNavigateBack() })
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = roseGold),
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Daftar Sekarang", color = Color.White)
                }
            }

            TextButton(onClick = { onNavigateBack() }) {
                Text("Sudah punya akun? Kembali ke Login", color = roseGold)
            }
        }
    }
}