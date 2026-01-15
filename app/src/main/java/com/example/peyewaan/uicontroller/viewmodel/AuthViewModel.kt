package com.example.peyewaan.uicontroller.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peyewaan.modeldata.User
import com.example.peyewaan.repository.AppRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AppRepository) : ViewModel() {

    // State untuk menampung input dari TextField di Halaman Login & Register
    var nama by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // State untuk status loading dan pesan error
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    /**
     * Fungsi Login: Mengirim email dan password ke server PHP
     */
    fun login(emailParam: String, passwordParam: String, onSuccess: (User) -> Unit) {
        viewModelScope.launch {
            // Validasi input sederhana
            if (emailParam.isEmpty() || passwordParam.isEmpty()) {
                errorMessage = "Email dan password tidak boleh kosong"
                return@launch
            }

            isLoading = true
            errorMessage = null
            try {
                // Memanggil repository untuk proses verifikasi ke server
                val user = repository.loginUser(emailParam, passwordParam)

                if (user != null) {
                    // Jika sukses, kirim objek User (id_user, nama, email) ke UI
                    onSuccess(user)
                } else {
                    // Jika user null (misal: password salah di PHP)
                    errorMessage = "Email atau password salah"
                }
            } catch (e: Exception) {
                // Menangkap error JSON atau HTTP 401 agar tidak force close
                errorMessage = "Gagal login: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Fungsi Register: Menyimpan user baru ke database MySQL
     */
    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                errorMessage = "Semua kolom wajib diisi"
                return@launch
            }

            isLoading = true
            errorMessage = null
            try {
                // Mengirim data pendaftaran ke PHP
                repository.registerUser(nama, email, password)

                // Jika tidak ada Exception dari Retrofit, berarti registrasi sukses
                onSuccess()
            } catch (e: Exception) {
                // Menangani error agar aplikasi tidak tertutup sendiri
                errorMessage = "Gagal mendaftar: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Membersihkan state form agar kosong kembali saat berpindah halaman
     */
    fun resetState() {
        nama = ""
        email = ""
        password = ""
        errorMessage = null
    }
}