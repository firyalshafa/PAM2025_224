package com.example.peyewaan.uicontroller.components



import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun DialogPanduan(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cara Penggunaan") },
        text = {
            Column {
                Text("1. Pilih alat olahraga yang ingin disewa.")
                Text("2. Tentukan jumlah dan klik 'Sewa Sekarang'.")
                Text("3. Lihat status pesanan di menu 'Riwayat'.")
                Text("4. Ambil barang di toko dengan menunjukkan ID Pesanan.")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Mengerti")
            }
        }
    )
}