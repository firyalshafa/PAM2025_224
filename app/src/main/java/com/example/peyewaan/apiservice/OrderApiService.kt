package com.example.peyewaan.apiservice

import com.example.peyewaan.modeldata.ApiResponse
import com.example.peyewaan.modeldata.DetailOrderResponse
import com.example.peyewaan.modeldata.RiwayatResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OrderApiService {

    @FormUrlEncoded
    @POST("order/get_riwayat.php")
    suspend fun getRiwayat(
        @Field("id_user") idUser: Int
    ): RiwayatResponse

    @FormUrlEncoded
    @POST("order/get_order.php")
    suspend fun getDetailOrder(
        @Field("id_pesanan") idPesanan: Int
    ): DetailOrderResponse

    @FormUrlEncoded
    @POST("order/insert_order.php")
    suspend fun insertOrder(
        @Field("id_user") idUser: Int,
        @Field("id_alat") idAlat: Int,
        @Field("jumlah") jumlah: Int,
        @Field("tgl_pengambilan") tglAmbil: String,
        @Field("tgl_pengembalian") tglKembali: String,
        @Field("total_harga") totalHarga: Double
    ): ApiResponse

    @FormUrlEncoded
    @POST("order/update.php")
    suspend fun updateOrder(
        @Field("id_pesanan") idPesanan: Int,
        @Field("jumlah") jumlah: Int
    ): ApiResponse

    @FormUrlEncoded
    @POST("order/cancel_order.php")
    suspend fun cancelOrder(
        @Field("id_pesanan") idPesanan: Int,
        @Field("id_user") idUser: Int
    ): ApiResponse
}
