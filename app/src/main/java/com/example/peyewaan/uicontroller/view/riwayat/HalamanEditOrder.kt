package com.example.peyewaan.uicontroller.view.riwayat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.peyewaan.uicontroller.viewmodel.AppViewModelProvider
import com.example.peyewaan.uicontroller.viewmodel.OrderViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEditOrder(
    idOrder: Int,
    onNavigateBack: () -> Unit,
    viewModel: OrderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val roseGold = Color(0xFFB76E79)
    val creamBackground = Color(0xFFFFFDD0)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Ambil data pesanan lama saat halaman dibuka
    LaunchedEffect(idOrder) {
        viewModel.getDetailOrder(idOrder)
    }

    // ===== Validasi input jumlah =====
    val jumlahText = viewModel.jumlahEdit
    val jumlahInt = jumlahText.toIntOrNull()

    val isJumlahValid = jumlahInt != null && jumlahInt > 0
    val showError = jumlahText.isNotBlank() && !isJumlahValid

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Edit Pesanan", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = roseGold)
            )
        },
        containerColor = creamBackground
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Ubah Jumlah Sewa",
                        color = roseGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Update jumlah alat untuk Pesanan #$idOrder",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = jumlahText,
                        onValueChange = { input ->
                            // ✅ Filter input: hanya angka (biar gak error)
                            val onlyDigits = input.filter { it.isDigit() }
                            viewModel.updateJumlahState(onlyDigits)
                        },
                        label = { Text("Jumlah Alat") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = showError,
                        supportingText = {
                            if (showError) {
                                Text("Jumlah harus berupa angka dan lebih dari 0")
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = roseGold,
                            focusedLabelColor = roseGold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Simpan
            Button(
                onClick = {
                    if (!isJumlahValid) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Jumlah tidak valid. Masukkan angka > 0")
                        }
                        return@Button
                    }

                    // ✅ Panggil updateOrder, lalu kalau sukses back
                    // (Karena kamu sudah desain updateOrder menerima onNavigateBack)
                    viewModel.updateOrder(idOrder, onNavigateBack)

                    // Optional: snackbar feedback cepat (kalau updateOrder kamu belum punya snackbar)
                    scope.launch {
                        snackbarHostState.showSnackbar("Memproses update pesanan...")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = roseGold),
                shape = RoundedCornerShape(12.dp),
                enabled = isJumlahValid && !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Simpan Perubahan", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = roseGold),
                border = BorderStroke(1.dp, roseGold),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Batal")
            }
        }
    }
}
