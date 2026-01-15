package com.example.peyewaan.uicontroller.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.peyewaan.PenyewaanApp

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            AuthViewModel(penyewaanApp().container.appRepository)
        }

        initializer {
            HomeViewModel(penyewaanApp().container.appRepository)
        }

        initializer {
            DetailViewModel(penyewaanApp().container.appRepository)
        }

        initializer {
            OrderViewModel(penyewaanApp().container.appRepository) // ✅ ini benar kalau OrderViewModel terima AppRepository
        }
    }
}

fun CreationExtras.penyewaanApp(): PenyewaanApp =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PenyewaanApp)
