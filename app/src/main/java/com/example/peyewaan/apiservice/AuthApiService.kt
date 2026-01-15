package com.example.peyewaan.apiservice

import com.example.peyewaan.modeldata.ApiResponse
import com.example.peyewaan.modeldata.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
interface AuthApiService {
    @FormUrlEncoded
    @POST("auth/login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") pass: String
    ): User // Harus User agar id_user terbawa untuk riwayat

    @FormUrlEncoded
    @POST("auth/register.php")
    suspend fun register(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("password") pass: String
    ) // Tetap Unit, tapi pastikan PHP hanya kirim status HTTP 200
}