package com.example.ecoscan.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ecoscan.R
import com.example.ecoscan.databinding.ItemGetStartedBinding
import com.example.ecoscan.databinding.ItemLoginRegisterBinding
import com.example.ecoscan.ui.login.LoginActivity
import com.example.ecoscan.ui.signup.SignupActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showGetStarted()
    }

    private fun showGetStarted() {
        val sheetDialog = BottomSheetDialog(this)
        val bindingGetStarted = ItemGetStartedBinding.inflate(layoutInflater)
        val bindingItemLogin = ItemLoginRegisterBinding.inflate(layoutInflater)
        sheetDialog.apply {
            setContentView(bindingGetStarted.root)
            show()
            setOnDismissListener{
                show()
            }
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