package com.example.ecoscan.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ecoscan.data.repository.UserRepository
import com.example.ecoscan.remote.response.UserDetail

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserDetail> {
        return repository.getSession().asLiveData()
    }
}