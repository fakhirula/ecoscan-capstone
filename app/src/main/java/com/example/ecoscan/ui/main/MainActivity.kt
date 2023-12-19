package com.example.ecoscan.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.ecoscan.R
import com.example.ecoscan.databinding.ActivityMainBinding
import com.example.ecoscan.databinding.ItemGetStartedBinding
import com.example.ecoscan.databinding.ItemLoginRegisterBinding
import com.example.ecoscan.ui.ViewModelFactory
import com.example.ecoscan.ui.home.HomeActivity
import com.example.ecoscan.ui.login.LoginActivity
import com.example.ecoscan.ui.signup.SignupActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bg.setOnClickListener {
            showGetStarted()
        }
        showGetStarted()
    }
    override fun onResume() {
        super.onResume()
        viewModel.getSession().observe(this) {
            if (it.token != "") {
                Log.d("MainActivity","isi token dari MainActivity : ${it.token}")
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showGetStarted() {
        val sheetDialog = BottomSheetDialog(this)
        val bindingGetStarted = ItemGetStartedBinding.inflate(layoutInflater)
        val bindingItemLogin = ItemLoginRegisterBinding.inflate(layoutInflater)
        sheetDialog.apply {
            setContentView(bindingGetStarted.root)
            show()
        }
        bindingGetStarted.btnGetStarted.setOnClickListener {
            sheetDialog.dismiss()
            sheetDialog.apply {
                setContentView(bindingItemLogin.root)
                show()
                setOnDismissListener{
                    sheetDialog.dismiss()
                    showGetStarted()
                }
                bindingItemLogin.btnRegister.setOnClickListener {
                    val intent = Intent(context, SignupActivity::class.java )
                    startActivity(intent)
                }
                bindingItemLogin.btnLogin.setOnClickListener {
                    val intent = Intent(context, LoginActivity::class.java )
                    startActivity(intent)
                }
            }
        }
    }
}