package com.example.ecoscan.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoscan.data.model.AlertModel
import com.example.ecoscan.data.repository.UserRepository
import com.example.ecoscan.remote.response.RegisterResponse
import com.example.ecoscan.ui.profile.ProfileViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SignupViewModel(private val repository: UserRepository) :
    ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _alert = MutableLiveData<AlertModel>()
    val alert: LiveData<AlertModel> = _alert

    fun registerUser(name: String, username:String,email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val successResponse = repository.register(name, username,email, password)
                Log.d(TAG, "isi message try signup ${successResponse.message}")
                _alert.value = AlertModel(true, successResponse.message)
                _isLoading.value = false
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                Log.d(TAG, "isi message catch signup ${errorResponse.message}")
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
        private const val TAG = "SignupViewModel"
    }
}