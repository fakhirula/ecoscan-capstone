package com.example.ecoscan.data.repository

import androidx.lifecycle.liveData
import com.example.ecoscan.data.pref.UserPreference
import com.example.ecoscan.remote.apisetup.ApiConfig
import com.example.ecoscan.remote.apisetup.ApiService
import com.example.ecoscan.remote.response.LoginResponse
import com.example.ecoscan.remote.response.RegisterResponse
import com.example.ecoscan.remote.response.UploadImageResponse
import com.example.ecoscan.remote.response.UserDetail
import com.example.ecoscan.remote.response.UserScan10Response
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Path

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserDetail) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserDetail> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }
    suspend fun register(fullname:String,email: String, password: String, city: String): RegisterResponse {
        return apiService.register( fullname ,email, password, city)
    }
    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }
    suspend fun uploadImage(photo: MultipartBody.Part, token:String): UploadImageResponse{
        return  ApiConfig.getApiService(token).uploadImage(photo)
    }
    suspend fun get10ScanUser(token: String):UserScan10Response{
        return ApiConfig.getApiService(token).get10ScanUser()
    }

    companion object {
        fun getInstance(apiService: ApiService, userPreference: UserPreference): UserRepository = UserRepository(apiService, userPreference)
    }
}