package com.example.peyewaan.uicontroller.view.detail

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.peyewaan.uicontroller.viewmodel.AppViewModelProvider
import com.example.peyewaan.uicontroller.viewmodel.DetailViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetail(
    idUser: Int,
    idAlat: Int,
    onNavigateBack: () -> Unit,
    onNavigateToRiwayat: () -> Unit,
    detailViewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val roseGold = Color(0xFFB76E79)
    val creamBackground = Color(0xFFFFFDD0)
    val context = LocalContext.current

    // ✅ formatter aman untuk API 24
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    fun rupiah(value: Double): String {
        val nf = NumberFormat.getNumberInstance(Locale("in", "ID"))
        return "Rp${nf.format(value)}"
    }

    fun parseMillis(dateStr: String): Long? {
        return try {
            sdf.parse(dateStr)?.time
        } catch (_: Exception) {
            null
        }
    }

    fun showDatePicker(onPicked: (String) -> Unit) {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val pickedCal = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    // normalisasi jam biar hitung hari aman
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onPicked(sdf.format(pickedCal.time))
            },
            y, m, d
        ).show()
    }

    // ambil data alat saat halaman dibuka
    LaunchedEffect(idAlat) {
        detailViewModel.getEquipmentById(idAlat)
        detailViewModel.jumlahSewa = 1
        detailViewModel.tglAmbil = ""
        detailViewModel.tglKembali = ""
    }

    val alat = detailViewModel.detailUiState

    // ===== Validasi tanggal & hitung durasi =====
    val ambilMillis = parseMillis(detailViewModel.tglAmbil)
    val kembaliMillis = parseMillis(detailViewModel.tglKembali)

    val isTanggalLengkap =
        detailViewModel.tglAmbil.isNotBlank() && detailViewModel.tglKembali.isNotBlank()

    val isTanggalValid =
        ambilMillis != null && kembaliMillis != null && kembaliMillis >= ambilMillis

    val lamaHari = if (isTanggalValid) {
        val diffMillis = kembaliMillis!! - ambilMillis!!
        // inclusive: hari yang sama = 1
        TimeUnit.MILLISECONDS.toDays(diffMillis).toInt() + 1
    } else 0

    val hargaPerHari = alat?.harga ?: 0.0
    val jumlah = detailViewModel.jumlahSewa
    val total = hargaPerHari * jumlah * lamaHari

    var showConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Alat", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = roseGold,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = creamBackground
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (detailViewModel.isLoading && alat == null) {
                CircularProgressIndicator(color = roseGold)
                return@Column
            }

            if (alat == null) {
                Text(detailViewModel.errorMessage ?: "Data alat tidak ditemukan.", color = Color.Red)
                return@Column
            }

            // ===== Gambar =====
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                AsyncImage(
                    model = alat.gambar_url,
                    contentDescription = null,
                    modifier = Modifier
                        .size(250.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(20.dp))

            // ===== Info alat =====
            Text(
                text = alat.nama_alat,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${rupiah(alat.harga)} / hari",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = roseGold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = alat.deskripsi,
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(18.dp))

            // ===== Kartu input sewa =====
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Atur Sewa", fontWeight = FontWeight.Bold, color = roseGold)
                    Spacer(Modifier.height(12.dp))

                    Text("Stok: ${alat.stok}", color = Color.Gray, fontSize = 13.sp)
                    Spacer(Modifier.height(12.dp))

                    Text("Jumlah Unit Sewa", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = { if (detailViewModel.jumlahSewa > 1) detailViewModel.jumlahSewa-- },
                            enabled = detailViewModel.jumlahSewa > 1
                        ) { Text("-") }

                        Text(
                            text = detailViewModel.jumlahSewa.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        OutlinedButton(
                            onClick = { if (detailViewModel.jumlahSewa < alat.stok) detailViewModel.jumlahSewa++ },
                            enabled = detailViewModel.jumlahSewa < alat.stok
                        ) { Text("+") }
                    }

                    Spacer(Modifier.height(18.dp))

                    Text("Durasi Sewa", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = detailViewModel.tglAmbil,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Tanggal Ambil") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            TextButton(onClick = { showDatePicker { detailViewModel.tglAmbil = it } }) {
                                Text("Pilih", color = roseGold)
                            }
                        }
                    )

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = detailViewModel.tglKembali,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Tanggal Kembali") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            TextButton(onClick = { showDatePicker { detailViewModel.tglKembali = it } }) {
                                Text("Pilih", color = roseGold)
                            }
                        }
                    )

                    if (isTanggalLengkap && !isTanggalValid) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Tanggal kembali harus >= tanggal ambil.",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = creamBackground),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Ringkasan Bayar", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Text("Unit: $jumlah")
                            Text("Durasi: $lamaHari hari")
                            Text(
                                "Total: ${rupiah(total)}",
                                fontWeight = FontWeight.ExtraBold,
                                color = roseGold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            val canSewa = alat.stok > 0 && isTanggalValid && lamaHari > 0 && jumlah in 1..alat.stok

            Button(
                onClick = { showConfirm = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = canSewa && !detailViewModel.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = roseGold),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (detailViewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                } else {
                    Text("Sewa Sekarang", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            detailViewModel.errorMessage?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, color = Color.Red, fontSize = 12.sp)
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Konfirmasi Sewa") },
            text = {
                Text(
                    "Sewa ${detailViewModel.jumlahSewa} unit selama $lamaHari hari.\n" +
                            "Total bayar: ${rupiah(total)}\n\n" +
                            "Lanjutkan?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false
                    detailViewModel.buatPesanan(idUser) {
                        onNavigateToRiwayat()
                    }
                }) {
                    Text("Ya", color = roseGold, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
