package com.example.peyewaan.uicontroller.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.peyewaan.modeldata.Order

@Composable
fun OrderItemCard(
    order: Order,
    onEditClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Order #${order.id_pesanan}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Total: Rp ${order.total_harga}")
                Text(
                    text = "Status: ${order.status_pesanan}",
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Tombol edit hanya muncul jika status masih pending (FR-EDIT-01)
            if (order.status_pesanan == "Menunggu Pengambilan") {
                Button(onClick = { onEditClick(order.id_pesanan) }) {
                    Text("Edit")
                }
            }
        }
    }
}