package com.example.ecoscan.ui.resultimage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ecoscan.data.model.AlertModel
import com.example.ecoscan.data.repository.UserRepository
import com.example.ecoscan.remote.response.UploadImageResponse
import com.example.ecoscan.remote.response.UserDetail
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException

class ResultViewModel(private val repository: UserRepository) : ViewModel() {
    private val _alert = MutableLiveData<AlertModel>()
    val alert: LiveData<AlertModel> = _alert

    private val _waste = MutableLiveData<String>()
    val waste: LiveData<String> = _waste

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadImage(photo: MultipartBody.Part, token:String) {
        _isLoading.value = true
        viewModelScope.launch {
            Log.d(TAG, "isi token upload $token")
            try {
                val successResponse = repository.uploadImage(photo, token)
                Log.d(TAG, "isi response upload $successResponse")
                if (successResponse.success){
                    _waste.postValue(successResponse.result.wasteType)
                    _isLoading.value = false
                    _alert.value = AlertModel(true, successResponse.message)
                }else{
                    _alert.value = AlertModel(false, successResponse.message)
                    _isLoading.value = false
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, UploadImageResponse::class.java)
                _alert.value = AlertModel(false, errorResponse.message)
                _isLoading.value = false
                Log.e(TAG, "isi response upload $errorResponse")
            } catch (e: IOException) {
                Log.e(TAG, "IOException: ${e.message}")
                _alert.value = AlertModel(false, "No internet connection.")
                _isLoading.value = false
            }catch (e: Exception) {
                // Tangani jenis kesalahan lain jika diperlukan
                Log.e(TAG, "An unexpected error occurred: ${e.message}")
                _alert.value = AlertModel(false, "An unexpected error occurred.")
                _isLoading.value = false
            }
        }
    }
    fun getSession(): LiveData<UserDetail> {
        return repository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    companion object{
        private const val TAG= "ResultViewModel"
    }
}