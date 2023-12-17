package com.example.ecoscan.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoscan.data.model.AlertModel
import com.example.ecoscan.data.repository.UserRepository
import com.example.ecoscan.remote.response.LoginResponse
import com.example.ecoscan.remote.response.UserDetail
import com.example.ecoscan.ui.signup.SignupViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _alert = MutableLiveData<AlertModel>()
    val alert: LiveData<AlertModel> = _alert

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val successResponse = repository.login(email, password)
                val userId = successResponse.userDetail.userId
                val name =successResponse.userDetail.fullname
                val email =successResponse.userDetail.email
                val token = successResponse.userDetail.token
                Log.d(TAG, "isi success try ${successResponse.success}")
                Log.d(TAG, "isi token ${successResponse.userDetail.token}")
                repository.saveSession(UserDetail(name,userId,email,token))
                _alert.value = AlertModel(true, successResponse.message)
                _isLoading.value = false
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                Log.d(TAG, "isi message catch ${errorResponse.success}")
                _alert.value = AlertModel(false, errorResponse.message)
                _isLoading.value = false
            } catch (e: IOException) {
                Log.e(TAG, "IOException: ${e.message}")
                _alert.value = AlertModel(false, "No internet connection.")
                _isLoading.value = false
            }catch (e: Exception) {
                Log.e(TAG, "An unexpected error occurred: ${e.message}")
                _alert.value = AlertModel(false, "An unexpected error occurred.")
                _isLoading.value = false
            }
        }
    }
    companion object {
        private const val TAG = "LoginViewModel"
    }

}