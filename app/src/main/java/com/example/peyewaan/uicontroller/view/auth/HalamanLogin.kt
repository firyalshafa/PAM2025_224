package com.example.peyewaan.uicontroller.view.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.peyewaan.R
import com.example.peyewaan.uicontroller.components.DialogPanduan   // ✅ ambil dari file kamu
import com.example.peyewaan.uicontroller.viewmodel.AppViewModelProvider
import com.example.peyewaan.uicontroller.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanLogin(
    onRegisterClick: () -> Unit,
    onLoginSuccess: (Int) -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val roseGold = Color(0xFFB76E79)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ✅ dropdown panduan
    var expanded by remember { mutableStateOf(false) }
    var selectedGuide by remember { mutableStateOf("Panduan") } // tampilan field
    var showPanduan by remember { mutableStateOf(false) }       // ✅ trigger DialogPanduan

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = roseGold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Selamat Datang",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = roseGold
        )

        Text(
            text = "Silakan masuk ke akun Anda",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.login(email, password) { user ->
                    onLoginSuccess(user.id_user)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = roseGold)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ bawah "Belum punya akun" + dropdown panduan
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Belum punya akun? ", fontSize = 14.sp)
                TextButton(onClick = onRegisterClick) {
                    Text("Daftar Sekarang", color = roseGold, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedGuide,
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .width(180.dp),
                    label = { Text("Panduan") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = roseGold,
                        focusedLabelColor = roseGold
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Panduan Aplikasi") },
                        onClick = {
                            selectedGuide = "Panduan Aplikasi"
                            expanded = false
                            showPanduan = true     // ✅ munculin dialog dari DialogPanduan.kt
                        }
                    )
                }
            }
        }

        // error login
        viewModel.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp
            )
        }
    }

    // ✅ PAKAI DIALOG PANDUAN DARI FILE KAMU
    if (showPanduan) {
        DialogPanduan(
            onDismiss = { showPanduan = false }  // kalau di file kamu namanya beda, ganti ini
        )
    }
}
