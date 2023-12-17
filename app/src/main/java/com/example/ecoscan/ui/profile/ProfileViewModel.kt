package com.example.ecoscan.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ecoscan.data.model.AlertModel
import com.example.ecoscan.data.repository.UserRepository
import com.example.ecoscan.remote.response.ListScansItem
import com.example.ecoscan.remote.response.UserDetail
import com.example.ecoscan.remote.response.UserScan10Response
import com.example.ecoscan.ui.resultimage.ResultViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProfileViewModel (private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _alert = MutableLiveData<AlertModel>()
    val alert: LiveData<AlertModel> = _alert

    private val _listScan = MutableLiveData<List<ListScansItem>>()
    val listScan: LiveData<List<ListScansItem>> = _listScan

    fun set10Scan(token:String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val successResponse = repository.get10ScanUser(token)
                if (successResponse.success){
                    _listScan.postValue(successResponse.listScans)
                    _isLoading.value = false
                }else{
                    _isLoading.value = false
                    _alert.value = AlertModel(false, successResponse.message)
                }
                Log.d(TAG, "isi response get Scan User ${successResponse.listScans}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, UserScan10Response::class.java)
                Log.e(TAG, "isi response get Scan User $errorResponse")
                _alert.value = AlertModel(false, errorResponse.message)
                _isLoading.value = false
            }catch (e: IOException) {
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

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<UserDetail> {
        return repository.getSession().asLiveData()
    }
    companion object{
        private const val TAG= "ProfileViewModel"
    }

}