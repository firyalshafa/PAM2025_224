package com.example.peyewaan.uicontroller.view.riwayat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.peyewaan.modeldata.Order
import com.example.peyewaan.uicontroller.viewmodel.AppViewModelProvider
import com.example.peyewaan.uicontroller.viewmodel.OrderViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanRiwayat(
    idUser: Int,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDashboardClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: OrderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val roseGold = Color(0xFFB76E79)

    LaunchedEffect(idUser) {
        viewModel.getRiwayat(idUser)
    }

    fun rupiah(value: Double): String {
        val nf = NumberFormat.getNumberInstance(Locale("in", "ID"))
        return "Rp${nf.format(value)}"
    }

    var showCancelDialog by remember { mutableStateOf(false) }
    var selectedOrder by remember { mutableStateOf<Order?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Pesanan", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // ✅ aman tanpa icon dependency
                    TextButton(onClick = onDashboardClick) {
                        Text("Dashboard", color = Color.White)
                    }
                    TextButton(onClick = onLogoutClick) {
                        Text("Logout", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = roseGold)
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                viewModel.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = roseGold
                    )
                }

                viewModel.orderList.isEmpty() -> {
                    Text(
                        "Belum ada riwayat pesanan.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(viewModel.orderList) { order ->
                            val status = order.status_pesanan ?: "-"
                            val canEditCancel = status == "Menunggu Pengambilan"

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                elevation = CardDefaults.cardElevation(3.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFEAE6EB))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {

                                    Text(
                                        text = "Pesanan #${order.id_pesanan}",
                                        fontWeight = FontWeight.Bold,
                                        color = roseGold
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    Text(
                                        text = order.nama_alat ?: "Alat Olahraga",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        text = "Total: ${rupiah(order.total_harga)}",
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Status: $status",
                                        color = Color.Gray
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    val ambil = order.tgl_pengambilan ?: "-"
                                    val kembali = order.tgl_pengembalian ?: "-"
                                    Text(
                                        text = "Ambil: $ambil | Kembali: $kembali",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )

                                    Spacer(Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        OutlinedButton(
                                            onClick = { onEditClick(order.id_pesanan) },
                                            enabled = canEditCancel,
                                            shape = RoundedCornerShape(10.dp)
                                        ) { Text("Edit") }

                                        Spacer(Modifier.width(10.dp))

                                        Button(
                                            onClick = {
                                                selectedOrder = order
                                                showCancelDialog = true
                                            },
                                            enabled = canEditCancel,
                                            colors = ButtonDefaults.buttonColors(containerColor = roseGold),
                                            shape = RoundedCornerShape(10.dp)
                                        ) { Text("Batalkan", color = Color.White) }
                                    }

                                    if (!canEditCancel) {
                                        Spacer(Modifier.height(8.dp))
                                        Text(
                                            "Pesanan tidak bisa diedit/dibatalkan karena status sudah berubah.",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            viewModel.errorMessage?.let { err ->
                Text(
                    text = err,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }

    if (showCancelDialog && selectedOrder != null) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Batalkan Pesanan?") },
            text = { Text("Pesanan #${selectedOrder!!.id_pesanan} akan dibatalkan dan stok akan dikembalikan.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                        viewModel.cancelOrder(selectedOrder!!.id_pesanan, idUser)
                    }
                ) { Text("Ya, Batalkan", color = roseGold, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) { Text("Tidak") }
            }
        )
    }
}
