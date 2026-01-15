package com.example.peyewaan.uicontroller.view.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.peyewaan.uicontroller.components.EquipmentCard
import com.example.peyewaan.uicontroller.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    onDetailClick: (Int) -> Unit,
    onHistoryClick: () -> Unit,
    viewModel: HomeViewModel
) {
    val roseGold = Color(0xFFB76E79)
    val creamBackground = Color(0xFFFFFDD0)

    // ✅ REFRESH OTOMATIS SETIAP BALIK KE DASHBOARD
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dashboard Sewa Alat",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = roseGold),
                actions = {
                    TextButton(onClick = onHistoryClick) {
                        Text("Riwayat", color = Color.White)
                    }
                }
            )
        },
        containerColor = creamBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                label = { Text("Cari Alat Olahraga...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = roseGold,
                    focusedLabelColor = roseGold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                viewModel.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = roseGold)
                    }
                }

                viewModel.isError -> {
                    Text(
                        "Gagal memuat data alat",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                viewModel.filteredList.isEmpty() -> {
                    Text(
                        "Alat tidak ditemukan",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(viewModel.filteredList) { alat ->
                            EquipmentCard(
                                equipment = alat,
                                onCardClick = { onDetailClick(alat.id_alat) }
                            )
                        }
                    }
                }
            }
        }
    }
}
