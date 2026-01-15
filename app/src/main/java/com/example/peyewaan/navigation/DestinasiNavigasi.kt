package com.example.peyewaan.navigation



/**
 * Interface PetaNavigasi sebagai kontrak utama untuk semua halaman.
 * Ini menggantikan peran DestinasiNavigasi agar penamaan lebih konsisten.
 */
interface PetaNavigasi {
    val rute: String
    val judulRes: String
}

// 1. Destinasi Login (FR-LOG) [cite: 263, 483]
object DestinasiLogin : PetaNavigasi {
    override val rute = "login"
    override val judulRes = "Masuk"
}

// 2. Destinasi Register (FR-REG) [cite: 223, 478]
object DestinasiRegister : PetaNavigasi {
    override val rute = "register"
    override val judulRes = "Daftar"
}

// 3. Destinasi Home / Dashboard (FR-DASH) [cite: 292, 491]
object DestinasiHome : PetaNavigasi {
    override val rute = "home"
    override val judulRes = "Beranda"
}

// 4. Destinasi Detail Alat (FR-DET) [cite: 315, 498]
object DestinasiDetail : PetaNavigasi {
    override val rute = "detail"
    const val ALAT_ID = "id_alat"
    val ruteDenganArgumen = "$rute/{$ALAT_ID}"
    override val judulRes = "Detail Alat"
}

// 5. Destinasi Riwayat Pesanan (FR-HIS) [cite: 373, 504]
object DestinasiRiwayat : PetaNavigasi {
    override val rute = "riwayat"
    override val judulRes = "Riwayat Pesanan"
}

// 6. Destinasi Edit Pesanan (FR-EDIT) [cite: 397]
// Hanya bisa diakses jika status 'Menunggu Pengambilan'
object DestinasiEditOrder : PetaNavigasi {
    override val rute = "edit_order"
    const val ORDER_ID = "id_order"
    val ruteDenganArgumen = "$rute/{$ORDER_ID}"
    override val judulRes = "Edit Pesanan"
}