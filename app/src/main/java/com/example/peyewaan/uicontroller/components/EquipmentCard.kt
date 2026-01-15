package com.example.peyewaan.uicontroller.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.peyewaan.modeldata.Equipment
import coil.compose.AsyncImage

@Composable
fun EquipmentCard(
    equipment: Equipment,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // MENAMPILKAN GAMBAR DARI URL SQL (FR-DASH-02)
            AsyncImage(
                model = equipment.gambar_url, // Mengambil link dari kolom gambar_url di SQL
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
                // Jika gambar gagal dimuat, kamu bisa tambah placeholder di sini
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = equipment.nama_alat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFFB76E79) // Rose Gold
                )
                Text(text = "Harga: Rp ${equipment.harga} / hari", fontSize = 14.sp)
                Text(text = "Stok: ${equipment.stok}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}