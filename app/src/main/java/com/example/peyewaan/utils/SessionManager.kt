package com.example.peyewaan.utils


import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        const val USER_ID = "user_id"
        const val IS_LOGIN = "is_login"
    }

    // Simpan ID User setelah login berhasil
    fun saveAuthToken(idUser: String) {
        val editor = prefs.edit()
        editor.putString(USER_ID, idUser)
        editor.putBoolean(IS_LOGIN, true)
        editor.apply()
    }

    // Ambil ID User untuk proses sewa/order
    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    // Cek apakah user sudah login atau belum
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGIN, false)
    }

    // Hapus sesi saat logout
    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}