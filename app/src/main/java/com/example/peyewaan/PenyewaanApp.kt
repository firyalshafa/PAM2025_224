package com.example.peyewaan


import android.app.Application
import com.example.peyewaan.Container.AppContainer
import com.example.peyewaan.Container.DefaultAppContainer


class PenyewaanApp : Application() {
    // Inisialisasi container saat aplikasi pertama kali menyala
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}