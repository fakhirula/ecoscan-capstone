package com.example.ecoscan.remote.apisetup

import com.example.ecoscan.remote.response.DeleteResponse
import com.example.ecoscan.remote.response.LoginResponse
import com.example.ecoscan.remote.response.RegisterResponse
import com.example.ecoscan.remote.response.UploadImageResponse
import com.example.ecoscan.remote.response.UserScan10Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("city") city: String
    ): RegisterResponse
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("api/insertAndPredict")
    suspend fun uploadImage(
        @Part attachment: MultipartBody.Part,
    ): UploadImageResponse

    @GET("api/getscans/user")
    suspend fun get10ScanUser(
    ):UserScan10Response

    @DELETE("/api/deletescan/:id")
    suspend fun deleteScanById(
        @Path("id") id:Int
    ):DeleteResponse
}